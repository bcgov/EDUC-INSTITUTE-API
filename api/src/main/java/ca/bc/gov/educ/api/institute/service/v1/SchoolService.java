package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolRepository;
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

  private static final String DISTRICT_ID_ATTR = "schoolId";

  @Getter(AccessLevel.PRIVATE)
  private final SchoolRepository schoolRepository;

  private final SchoolHistoryService schoolHistoryService;


  @Autowired
  public SchoolService(SchoolRepository schoolRepository, SchoolHistoryService schoolHistoryService) {
    this.schoolRepository = schoolRepository;
    this.schoolHistoryService = schoolHistoryService;
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
    val entity = entityOptional.orElseThrow(() -> new EntityNotFoundException(SchoolEntity.class, DISTRICT_ID_ATTR, schoolId.toString()));
    schoolHistoryService.deleteBySchoolID(schoolId);
    schoolRepository.delete(entity);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {EntityNotFoundException.class})
  public SchoolEntity updateSchool(School schoolUpdate, UUID schoolId) {
    var school = SchoolMapper.mapper.toModel(schoolUpdate);
    if (schoolId == null || !schoolId.equals(school.getSchoolId())) {
      throw new EntityNotFoundException(SchoolEntity.class, DISTRICT_ID_ATTR, String.valueOf(schoolId));
    }

    Optional<SchoolEntity> curSchoolEntityOptional = schoolRepository.findById(school.getSchoolId());

    if (curSchoolEntityOptional.isPresent()) {
      final SchoolEntity currentSchoolEntity = curSchoolEntityOptional.get();
      BeanUtils.copyProperties(school, currentSchoolEntity, "createDate", "createUser", "grades", "neighborhoodLearning"); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentSchoolEntity); // convert the input to upper case.
      schoolHistoryService.createSchoolHistory(currentSchoolEntity, currentSchoolEntity.getUpdateUser(), false);
      schoolRepository.save(currentSchoolEntity);
      return currentSchoolEntity;
    } else {
      throw new EntityNotFoundException(SchoolEntity.class, DISTRICT_ID_ATTR, String.valueOf(schoolId));
    }
  }
}
