package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IndependentAuthorityRepository extends JpaRepository<IndependentAuthorityEntity, UUID>, JpaSpecificationExecutor<IndependentAuthorityEntity> {

}
