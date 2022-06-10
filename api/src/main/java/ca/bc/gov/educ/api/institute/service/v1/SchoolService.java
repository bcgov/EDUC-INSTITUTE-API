package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.AddressMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.ContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.model.v1.AddressEntity;
import ca.bc.gov.educ.api.institute.model.v1.ContactEntity;
import ca.bc.gov.educ.api.institute.model.v1.NoteEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.repository.v1.AddressRepository;
import ca.bc.gov.educ.api.institute.repository.v1.ContactRepository;
import ca.bc.gov.educ.api.institute.repository.v1.NoteRepository;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolRepository;
import ca.bc.gov.educ.api.institute.struct.v1.Address;
import ca.bc.gov.educ.api.institute.struct.v1.Contact;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
import ca.bc.gov.educ.api.institute.struct.v1.School;
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
public class SchoolService {

  private static final String SCHOOL_ID_ATTR = "schoolId";

  private static final String CONTACT_ID_ATTR = "contactId";

  private static final String ADDRESS_ID_ATTR = "addressId";

  private static final String NOTE_ID_ATTR = "noteId";

  private static final String CREATE_DATE = "createDate";

  private static final String CREATE_USER = "createUser";
  @Getter(AccessLevel.PRIVATE)
  private final SchoolRepository schoolRepository;

  private final SchoolHistoryService schoolHistoryService;

  private final AddressHistoryService addressHistoryService;

  private final ContactRepository contactRepository;

  private final AddressRepository addressRepository;

  private final NoteRepository noteRepository;

  @Autowired
  public SchoolService(SchoolRepository schoolRepository, SchoolHistoryService schoolHistoryService, AddressHistoryService addressHistoryService, ContactRepository contactRepository, AddressRepository addressRepository, NoteRepository noteRepository) {
    this.schoolRepository = schoolRepository;
    this.schoolHistoryService = schoolHistoryService;
    this.addressHistoryService = addressHistoryService;
    this.contactRepository = contactRepository;
    this.addressRepository = addressRepository;
    this.noteRepository = noteRepository;
  }

  public List<SchoolEntity> getAllSchoolsList() {
    return schoolRepository.findAll();
  }

  public Optional<SchoolEntity> getSchool(UUID schoolId) {
    return schoolRepository.findById(schoolId);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public SchoolEntity createSchool(School school) {
    var schoolEntity = SchoolMapper.mapper.toModel(school);
    TransformUtil.uppercaseFields(schoolEntity);
    schoolRepository.save(schoolEntity);
    schoolHistoryService.createSchoolHistory(schoolEntity, school.getCreateUser(), false);
    return schoolEntity;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteSchool(UUID schoolId) {
    val entityOptional = schoolRepository.findById(schoolId);
    val entity = entityOptional.orElseThrow(() -> new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, schoolId.toString()));
    schoolHistoryService.deleteBySchoolID(schoolId);
    schoolRepository.delete(entity);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {EntityNotFoundException.class})
  public SchoolEntity updateSchool(School schoolUpdate, UUID schoolId) {
    var school = SchoolMapper.mapper.toModel(schoolUpdate);
    if (schoolId == null || !schoolId.equals(school.getSchoolId())) {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }

    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(school.getSchoolId());

    if (curSchoolEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      BeanUtils.copyProperties(school, currentSchoolEntity, CREATE_DATE, CREATE_USER, "grades", "neighborhoodLearning"); // update current student entity with incoming payload ignoring the fields.

      currentSchoolEntity.getGrades().clear();
      currentSchoolEntity.getNeighborhoodLearning().clear();

      currentSchoolEntity.getGrades().addAll(school.getGrades());
      currentSchoolEntity.getNeighborhoodLearning().addAll(school.getNeighborhoodLearning());

      TransformUtil.uppercaseFields(currentSchoolEntity); // convert the input to upper case.
      schoolHistoryService.createSchoolHistory(currentSchoolEntity, currentSchoolEntity.getUpdateUser(), false);
      schoolRepository.save(currentSchoolEntity);
      return currentSchoolEntity;
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  public Optional<ContactEntity> getSchoolContact(UUID schoolId, UUID contactId) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      return contactRepository.findByContactIdAndSchoolEntity(contactId, currentSchoolEntity);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ContactEntity createSchoolContact(Contact contact, UUID schoolId) {
    var contactEntity = ContactMapper.mapper.toModel(contact);
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      contactEntity.setSchoolEntity(curSchoolEntityOptional.get());
      TransformUtil.uppercaseFields(contactEntity);
      contactRepository.save(contactEntity);
      return contactEntity;
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ContactEntity updateSchoolContact(Contact contact, UUID schoolId, UUID contactId) {
    var contactEntity = ContactMapper.mapper.toModel(contact);
    if (contactId == null || !contactId.equals(contactEntity.getContactId())) {
      throw new EntityNotFoundException(ContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }

    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }

    Optional<ContactEntity> curContactEntityOptional = contactRepository.findById(contactEntity.getContactId());

    if (curContactEntityOptional.isPresent()) {
      if (!schoolId.equals(curContactEntityOptional.get().getSchoolEntity().getSchoolId())) {
        throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
      }
      final ContactEntity currentContactEntity = curContactEntityOptional.get();
      BeanUtils.copyProperties(contactEntity, currentContactEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentContactEntity); // convert the input to upper case.
      currentContactEntity.setSchoolEntity(curSchoolEntityOptional.get());
      contactRepository.save(currentContactEntity);
      return currentContactEntity;
    } else {
      throw new EntityNotFoundException(ContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteSchoolContact(UUID schoolId, UUID contactId) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      contactRepository.deleteByContactIdAndSchoolEntity(contactId, currentSchoolEntity);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }

  }

  public Optional<AddressEntity> getSchoolAddress(UUID schoolId, UUID addressId) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      return addressRepository.findByAddressIdAndSchoolEntity(addressId, currentSchoolEntity);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public AddressEntity createSchoolAddress(Address address, UUID schoolId) {
    var addressEntity = AddressMapper.mapper.toModel(address);
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      addressEntity.setSchoolEntity(curSchoolEntityOptional.get());
      TransformUtil.uppercaseFields(addressEntity);
      addressRepository.save(addressEntity);
      addressHistoryService.createAddressHistory(addressEntity, address.getCreateUser(), false);
      return addressEntity;
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public AddressEntity updateSchoolAddress(Address address, UUID schoolId, UUID addressId) {
    var addressEntity = AddressMapper.mapper.toModel(address);
    if (addressId == null || !addressId.equals(addressEntity.getAddressId())) {
      throw new EntityNotFoundException(AddressEntity.class, ADDRESS_ID_ATTR, String.valueOf(addressId));
    }

    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }

    Optional<AddressEntity> curAddressEntityOptional = addressRepository.findById(addressEntity.getAddressId());

    if (curAddressEntityOptional.isPresent()) {
      if (!schoolId.equals(curAddressEntityOptional.get().getSchoolEntity().getSchoolId())) {
        throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
      }
      final AddressEntity currentAddressEntity = curAddressEntityOptional.get();
      BeanUtils.copyProperties(addressEntity, currentAddressEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentAddressEntity); // convert the input to upper case.
      currentAddressEntity.setSchoolEntity(curSchoolEntityOptional.get());
      addressHistoryService.createAddressHistory(currentAddressEntity, currentAddressEntity.getUpdateUser(), false);
      addressRepository.save(currentAddressEntity);
      return currentAddressEntity;
    } else {
      throw new EntityNotFoundException(AddressEntity.class, ADDRESS_ID_ATTR, String.valueOf(addressId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteSchoolAddress(UUID schoolId, UUID addressId) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);
    Optional<AddressEntity> curAddressEntityOptional = addressRepository.findById(addressId);

    if (curSchoolEntityOptional.isPresent() && curAddressEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      addressHistoryService.deleteByAddressID(addressId);
      addressRepository.deleteByAddressIdAndSchoolEntity(addressId, currentSchoolEntity);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  public Optional<NoteEntity> getSchoolNote(UUID schoolId, UUID noteId) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      return noteRepository.findByNoteIdAndSchoolEntity(noteId, currentSchoolEntity);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public NoteEntity createSchoolNote(Note note, UUID schoolId) {
    var noteEntity = NoteMapper.mapper.toModel(note);
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      noteEntity.setSchoolEntity(curSchoolEntityOptional.get());
      TransformUtil.uppercaseFields(noteEntity);
      noteRepository.save(noteEntity);
      return noteEntity;
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public NoteEntity updateSchoolNote(Note note, UUID schoolId, UUID noteId) {
    var noteEntity = NoteMapper.mapper.toModel(note);
    if (noteId == null || !noteId.equals(noteEntity.getNoteId())) {
      throw new EntityNotFoundException(NoteEntity.class, NOTE_ID_ATTR, String.valueOf(noteId));
    }

    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }

    Optional<NoteEntity> curNoteEntityOptional = noteRepository.findById(noteEntity.getNoteId());

    if (curNoteEntityOptional.isPresent()) {
      if (!schoolId.equals(curNoteEntityOptional.get().getSchoolEntity().getSchoolId())) {
        throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
      }
      final NoteEntity currentNoteEntity = curNoteEntityOptional.get();
      BeanUtils.copyProperties(noteEntity, currentNoteEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentNoteEntity); // convert the input to upper case.
      currentNoteEntity.setSchoolEntity(curSchoolEntityOptional.get());
      noteRepository.save(currentNoteEntity);
      return currentNoteEntity;
    } else {
      throw new EntityNotFoundException(NoteEntity.class, NOTE_ID_ATTR, String.valueOf(noteId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteSchoolNote(UUID schoolId, UUID noteId) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);
    Optional<NoteEntity> curNoteEntityOptional = noteRepository.findById(noteId);

    if (curSchoolEntityOptional.isPresent() && curNoteEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      noteRepository.deleteByNoteIdAndSchoolEntity(noteId, currentSchoolEntity);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }
}
