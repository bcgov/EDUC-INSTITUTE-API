package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface IndependentAuthorityRepository extends JpaRepository<IndependentAuthorityEntity, UUID>, JpaSpecificationExecutor<IndependentAuthorityEntity> {

    @Transactional
    @Query(value = "select authority_number from INDEPENDENT_AUTHORITY order by CAST(authority_number as int) desc LIMIT 1", nativeQuery = true)
    String findLastAuthorityNumber();
}
