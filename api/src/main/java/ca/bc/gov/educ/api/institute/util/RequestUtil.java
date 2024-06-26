package ca.bc.gov.educ.api.institute.util;

import ca.bc.gov.educ.api.institute.model.v1.*;
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

  public static void setAuditColumnsForFundingGroups(@NotNull IndependentSchoolFundingGroupEntity group) {
    if(group.getCreateDate() == null) {
      if (StringUtils.isBlank(group.getCreateUser())) {
        group.setCreateUser(ApplicationProperties.INSTITUTE_API);
      }
      group.setCreateDate(LocalDateTime.now());
    }
    setAuditColumnsForFundingGroupUpdate(group);
  }

  private static void setAuditColumnsForFundingGroupUpdate(@NotNull IndependentSchoolFundingGroupEntity group) {
    if (StringUtils.isBlank(group.getUpdateUser())) {
      group.setUpdateUser(ApplicationProperties.INSTITUTE_API);
    }
    group.setUpdateDate(LocalDateTime.now());
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

  public static void setAuditColumnsForSchoolMove(@NotNull SchoolMoveEntity schoolMoveEntity) {
    if (schoolMoveEntity.getCreateDate() == null) {
      if (StringUtils.isBlank(schoolMoveEntity.getCreateUser())) {
        schoolMoveEntity.setCreateUser(ApplicationProperties.INSTITUTE_API);
      }
      schoolMoveEntity.setCreateDate(LocalDateTime.now());
    }
    setAuditColumnsForSchoolMoveUpdate(schoolMoveEntity);
  }

  public static void setAuditColumnsForSchoolMoveUpdate(@NotNull SchoolMoveEntity schoolMoveEntity) {
    if (StringUtils.isBlank(schoolMoveEntity.getUpdateUser())) {
      schoolMoveEntity.setUpdateUser(ApplicationProperties.INSTITUTE_API);
    }
    schoolMoveEntity.setUpdateDate(LocalDateTime.now());
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
