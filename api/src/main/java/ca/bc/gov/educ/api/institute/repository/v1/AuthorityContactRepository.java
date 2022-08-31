package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.AuthorityContactEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorityContactRepository extends JpaRepository<AuthorityContactEntity, UUID> {

  Optional<AuthorityContactEntity> findByAuthorityContactIdAndIndependentAuthorityEntity(UUID authorityContactId, IndependentAuthorityEntity independentAuthorityEntity);

  void deleteByAuthorityContactIdAndIndependentAuthorityEntity(UUID authorityContactId, IndependentAuthorityEntity independentAuthorityEntity);
}
