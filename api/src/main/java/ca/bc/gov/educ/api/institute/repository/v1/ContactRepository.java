package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.ContactEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, UUID> {

  Optional<ContactEntity> findByContactIdAndDistrictEntity(UUID contactId, DistrictEntity districtEntity);

  void deleteByContactIdAndDistrictEntity(UUID contactId, DistrictEntity districtEntity);

  Optional<ContactEntity> findByContactIdAndSchoolEntity(UUID contactId, SchoolEntity schoolEntity);

  void deleteByContactIdAndSchoolEntity(UUID contactId, SchoolEntity schoolEntity);

  Optional<ContactEntity> findByContactIdAndIndependentAuthorityEntity(UUID contactId, IndependentAuthorityEntity independentAuthorityEntity);

  void deleteByContactIdAndIndependentAuthorityEntity(UUID contactId, IndependentAuthorityEntity independentAuthorityEntity);
}
