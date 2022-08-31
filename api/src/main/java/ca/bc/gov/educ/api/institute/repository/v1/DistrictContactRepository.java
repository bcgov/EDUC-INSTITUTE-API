package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.DistrictContactEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DistrictContactRepository extends JpaRepository<DistrictContactEntity, UUID> {

  Optional<DistrictContactEntity> findByDistrictContactIdAndDistrictEntity(UUID districtContactId, DistrictEntity districtEntity);

  void deleteByDistrictContactIdAndDistrictEntity(UUID districtContactId, DistrictEntity districtEntity);

}
