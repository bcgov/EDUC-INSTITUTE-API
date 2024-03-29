package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.SchoolHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SchoolHistoryRepository extends JpaRepository<SchoolHistoryEntity, UUID>, JpaSpecificationExecutor<SchoolHistoryEntity> {

  List<SchoolHistoryEntity> findBySchoolId(UUID schoolId);

  void deleteBySchoolId(UUID schoolId);

}