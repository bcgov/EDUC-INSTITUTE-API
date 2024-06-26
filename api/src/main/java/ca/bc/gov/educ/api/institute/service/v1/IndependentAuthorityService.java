package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.AuthorityContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.model.v1.AuthorityContactEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import ca.bc.gov.educ.api.institute.model.v1.NoteEntity;
import ca.bc.gov.educ.api.institute.repository.v1.AuthorityContactRepository;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityRepository;
import ca.bc.gov.educ.api.institute.repository.v1.InstituteEventRepository;
import ca.bc.gov.educ.api.institute.repository.v1.NoteRepository;
import ca.bc.gov.educ.api.institute.struct.v1.AuthorityContact;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthority;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
import ca.bc.gov.educ.api.institute.util.EventUtil;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.util.TransformUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.Getter;
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
public class IndependentAuthorityService {

  private static final String INDEPENDENT_AUTHORITY_ID_ATTR = "independentAuthorityId";

  private static final String CONTACT_ID_ATTR = "contactId";

  private static final String NOTE_ID_ATTR = "noteId";

  private static final String CREATE_DATE = "createDate";

  private static final String CREATE_USER = "createUser";

  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityRepository independentAuthorityRepository;

  private final IndependentAuthorityHistoryService independentAuthorityHistoryService;

  private final AuthorityContactRepository authorityContactRepository;

  private final NoteRepository noteRepository;

  private final InstituteEventRepository instituteEventRepository;

  @Autowired
  public IndependentAuthorityService(IndependentAuthorityRepository independentAuthorityRepository, IndependentAuthorityHistoryService independentAuthorityHistoryService, AuthorityContactRepository authorityContactRepository, NoteRepository noteRepository, InstituteEventRepository instituteEventRepository) {
    this.independentAuthorityRepository = independentAuthorityRepository;
    this.independentAuthorityHistoryService = independentAuthorityHistoryService;
    this.authorityContactRepository = authorityContactRepository;
    this.noteRepository = noteRepository;
    this.instituteEventRepository = instituteEventRepository;
  }

  public List<IndependentAuthorityEntity> getAllIndependentAuthoritysList() {
    return independentAuthorityRepository.findAll();
  }

  public Optional<IndependentAuthorityEntity> getIndependentAuthority(UUID independentAuthorityId) {
    return independentAuthorityRepository.findById(independentAuthorityId);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Pair<IndependentAuthorityEntity, InstituteEvent> createIndependentAuthority(IndependentAuthority independentAuthority) throws JsonProcessingException {
    var independentAuthorityEntity = IndependentAuthorityMapper.mapper.toModel(independentAuthority);

    String nextAuthorityNumber = independentAuthorityRepository.findNextAuthorityNumber();
    independentAuthorityEntity.setAuthorityNumber(Integer.valueOf(nextAuthorityNumber));
    independentAuthorityEntity.getAddresses().stream().forEach(addy -> {
      RequestUtil.setAuditColumnsForAddress(addy);
      TransformUtil.uppercaseFields(addy);
      addy.setIndependentAuthorityEntity(independentAuthorityEntity);
    });
    TransformUtil.uppercaseFields(independentAuthorityEntity);
    independentAuthorityRepository.save(independentAuthorityEntity);
    independentAuthorityHistoryService.createIndependentAuthorityHistory(independentAuthorityEntity, independentAuthority.getCreateUser());

    final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(independentAuthorityEntity.getUpdateUser(), independentAuthorityEntity.getUpdateUser(), JsonUtil.getJsonStringFromObject(IndependentAuthorityMapper.mapper.toStructure(independentAuthorityEntity)), CREATE_AUTHORITY, AUTHORITY_CREATED);
    instituteEventRepository.save(instituteEvent);

    return Pair.of(independentAuthorityEntity, instituteEvent);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteIndependentAuthority(UUID independentAuthorityId) {
    val entityOptional = independentAuthorityRepository.findById(independentAuthorityId);
    val entity = entityOptional.orElseThrow(() -> new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, independentAuthorityId.toString()));
    independentAuthorityHistoryService.deleteByIndependentAuthorityID(independentAuthorityId);
    independentAuthorityRepository.delete(entity);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {EntityNotFoundException.class})
  public Pair<IndependentAuthorityEntity, InstituteEvent> updateIndependentAuthority(IndependentAuthority independentAuthorityUpdate, UUID independentAuthorityId) throws JsonProcessingException {
    var independentAuthority = IndependentAuthorityMapper.mapper.toModel(independentAuthorityUpdate);
    if (independentAuthorityId == null || !independentAuthorityId.equals(independentAuthority.getIndependentAuthorityId())) {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }

    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthority.getIndependentAuthorityId());

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      BeanUtils.copyProperties(independentAuthority, currentIndependentAuthorityEntity, CREATE_DATE, CREATE_USER, "addresses"); // update current student entity with incoming payload ignoring the fields.
      setAddresses(currentIndependentAuthorityEntity, independentAuthority);
      TransformUtil.uppercaseFields(currentIndependentAuthorityEntity); // convert the input to upper case.
      var savedAuthority = independentAuthorityRepository.save(currentIndependentAuthorityEntity);
      independentAuthorityHistoryService.createIndependentAuthorityHistory(savedAuthority, savedAuthority.getUpdateUser());
      final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(savedAuthority.getUpdateUser(), savedAuthority.getUpdateUser(), JsonUtil.getJsonStringFromObject(IndependentAuthorityMapper.mapper.toStructure(savedAuthority)), UPDATE_AUTHORITY, AUTHORITY_UPDATED);
      instituteEventRepository.save(instituteEvent);

      return Pair.of(savedAuthority, instituteEvent);
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  private void setAddresses(IndependentAuthorityEntity currentAuthorityEntity, IndependentAuthorityEntity authority){
    currentAuthorityEntity.getAddresses().clear();
    authority.getAddresses().stream().forEach(address -> {
      RequestUtil.setAuditColumnsForAddress(address);
      TransformUtil.uppercaseFields(address);
      address.setIndependentAuthorityEntity(currentAuthorityEntity);
      currentAuthorityEntity.getAddresses().add(address);
    });
  }

  public Optional<AuthorityContactEntity> getIndependentAuthorityContact(UUID independentAuthorityId, UUID contactId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      return authorityContactRepository.findByAuthorityContactIdAndIndependentAuthorityEntity(contactId, currentIndependentAuthorityEntity);
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Pair<AuthorityContactEntity, InstituteEvent> createIndependentAuthorityContact(AuthorityContact contact, UUID independentAuthorityId) throws JsonProcessingException {
    var contactEntity = AuthorityContactMapper.mapper.toModel(contact);
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      contactEntity.setIndependentAuthorityEntity(curIndependentAuthorityEntityOptional.get());
      TransformUtil.uppercaseFields(contactEntity);
      authorityContactRepository.save(contactEntity);
      final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(
              contact.getCreateUser(),
              contact.getUpdateUser(),
              JsonUtil.getJsonStringFromObject(AuthorityContactMapper.mapper.toStructure(contactEntity)),
              CREATE_AUTHORITY_CONTACT,
              AUTHORITY_CONTACT_CREATED
              );
      instituteEventRepository.save(instituteEvent);
      return Pair.of(contactEntity, instituteEvent);
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Pair<AuthorityContactEntity, InstituteEvent> updateIndependentAuthorityContact(AuthorityContact contact, UUID independentAuthorityId, UUID contactId) throws JsonProcessingException {
    var contactEntity = AuthorityContactMapper.mapper.toModel(contact);
    if (contactId == null || !contactId.equals(contactEntity.getAuthorityContactId())) {
      throw new EntityNotFoundException(AuthorityContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }

    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }

    Optional<AuthorityContactEntity> curContactEntityOptional = authorityContactRepository.findById(contactEntity.getAuthorityContactId());

    if (curContactEntityOptional.isPresent()) {
      if (!independentAuthorityId.equals(curContactEntityOptional.get().getIndependentAuthorityEntity().getIndependentAuthorityId())) {
        throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
      }
      final AuthorityContactEntity currentContactEntity = curContactEntityOptional.get();
      BeanUtils.copyProperties(contactEntity, currentContactEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentContactEntity); // convert the input to upper case.
      currentContactEntity.setIndependentAuthorityEntity(curIndependentAuthorityEntityOptional.get());
      authorityContactRepository.save(currentContactEntity);
      final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(
             contact.getCreateUser(),
             contact.getUpdateUser(),
              JsonUtil.getJsonStringFromObject(AuthorityContactMapper.mapper.toStructure(contactEntity)),
              UPDATE_AUTHORITY_CONTACT,
              AUTHORITY_CONTACT_UPDATED
      );
      instituteEventRepository.save(instituteEvent);
      return Pair.of(currentContactEntity, instituteEvent);
    } else {
      throw new EntityNotFoundException(AuthorityContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public InstituteEvent deleteIndependentAuthorityContact(UUID contactId) throws JsonProcessingException {
    Optional<AuthorityContactEntity> authorityContactEntityOptional = authorityContactRepository.findById(contactId);
      if(authorityContactEntityOptional.isPresent()){
        AuthorityContactEntity authorityContactEntity = authorityContactEntityOptional.get();
        authorityContactRepository.delete(authorityContactEntity);
        final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(
          authorityContactEntity.getCreateUser(),
          authorityContactEntity.getUpdateUser(),
          JsonUtil.getJsonStringFromObject(AuthorityContactMapper.mapper.toStructure(authorityContactEntity)),
          DELETE_AUTHORITY_CONTACT,
                AUTHORITY_CONTACT_DELETED
        );
        instituteEventRepository.save(instituteEvent);
        return instituteEvent;
      }
      else {
      throw new EntityNotFoundException(AuthorityContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }
  }

  public Optional<NoteEntity> getIndependentAuthorityNote(UUID independentAuthorityId, UUID noteId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      return noteRepository.findByNoteIdAndIndependentAuthorityID(noteId, currentIndependentAuthorityEntity.getIndependentAuthorityId());
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  public List<NoteEntity> getIndependentAuthorityNotes(UUID independentAuthorityId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      return noteRepository.findByIndependentAuthorityID(independentAuthorityId);
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public NoteEntity createIndependentAuthorityNote(Note note, UUID independentAuthorityId) {
    var noteEntity = NoteMapper.mapper.toModel(note);
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      noteEntity.setIndependentAuthorityID(curIndependentAuthorityEntityOptional.get().getIndependentAuthorityId());
      TransformUtil.uppercaseFields(noteEntity);
      noteRepository.save(noteEntity);
      return noteEntity;
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public NoteEntity updateIndependentAuthorityNote(Note note, UUID independentAuthorityId, UUID noteId) {
    var noteEntity = NoteMapper.mapper.toModel(note);
    if (noteId == null || !noteId.equals(noteEntity.getNoteId())) {
      throw new EntityNotFoundException(NoteEntity.class, NOTE_ID_ATTR, String.valueOf(noteId));
    }

    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }

    Optional<NoteEntity> curNoteEntityOptional = noteRepository.findById(noteEntity.getNoteId());

    if (curNoteEntityOptional.isPresent()) {
      if (!independentAuthorityId.equals(curNoteEntityOptional.get().getIndependentAuthorityID())) {
        throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
      }
      final NoteEntity currentNoteEntity = curNoteEntityOptional.get();
      BeanUtils.copyProperties(noteEntity, currentNoteEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentNoteEntity); // convert the input to upper case.
      currentNoteEntity.setIndependentAuthorityID(curIndependentAuthorityEntityOptional.get().getIndependentAuthorityId());
      noteRepository.save(currentNoteEntity);
      return currentNoteEntity;
    } else {
      throw new EntityNotFoundException(NoteEntity.class, NOTE_ID_ATTR, String.valueOf(noteId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteIndependentAuthorityNote(UUID independentAuthorityId, UUID noteId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);
    Optional<NoteEntity> curNoteEntityOptional = noteRepository.findById(noteId);

    if (curIndependentAuthorityEntityOptional.isPresent() && curNoteEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      noteRepository.deleteByNoteIdAndIndependentAuthorityID(noteId, currentIndependentAuthorityEntity.getIndependentAuthorityId());
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }
}
