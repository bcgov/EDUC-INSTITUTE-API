package ca.bc.gov.educ.api.institute.endpoint.v1;

import ca.bc.gov.educ.api.institute.struct.v1.School;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
/**
 * The interface School api endpoint.
 */
@RequestMapping("/api/v1/schools")
@OpenAPIDefinition(info = @Info(title = "API to School CRU.", description = "This API is related to school data.", version = "1"),
  security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_SCHOOL"})})
public interface SchoolAPIEndpoint {
  /**
   * Get school by mincode.
   *
   * @param mincode the mincode
   * @return the pen request batch
   */
  @GetMapping("/{minCode}")
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get School Entity.", description = "Endpoint to get School Entity By Mincode.")
  @Schema(name = "School", implementation = School.class)
  School getSchoolByMinCode(@PathVariable("minCode")  String mincode);

  /**
   * Gets all schools.
   *
   * @return the all schools
   */
  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get All School Entity.", description = "Endpoint to get All School Entity.")
  @Schema(name = "School", implementation = School.class)
  List<School> getAllSchools();

}
