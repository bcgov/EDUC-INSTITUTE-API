package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.ContactEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, UUID> {

  Optional<ContactEntity> findByContactIdAndDistrictEntity(UUID contactId, DistrictEntity districtEntity);

  void deleteByContactIdAndDistrictEntity(UUID contactId, DistrictEntity districtEntity);
}
