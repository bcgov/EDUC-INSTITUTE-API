package ca.bc.gov.educ.api.institute.util;

import ca.bc.gov.educ.api.institute.model.v1.BaseAddressEntity;
import ca.bc.gov.educ.api.institute.model.v1.NeighborhoodLearningEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolGradeEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolMoveHistoryEntity;
import ca.bc.gov.educ.api.institute.properties.ApplicationProperties;
import ca.bc.gov.educ.api.institute.struct.v1.BaseRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The type Request util.
 */
public class RequestUtil {
  private RequestUtil() {
  }

  /**
   * set audit data to the object.
   *
   * @param baseRequest The object which will be persisted.
   */
  public static void setAuditColumnsForCreate(@NotNull BaseRequest baseRequest) {
    if (StringUtils.isBlank(baseRequest.getCreateUser())) {
      baseRequest.setCreateUser(ApplicationProperties.INSTITUTE_API);
    }
    baseRequest.setCreateDate(LocalDateTime.now().toString());
    setAuditColumnsForUpdate(baseRequest);
  }

  /**
   * set audit data to the object.
   *
   * @param baseRequest The object which will be persisted.
   */
  public static void setAuditColumnsForUpdate(@NotNull BaseRequest baseRequest) {
    if (StringUtils.isBlank(baseRequest.getUpdateUser())) {
      baseRequest.setUpdateUser(ApplicationProperties.INSTITUTE_API);
    }
    baseRequest.setUpdateDate(LocalDateTime.now().toString());
  }

  public static void setAuditColumnsForAddress(@NotNull BaseAddressEntity address) {
    if(address.getCreateDate() == null) {
      if (StringUtils.isBlank(address.getCreateUser())) {
        address.setCreateUser(ApplicationProperties.INSTITUTE_API);
      }
      address.setCreateDate(LocalDateTime.now());
    }
    setAuditColumnsForAddressUpdate(address);
  }

  private static void setAuditColumnsForAddressUpdate(@NotNull BaseAddressEntity address) {
    if (StringUtils.isBlank(address.getUpdateUser())) {
      address.setUpdateUser(ApplicationProperties.INSTITUTE_API);
    }
    address.setUpdateDate(LocalDateTime.now());
  }

  public static void setAuditColumnsForGrades(@NotNull SchoolGradeEntity grade) {
    if(grade.getCreateDate() == null) {
      if (StringUtils.isBlank(grade.getCreateUser())) {
        grade.setCreateUser(ApplicationProperties.INSTITUTE_API);
      }
      grade.setCreateDate(LocalDateTime.now());
    }
    setAuditColumnsForGradeUpdate(grade);
  }

  private static void setAuditColumnsForGradeUpdate(@NotNull SchoolGradeEntity grade) {
    if (StringUtils.isBlank(grade.getUpdateUser())) {
      grade.setUpdateUser(ApplicationProperties.INSTITUTE_API);
    }
    grade.setUpdateDate(LocalDateTime.now());
  }

  public static void setAuditColumnsForNeighborhoodLearning(@NotNull NeighborhoodLearningEntity neighborhoodLearningEntity) {
    if(neighborhoodLearningEntity.getCreateDate() == null) {
      if (StringUtils.isBlank(neighborhoodLearningEntity.getCreateUser())) {
        neighborhoodLearningEntity.setCreateUser(ApplicationProperties.INSTITUTE_API);
      }
      neighborhoodLearningEntity.setCreateDate(LocalDateTime.now());
    }
    setAuditColumnsForNeighborhoodLearningUpdate(neighborhoodLearningEntity);
  }

  private static void setAuditColumnsForNeighborhoodLearningUpdate(@NotNull NeighborhoodLearningEntity neighborhoodLearningEntity) {
    if (StringUtils.isBlank(neighborhoodLearningEntity.getUpdateUser())) {
      neighborhoodLearningEntity.setUpdateUser(ApplicationProperties.INSTITUTE_API);
    }
    neighborhoodLearningEntity.setUpdateDate(LocalDateTime.now());
  }

  public static void setAuditColumnsForSchoolMoveHistory(@NotNull SchoolMoveHistoryEntity schoolMoveHistoryEntity) {
    if (schoolMoveHistoryEntity.getCreateDate() == null) {
      if (StringUtils.isBlank(schoolMoveHistoryEntity.getCreateUser())) {
        schoolMoveHistoryEntity.setCreateUser(ApplicationProperties.INSTITUTE_API);
      }
      schoolMoveHistoryEntity.setCreateDate(LocalDateTime.now());
    }
    setAuditColumnsForSchoolMoveHistoryUpdate(schoolMoveHistoryEntity);
  }

  public static void setAuditColumnsForSchoolMoveHistoryUpdate(@NotNull SchoolMoveHistoryEntity schoolMoveHistoryEntity) {
    if (StringUtils.isBlank(schoolMoveHistoryEntity.getUpdateUser())) {
      schoolMoveHistoryEntity.setUpdateUser(ApplicationProperties.INSTITUTE_API);
    }
    schoolMoveHistoryEntity.setUpdateDate(LocalDateTime.now());
  }

  /**
   * Get the Sort.Order list from JSON string
   *
   * @param sortCriteriaJson The sort criterio JSON
   * @param objectMapper     The object mapper
   * @param sorts            The Sort.Order list
   * @throws JsonProcessingException the json processing exception
   */
  public static void getSortCriteria(String sortCriteriaJson, ObjectMapper objectMapper, List<Sort.Order> sorts) throws JsonProcessingException {
    if (StringUtils.isNotBlank(sortCriteriaJson)) {
      Map<String, String> sortMap = objectMapper.readValue(sortCriteriaJson, new TypeReference<>() {
      });
      sortMap.forEach((k, v) -> {
        if ("ASC".equalsIgnoreCase(v)) {
          sorts.add(new Sort.Order(Sort.Direction.ASC, k));
        } else {
          sorts.add(new Sort.Order(Sort.Direction.DESC, k));
        }
      });
    }
  }
}
