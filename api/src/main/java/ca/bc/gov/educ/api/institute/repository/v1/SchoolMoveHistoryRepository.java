package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.SchoolMoveHistoryEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolMoveHistoryRepository extends JpaRepository<SchoolMoveHistoryEntity, UUID>, JpaSpecificationExecutor<SchoolMoveHistoryEntity> {
}
