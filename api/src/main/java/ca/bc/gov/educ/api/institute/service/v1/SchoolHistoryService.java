package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolHistoryEntity;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolHistoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
  public void createSchoolHistory(SchoolEntity curSchoolEntity, String updateUser, boolean copyAudit) {
    final SchoolHistoryEntity schoolHistoryEntity = new SchoolHistoryEntity();
    BeanUtils.copyProperties(curSchoolEntity, schoolHistoryEntity);
    schoolHistoryEntity.setCreateUser(updateUser);
    if (!copyAudit) {
      schoolHistoryEntity.setCreateDate(LocalDateTime.now());
    }
    schoolHistoryEntity.setUpdateUser(updateUser);
    schoolHistoryEntity.setUpdateDate(LocalDateTime.now());
    schoolHistoryRepository.save(schoolHistoryEntity);
  }

  public List<SchoolHistoryEntity> getAllSchoolHistoryList(UUID schoolId) {
    return schoolHistoryRepository.findBySchoolId(schoolId);
  }
}
