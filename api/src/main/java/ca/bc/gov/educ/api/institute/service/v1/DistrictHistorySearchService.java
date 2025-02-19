package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.InvalidParameterException;
import ca.bc.gov.educ.api.institute.exception.SchoolAPIRuntimeException;
import ca.bc.gov.educ.api.institute.filter.DistrictHistoryFilterSpecs;
import ca.bc.gov.educ.api.institute.filter.FilterOperation;
import ca.bc.gov.educ.api.institute.model.v1.DistrictHistoryEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictHistoryRepository;
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
public class DistrictHistorySearchService {
  private final DistrictHistoryFilterSpecs districtHistoryFilterSpecs;

  private final DistrictHistoryRepository districtHistoryRepository;

  private final Executor paginatedQueryExecutor = new EnhancedQueueExecutor.Builder()
    .setThreadFactory(new ThreadFactoryBuilder().setNameFormat("async-pagination-query-executor-%d").build())
    .setCorePoolSize(2).setMaximumPoolSize(10).setKeepAliveTime(Duration.ofSeconds(60)).build();

  public DistrictHistorySearchService(DistrictHistoryFilterSpecs districtHistoryFilterSpecs, DistrictHistoryRepository districtHistoryRepository) {
    this.districtHistoryFilterSpecs = districtHistoryFilterSpecs;
    this.districtHistoryRepository = districtHistoryRepository;
  }

  /**
   * Gets specifications.
   *
   * @param districtSpecs the pen reg batch specs
   * @param i            the
   * @param search       the search
   * @return the specifications
   */
  public Specification<DistrictHistoryEntity> getSpecifications(Specification<DistrictHistoryEntity> districtSpecs, int i, Search search) {
    if (i == 0) {
      districtSpecs = getDistrictHistoryEntitySpecification(search.getSearchCriteriaList());
    } else {
      if (search.getCondition() == Condition.AND) {
        districtSpecs = districtSpecs.and(getDistrictHistoryEntitySpecification(search.getSearchCriteriaList()));
      } else {
        districtSpecs = districtSpecs.or(getDistrictHistoryEntitySpecification(search.getSearchCriteriaList()));
      }
    }
    return districtSpecs;
  }

  private Specification<DistrictHistoryEntity> getDistrictHistoryEntitySpecification(List<SearchCriteria> criteriaList) {
    Specification<DistrictHistoryEntity> districtSpecs = null;
    if (!criteriaList.isEmpty()) {
      int i = 0;
      for (SearchCriteria criteria : criteriaList) {
        if (criteria.getKey() != null && criteria.getOperation() != null && criteria.getValueType() != null) {
          var criteriaValue = criteria.getValue();
          if(StringUtils.isNotBlank(criteria.getValue()) && TransformUtil.isUppercaseField(SchoolEntity.class, criteria.getKey())) {
            criteriaValue = criteriaValue.toUpperCase();
          }
          Specification<DistrictHistoryEntity> typeSpecification = getTypeSpecification(criteria.getKey(), criteria.getOperation(), criteriaValue, criteria.getValueType());
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
   * @param districtHistoryEntitySpecification the pen request batch entity specification
   * @param i                          the
   * @param criteria                   the criteria
   * @param typeSpecification          the type specification
   * @return the specification per group
   */
  private Specification<DistrictHistoryEntity> getSpecificationPerGroup(Specification<DistrictHistoryEntity> districtHistoryEntitySpecification, int i, SearchCriteria criteria, Specification<DistrictHistoryEntity> typeSpecification) {
    if (i == 0) {
      districtHistoryEntitySpecification = Specification.where(typeSpecification);
    } else {
      if (criteria.getCondition() == Condition.AND) {
        districtHistoryEntitySpecification = districtHistoryEntitySpecification.and(typeSpecification);
      } else {
        districtHistoryEntitySpecification = districtHistoryEntitySpecification.or(typeSpecification);
      }
    }
    return districtHistoryEntitySpecification;
  }

  private Specification<DistrictHistoryEntity> getTypeSpecification(String key, FilterOperation filterOperation, String value, ValueType valueType) {
    Specification<DistrictHistoryEntity> schoolEntitySpecification = null;
    switch (valueType) {
      case STRING:
        schoolEntitySpecification = districtHistoryFilterSpecs.getStringTypeSpecification(key, value, filterOperation);
        break;
      case DATE_TIME:
        schoolEntitySpecification = districtHistoryFilterSpecs.getDateTimeTypeSpecification(key, value, filterOperation);
        break;
      case LONG:
        schoolEntitySpecification = districtHistoryFilterSpecs.getLongTypeSpecification(key, value, filterOperation);
        break;
      case INTEGER:
        schoolEntitySpecification = districtHistoryFilterSpecs.getIntegerTypeSpecification(key, value, filterOperation);
        break;
      case DATE:
        schoolEntitySpecification = districtHistoryFilterSpecs.getDateTypeSpecification(key, value, filterOperation);
        break;
      case UUID:
        schoolEntitySpecification = districtHistoryFilterSpecs.getUUIDTypeSpecification(key, value, filterOperation);
        break;
      case BOOLEAN:
        schoolEntitySpecification = districtHistoryFilterSpecs.getBooleanTypeSpecification(key, value, filterOperation);
        break;
      default:
        break;
    }
    return schoolEntitySpecification;
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public CompletableFuture<Page<DistrictHistoryEntity>> findAll(Specification<DistrictHistoryEntity> districtSpecs, final Integer pageNumber, final Integer pageSize, final List<Sort.Order> sorts) {
    log.trace("In find all query: {}", districtSpecs);
    return CompletableFuture.supplyAsync(() -> {
      Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sorts));
      try {
        log.trace("Running paginated query: {}", districtSpecs);
        var results = this.districtHistoryRepository.findAll(districtSpecs, paging);
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
  public Specification<DistrictHistoryEntity> setSpecificationAndSortCriteria(String sortCriteriaJson, String searchCriteriaListJson, ObjectMapper objectMapper, List<Sort.Order> sorts) {
    Specification<DistrictHistoryEntity> districtSpecs = null;
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
