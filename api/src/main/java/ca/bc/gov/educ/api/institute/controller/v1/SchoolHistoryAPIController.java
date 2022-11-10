package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.SchoolHistoryAPIEndpoint;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolHistoryMapper;
import ca.bc.gov.educ.api.institute.model.v1.SchoolHistoryEntity;
import ca.bc.gov.educ.api.institute.service.v1.SchoolHistorySearchService;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolHistory;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SchoolHistoryAPIController implements SchoolHistoryAPIEndpoint {

  private static final SchoolHistoryMapper mapper = SchoolHistoryMapper.mapper;

  @Getter(AccessLevel.PRIVATE)
  private final SchoolHistorySearchService schoolHistorySearchService;

  @Autowired
  public SchoolHistoryAPIController(SchoolHistorySearchService schoolHistorySearchService) {
    this.schoolHistorySearchService = schoolHistorySearchService;
  }

  @Override
  public CompletableFuture<Page<SchoolHistory>> findAll(Integer pageNumber, Integer pageSize, String sortCriteriaJson, String searchCriteriaListJson) {
    final List<Sort.Order> sorts = new ArrayList<>();
    Specification<SchoolHistoryEntity> schoolHistorySpecs = schoolHistorySearchService.setSpecificationAndSortCriteria(sortCriteriaJson, searchCriteriaListJson, JsonUtil.mapper, sorts);
    return this.schoolHistorySearchService.findAll(schoolHistorySpecs, pageNumber, pageSize, sorts).thenApplyAsync(schoolHistoryEntities -> schoolHistoryEntities.map(mapper::toStructure));
  }

}
