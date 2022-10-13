package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolHistoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SchoolHistoryService {

  private final SchoolHistoryRepository schoolHistoryRepository;

  @Autowired
  public SchoolHistoryService(SchoolHistoryRepository schoolHistoryRepository) {
    this.schoolHistoryRepository = schoolHistoryRepository;
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void deleteBySchoolID(final UUID schoolId) {
    schoolHistoryRepository.deleteBySchoolId(schoolId);
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void createSchoolHistory(SchoolEntity curSchoolEntity, String updateUser, boolean copyAudit) {
    final SchoolHistoryEntity schoolHistoryEntity = new SchoolHistoryEntity();
    BeanUtils.copyProperties(curSchoolEntity, schoolHistoryEntity);
    schoolHistoryEntity.setCreateUser(updateUser);
    if (!copyAudit) {
      schoolHistoryEntity.setCreateDate(LocalDateTime.now());
    }
    schoolHistoryEntity.setUpdateUser(updateUser);
    schoolHistoryEntity.setUpdateDate(LocalDateTime.now());
    mapGradeCodeHistory(curSchoolEntity, schoolHistoryEntity);
    mapNeighbourhoodLearningHistory(curSchoolEntity, schoolHistoryEntity);
    schoolHistoryRepository.save(schoolHistoryEntity);
  }

  private void mapNeighbourhoodLearningHistory(SchoolEntity curSchoolEntity, SchoolHistoryEntity schoolHistoryEntity) {
    if (!CollectionUtils.isEmpty(curSchoolEntity.getNeighborhoodLearning())) {
      schoolHistoryEntity.getNeighbourhoodLearnings()
        .addAll(curSchoolEntity.getNeighborhoodLearning().stream()
          .map(el -> NeighbourhoodLearningSchoolHistoryEntity.builder()
            .schoolHistoryEntity(schoolHistoryEntity)
            .neighbourhoodLearningTypeCode(el.getNeighborhoodLearningTypeCode())
            .createDate(schoolHistoryEntity.getCreateDate())
            .updateDate(schoolHistoryEntity.getUpdateDate())
            .createUser(schoolHistoryEntity.getCreateUser())
            .updateUser(schoolHistoryEntity.getUpdateUser())
            .build())
          .collect(Collectors.toList()));

    }
  }

  private void mapGradeCodeHistory(SchoolEntity curSchoolEntity, SchoolHistoryEntity schoolHistoryEntity) {
    if (!CollectionUtils.isEmpty(curSchoolEntity.getGrades())) {
      schoolHistoryEntity.getSchoolGrades()
        .addAll(curSchoolEntity.getGrades().stream()
          .map(el -> SchoolGradeSchoolHistoryEntity.builder()
            .schoolHistoryEntity(schoolHistoryEntity)
            .schoolGradeCode(el.getSchoolGradeCode())
            .createDate(schoolHistoryEntity.getCreateDate())
            .updateDate(schoolHistoryEntity.getUpdateDate())
            .createUser(schoolHistoryEntity.getCreateUser())
            .updateUser(schoolHistoryEntity.getUpdateUser())
            .build())
          .collect(Collectors.toList()));

    }

  }

  public List<SchoolHistoryEntity> getAllSchoolHistoryList(UUID schoolId) {
    return schoolHistoryRepository.findBySchoolId(schoolId);
  }
}
