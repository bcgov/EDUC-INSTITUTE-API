package ca.bc.gov.educ.api.institute.endpoint.v1;

import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.struct.v1.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping(URL.BASE_URL_AUTHORITY)
public interface IndependentAuthorityAPIEndpoint {

  @GetMapping("/{independentAuthorityId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Independent Authority Entity", description = "Endpoint to get independent authority entity by ID.")
  @Schema(name = "IndependentAuthority", implementation = IndependentAuthority.class)
  IndependentAuthority getIndependentAuthority(@PathVariable("independentAuthorityId")  UUID independentAuthorityId);

  @GetMapping("/{independentAuthorityId}/history")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Independent Authority Entity", description = "Endpoint to get independent authority history entity list by independent authority ID.")
  @Schema(name = "IndependentAuthorityHistory", implementation = IndependentAuthorityHistory.class)
  List<IndependentAuthorityHistory> getIndependentAuthorityHistory(@PathVariable("independentAuthorityId")  UUID independentAuthorityId);

  @PostMapping
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Entity", description = "Endpoint to create independent authority entity.")
  @Schema(name = "IndependentAuthority", implementation = IndependentAuthority.class)
  @ResponseStatus(CREATED)
  IndependentAuthority createIndependentAuthority(@Validated @RequestBody IndependentAuthority independentAuthority);

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Tag(name = "Independent Authority Entity", description = "Endpoint to update independent authority entity.")
  @Schema(name = "IndependentAuthority", implementation = IndependentAuthority.class)
  IndependentAuthority updateIndependentAuthority(@PathVariable UUID id, @Validated @RequestBody IndependentAuthority independentAuthority);

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "404", description = "NOT FOUND."), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Tag(name = "Independent Authority Entity", description = "Endpoint to delete independent authority entity.")
  ResponseEntity<Void> deleteIndependentAuthority(@PathVariable UUID id);

  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Independent Authority Entity", description = "Endpoint to get all independent authority entities.")
  @Schema(name = "IndependentAuthority", implementation = IndependentAuthority.class)
  List<IndependentAuthority> getAllIndependentAuthorities();

  @GetMapping("/{independentAuthorityId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Contact Entity", description = "Endpoint to get independent authority contact entity.")
  @Schema(name = "Contact", implementation = Contact.class)
  Contact getIndependentAuthorityContact(@PathVariable UUID independentAuthorityId, @PathVariable UUID contactId);

  @PostMapping("/{independentAuthorityId}/contact")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Contact Entity", description = "Endpoint to create independent authority contact entity.")
  @Schema(name = "Contact", implementation = Contact.class)
  @ResponseStatus(CREATED)
  Contact createIndependentAuthorityContact(@PathVariable UUID independentAuthorityId, @Validated @RequestBody Contact contact);

  @PutMapping("/{independentAuthorityId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Contact Entity", description = "Endpoint to update independent authority contact entity.")
  @Schema(name = "Contact", implementation = Contact.class)
  Contact updateIndependentAuthorityContact(@PathVariable UUID independentAuthorityId, @PathVariable UUID contactId, @Validated @RequestBody Contact contact);

  @DeleteMapping("/{independentAuthorityId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_INDEPENDENT_AUTHORITY_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Contact Entity", description = "Endpoint to delete independent authority contact entity.")
  ResponseEntity<Void> deleteIndependentAuthorityContact(@PathVariable UUID independentAuthorityId, @PathVariable UUID contactId);

  @GetMapping("/{independentAuthorityId}/address/{addressId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY_ADDRESS')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Address Entity", description = "Endpoint to get independent authority address entity.")
  @Schema(name = "Address", implementation = Address.class)
  Address getIndependentAuthorityAddress(@PathVariable UUID independentAuthorityId, @PathVariable UUID addressId);

  @PostMapping("/{independentAuthorityId}/address")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY_ADDRESS')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Address Entity", description = "Endpoint to create independent authority address entity.")
  @Schema(name = "Address", implementation = Address.class)
  @ResponseStatus(CREATED)
  Address createIndependentAuthorityAddress(@PathVariable UUID independentAuthorityId, @Validated @RequestBody Address address);

  @PutMapping("/{independentAuthorityId}/address/{addressId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY_ADDRESS')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Address Entity", description = "Endpoint to update independent authority address entity.")
  @Schema(name = "Address", implementation = Address.class)
  Address updateIndependentAuthorityAddress(@PathVariable UUID independentAuthorityId, @PathVariable UUID addressId, @Validated @RequestBody Address address);

  @DeleteMapping("/{independentAuthorityId}/address/{addressId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_INDEPENDENT_AUTHORITY_ADDRESS')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Address Entity", description = "Endpoint to delete independent authority address entity.")
  ResponseEntity<Void> deleteIndependentAuthorityAddress(@PathVariable UUID independentAuthorityId, @PathVariable UUID addressId);

  @GetMapping("/{independentAuthorityId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Note Entity", description = "Endpoint to get independent authority note entity.")
  @Schema(name = "Note", implementation = Note.class)
  Note getIndependentAuthorityNote(@PathVariable UUID independentAuthorityId, @PathVariable UUID noteId);

  @PostMapping("/{independentAuthorityId}/note")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Note Entity", description = "Endpoint to create independent authority note entity.")
  @Schema(name = "Note", implementation = Note.class)
  @ResponseStatus(CREATED)
  Note createIndependentAuthorityNote(@PathVariable UUID independentAuthorityId, @Validated @RequestBody Note note);

  @PutMapping("/{independentAuthorityId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Note Entity", description = "Endpoint to update independentAuthority note entity.")
  @Schema(name = "Note", implementation = Note.class)
  Note updateIndependentAuthorityNote(@PathVariable UUID independentAuthorityId, @PathVariable UUID noteId, @Validated @RequestBody Note note);

  @DeleteMapping("/{independentAuthorityId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_INDEPENDENT_AUTHORITY_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Independent Authority Note Entity", description = "Endpoint to delete independent authority note entity.")
  ResponseEntity<Void> deleteIndependentAuthorityNote(@PathVariable UUID independentAuthorityId, @PathVariable UUID noteId);


}
