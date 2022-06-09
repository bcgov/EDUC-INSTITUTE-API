package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.AddressHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressHistoryRepository extends JpaRepository<AddressHistoryEntity, UUID> {
  List<AddressHistoryEntity> findByAddressId(UUID addressId);
  void deleteByAddressId(UUID addressId);
}
