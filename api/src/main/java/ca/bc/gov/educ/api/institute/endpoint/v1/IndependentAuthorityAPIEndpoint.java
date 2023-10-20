package ca.bc.gov.educ.api.institute.endpoint.v1;

import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.struct.v1.AuthorityContact;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthority;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthorityHistory;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
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

@RequestMapping(URL.BASE_URL_AUTHORITY)
public interface IndependentAuthorityAPIEndpoint {

  @GetMapping("/{independentAuthorityId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Independent Authority Entity", description = "Endpoints for independent authority entity.")
  @Schema(name = "IndependentAuthority", implementation = IndependentAuthority.class)
  IndependentAuthority getIndependentAuthority(@PathVariable("independentAuthorityId")  UUID independentAuthorityId);

  @GetMapping("/{independentAuthorityId}/history")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Independent Authority History Entity", description = "Endpoints for independent authority history entity.")
  @Schema(name = "IndependentAuthorityHistory", implementation = IndependentAuthorityHistory.class)
  List<IndependentAuthorityHistory> getIndependentAuthorityHistory(@PathVariable("independentAuthorityId")  UUID independentAuthorityId);

  @PostMapping
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Entity", description = "Endpoints for independent authority entity.")
  @Schema(name = "IndependentAuthority", implementation = IndependentAuthority.class)
  @ResponseStatus(CREATED)
  IndependentAuthority createIndependentAuthority(@Validated @RequestBody IndependentAuthority independentAuthority) throws JsonProcessingException;

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Tag(name = "Independent Authority Entity", description = "Endpoints for independent authority entity.")
  @Schema(name = "IndependentAuthority", implementation = IndependentAuthority.class)
  IndependentAuthority updateIndependentAuthority(@PathVariable UUID id, @Validated @RequestBody IndependentAuthority independentAuthority) throws JsonProcessingException;

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "404", description = "NOT FOUND."), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Tag(name = "Independent Authority Entity", description = "Endpoints for independent authority entity.")
  ResponseEntity<Void> deleteIndependentAuthority(@PathVariable UUID id);

  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Independent Authority Entity", description = "Endpoints for independent authority entity.")
  @Schema(name = "IndependentAuthority", implementation = IndependentAuthority.class)
  List<IndependentAuthority> getAllIndependentAuthorities();

  @GetMapping("/{independentAuthorityId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Contact Entity", description = "Endpoints for independent authority contact entity.")
  @Schema(name = "AuthorityContact", implementation = AuthorityContact.class)
  AuthorityContact getIndependentAuthorityContact(@PathVariable UUID independentAuthorityId, @PathVariable UUID contactId);

  @PostMapping("/{independentAuthorityId}/contact")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Contact Entity", description = "Endpoints for independent authority contact entity.")
  @Schema(name = "AuthorityContact", implementation = AuthorityContact.class)
  @ResponseStatus(CREATED)
  AuthorityContact createIndependentAuthorityContact(@PathVariable UUID independentAuthorityId, @Validated @RequestBody AuthorityContact contact);

  @PutMapping("/{independentAuthorityId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Contact Entity", description = "Endpoints for independent authority contact entity.")
  @Schema(name = "AuthorityContact", implementation = AuthorityContact.class)
  AuthorityContact updateIndependentAuthorityContact(@PathVariable UUID independentAuthorityId, @PathVariable UUID contactId, @Validated @RequestBody AuthorityContact contact);

  @DeleteMapping("/{independentAuthorityId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_INDEPENDENT_AUTHORITY_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Contact Entity", description = "Endpoints for independent authority contact entity.")
  ResponseEntity<Void> deleteIndependentAuthorityContact(@PathVariable UUID independentAuthorityId, @PathVariable UUID contactId);

  @GetMapping("/{independentAuthorityId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Note Entity", description = "Endpoints for independent authority note entity.")
  @Schema(name = "Note", implementation = Note.class)
  Note getIndependentAuthorityNote(@PathVariable UUID independentAuthorityId, @PathVariable UUID noteId);

  @GetMapping("/{independentAuthorityId}/note")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Note Entity", description = "Endpoints for authority note entity.")
  @Schema(name = "Note", implementation = Note.class)
  List<Note> getIndependentAuthorityNotes(@PathVariable UUID independentAuthorityId);

  @PostMapping("/{independentAuthorityId}/note")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Note Entity", description = "Endpoints for independent authority note entity.")
  @Schema(name = "Note", implementation = Note.class)
  @ResponseStatus(CREATED)
  Note createIndependentAuthorityNote(@PathVariable UUID independentAuthorityId, @Validated @RequestBody Note note);

  @PutMapping("/{independentAuthorityId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Note Entity", description = "Endpoints for independent authority note entity.")
  @Schema(name = "Note", implementation = Note.class)
  Note updateIndependentAuthorityNote(@PathVariable UUID independentAuthorityId, @PathVariable UUID noteId, @Validated @RequestBody Note note);

  @DeleteMapping("/{independentAuthorityId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_INDEPENDENT_AUTHORITY_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Note Entity", description = "Endpoints for independent authority note entity.")
  ResponseEntity<Void> deleteIndependentAuthorityNote(@PathVariable UUID independentAuthorityId, @PathVariable UUID noteId);

  @GetMapping("/paginated")
  @Async
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Transactional(readOnly = true)
  @Tag(name = "Independent Authority Entity", description = "Endpoints for school entity.")
  CompletableFuture<Page<IndependentAuthority>> findAll(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(name = "sort", defaultValue = "") String sortCriteriaJson,
                                          @RequestParam(name = "searchCriteriaList", required = false) String searchCriteriaListJson);

}
