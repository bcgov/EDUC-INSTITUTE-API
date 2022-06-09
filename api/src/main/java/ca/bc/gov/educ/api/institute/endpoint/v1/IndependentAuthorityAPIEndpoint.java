package ca.bc.gov.educ.api.institute.endpoint.v1;

import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthority;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthorityHistory;
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

@RequestMapping(URL.BASE_URL_AUTHORITY)
@OpenAPIDefinition(info = @Info(title = "API to Independent Authority CRUD.", description = "This API is related to independent authority data.", version = "1"),
  security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_INDEPENDENT_AUTHORITY", "WRITE_INDEPENDENT_AUTHORITY", "DELETE_INDEPENDENT_AUTHORITY"})})
public interface IndependentAuthorityAPIEndpoint {

  @GetMapping("/{independentAuthorityId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get independent authority entity.", description = "Endpoint to get independent authority entity by ID.")
  @Schema(name = "Independent Authority", implementation = IndependentAuthority.class)
  IndependentAuthority getIndependentAuthority(@PathVariable("independentAuthorityId")  String independentAuthorityId);

  @GetMapping("/{independentAuthorityId}/history")
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get independent authority history entity list by independent authority ID.", description = "Endpoint to get independent authority history entity list by independent authority ID.")
  @Schema(name = "IndependentAuthorityHistory", implementation = IndependentAuthorityHistory.class)
  List<IndependentAuthorityHistory> getIndependentAuthorityHistory(@PathVariable("independentAuthorityId")  String independentAuthorityId);

  @PostMapping
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to create independent authority entity.", description = "Endpoint to create independent authority entity.")
  @Schema(name = "IndependentAuthority", implementation = IndependentAuthority.class)
  @ResponseStatus(CREATED)
  IndependentAuthority createIndependentAuthority(@Validated @RequestBody IndependentAuthority independentAuthority) throws JsonProcessingException;

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Tag(name = "Endpoint to update independent authority entity.", description = "Endpoint to update independent authority entity.")
  @Schema(name = "IndependentAuthority", implementation = IndependentAuthority.class)
  IndependentAuthority updateIndependentAuthority(@PathVariable UUID id, @Validated @RequestBody IndependentAuthority independentAuthority) throws JsonProcessingException;

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "404", description = "NOT FOUND."), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Tag(name = "Endpoint to delete independent authority entity.", description = "Endpoint to delete independent authority entity.")
  ResponseEntity<Void> deleteIndependentAuthority(@PathVariable UUID id);

  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_READ_INDEPENDENT_AUTHORITY')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all independent authority entities.", description = "Endpoint to get all independent authority entities.")
  @Schema(name = "IndependentAuthority", implementation = IndependentAuthority.class)
  List<IndependentAuthority> getAllIndependentAuthorities();

}
