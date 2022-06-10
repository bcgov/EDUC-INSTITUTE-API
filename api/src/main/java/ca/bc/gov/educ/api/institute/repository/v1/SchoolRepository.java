package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, UUID>, JpaSpecificationExecutor<SchoolEntity> {

}
