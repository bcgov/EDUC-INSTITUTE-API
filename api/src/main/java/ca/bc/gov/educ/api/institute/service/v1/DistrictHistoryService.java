package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.model.v1.DistrictAddressHistoryEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictHistoryEntity;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictHistoryRepository;
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
  public void createDistrictHistory(DistrictEntity curDistrictEntity, String updateUser) {
    final DistrictHistoryEntity districtHistoryEntity = new DistrictHistoryEntity();
    BeanUtils.copyProperties(curDistrictEntity, districtHistoryEntity);
    districtHistoryEntity.setCreateUser(updateUser);
    districtHistoryEntity.setCreateDate(LocalDateTime.now());
    districtHistoryEntity.setUpdateUser(updateUser);
    districtHistoryEntity.setUpdateDate(LocalDateTime.now());
    mapAddressHistory(curDistrictEntity, districtHistoryEntity);
    districtHistoryRepository.save(districtHistoryEntity);
  }

  private void mapAddressHistory(DistrictEntity curDistrictEntity, DistrictHistoryEntity districtHistoryEntity) {
    if (!CollectionUtils.isEmpty(curDistrictEntity.getAddresses())) {
      districtHistoryEntity.getAddresses()
        .addAll(curDistrictEntity.getAddresses().stream()
          .map(el -> DistrictAddressHistoryEntity.builder()
            .districtHistoryEntity(districtHistoryEntity)
            .districtAddressId(el.getDistrictAddressId())
            .districtId(el.getDistrictEntity().getDistrictId())
            .addressLine1(el.getAddressLine1())
            .addressLine2(el.getAddressLine2())
            .city(el.getCity())
            .provinceCode(el.getProvinceCode())
            .postal(el.getPostal())
            .countryCode(el.getCountryCode())
            .addressTypeCode(el.getAddressTypeCode())
            .createDate(districtHistoryEntity.getCreateDate())
            .updateDate(districtHistoryEntity.getUpdateDate())
            .createUser(districtHistoryEntity.getCreateUser())
            .updateUser(districtHistoryEntity.getUpdateUser())
            .build())
          .collect(Collectors.toList()));
    }
  }

  public List<DistrictHistoryEntity> getAllDistrictHistoryList(UUID districtId) {
    return districtHistoryRepository.findByDistrictId(districtId);
  }
}
