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

@RequestMapping(URL.BASE_URL_DISTRICT)
public interface DistrictAPIEndpoint {

  @GetMapping("/{districtId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "District Entity", description = "Endpoints for district entity.")
  @Schema(name = "District", implementation = District.class)
  District getDistrict(@PathVariable("districtId") UUID districtId);

  @GetMapping("/{districtId}/history")
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "District History Entity", description = "Endpoints for district history entity.")
  @Schema(name = "DistrictHistory", implementation = DistrictHistory.class)
  List<DistrictHistory> getDistrictHistory(@PathVariable("districtId") UUID districtId);

  @PostMapping
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "District Entity", description = "Endpoints for district entity.")
  @Schema(name = "District", implementation = District.class)
  @ResponseStatus(CREATED)
  District createDistrict(@Validated @RequestBody District district) throws JsonProcessingException;

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Tag(name = "District Entity", description = "Endpoints for district entity.")
  @Schema(name = "District", implementation = District.class)
  District updateDistrict(@PathVariable UUID id, @Validated @RequestBody District district) throws JsonProcessingException;

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "404", description = "NOT FOUND."), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Tag(name = "District Entity", description = "Endpoints for district entity.")
  ResponseEntity<Void> deleteDistrict(@PathVariable UUID id);

  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "District Entity", description = "Endpoints for district entity.")
  @Schema(name = "DistrictTombstone", implementation = DistrictTombstone.class)
  List<District> getAllDistricts();

  @GetMapping("/paginated")
  @Async
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Transactional(readOnly = true)
  @Tag(name = "District Entity", description = "Endpoints for district note entity.")
  CompletableFuture<Page<District>> findAll(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(name = "sort", defaultValue = "") String sortCriteriaJson,
                                          @RequestParam(name = "searchCriteriaList", required = false) String searchCriteriaListJson);

  @GetMapping("/contact/paginated")
  @Async
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Transactional(readOnly = true)
  @Tag(name = "District Contact Entity", description = "Endpoints for district contact entity.")
  @Schema(name = "DistrictContact", implementation = DistrictContact.class)
  CompletableFuture<Page<DistrictContact>> findAllContacts(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(name = "sort", defaultValue = "") String sortCriteriaJson,
                                          @RequestParam(name = "searchCriteriaList", required = false) String searchCriteriaListJson);

  @GetMapping("/{districtId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "District Contact Entity", description = "Endpoints for district contact entity.")
  @Schema(name = "DistrictContact", implementation = DistrictContact.class)
  DistrictContact getDistrictContact(@PathVariable UUID districtId, @PathVariable UUID contactId);

  @PostMapping("/{districtId}/contact")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "District Contact Entity", description = "Endpoints for district contact entity.")
  @Schema(name = "DistrictContact", implementation = DistrictContact.class)
  @ResponseStatus(CREATED)
  DistrictContact createDistrictContact(@PathVariable UUID districtId, @Validated @RequestBody DistrictContact contact) throws JsonProcessingException;

  @PutMapping("/{districtId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "District Contact Entity", description = "Endpoints for district contact entity.")
  @Schema(name = "DistrictContact", implementation = DistrictContact.class)
  DistrictContact updateDistrictContact(@PathVariable UUID districtId, @PathVariable UUID contactId, @Validated @RequestBody DistrictContact contact) throws JsonProcessingException;

  @DeleteMapping("/{districtId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_DISTRICT_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "District Contact Entity", description = "Endpoints for district contact entity.")
  ResponseEntity<Void> deleteDistrictContact(@PathVariable UUID districtId, @PathVariable UUID contactId) throws JsonProcessingException;

  @GetMapping("/{districtId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "District Note Entity", description = "Endpoints for district note entity.")
  @Schema(name = "Note", implementation = Note.class)
  Note getDistrictNote(@PathVariable UUID districtId, @PathVariable UUID noteId);

  @GetMapping("/{districtId}/note")
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "District Note Entity", description = "Endpoints for district note entity.")
  @Schema(name = "Note", implementation = Note.class)
  List<Note> getDistrictNotes(@PathVariable UUID districtId);

  @PostMapping("/{districtId}/note")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "District Note Entity", description = "Endpoints for district note entity.")
  @Schema(name = "Note", implementation = Note.class)
  @ResponseStatus(CREATED)
  Note createDistrictNote(@PathVariable UUID districtId, @Validated @RequestBody Note note);

  @PutMapping("/{districtId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "District Note Entity", description = "Endpoints for district note entity.")
  @Schema(name = "Note", implementation = Note.class)
  Note updateDistrictNote(@PathVariable UUID districtId, @PathVariable UUID noteId, @Validated @RequestBody Note note);

  @DeleteMapping("/{districtId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_DISTRICT_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "District Note Entity", description = "Endpoints for district note entity.")
  ResponseEntity<Void> deleteDistrictNote(@PathVariable UUID districtId, @PathVariable UUID noteId);

}
