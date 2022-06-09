package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityHistoryEntity;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityHistoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class IndependentAuthorityHistoryService {

  private final IndependentAuthorityHistoryRepository independentAuthorityHistoryRepository;

  @Autowired
  public IndependentAuthorityHistoryService(IndependentAuthorityHistoryRepository independentAuthorityHistoryRepository) {
    this.independentAuthorityHistoryRepository = independentAuthorityHistoryRepository;
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void deleteByIndependentAuthorityID(final UUID independentAuthorityId) {
    independentAuthorityHistoryRepository.deleteByIndependentAuthorityId(independentAuthorityId);
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void createIndependentAuthorityHistory(IndependentAuthorityEntity curIndependentAuthorityEntity, String updateUser, boolean copyAudit) {
    final IndependentAuthorityHistoryEntity independentAuthorityHistoryEntity = new IndependentAuthorityHistoryEntity();
    BeanUtils.copyProperties(curIndependentAuthorityEntity, independentAuthorityHistoryEntity);
    independentAuthorityHistoryEntity.setCreateUser(updateUser);
    if (!copyAudit) {
      independentAuthorityHistoryEntity.setCreateDate(LocalDateTime.now());
    }
    independentAuthorityHistoryEntity.setUpdateUser(updateUser);
    independentAuthorityHistoryEntity.setUpdateDate(LocalDateTime.now());
    independentAuthorityHistoryRepository.save(independentAuthorityHistoryEntity);
  }

  public List<IndependentAuthorityHistoryEntity> getAllIndependentAuthorityHistoryList(UUID independentAuthorityId) {
    return independentAuthorityHistoryRepository.findByIndependentAuthorityId(independentAuthorityId);
  }
}
