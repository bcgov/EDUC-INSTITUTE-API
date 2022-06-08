package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictHistoryEntity;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictHistoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DistrictHistoryService {

  private final DistrictHistoryRepository districtHistoryRepository;

  @Autowired
  public DistrictHistoryService(DistrictHistoryRepository districtHistoryRepository) {
    this.districtHistoryRepository = districtHistoryRepository;
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void deleteByDistrictID(final UUID districtId) {
    districtHistoryRepository.deleteByDistrictId(districtId);
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void createDistrictHistory(DistrictEntity curDistrictEntity, String updateUser, boolean copyAudit) {
    final DistrictHistoryEntity districtHistoryEntity = new DistrictHistoryEntity();
    BeanUtils.copyProperties(curDistrictEntity, districtHistoryEntity);
    districtHistoryEntity.setCreateUser(updateUser);
    if (!copyAudit) {
      districtHistoryEntity.setCreateDate(LocalDateTime.now());
    }
    districtHistoryEntity.setUpdateUser(updateUser);
    districtHistoryEntity.setUpdateDate(LocalDateTime.now());
    districtHistoryRepository.save(districtHistoryEntity);
  }
}
