package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {
  Optional<AddressEntity> findByAddressIdAndDistrictEntity(UUID addressId, DistrictEntity districtEntity);

  void deleteByAddressIdAndDistrictEntity(UUID addressId, DistrictEntity districtEntity);

  Optional<AddressEntity> findByAddressIdAndSchoolEntity(UUID addressId, SchoolEntity schoolEntity);

  void deleteByAddressIdAndSchoolEntity(UUID addressId, SchoolEntity schoolEntity);

  Optional<AddressEntity> findByAddressIdAndIndependentAuthorityEntity(UUID addressId, IndependentAuthorityEntity independentAuthorityEntity);

  void deleteByAddressIdAndIndependentAuthorityEntity(UUID addressId, IndependentAuthorityEntity independentAuthorityEntity);
}
