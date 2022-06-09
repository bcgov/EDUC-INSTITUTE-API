package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.AddressMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.ContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.DistrictMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.model.v1.AddressEntity;
import ca.bc.gov.educ.api.institute.model.v1.ContactEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.model.v1.NoteEntity;
import ca.bc.gov.educ.api.institute.repository.v1.AddressRepository;
import ca.bc.gov.educ.api.institute.repository.v1.ContactRepository;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictRepository;
import ca.bc.gov.educ.api.institute.repository.v1.NoteRepository;
import ca.bc.gov.educ.api.institute.struct.v1.Address;
import ca.bc.gov.educ.api.institute.struct.v1.Contact;
import ca.bc.gov.educ.api.institute.struct.v1.District;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
import ca.bc.gov.educ.api.institute.util.TransformUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DistrictService {

  private static final String DISTRICT_ID_ATTR = "districtId";

  private static final String CONTACT_ID_ATTR = "contactId";

  private static final String ADDRESS_ID_ATTR = "addressId";

  private static final String NOTE_ID_ATTR = "noteId";

  private static final String CREATE_DATE = "createDate";
  
  private static final String CREATE_USER = "createUser";

  @Getter(AccessLevel.PRIVATE)
  private final DistrictRepository districtRepository;

  private final DistrictHistoryService districtHistoryService;

  private final AddressHistoryService addressHistoryService;

  private final ContactRepository contactRepository;

  private final AddressRepository addressRepository;

  private final NoteRepository noteRepository;


  @Autowired
  public DistrictService(DistrictRepository districtRepository, DistrictHistoryService districtHistoryService, ContactRepository contactRepository, AddressRepository addressRepository, NoteRepository noteRepository, AddressHistoryService addressHistoryService) {
    this.districtRepository = districtRepository;
    this.districtHistoryService = districtHistoryService;
    this.contactRepository = contactRepository;
    this.addressRepository = addressRepository;
    this.noteRepository = noteRepository;
    this.addressHistoryService = addressHistoryService;
  }

  public List<DistrictEntity> getAllDistrictsList() {
    return districtRepository.findAll();
  }

  public Optional<DistrictEntity> getDistrict(UUID districtId) {
    return districtRepository.findById(districtId);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public DistrictEntity createDistrict(District district) {
    var districtEntity = DistrictMapper.mapper.toModel(district);
    TransformUtil.uppercaseFields(districtEntity);
    districtRepository.save(districtEntity);
    districtHistoryService.createDistrictHistory(districtEntity, district.getCreateUser(), false);
    return districtEntity;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteDistrict(UUID districtId) {
    val entityOptional = districtRepository.findById(districtId);
    val entity = entityOptional.orElseThrow(() -> new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, districtId.toString()));
    districtHistoryService.deleteByDistrictID(districtId);
    districtRepository.delete(entity);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {EntityNotFoundException.class})
  public DistrictEntity updateDistrict(District districtUpdate, UUID districtId) {
    var district = DistrictMapper.mapper.toModel(districtUpdate);
    if (districtId == null || !districtId.equals(district.getDistrictId())) {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }

    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(district.getDistrictId());

    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      BeanUtils.copyProperties(district, currentDistrictEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentDistrictEntity); // convert the input to upper case.
      districtHistoryService.createDistrictHistory(currentDistrictEntity, currentDistrictEntity.getUpdateUser(), false);
      districtRepository.save(currentDistrictEntity);
      return currentDistrictEntity;
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  public Optional<ContactEntity> getDistrictContact(UUID districtId, UUID contactId) {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      return contactRepository.findByContactIdAndDistrictEntity(contactId, currentDistrictEntity);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ContactEntity createDistrictContact(Contact contact, UUID districtId) {
    var contactEntity = ContactMapper.mapper.toModel(contact);
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      contactEntity.setDistrictEntity(curDistrictEntityOptional.get());
      TransformUtil.uppercaseFields(contactEntity);
      contactRepository.save(contactEntity);
      return contactEntity;
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ContactEntity updateDistrictContact(Contact contact, UUID districtId, UUID contactId) {
    var contactEntity = ContactMapper.mapper.toModel(contact);
    if (contactId == null || !contactId.equals(contactEntity.getContactId())) {
      throw new EntityNotFoundException(ContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }

    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }

    Optional<ContactEntity> curContactEntityOptional = contactRepository.findById(contactEntity.getContactId());

    if (curContactEntityOptional.isPresent()) {
      if (!districtId.equals(curContactEntityOptional.get().getDistrictEntity().getDistrictId())) {
        throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
      }
      final ContactEntity currentContactEntity = curContactEntityOptional.get();
      BeanUtils.copyProperties(contactEntity, currentContactEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentContactEntity); // convert the input to upper case.
      currentContactEntity.setDistrictEntity(curDistrictEntityOptional.get());
      contactRepository.save(currentContactEntity);
      return currentContactEntity;
    } else {
      throw new EntityNotFoundException(ContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteDistrictContact(UUID districtId, UUID contactId) {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      contactRepository.deleteByContactIdAndDistrictEntity(contactId, currentDistrictEntity);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }

  }

  public Optional<AddressEntity> getDistrictAddress(UUID districtId, UUID addressId) {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      return addressRepository.findByAddressIdAndDistrictEntity(addressId, currentDistrictEntity);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public AddressEntity createDistrictAddress(Address address, UUID districtId) {
    var addressEntity = AddressMapper.mapper.toModel(address);
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      addressEntity.setDistrictEntity(curDistrictEntityOptional.get());
      TransformUtil.uppercaseFields(addressEntity);
      addressRepository.save(addressEntity);
      addressHistoryService.createAddressHistory(addressEntity, address.getCreateUser(), false);
      return addressEntity;
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public AddressEntity updateDistrictAddress(Address address, UUID districtId, UUID addressId) {
    var addressEntity = AddressMapper.mapper.toModel(address);
    if (addressId == null || !addressId.equals(addressEntity.getAddressId())) {
      throw new EntityNotFoundException(AddressEntity.class, ADDRESS_ID_ATTR, String.valueOf(addressId));
    }

    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }

    Optional<AddressEntity> curAddressEntityOptional = addressRepository.findById(addressEntity.getAddressId());

    if (curAddressEntityOptional.isPresent()) {
      if (!districtId.equals(curAddressEntityOptional.get().getDistrictEntity().getDistrictId())) {
        throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
      }
      final AddressEntity currentAddressEntity = curAddressEntityOptional.get();
      BeanUtils.copyProperties(addressEntity, currentAddressEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentAddressEntity); // convert the input to upper case.
      currentAddressEntity.setDistrictEntity(curDistrictEntityOptional.get());
      addressHistoryService.createAddressHistory(currentAddressEntity, currentAddressEntity.getUpdateUser(), false);
      addressRepository.save(currentAddressEntity);
      return currentAddressEntity;
    } else {
      throw new EntityNotFoundException(AddressEntity.class, ADDRESS_ID_ATTR, String.valueOf(addressId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteDistrictAddress(UUID districtId, UUID addressId) {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);
    Optional<AddressEntity> curAddressEntityOptional = addressRepository.findById(addressId);

    if (curDistrictEntityOptional.isPresent() && curAddressEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      addressHistoryService.deleteByAddressID(addressId);
      addressRepository.deleteByAddressIdAndDistrictEntity(addressId, currentDistrictEntity);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  public Optional<NoteEntity> getDistrictNote(UUID districtId, UUID noteId) {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      return noteRepository.findByNoteIdAndDistrictEntity(noteId, currentDistrictEntity);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public NoteEntity createDistrictNote(Note note, UUID districtId) {
    var noteEntity = NoteMapper.mapper.toModel(note);
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      noteEntity.setDistrictEntity(curDistrictEntityOptional.get());
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
      if (!districtId.equals(curNoteEntityOptional.get().getDistrictEntity().getDistrictId())) {
        throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
      }
      final NoteEntity currentNoteEntity = curNoteEntityOptional.get();
      BeanUtils.copyProperties(noteEntity, currentNoteEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentNoteEntity); // convert the input to upper case.
      currentNoteEntity.setDistrictEntity(curDistrictEntityOptional.get());
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
      noteRepository.deleteByNoteIdAndDistrictEntity(noteId, currentDistrictEntity);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }
}
