package ca.bc.gov.educ.api.institute.endpoint.v1;

import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolHistory;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(URL.BASE_URL_SCHOOL_HISTORY)
public interface SchoolHistoryAPIEndpoint {

  @GetMapping("/paginated")
  @Async
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL_HISTORY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Transactional(readOnly = true)
  @Tag(name = "School History Entity", description = "Endpoints for school history entity.")
  CompletableFuture<Page<SchoolHistory>> findAll(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           @RequestParam(name = "sort", defaultValue = "") String sortCriteriaJson,
                                           @RequestParam(name = "searchCriteriaList", required = false) String searchCriteriaListJson);
}
