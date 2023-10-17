package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.SchoolContactTombstoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SchoolContactTombstoneRepository extends JpaRepository<SchoolContactTombstoneEntity, UUID>, JpaSpecificationExecutor<SchoolContactTombstoneEntity> {

}
