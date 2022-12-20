package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.DistrictAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DistrictAddressRepository extends JpaRepository<DistrictAddressEntity, UUID> {
  Optional<DistrictAddressEntity> findByDistrictAddressId(UUID districtAddressId);

  void deleteByDistrictAddressId(UUID districtAddressId);
}
