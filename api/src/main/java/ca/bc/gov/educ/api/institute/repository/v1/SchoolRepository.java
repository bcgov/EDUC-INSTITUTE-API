package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, UUID>, JpaSpecificationExecutor<SchoolEntity> {
  @Transactional
  @Query(value = """
          SELECT MAX(S.SCHOOL_NUMBER)
          FROM SCHOOL S, DISTRICT D
          WHERE S.DISTRICT_ID = D.DISTRICT_ID
          AND D.DISTRICT_NUMBER = :districtNumber
          AND S.SCHOOL_NUMBER LIKE :pattern"""
          , nativeQuery = true)
  String findLastSchoolNumberWithPattern(String districtNumber, String pattern);

  @Transactional
  @Query(value = """
          SELECT MAX(S.SCHOOL_NUMBER)
          FROM SCHOOL S, DISTRICT D
          WHERE S.DISTRICT_ID = D.DISTRICT_ID
          AND D.DISTRICT_NUMBER = :districtNumber
          AND S.INDEPENDENT_AUTHORITY_ID IS NOT DISTINCT FROM :authorityId"""
          , nativeQuery = true)
  String findLastSchoolNumber(String districtNumber, UUID authorityId);

  @Transactional
  @Query(value = """
          SELECT MIN(S.ID) AS MISSING_NUM
          FROM generate_series(:lowerRange, :upperRange) S(ID)
          WHERE NOT EXISTS
          (SELECT 1 FROM SCHOOL SCH, DISTRICT DIS
          WHERE CAST(SCH.SCHOOL_NUMBER as INTEGER) = S.ID
          AND SCH.DISTRICT_ID = DIS.DISTRICT_ID
          AND DIS.DISTRICT_NUMBER = :districtNumber
          AND (SCH.INDEPENDENT_AUTHORITY_ID IS NULL OR SCH.INDEPENDENT_AUTHORITY_ID IS NOT DISTINCT FROM :authorityId))"""
          , nativeQuery = true)
  Integer findFirstAvailableSchoolNumber(String districtNumber, UUID authorityId, Integer lowerRange, Integer upperRange);

  List<SchoolEntity> findBySchoolNumberAndDistrictID(String schoolNumber, UUID districtID);
}
