package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.DistrictContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.DistrictMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolContactMapper;
import ca.bc.gov.educ.api.institute.messaging.jetstream.Publisher;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.*;
import ca.bc.gov.educ.api.institute.struct.v1.District;
import ca.bc.gov.educ.api.institute.struct.v1.DistrictContact;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
import ca.bc.gov.educ.api.institute.util.EventUtil;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.util.TransformUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.educ.api.institute.constants.v1.EventOutcome.*;
import static ca.bc.gov.educ.api.institute.constants.v1.EventType.*;

@Service
@Slf4j
public class DistrictService {

  private static final String DISTRICT_ID_ATTR = "districtId";

  private static final String CONTACT_ID_ATTR = "contactId";

  private static final String NOTE_ID_ATTR = "noteId";

  private static final String CREATE_DATE = "createDate";
  
  private static final String CREATE_USER = "createUser";

  @Getter(AccessLevel.PRIVATE)
  private final DistrictRepository districtRepository;

  private final DistrictTombstoneRepository districtTombstoneRepository;

  private final DistrictHistoryService districtHistoryService;

  private final DistrictContactRepository districtContactRepository;

  private final NoteRepository noteRepository;

  private final InstituteEventRepository instituteEventRepository;

  @Getter(AccessLevel.PRIVATE)
  private final Publisher publisher;


  @Autowired
  public DistrictService(DistrictRepository districtRepository, DistrictTombstoneRepository districtTombstoneRepository, DistrictHistoryService districtHistoryService, DistrictContactRepository districtContactRepository, NoteRepository noteRepository, InstituteEventRepository instituteEventRepository, Publisher publisher) {
    this.districtRepository = districtRepository;
    this.districtTombstoneRepository = districtTombstoneRepository;
    this.districtHistoryService = districtHistoryService;
    this.districtContactRepository = districtContactRepository;
    this.noteRepository = noteRepository;
    this.instituteEventRepository = instituteEventRepository;
    this.publisher = publisher;
  }

  public List<DistrictTombstoneEntity> getAllDistrictsList() {
    return districtTombstoneRepository.findAll();
  }

  public Optional<DistrictEntity> getDistrict(UUID districtId) {
    return districtRepository.findById(districtId);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Pair<DistrictEntity, InstituteEvent> createDistrict(District district) throws JsonProcessingException {
    var districtEntity = DistrictMapper.mapper.toModel(district);

    districtEntity.getAddresses().stream().forEach(address -> {
      RequestUtil.setAuditColumnsForAddress(address);
      TransformUtil.uppercaseFields(address);
      address.setDistrictEntity(districtEntity);
    });

    TransformUtil.uppercaseFields(districtEntity);
    districtRepository.save(districtEntity);
    districtHistoryService.createDistrictHistory(districtEntity, district.getCreateUser());
    final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(districtEntity.getUpdateUser(), districtEntity.getUpdateUser(), JsonUtil.getJsonStringFromObject(DistrictMapper.mapper.toStructure(districtEntity)), CREATE_DISTRICT, DISTRICT_CREATED);
    instituteEventRepository.save(instituteEvent);

    return Pair.of(districtEntity, instituteEvent);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteDistrict(UUID districtId) {
    val entityOptional = districtRepository.findById(districtId);
    val entity = entityOptional.orElseThrow(() -> new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, districtId.toString()));
    districtHistoryService.deleteByDistrictID(districtId);
    districtRepository.delete(entity);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {EntityNotFoundException.class})
  public Pair<DistrictEntity, InstituteEvent> updateDistrict(District districtUpdate, UUID districtId) throws JsonProcessingException {
    var district = DistrictMapper.mapper.toModel(districtUpdate);
    if (districtId == null || !districtId.equals(district.getDistrictId())) {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }

    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(district.getDistrictId());

    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      BeanUtils.copyProperties(district, currentDistrictEntity, CREATE_DATE, CREATE_USER, "addresses"); // update current student entity with incoming payload ignoring the fields.
      setAddresses(currentDistrictEntity, district);
      TransformUtil.uppercaseFields(currentDistrictEntity); // convert the input to upper case.
      var savedDistrict = districtRepository.save(currentDistrictEntity);
      districtHistoryService.createDistrictHistory(savedDistrict, savedDistrict.getUpdateUser());
      final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(savedDistrict.getUpdateUser(), savedDistrict.getUpdateUser(), JsonUtil.getJsonStringFromObject(DistrictMapper.mapper.toStructure(savedDistrict)), UPDATE_DISTRICT, DISTRICT_UPDATED);
      instituteEventRepository.save(instituteEvent);

      return Pair.of(savedDistrict, instituteEvent);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  private void setAddresses(DistrictEntity currentDistrictEntity, DistrictEntity district){
    currentDistrictEntity.getAddresses().clear();
    district.getAddresses().stream().forEach(address -> {
      RequestUtil.setAuditColumnsForAddress(address);
      TransformUtil.uppercaseFields(address);
      address.setDistrictEntity(currentDistrictEntity);
      currentDistrictEntity.getAddresses().add(address);
    });
  }

  public Optional<DistrictContactEntity> getDistrictContact(UUID districtId, UUID contactId) {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      return districtContactRepository.findByDistrictContactIdAndDistrictEntity(contactId, currentDistrictEntity);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Pair<DistrictContactEntity, InstituteEvent> createDistrictContact(DistrictContact contact, UUID districtId) throws JsonProcessingException {
    var contactEntity = DistrictContactMapper.mapper.toModel(contact);
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      contactEntity.setDistrictEntity(curDistrictEntityOptional.get());
      TransformUtil.uppercaseFields(contactEntity);
      districtContactRepository.save(contactEntity);
      final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(
              contact.getCreateUser(),
              contact.getUpdateUser(),
              JsonUtil.getJsonStringFromObject(DistrictContactMapper.mapper.toStructure(contactEntity)),
              CREATE_DISTRICT_CONTACT,
              DISTRICT_CONTACT_CREATED
      );
      instituteEventRepository.save(instituteEvent);
      return Pair.of(contactEntity, instituteEvent);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Pair<DistrictContactEntity, InstituteEvent> updateDistrictContact(DistrictContact contact, UUID districtId, UUID contactId) throws JsonProcessingException {
    var contactEntity = DistrictContactMapper.mapper.toModel(contact);
    if (contactId == null || !contactId.equals(contactEntity.getDistrictContactId())) {
      throw new EntityNotFoundException(DistrictContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }

    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }

    Optional<DistrictContactEntity> curContactEntityOptional = districtContactRepository.findById(contactEntity.getDistrictContactId());

    if (curContactEntityOptional.isPresent()) {
      if (!districtId.equals(curContactEntityOptional.get().getDistrictEntity().getDistrictId())) {
        throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
      }
      final DistrictContactEntity currentContactEntity = curContactEntityOptional.get();
      BeanUtils.copyProperties(contactEntity, currentContactEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentContactEntity); // convert the input to upper case.
      currentContactEntity.setDistrictEntity(curDistrictEntityOptional.get());
      districtContactRepository.save(currentContactEntity);
      final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(
              contact.getCreateUser(),
              contact.getUpdateUser(),
              JsonUtil.getJsonStringFromObject(DistrictContactMapper.mapper.toStructure(contactEntity)),
              UPDATE_DISTRICT_CONTACT,
              DISTRICT_CONTACT_UPDATED
      );
      instituteEventRepository.save(instituteEvent);
      return Pair.of(contactEntity, instituteEvent);
    } else {
      throw new EntityNotFoundException(DistrictContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public InstituteEvent deleteDistrictContact(UUID districtId, UUID contactId) throws JsonProcessingException {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);
    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      final Optional<DistrictContactEntity> districtContactEntityOptional = currentDistrictEntity.getContacts()
              .stream()
              .filter(e -> e.getDistrictContactId().equals(contactId))
              .findFirst();
      if (districtContactEntityOptional.isPresent()){
        districtContactRepository.deleteByDistrictContactIdAndDistrictEntity(contactId, currentDistrictEntity);
        DistrictContactEntity districtContactEntity = districtContactEntityOptional.get();
        final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(
                  districtContactEntity.getCreateUser(),
                  districtContactEntity.getUpdateUser(),
                  JsonUtil.getJsonStringFromObject(DistrictContactMapper.mapper.toStructure(districtContactEntity)),
                  DELETE_DISTRICT_CONTACT,
                  DISTRICT_CONTACT_DELETED
          );
          instituteEventRepository.save(instituteEvent);
          return instituteEvent;
      }

    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
    // Contact not found, so return null, no event. Intention is to DELETE.
    return null;
  }

  public Optional<NoteEntity> getDistrictNote(UUID districtId, UUID noteId) {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      return noteRepository.findByNoteIdAndDistrictID(noteId, currentDistrictEntity.getDistrictId());
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  public List<NoteEntity> getDistrictNotes(UUID districtID) {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtID);

    if (curDistrictEntityOptional.isPresent()) {
      return noteRepository.findByDistrictID(districtID);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtID));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public NoteEntity createDistrictNote(Note note, UUID districtId) {
    var noteEntity = NoteMapper.mapper.toModel(note);
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      noteEntity.setDistrictID(curDistrictEntityOptional.get().getDistrictId());
      TransformUtil.uppercaseFields(noteEntity);
      noteRepository.save(noteEntity);
      return noteEntity;
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public NoteEntity updateDistrictNote(Note note, UUID districtId, UUID noteId) {
    var noteEntity = NoteMapper.mapper.toModel(note);
    if (noteId == null || !noteId.equals(noteEntity.getNoteId())) {
      throw new EntityNotFoundException(NoteEntity.class, NOTE_ID_ATTR, String.valueOf(noteId));
    }

    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }

    Optional<NoteEntity> curNoteEntityOptional = noteRepository.findById(noteEntity.getNoteId());

    if (curNoteEntityOptional.isPresent()) {
      if (!districtId.equals(curNoteEntityOptional.get().getDistrictID())) {
        throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
      }
      final NoteEntity currentNoteEntity = curNoteEntityOptional.get();
      BeanUtils.copyProperties(noteEntity, currentNoteEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentNoteEntity); // convert the input to upper case.
      currentNoteEntity.setDistrictID(curDistrictEntityOptional.get().getDistrictId());
      noteRepository.save(currentNoteEntity);
      return currentNoteEntity;
    } else {
      throw new EntityNotFoundException(NoteEntity.class, NOTE_ID_ATTR, String.valueOf(noteId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteDistrictNote(UUID districtId, UUID noteId) {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);
    Optional<NoteEntity> curNoteEntityOptional = noteRepository.findById(noteId);

    if (curDistrictEntityOptional.isPresent() && curNoteEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      noteRepository.deleteByNoteIdAndDistrictID(noteId, currentDistrictEntity.getDistrictId());
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }
}
