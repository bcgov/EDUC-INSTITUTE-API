package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.*;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolAddress;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolContact;
import ca.bc.gov.educ.api.institute.util.BeanComparatorUtil;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.util.TransformUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchoolService {

  private static final String SCHOOL_ID_ATTR = "schoolId";

  private static final String CONTACT_ID_ATTR = "contactId";

  private static final String NOTE_ID_ATTR = "noteId";

  private static final String CREATE_DATE = "createDate";

  private static final String CREATE_USER = "createUser";
  @Getter(AccessLevel.PRIVATE)
  private final SchoolRepository schoolRepository;

  private final SchoolTombstoneRepository schoolTombstoneRepository;

  private final SchoolHistoryService schoolHistoryService;

  private final SchoolContactRepository schoolContactRepository;

  private final NoteRepository noteRepository;

  private final DistrictRepository districtRepository;

  @Autowired
  public SchoolService(SchoolRepository schoolRepository, SchoolTombstoneRepository schoolTombstoneRepository, SchoolHistoryService schoolHistoryService, SchoolContactRepository schoolContactRepository, NoteRepository noteRepository, DistrictRepository districtRepository) {
    this.schoolRepository = schoolRepository;
    this.schoolTombstoneRepository = schoolTombstoneRepository;
    this.schoolHistoryService = schoolHistoryService;
    this.schoolContactRepository = schoolContactRepository;
    this.noteRepository = noteRepository;
    this.districtRepository = districtRepository;
  }

  public List<SchoolTombstoneEntity> getAllSchoolsList() {
    return schoolTombstoneRepository.findAll();
  }

  public Optional<SchoolEntity> getSchool(UUID schoolId) {
    return schoolRepository.findById(schoolId);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public SchoolEntity createSchool(School school) {
    var schoolEntity = SchoolMapper.mapper.toModel(school);
    Optional<DistrictEntity> district = districtRepository.findById(UUID.fromString(school.getDistrictId()));
    if(district.isPresent()) {
      schoolEntity.setDistrictEntity(district.get());
    }
    TransformUtil.uppercaseFields(schoolEntity);
    schoolRepository.save(schoolEntity);
    schoolHistoryService.createSchoolHistory(schoolEntity, school.getCreateUser());
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
      BeanUtils.copyProperties(school, currentSchoolEntity, CREATE_DATE, CREATE_USER, "grades", "neighborhoodLearning", "districtEntity", "addresses"); // update current student entity with incoming payload ignoring the fields.

      setGradesAndNeighborhoodLearning(currentSchoolEntity, school);
      setAddresses(currentSchoolEntity, school);

      TransformUtil.uppercaseFields(currentSchoolEntity); // convert the input to upper case.
      var savedSchool = schoolRepository.save(currentSchoolEntity);
      schoolHistoryService.createSchoolHistory(savedSchool, savedSchool.getUpdateUser());
      return savedSchool;
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  private void setAddresses(SchoolEntity currentSchoolEntity, SchoolEntity school){
    currentSchoolEntity.getAddresses().clear();
    school.getAddresses().stream().forEach(address -> {
      RequestUtil.setAuditColumnsForAddress(address);
      TransformUtil.uppercaseFields(address);
      address.setSchoolEntity(currentSchoolEntity);
      currentSchoolEntity.getAddresses().add(address);
    });
  }

  private void setGradesAndNeighborhoodLearning(SchoolEntity currentSchoolEntity, SchoolEntity school){
    currentSchoolEntity.getGrades().clear();
    school.getGrades().stream().forEach(grade -> {
      RequestUtil.setAuditColumnsForGrades(grade);
      grade.setSchoolEntity(currentSchoolEntity);
      TransformUtil.uppercaseFields(grade);
      currentSchoolEntity.getGrades().add(grade);
    });

    currentSchoolEntity.getNeighborhoodLearning().clear();
    school.getNeighborhoodLearning().stream().forEach(neighborhoodLearning -> {
      RequestUtil.setAuditColumnsForNeighborhoodLearning(neighborhoodLearning);
      neighborhoodLearning.setSchoolEntity(currentSchoolEntity);
      TransformUtil.uppercaseFields(neighborhoodLearning);
      currentSchoolEntity.getNeighborhoodLearning().add(neighborhoodLearning);
    });
  }

  public Optional<SchoolContactEntity> getSchoolContact(UUID schoolId, UUID contactId) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      return schoolContactRepository.findBySchoolContactIdAndSchoolEntity(contactId, currentSchoolEntity);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public SchoolContactEntity createSchoolContact(SchoolContact contact, UUID schoolId) {
    var contactEntity = SchoolContactMapper.mapper.toModel(contact);
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      contactEntity.setSchoolEntity(curSchoolEntityOptional.get());
      TransformUtil.uppercaseFields(contactEntity);
      schoolContactRepository.save(contactEntity);
      return contactEntity;
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public SchoolContactEntity updateSchoolContact(SchoolContact contact, UUID schoolId, UUID contactId) {
    var contactEntity = SchoolContactMapper.mapper.toModel(contact);
    if (contactId == null || !contactId.equals(contactEntity.getSchoolContactId())) {
      throw new EntityNotFoundException(SchoolContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }

    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }

    Optional<SchoolContactEntity> curContactEntityOptional = schoolContactRepository.findById(contactEntity.getSchoolContactId());

    if (curContactEntityOptional.isPresent()) {
      if (!schoolId.equals(curContactEntityOptional.get().getSchoolEntity().getSchoolId())) {
        throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
      }
      final SchoolContactEntity currentContactEntity = curContactEntityOptional.get();
      BeanUtils.copyProperties(contactEntity, currentContactEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentContactEntity); // convert the input to upper case.
      currentContactEntity.setSchoolEntity(curSchoolEntityOptional.get());
      schoolContactRepository.save(currentContactEntity);
      return currentContactEntity;
    } else {
      throw new EntityNotFoundException(SchoolContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteSchoolContact(UUID schoolId, UUID contactId) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      schoolContactRepository.deleteBySchoolContactIdAndSchoolEntity(contactId, currentSchoolEntity);
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
