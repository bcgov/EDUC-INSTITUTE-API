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
  @Query(value = "SELECT MAX(S.SCHOOL_NUMBER)\n" +
          "FROM SCHOOL S, DISTRICT D\n" +
          "WHERE S.DISTRICT_ID = D.DISTRICT_ID\n" +
          "AND D.DISTRICT_NUMBER = :districtNumber\n" +
          "AND S.SCHOOL_NUMBER LIKE :pattern \n" +
          "AND S.INDEPENDENT_AUTHORITY_ID IS NOT DISTINCT FROM :authorityId", nativeQuery = true)
  String findLastSchoolNumberWithPattern(String districtNumber, UUID authorityId, String pattern);

  @Transactional
  @Query(value = "SELECT MAX(S.SCHOOL_NUMBER)\n" +
          "FROM SCHOOL S, DISTRICT D\n" +
          "WHERE S.DISTRICT_ID = D.DISTRICT_ID\n" +
          "AND D.DISTRICT_NUMBER = :districtNumber\n" +
          "AND S.INDEPENDENT_AUTHORITY_ID IS NOT DISTINCT FROM :authorityId", nativeQuery = true)
  String findLastSchoolNumber(String districtNumber, UUID authorityId);

  @Transactional
  @Query(value = "SELECT MIN(S.ID) AS MISSING_NUM\n" +
          "FROM generate_series(:lowerRange, :upperRange) S(ID)\n" +
          "WHERE NOT EXISTS\n" +
          "(SELECT 1 FROM SCHOOL SCH, DISTRICT DIS\n" +
          "WHERE CAST(SCH.SCHOOL_NUMBER as INTEGER) = S.ID\n" +
          "AND SCH.DISTRICT_ID = DIS.DISTRICT_ID\n" +
          "AND DIS.DISTRICT_NUMBER = :districtNumber\n" +
          "AND SCH.INDEPENDENT_AUTHORITY_ID IS NOT DISTINCT FROM :authorityId)", nativeQuery = true)
  Integer findFirstAvailableSchoolNumber(String districtNumber, UUID authorityId, Integer lowerRange, Integer upperRange);
}
