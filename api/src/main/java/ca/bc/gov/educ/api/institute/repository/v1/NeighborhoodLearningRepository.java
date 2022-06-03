package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.NeighborhoodLearningEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NeighborhoodLearningRepository extends JpaRepository<NeighborhoodLearningEntity, UUID> {

}
