package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.model.v1.AddressEntity;
import ca.bc.gov.educ.api.institute.model.v1.AddressHistoryEntity;
import ca.bc.gov.educ.api.institute.repository.v1.AddressHistoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AddressHistoryService {

  private final AddressHistoryRepository addressHistoryRepository;

  @Autowired
  public AddressHistoryService(AddressHistoryRepository addressHistoryRepository) {
    this.addressHistoryRepository = addressHistoryRepository;
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void createAddressHistory(AddressEntity curAddressEntity, String updateUser, boolean copyAudit) {
    final AddressHistoryEntity addressHistoryEntity = new AddressHistoryEntity();
    BeanUtils.copyProperties(curAddressEntity, addressHistoryEntity);
    addressHistoryEntity.setCreateUser(updateUser);
    if (!copyAudit) {
      addressHistoryEntity.setCreateDate(LocalDateTime.now());
    }
    addressHistoryEntity.setUpdateUser(updateUser);
    addressHistoryEntity.setUpdateDate(LocalDateTime.now());
    addressHistoryRepository.save(addressHistoryEntity);
  }
}
