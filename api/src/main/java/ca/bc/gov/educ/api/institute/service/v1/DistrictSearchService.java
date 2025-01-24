package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.InvalidParameterException;
import ca.bc.gov.educ.api.institute.exception.SchoolAPIRuntimeException;
import ca.bc.gov.educ.api.institute.filter.DistrictFilterSpecs;
import ca.bc.gov.educ.api.institute.filter.FilterOperation;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictRepository;
import ca.bc.gov.educ.api.institute.struct.v1.Condition;
import ca.bc.gov.educ.api.institute.struct.v1.Search;
import ca.bc.gov.educ.api.institute.struct.v1.SearchCriteria;
import ca.bc.gov.educ.api.institute.struct.v1.ValueType;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.util.TransformUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jboss.threads.EnhancedQueueExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

/**
 * The type School search service.
 */
@Service
@Slf4j
public class DistrictSearchService {
  private final DistrictFilterSpecs districtFilterSpecs;

  private final DistrictRepository districtRepository;

  private final Executor paginatedQueryExecutor = new EnhancedQueueExecutor.Builder()
    .setThreadFactory(new ThreadFactoryBuilder().setNameFormat("async-pagination-query-executor-%d").build())
    .setCorePoolSize(2).setMaximumPoolSize(10).setKeepAliveTime(Duration.ofSeconds(60)).build();

  public DistrictSearchService(DistrictFilterSpecs districtFilterSpecs, DistrictRepository districtRepository) {
    this.districtFilterSpecs = districtFilterSpecs;
    this.districtRepository = districtRepository;
  }

  /**
   * Gets specifications.
   *
   * @param districtSpecs the pen reg batch specs
   * @param i            the
   * @param search       the search
   * @return the specifications
   */
  public Specification<DistrictEntity> getSpecifications(Specification<DistrictEntity> districtSpecs, int i, Search search) {
    if (i == 0) {
      districtSpecs = getDistrictEntitySpecification(search.getSearchCriteriaList());
    } else {
      if (search.getCondition() == Condition.AND) {
        districtSpecs = districtSpecs.and(getDistrictEntitySpecification(search.getSearchCriteriaList()));
      } else {
        districtSpecs = districtSpecs.or(getDistrictEntitySpecification(search.getSearchCriteriaList()));
      }
    }
    return districtSpecs;
  }

  private Specification<DistrictEntity> getDistrictEntitySpecification(List<SearchCriteria> criteriaList) {
    Specification<DistrictEntity> districtSpecs = null;
    if (!criteriaList.isEmpty()) {
      int i = 0;
      for (SearchCriteria criteria : criteriaList) {
        if (criteria.getKey() != null && criteria.getOperation() != null && criteria.getValueType() != null) {
          var criteriaValue = criteria.getValue();
          if(StringUtils.isNotBlank(criteria.getValue()) && TransformUtil.isUppercaseField(SchoolEntity.class, criteria.getKey())) {
            criteriaValue = criteriaValue.toUpperCase();
          }
          Specification<DistrictEntity> typeSpecification = getTypeSpecification(criteria.getKey(), criteria.getOperation(), criteriaValue, criteria.getValueType());
          districtSpecs = getSpecificationPerGroup(districtSpecs, i, criteria, typeSpecification);
          i++;
        } else {
          throw new InvalidParameterException("Search Criteria can not contain null values for key, value and operation type");
        }
      }
    }
    return districtSpecs;
  }

  /**
   * Gets specification per group.
   *
   * @param districtEntitySpecification the pen request batch entity specification
   * @param i                          the
   * @param criteria                   the criteria
   * @param typeSpecification          the type specification
   * @return the specification per group
   */
  private Specification<DistrictEntity> getSpecificationPerGroup(Specification<DistrictEntity> districtEntitySpecification, int i, SearchCriteria criteria, Specification<DistrictEntity> typeSpecification) {
    if (i == 0) {
      districtEntitySpecification = Specification.where(typeSpecification);
    } else {
      if (criteria.getCondition() == Condition.AND) {
        districtEntitySpecification = districtEntitySpecification.and(typeSpecification);
      } else {
        districtEntitySpecification = districtEntitySpecification.or(typeSpecification);
      }
    }
    return districtEntitySpecification;
  }

  private Specification<DistrictEntity> getTypeSpecification(String key, FilterOperation filterOperation, String value, ValueType valueType) {
    Specification<DistrictEntity> schoolEntitySpecification = null;
    switch (valueType) {
      case STRING:
        schoolEntitySpecification = districtFilterSpecs.getStringTypeSpecification(key, value, filterOperation);
        break;
      case DATE_TIME:
        schoolEntitySpecification = districtFilterSpecs.getDateTimeTypeSpecification(key, value, filterOperation);
        break;
      case LONG:
        schoolEntitySpecification = districtFilterSpecs.getLongTypeSpecification(key, value, filterOperation);
        break;
      case INTEGER:
        schoolEntitySpecification = districtFilterSpecs.getIntegerTypeSpecification(key, value, filterOperation);
        break;
      case DATE:
        schoolEntitySpecification = districtFilterSpecs.getDateTypeSpecification(key, value, filterOperation);
        break;
      case UUID:
        schoolEntitySpecification = districtFilterSpecs.getUUIDTypeSpecification(key, value, filterOperation);
        break;
      case BOOLEAN:
        schoolEntitySpecification = districtFilterSpecs.getBooleanTypeSpecification(key, value, filterOperation);
        break;
      default:
        break;
    }
    return schoolEntitySpecification;
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public CompletableFuture<Page<DistrictEntity>> findAll(Specification<DistrictEntity> districtSpecs, final Integer pageNumber, final Integer pageSize, final List<Sort.Order> sorts) {
    log.trace("In find all query: {}", districtSpecs);
    return CompletableFuture.supplyAsync(() -> {
      Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sorts));
      try {
        log.trace("Running paginated query: {}", districtSpecs);
        var results = this.districtRepository.findAll(districtSpecs, paging);
        log.trace("Paginated query returned with results: {}", results);
        return results;
      } catch (final Throwable ex) {
        log.error("Failure querying for paginated schools: {}", ex.getMessage());
        throw new CompletionException(ex);
      }
    }, paginatedQueryExecutor);

  }

  /**
   * Sets specification and sort criteria.
   *
   * @param sortCriteriaJson       the sort criteria json
   * @param searchCriteriaListJson the search criteria list json
   * @param objectMapper           the object mapper
   * @param sorts                  the sorts
   * @return the specification and sort criteria
   */
  public Specification<DistrictEntity> setSpecificationAndSortCriteria(String sortCriteriaJson, String searchCriteriaListJson, ObjectMapper objectMapper, List<Sort.Order> sorts) {
    Specification<DistrictEntity> districtSpecs = null;
    try {
      RequestUtil.getSortCriteria(sortCriteriaJson, objectMapper, sorts);
      if (StringUtils.isNotBlank(searchCriteriaListJson)) {
        List<Search> searches = objectMapper.readValue(searchCriteriaListJson, new TypeReference<>() {
        });
        int i = 0;
        for (var search : searches) {
          districtSpecs = getSpecifications(districtSpecs, i, search);
          i++;
        }
      }
    } catch (JsonProcessingException e) {
      throw new SchoolAPIRuntimeException(e.getMessage());
    }
    return districtSpecs;
  }
}
