package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.model.v1.AuthorityAddressHistoryEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityHistoryEntity;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityHistoryRepository;
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
  public void createIndependentAuthorityHistory(IndependentAuthorityEntity curIndependentAuthorityEntity, String updateUser) {
    final IndependentAuthorityHistoryEntity independentAuthorityHistoryEntity = new IndependentAuthorityHistoryEntity();
    BeanUtils.copyProperties(curIndependentAuthorityEntity, independentAuthorityHistoryEntity);
    independentAuthorityHistoryEntity.setCreateUser(updateUser);
    independentAuthorityHistoryEntity.setCreateDate(LocalDateTime.now());
    independentAuthorityHistoryEntity.setUpdateUser(updateUser);
    independentAuthorityHistoryEntity.setUpdateDate(LocalDateTime.now());
    mapAddressHistory(curIndependentAuthorityEntity, independentAuthorityHistoryEntity);
    independentAuthorityHistoryRepository.save(independentAuthorityHistoryEntity);
  }

  private void mapAddressHistory(IndependentAuthorityEntity curIndependentAuthorityEntity, IndependentAuthorityHistoryEntity independentAuthorityHistoryEntity) {
    if (!CollectionUtils.isEmpty(curIndependentAuthorityEntity.getAddresses())) {
      independentAuthorityHistoryEntity.getAddresses()
        .addAll(curIndependentAuthorityEntity.getAddresses().stream()
          .map(el -> AuthorityAddressHistoryEntity.builder()
            .independentAuthorityHistoryEntity(independentAuthorityHistoryEntity)
            .independentAuthorityAddressId(el.getIndependentAuthorityAddressId())
            .independentAuthorityId(el.getIndependentAuthorityEntity().getIndependentAuthorityId())
            .addressLine1(el.getAddressLine1())
            .addressLine2(el.getAddressLine2())
            .city(el.getCity())
            .provinceCode(el.getProvinceCode())
            .postal(el.getPostal())
            .countryCode(el.getCountryCode())
            .addressTypeCode(el.getAddressTypeCode())
            .createDate(independentAuthorityHistoryEntity.getCreateDate())
            .updateDate(independentAuthorityHistoryEntity.getUpdateDate())
            .createUser(independentAuthorityHistoryEntity.getCreateUser())
            .updateUser(independentAuthorityHistoryEntity.getUpdateUser())
            .build())
          .collect(Collectors.toList()));
    }
  }

  public List<IndependentAuthorityHistoryEntity> getAllIndependentAuthorityHistoryList(UUID independentAuthorityId) {
    return independentAuthorityHistoryRepository.findByIndependentAuthorityId(independentAuthorityId);
  }
}
