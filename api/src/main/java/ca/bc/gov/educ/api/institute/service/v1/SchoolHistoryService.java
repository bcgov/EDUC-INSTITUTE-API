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
  public void createSchoolHistory(SchoolEntity curSchoolEntity, String updateUser) {
    final SchoolHistoryEntity schoolHistoryEntity = new SchoolHistoryEntity();
    BeanUtils.copyProperties(curSchoolEntity, schoolHistoryEntity);
    schoolHistoryEntity.setCreateUser(updateUser);
    schoolHistoryEntity.setCreateDate(LocalDateTime.now());
    schoolHistoryEntity.setUpdateUser(updateUser);
    schoolHistoryEntity.setUpdateDate(LocalDateTime.now());
    mapGradeCodeHistory(curSchoolEntity, schoolHistoryEntity);
    mapFundingGroupHistory(curSchoolEntity, schoolHistoryEntity);
    mapNeighbourhoodLearningHistory(curSchoolEntity, schoolHistoryEntity);
    mapAddressHistory(curSchoolEntity, schoolHistoryEntity);
    schoolHistoryRepository.save(schoolHistoryEntity);
  }

  private void mapAddressHistory(SchoolEntity curSchoolEntity, SchoolHistoryEntity schoolHistoryEntity) {
    if (!CollectionUtils.isEmpty(curSchoolEntity.getAddresses())) {
      schoolHistoryEntity.getAddresses()
        .addAll(curSchoolEntity.getAddresses().stream()
          .map(el -> SchoolAddressHistoryEntity.builder()
            .schoolHistoryEntity(schoolHistoryEntity)
            .schoolId(el.getSchoolEntity().getSchoolId())
            .addressLine1(el.getAddressLine1())
            .addressLine2(el.getAddressLine2())
            .city(el.getCity())
            .provinceCode(el.getProvinceCode())
            .postal(el.getPostal())
            .countryCode(el.getCountryCode())
            .addressTypeCode(el.getAddressTypeCode())
            .createDate(schoolHistoryEntity.getCreateDate())
            .updateDate(schoolHistoryEntity.getUpdateDate())
            .createUser(schoolHistoryEntity.getCreateUser())
            .updateUser(schoolHistoryEntity.getUpdateUser())
            .build())
                .toList());
    }
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
                .toList());

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
            .toList());

    }

  }

  private void mapFundingGroupHistory(SchoolEntity curSchoolEntity, SchoolHistoryEntity schoolHistoryEntity) {
    if (!CollectionUtils.isEmpty(curSchoolEntity.getSchoolFundingGroups())) {
      schoolHistoryEntity.getSchoolFundingGroups()
              .addAll(curSchoolEntity.getSchoolFundingGroups().stream()
                      .map(el -> IndependentSchoolFundingGroupSchoolHistoryEntity.builder()
                              .schoolHistoryEntity(schoolHistoryEntity)
                              .schoolFundingGroupCode(el.getSchoolFundingGroupCode())
                              .schoolID(el.getSchoolEntity().getSchoolId())
                              .schoolGradeCode(el.getSchoolGradeCode())
                              .createDate(schoolHistoryEntity.getCreateDate())
                              .updateDate(schoolHistoryEntity.getUpdateDate())
                              .createUser(schoolHistoryEntity.getCreateUser())
                              .updateUser(schoolHistoryEntity.getUpdateUser())
                              .build())
                      .toList());
    }
  }

  public List<SchoolHistoryEntity> getAllSchoolHistoryList(UUID schoolId) {
    return schoolHistoryRepository.findBySchoolId(schoolId);
  }
}
