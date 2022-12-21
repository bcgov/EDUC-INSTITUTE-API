package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, UUID>, JpaSpecificationExecutor<SchoolEntity> {

  @Transactional
  @Query(value = "select school_number from SCHOOL where CAST(school_number as int) < 80000 order by CAST(school_number as int) desc LIMIT 1", nativeQuery = true)
  String findLastSchoolNumber();
}
