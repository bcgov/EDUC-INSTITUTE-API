package ca.bc.gov.educ.api.institute.endpoint.v1;

import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.struct.v1.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping(URL.BASE_URL_SCHOOL)
public interface SchoolAPIEndpoint {

  @GetMapping("/{schoolId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "School Entity", description = "Endpoints for school entity.")
  @Schema(name = "School", implementation = School.class)
  School getSchool(@PathVariable("schoolId")  UUID schoolId);

  @GetMapping("/{schoolId}/history")
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "School History Entity", description = "Endpoints for school history entity.")
  @Schema(name = "SchoolHistory", implementation = SchoolHistory.class)
  List<SchoolHistory> getSchoolHistory(@PathVariable("schoolId")  UUID schoolId);

  @PostMapping
  @PreAuthorize("hasAuthority('SCOPE_WRITE_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "School Entity", description = "Endpoints for school entity.")
  @Schema(name = "School", implementation = School.class)
  @ResponseStatus(CREATED)
  School createSchool(@Validated @RequestBody School school) throws JsonProcessingException;

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Tag(name = "School Entity", description = "Endpoints for school entity.")
  @Schema(name = "School", implementation = School.class)
  School updateSchool(@PathVariable UUID id, @Validated @RequestBody School school) throws JsonProcessingException;

  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "School Entity", description = "Endpoints for school entity.")
  @Schema(name = "SchoolTombstone", implementation = SchoolTombstone.class)
  List<SchoolTombstone> getAllSchools();

  @GetMapping("/{schoolId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "School Contact Entity", description = "Endpoints for school contact entity.")
  @Schema(name = "SchoolContact", implementation = SchoolContact.class)
  SchoolContact getSchoolContact(@PathVariable UUID schoolId, @PathVariable UUID contactId);

  @PostMapping("/{schoolId}/contact")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_SCHOOL_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "School Contact Entity", description = "Endpoints for school contact entity.")
  @Schema(name = "SchoolContact", implementation = SchoolContact.class)
  @ResponseStatus(CREATED)
  SchoolContact createSchoolContact(@PathVariable UUID schoolId, @Validated @RequestBody SchoolContact contact) throws JsonProcessingException;

  @PutMapping("/{schoolId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_SCHOOL_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "School Contact Entity", description = "Endpoints for school contact entity.")
  @Schema(name = "SchoolContact", implementation = SchoolContact.class)
  SchoolContact updateSchoolContact(@PathVariable UUID schoolId, @PathVariable UUID contactId, @Validated @RequestBody SchoolContact contact) throws JsonProcessingException;

  @DeleteMapping("/{schoolId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_SCHOOL_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "School Contact Entity", description = "Endpoints for school contact entity.")
  ResponseEntity<Void> deleteSchoolContact(@PathVariable UUID schoolId, @PathVariable UUID contactId) throws JsonProcessingException;

  @GetMapping("/{schoolId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "School Note Entity", description = "Endpoints for school note entity.")
  @Schema(name = "Note", implementation = Note.class)
  Note getSchoolNote(@PathVariable UUID schoolId, @PathVariable UUID noteId);

  @GetMapping("/{schoolId}/note")
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "School Note Entity", description = "Endpoints for school note entity.")
  @Schema(name = "Note", implementation = Note.class)
  List<Note> getSchoolNotes(@PathVariable UUID schoolId);

  @PostMapping("/{schoolId}/note")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_SCHOOL_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "School Note Entity", description = "Endpoints for school note entity.")
  @Schema(name = "Note", implementation = Note.class)
  @ResponseStatus(CREATED)
  Note createSchoolNote(@PathVariable UUID schoolId, @Validated @RequestBody Note note);

  @PutMapping("/{schoolId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_SCHOOL_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "School Note Entity", description = "Endpoints for school note entity.")
  @Schema(name = "Note", implementation = Note.class)
  Note updateSchoolNote(@PathVariable UUID schoolId, @PathVariable UUID noteId, @Validated @RequestBody Note note);

  @DeleteMapping("/{schoolId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_SCHOOL_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "School Note Entity", description = "Endpoints for school note entity.")
  ResponseEntity<Void> deleteSchoolNote(@PathVariable UUID schoolId, @PathVariable UUID noteId);

  @GetMapping("/paginated")
  @Async
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Transactional(readOnly = true)
  @Tag(name = "School Entity", description = "Endpoints for school entity.")
  CompletableFuture<Page<School>> findAll(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           @RequestParam(name = "sort", defaultValue = "") String sortCriteriaJson,
                                           @RequestParam(name = "searchCriteriaList", required = false) String searchCriteriaListJson);

  @GetMapping("/history/paginated")
  @Async
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL_HISTORY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Transactional(readOnly = true)
  @Tag(name = "School History Entity", description = "Endpoints for school history entity.")
  CompletableFuture<Page<SchoolHistory>> schoolHistoryFindAll(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
      @RequestParam(name = "sort", defaultValue = "") String sortCriteriaJson,
      @RequestParam(name = "searchCriteriaList", required = false) String searchCriteriaListJson);

  @GetMapping("/contact/paginated")
  @Async
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Transactional(readOnly = true)
  @Tag(name = "School Contact Entity", description = "Endpoints for school contact entity.")
  @Schema(name = "SchoolContact", implementation = SchoolContact.class)
  CompletableFuture<Page<SchoolContact>> findAllContacts(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           @RequestParam(name = "sort", defaultValue = "") String sortCriteriaJson,
                                                           @RequestParam(name = "searchCriteriaList", required = false) String searchCriteriaListJson);

  @GetMapping("/funding-groups")
  @PreAuthorize("hasAuthority('SCOPE_READ_SCHOOL')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "School Entity", description = "Endpoints for school entity.")
  @Schema(name = "IndependentSchoolFundingGroup", implementation = IndependentSchoolFundingGroup.class)
  List<IndependentSchoolFundingGroup> getAllSchoolFundingGroups();

}
