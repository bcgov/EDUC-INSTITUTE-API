package ca.bc.gov.educ.api.institute.endpoint.v1;

import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolHistory;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping(URL.BASE_URL_SCHOOL)
@OpenAPIDefinition(info = @Info(title = "API to School CRUD.", description = "This API is related to school data.", version = "1"),
  security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_SCHOOL", "WRITE_SCHOOL", "DELETE_SCHOOL"})})
public interface SchoolAPIEndpoint {

  @GetMapping("/{schoolId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get school entity.", description = "Endpoint to get school entity by ID.")
  @Schema(name = "School", implementation = School.class)
  School getSchool(@PathVariable("schoolId")  String schoolId);

  @GetMapping("/{schoolId}/history")
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get school history entity list by school ID.", description = "Endpoint to get school history entity list by school ID.")
  @Schema(name = "SchoolHistory", implementation = SchoolHistory.class)
  List<SchoolHistory> getSchoolHistory(@PathVariable("schoolId")  String schoolId);

  @PostMapping
  @PreAuthorize("hasAuthority('SCOPE_WRITE_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to create school entity.", description = "Endpoint to create school entity.")
  @Schema(name = "School", implementation = School.class)
  @ResponseStatus(CREATED)
  School createSchool(@Validated @RequestBody School school) throws JsonProcessingException;

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Tag(name = "Endpoint to update school entity.", description = "Endpoint to update school entity.")
  @Schema(name = "School", implementation = School.class)
  School updateSchool(@PathVariable UUID id, @Validated @RequestBody School school) throws JsonProcessingException;

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "404", description = "NOT FOUND."), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Tag(name = "Endpoint to delete school entity.", description = "Endpoint to delete school entity.")
  ResponseEntity<Void> deleteSchool(@PathVariable UUID id);

  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all school entities.", description = "Endpoint to get all school entities.")
  @Schema(name = "School", implementation = School.class)
  List<School> getAllSchools();

}
