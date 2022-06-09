package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.AddressEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {
  Optional<AddressEntity> findByAddressIdAndDistrictEntity(UUID addressId, DistrictEntity districtEntity);

  void deleteByAddressIdAndDistrictEntity(UUID addressId, DistrictEntity districtEntity);
}
