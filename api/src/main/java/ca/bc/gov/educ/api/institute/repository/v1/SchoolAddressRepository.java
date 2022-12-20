package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.SchoolAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SchoolAddressRepository extends JpaRepository<SchoolAddressEntity, UUID> {
  Optional<SchoolAddressEntity> findBySchoolAddressId(UUID schoolAddressId);

  void deleteBySchoolAddressId(UUID schoolAddressId);
}
