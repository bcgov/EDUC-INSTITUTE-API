package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.AuthorityAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorityAddressRepository extends JpaRepository<AuthorityAddressEntity, UUID> {
  Optional<AuthorityAddressEntity> findByIndependentAuthorityAddressId(UUID independentAuthorityAddressId);

  void deleteByIndependentAuthorityAddressId(UUID independentAuthorityAddressId);
}
