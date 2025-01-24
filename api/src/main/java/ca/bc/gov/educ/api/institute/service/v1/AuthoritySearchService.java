package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.AuthorityAPIRuntimeException;
import ca.bc.gov.educ.api.institute.exception.InvalidParameterException;
import ca.bc.gov.educ.api.institute.filter.AuthorityFilterSpecs;
import ca.bc.gov.educ.api.institute.filter.FilterOperation;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityRepository;
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
 * The type Authority search service.
 */
@Service
public class AuthoritySearchService {
  private final AuthorityFilterSpecs authorityFilterSpecs;

  private final IndependentAuthorityRepository authorityRepository;

  private final Executor paginatedQueryExecutor = new EnhancedQueueExecutor.Builder()
    .setThreadFactory(new ThreadFactoryBuilder().setNameFormat("async-pagination-query-executor-%d").build())
    .setCorePoolSize(2).setMaximumPoolSize(10).setKeepAliveTime(Duration.ofSeconds(60)).build();

  /**
   * Instantiates a new Authority search service.
   *
   * @param authorityFilterSpecs the authority filter specs
   * @param authorityRepository
   */
  public AuthoritySearchService(AuthorityFilterSpecs authorityFilterSpecs, IndependentAuthorityRepository authorityRepository) {
    this.authorityFilterSpecs = authorityFilterSpecs;
    this.authorityRepository = authorityRepository;
  }

  /**
   * Gets specifications.
   *
   * @param authoritySpecs the pen reg batch specs
   * @param i            the
   * @param search       the search
   * @return the specifications
   */
  public Specification<IndependentAuthorityEntity> getSpecifications(Specification<IndependentAuthorityEntity> authoritySpecs, int i, Search search) {
    if (i == 0) {
      authoritySpecs = getAuthorityEntitySpecification(search.getSearchCriteriaList());
    } else {
      if (search.getCondition() == Condition.AND) {
        authoritySpecs = authoritySpecs.and(getAuthorityEntitySpecification(search.getSearchCriteriaList()));
      } else {
        authoritySpecs = authoritySpecs.or(getAuthorityEntitySpecification(search.getSearchCriteriaList()));
      }
    }
    return authoritySpecs;
  }

  private Specification<IndependentAuthorityEntity> getAuthorityEntitySpecification(List<SearchCriteria> criteriaList) {
    Specification<IndependentAuthorityEntity> authoritySpecs = null;
    if (!criteriaList.isEmpty()) {
      int i = 0;
      for (SearchCriteria criteria : criteriaList) {
        if (criteria.getKey() != null && criteria.getOperation() != null && criteria.getValueType() != null) {
          var criteriaValue = criteria.getValue();
          if(StringUtils.isNotBlank(criteria.getValue()) && TransformUtil.isUppercaseField(SchoolEntity.class, criteria.getKey())) {
            criteriaValue = criteriaValue.toUpperCase();
          }
          Specification<IndependentAuthorityEntity> typeSpecification = getTypeSpecification(criteria.getKey(), criteria.getOperation(), criteriaValue, criteria.getValueType());
          authoritySpecs = getSpecificationPerGroup(authoritySpecs, i, criteria, typeSpecification);
          i++;
        } else {
          throw new InvalidParameterException("Search Criteria can not contain null values for key, value and operation type");
        }
      }
    }
    return authoritySpecs;
  }

  /**
   * Gets specification per group.
   *
   * @param authorityEntitySpecification the pen request batch entity specification
   * @param i                          the
   * @param criteria                   the criteria
   * @param typeSpecification          the type specification
   * @return the specification per group
   */
  private Specification<IndependentAuthorityEntity> getSpecificationPerGroup(Specification<IndependentAuthorityEntity> authorityEntitySpecification, int i, SearchCriteria criteria, Specification<IndependentAuthorityEntity> typeSpecification) {
    if (i == 0) {
      authorityEntitySpecification = Specification.where(typeSpecification);
    } else {
      if (criteria.getCondition() == Condition.AND) {
        authorityEntitySpecification = authorityEntitySpecification.and(typeSpecification);
      } else {
        authorityEntitySpecification = authorityEntitySpecification.or(typeSpecification);
      }
    }
    return authorityEntitySpecification;
  }

  private Specification<IndependentAuthorityEntity> getTypeSpecification(String key, FilterOperation filterOperation, String value, ValueType valueType) {
    Specification<IndependentAuthorityEntity> authorityEntitySpecification = null;
    switch (valueType) {
      case STRING:
        authorityEntitySpecification = authorityFilterSpecs.getStringTypeSpecification(key, value, filterOperation);
        break;
      case DATE_TIME:
        authorityEntitySpecification = authorityFilterSpecs.getDateTimeTypeSpecification(key, value, filterOperation);
        break;
      case UUID:
        authorityEntitySpecification = authorityFilterSpecs.getUUIDTypeSpecification(key, value, filterOperation);
        break;
      case BOOLEAN:
        authorityEntitySpecification = authorityFilterSpecs.getBooleanTypeSpecification(key, value, filterOperation);
        break;
      default:
        break;
    }
    return authorityEntitySpecification;
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public CompletableFuture<Page<IndependentAuthorityEntity>> findAll(Specification<IndependentAuthorityEntity> authoritySpecs, final Integer pageNumber, final Integer pageSize, final List<Sort.Order> sorts) {
    return CompletableFuture.supplyAsync(() -> {
      Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sorts));
      try {
        return this.authorityRepository.findAll(authoritySpecs, paging);
      } catch (final Exception ex) {
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
  public Specification<IndependentAuthorityEntity> setSpecificationAndSortCriteria(String sortCriteriaJson, String searchCriteriaListJson, ObjectMapper objectMapper, List<Sort.Order> sorts) {
    Specification<IndependentAuthorityEntity> authoritySpecs = null;
    try {
      RequestUtil.getSortCriteria(sortCriteriaJson, objectMapper, sorts);
      if (StringUtils.isNotBlank(searchCriteriaListJson)) {
        List<Search> searches = objectMapper.readValue(searchCriteriaListJson, new TypeReference<>() {
        });
        int i = 0;
        for (var search : searches) {
          authoritySpecs = getSpecifications(authoritySpecs, i, search);
          i++;
        }
      }
    } catch (JsonProcessingException e) {
      throw new AuthorityAPIRuntimeException(e.getMessage());
    }
    return authoritySpecs;
  }
}
