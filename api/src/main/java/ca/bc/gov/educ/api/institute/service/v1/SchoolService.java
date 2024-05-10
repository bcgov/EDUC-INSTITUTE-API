package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.*;
import ca.bc.gov.educ.api.institute.struct.v1.*;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ca.bc.gov.educ.api.institute.constants.v1.EventOutcome.*;
import static ca.bc.gov.educ.api.institute.constants.v1.EventType.*;

@Service
@Slf4j
public class SchoolService {

  private static final String SCHOOL_ID_ATTR = "schoolId";

  private static final String CONTACT_ID_ATTR = "contactId";

  private static final String NOTE_ID_ATTR = "noteId";

  private static final String CREATE_DATE = "createDate";

  private static final String CREATE_USER = "createUser";

  private static final String FROM_SCHOOL_ID_ATTR = "fromSchoolId";
  @Getter(AccessLevel.PRIVATE)
  private final SchoolRepository schoolRepository;
  private final SchoolMoveRepository schoolMoveRepository;
  private final InstituteEventRepository instituteEventRepository;

  private final SchoolTombstoneRepository schoolTombstoneRepository;

  private final DistrictTombstoneRepository districtTombstoneRepository;

  private final SchoolHistoryService schoolHistoryService;

  private final SchoolContactRepository schoolContactRepository;

  private final NoteRepository noteRepository;

  private final SchoolNumberGenerationService schoolNumberGenerationService;


  @Autowired
  public SchoolService(SchoolRepository schoolRepository,
      SchoolTombstoneRepository schoolTombstoneRepository,
      SchoolMoveRepository schoolMoveRepository,
      DistrictTombstoneRepository districtTombstoneRepository,
      SchoolHistoryService schoolHistoryService, SchoolContactRepository schoolContactRepository,
      NoteRepository noteRepository, DistrictRepository districtRepository,
      InstituteEventRepository instituteEventRepository,
      SchoolNumberGenerationService schoolNumberGenerationService) {
    this.schoolRepository = schoolRepository;
    this.schoolTombstoneRepository = schoolTombstoneRepository;
    this.schoolMoveRepository = schoolMoveRepository;
    this.districtTombstoneRepository = districtTombstoneRepository;
    this.schoolHistoryService = schoolHistoryService;
    this.schoolContactRepository = schoolContactRepository;
    this.noteRepository = noteRepository;
    this.instituteEventRepository = instituteEventRepository;
    this.schoolNumberGenerationService = schoolNumberGenerationService;
  }

  public List<SchoolTombstoneEntity> getAllSchoolsList() {
    return schoolTombstoneRepository.findAll();
  }

  public Optional<SchoolEntity> getSchool(UUID schoolId) {
    return schoolRepository.findById(schoolId);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Pair<SchoolEntity, InstituteEvent> createSchool(School school)
      throws JsonProcessingException {

    SchoolEntity schoolEntity = createSchoolHelper(school);
    final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(
        schoolEntity.getUpdateUser(), schoolEntity.getUpdateUser(),
        JsonUtil.getJsonStringFromObject(SchoolMapper.mapper.toStructure(schoolEntity)),
        CREATE_SCHOOL, SCHOOL_CREATED);
    instituteEventRepository.save(instituteEvent);
    return Pair.of(schoolEntity, instituteEvent);
  }

  private SchoolEntity createSchoolHelper(School school) {
    var schoolEntity = SchoolMapper.mapper.toModel(school);
    Optional<DistrictTombstoneEntity> district = districtTombstoneRepository.findById(
        UUID.fromString(school.getDistrictId()));
    if (district.isPresent()) {
      schoolEntity.setDistrictEntity(district.get());

      schoolEntity.setSchoolNumber(schoolNumberGenerationService.generateSchoolNumber(district.get().getDistrictNumber(),
              school.getFacilityTypeCode(), school.getSchoolCategoryCode(),
              school.getIndependentAuthorityId()));
    } else {
      throw new EntityNotFoundException(DistrictTombstoneEntity.class, school.getDistrictId(), school.getSchoolId());
    }

    schoolEntity.getAddresses().stream().forEach(address -> {
      RequestUtil.setAuditColumnsForAddress(address);
      TransformUtil.uppercaseFields(address);
      address.setSchoolEntity(schoolEntity);
    });

    schoolEntity.getGrades().stream().forEach(grade -> {
      RequestUtil.setAuditColumnsForGrades(grade);
      TransformUtil.uppercaseFields(grade);
      grade.setSchoolEntity(schoolEntity);
    });

    schoolEntity.getNeighborhoodLearning().stream().forEach(neighborhoodLearning -> {
      RequestUtil.setAuditColumnsForNeighborhoodLearning(neighborhoodLearning);
      TransformUtil.uppercaseFields(neighborhoodLearning);
      neighborhoodLearning.setSchoolEntity(schoolEntity);
    });

    TransformUtil.uppercaseFields(schoolEntity);
    schoolRepository.save(schoolEntity);
    schoolHistoryService.createSchoolHistory(schoolEntity, school.getCreateUser());

    return schoolEntity;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteSchool(UUID schoolId) {
    val entityOptional = schoolRepository.findById(schoolId);
    val entity = entityOptional.orElseThrow(
        () -> new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, schoolId.toString()));
    schoolHistoryService.deleteBySchoolID(schoolId);
    schoolRepository.delete(entity);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {
      EntityNotFoundException.class})
  public Pair<SchoolEntity, InstituteEvent> updateSchool(School schoolUpdate, UUID schoolId)
      throws JsonProcessingException {
    var school = SchoolMapper.mapper.toModel(schoolUpdate);
    if (schoolId == null || !schoolId.equals(school.getSchoolId())) {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR,
          String.valueOf(schoolId));
    }

    var savedSchool = updateSchoolHelper(school);

    final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(
        savedSchool.getUpdateUser(), savedSchool.getUpdateUser(),
        JsonUtil.getJsonStringFromObject(SchoolMapper.mapper.toStructure(savedSchool)),
        UPDATE_SCHOOL, SCHOOL_UPDATED);
    instituteEventRepository.save(instituteEvent);

    return Pair.of(savedSchool, instituteEvent);
  }

  private SchoolEntity updateSchoolHelper(SchoolEntity school) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(
        school.getSchoolId());

    if (curSchoolEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      BeanUtils.copyProperties(school, currentSchoolEntity, CREATE_DATE, CREATE_USER, "grades",
          "neighborhoodLearning", "districtEntity", "schoolFundingGroups",
          "addresses"); // update current student entity with incoming payload ignoring the fields.

      setGradesAndNeighborhoodLearning(currentSchoolEntity, school);
      setAddresses(currentSchoolEntity, school);
      setSchoolFundingGroups(currentSchoolEntity, school);

      return saveSchoolWithHistory(currentSchoolEntity);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR,
          String.valueOf(school.getSchoolId()));
    }
  }

  private SchoolEntity saveSchoolWithHistory(SchoolEntity currentSchoolEntity) {
    TransformUtil.uppercaseFields(currentSchoolEntity); // convert the input to upper case.
    SchoolEntity savedSchool = schoolRepository.save(currentSchoolEntity);
    schoolHistoryService.createSchoolHistory(savedSchool, savedSchool.getUpdateUser());

    return savedSchool;
  }

  private void setAddresses(SchoolEntity currentSchoolEntity, SchoolEntity school) {
    currentSchoolEntity.getAddresses().clear();
    school.getAddresses().stream().forEach(address -> {
      RequestUtil.setAuditColumnsForAddress(address);
      TransformUtil.uppercaseFields(address);
      address.setSchoolEntity(currentSchoolEntity);
      currentSchoolEntity.getAddresses().add(address);
    });
  }

  private void setSchoolFundingGroups(SchoolEntity currentSchoolEntity, SchoolEntity school) {
    currentSchoolEntity.getSchoolFundingGroups().clear();
    school.getSchoolFundingGroups().stream().forEach(group -> {
      RequestUtil.setAuditColumnsForFundingGroups(group);
      group.setSchoolEntity(currentSchoolEntity);
      TransformUtil.uppercaseFields(group);
      currentSchoolEntity.getSchoolFundingGroups().add(group);
    });
  }

  private void setGradesAndNeighborhoodLearning(SchoolEntity currentSchoolEntity, SchoolEntity school) {
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
      return schoolContactRepository.findBySchoolContactIdAndSchoolEntity(contactId,
          currentSchoolEntity);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR,
          String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Pair<SchoolContactEntity, InstituteEvent> createSchoolContact(SchoolContact contact, UUID schoolId) throws JsonProcessingException {
    var contactEntity = SchoolContactMapper.mapper.toModel(contact);
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      contactEntity.setSchoolEntity(curSchoolEntityOptional.get());
      TransformUtil.uppercaseFields(contactEntity);
      schoolContactRepository.save(contactEntity);
      final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(
              contact.getCreateUser(), contact.getUpdateUser(),
              JsonUtil.getJsonStringFromObject(SchoolContactMapper.mapper.toStructure(contactEntity)),
              CREATE_CONTACT, CONTACT_CREATED
      );
      instituteEventRepository.save(instituteEvent);
      return Pair.of(contactEntity, instituteEvent);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR,
          String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public SchoolContactEntity updateSchoolContact(SchoolContact contact, UUID schoolId,
      UUID contactId) {
    var contactEntity = SchoolContactMapper.mapper.toModel(contact);
    if (contactId == null || !contactId.equals(contactEntity.getSchoolContactId())) {
      throw new EntityNotFoundException(SchoolContactEntity.class, CONTACT_ID_ATTR,
          String.valueOf(contactId));
    }

    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR,
          String.valueOf(schoolId));
    }

    Optional<SchoolContactEntity> curContactEntityOptional = schoolContactRepository.findById(
        contactEntity.getSchoolContactId());

    if (curContactEntityOptional.isPresent()) {
      if (!schoolId.equals(curContactEntityOptional.get().getSchoolEntity().getSchoolId())) {
        throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR,
            String.valueOf(schoolId));
      }
      final SchoolContactEntity currentContactEntity = curContactEntityOptional.get();
      BeanUtils.copyProperties(contactEntity, currentContactEntity, CREATE_DATE,
          CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentContactEntity); // convert the input to upper case.
      currentContactEntity.setSchoolEntity(curSchoolEntityOptional.get());
      schoolContactRepository.save(currentContactEntity);
      return currentContactEntity;
    } else {
      throw new EntityNotFoundException(SchoolContactEntity.class, CONTACT_ID_ATTR,
          String.valueOf(contactId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteSchoolContact(UUID schoolId, UUID contactId) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      schoolContactRepository.deleteBySchoolContactIdAndSchoolEntity(contactId,
          currentSchoolEntity);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR,
          String.valueOf(schoolId));
    }
  }

  public Optional<NoteEntity> getSchoolNote(UUID schoolId, UUID noteId) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      return noteRepository.findByNoteIdAndSchoolID(noteId, currentSchoolEntity.getSchoolId());
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  public List<NoteEntity> getSchoolNotes(UUID schoolId) {
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      return noteRepository.findBySchoolID(schoolId);
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR, String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public NoteEntity createSchoolNote(Note note, UUID schoolId) {
    var noteEntity = NoteMapper.mapper.toModel(note);
    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(schoolId);

    if (curSchoolEntityOptional.isPresent()) {
      noteEntity.setSchoolID(curSchoolEntityOptional.get().getSchoolId());
      TransformUtil.uppercaseFields(noteEntity);
      noteRepository.save(noteEntity);
      return noteEntity;
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR,
          String.valueOf(schoolId));
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
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR,
          String.valueOf(schoolId));
    }

    Optional<NoteEntity> curNoteEntityOptional = noteRepository.findById(noteEntity.getNoteId());

    if (curNoteEntityOptional.isPresent()) {
      if (!schoolId.equals(curNoteEntityOptional.get().getSchoolID())) {
        throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR,
            String.valueOf(schoolId));
      }
      final NoteEntity currentNoteEntity = curNoteEntityOptional.get();
      BeanUtils.copyProperties(noteEntity, currentNoteEntity, CREATE_DATE,
          CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentNoteEntity); // convert the input to upper case.
      currentNoteEntity.setSchoolID(curSchoolEntityOptional.get().getSchoolId());
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
      noteRepository.deleteByNoteIdAndSchoolID(noteId, currentSchoolEntity.getSchoolId());
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, SCHOOL_ID_ATTR,
          String.valueOf(schoolId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Pair<MoveSchoolData, InstituteEvent> moveSchool(MoveSchoolData moveSchoolData)
      throws JsonProcessingException {

    val schoolEntityOptional = schoolRepository.findById(
            UUID.fromString(moveSchoolData.getFromSchoolId()));
    SchoolEntity fromSchoolEntity = schoolEntityOptional.orElseThrow(
            () -> new EntityNotFoundException(SchoolEntity.class, FROM_SCHOOL_ID_ATTR,
                    moveSchoolData.getFromSchoolId()));

    SchoolEntity movedSchool = moveSchoolHelper(moveSchoolData.getToSchool(), SchoolMapper.mapper.toStructure(fromSchoolEntity));
    log.info("School created for move schoolId :: {}", movedSchool.getSchoolId());

    fromSchoolEntity.setClosedDate(LocalDateTime.parse(moveSchoolData.getMoveDate()));
    fromSchoolEntity.setUpdateUser(moveSchoolData.getToSchool().getCreateUser());
    fromSchoolEntity.setUpdateDate(LocalDateTime.now());

    SchoolEntity savedSchool = saveSchoolWithHistory(fromSchoolEntity);
    log.info("Close date set to {} for schoolId :: {}", savedSchool.getClosedDate(),
        fromSchoolEntity.getSchoolId());

    SchoolMoveEntity schoolMoveEntity = SchoolMoveEntity.builder()
        .toSchoolId(movedSchool.getSchoolId())
        .fromSchoolId(savedSchool.getSchoolId())
        .moveDate(LocalDateTime.parse(moveSchoolData.getMoveDate()))
        .createUser(moveSchoolData.getCreateUser())
        .updateUser(moveSchoolData.getCreateUser())
        .build();

    RequestUtil.setAuditColumnsForSchoolMove(schoolMoveEntity);

    schoolMoveRepository.save(schoolMoveEntity);

    moveSchoolData.setToSchool(SchoolMapper.mapper.toStructure(movedSchool));

    final InstituteEvent instituteEvent = EventUtil.createInstituteEvent(
        movedSchool.getUpdateUser(), movedSchool.getUpdateUser(),
        JsonUtil.getJsonStringFromObject(moveSchoolData), MOVE_SCHOOL, SCHOOL_MOVED);
    return Pair.of(moveSchoolData, instituteEvent);
  }

  private SchoolEntity moveSchoolHelper(School school, School existingSchool) {
    var schoolEntity = SchoolMapper.mapper.toModel(school);
    Optional<DistrictTombstoneEntity> district = districtTombstoneRepository.findById(
            UUID.fromString(school.getDistrictId()));
    if (district.isPresent()) {
      schoolEntity.setDistrictEntity(district.get());

      List<SchoolEntity> schools = schoolRepository.findBySchoolNumberAndDistrictID(
              school.getSchoolNumber(), UUID.fromString(school.getDistrictId()));

      if(school.getSchoolCategoryCode().equals(existingSchool.getSchoolCategoryCode())
              && school.getFacilityTypeCode().equals(existingSchool.getFacilityTypeCode()) && schools.isEmpty()) {
        schoolEntity.setSchoolNumber(school.getSchoolNumber());
      } else {
        schoolEntity.setSchoolNumber(
                schoolNumberGenerationService.generateSchoolNumber(district.get().getDistrictNumber(),
                        school.getFacilityTypeCode(), school.getSchoolCategoryCode(),
                        school.getIndependentAuthorityId()));
      }
    } else {
      throw new EntityNotFoundException(DistrictTombstoneEntity.class, school.getDistrictId(), school.getSchoolId());
    }

    schoolEntity.getAddresses().stream().forEach(address -> {
      RequestUtil.setAuditColumnsForAddress(address);
      TransformUtil.uppercaseFields(address);
      address.setSchoolEntity(schoolEntity);
    });

    schoolEntity.getGrades().stream().forEach(grade -> {
      RequestUtil.setAuditColumnsForGrades(grade);
      TransformUtil.uppercaseFields(grade);
      grade.setSchoolEntity(schoolEntity);
    });

    schoolEntity.getNeighborhoodLearning().stream().forEach(neighborhoodLearning -> {
      RequestUtil.setAuditColumnsForNeighborhoodLearning(neighborhoodLearning);
      TransformUtil.uppercaseFields(neighborhoodLearning);
      neighborhoodLearning.setSchoolEntity(schoolEntity);
    });

    TransformUtil.uppercaseFields(schoolEntity);
    schoolRepository.save(schoolEntity);
    schoolHistoryService.createSchoolHistory(schoolEntity, school.getCreateUser());

    return schoolEntity;
  }
}
