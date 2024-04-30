package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.IndependentSchoolFundingGroupHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IndependentSchoolFundingGroupHistoryRepository extends JpaRepository<IndependentSchoolFundingGroupHistoryEntity, UUID> {

    List<IndependentSchoolFundingGroupHistoryEntity> findAllBySchoolID(UUID schoolID);
}
