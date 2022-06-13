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

@RequestMapping(URL.BASE_URL_DISTRICT)
public interface DistrictAPIEndpoint {

  @GetMapping("/{districtId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get district entity.", description = "Endpoint to get district entity by ID.")
  @Schema(name = "District", implementation = District.class)
  District getDistrict(@PathVariable("districtId") UUID districtId);

  @GetMapping("/{districtId}/history")
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get district history entity list by district ID.", description = "Endpoint to get district history entity list by district ID.")
  @Schema(name = "DistrictHistory", implementation = DistrictHistory.class)
  List<DistrictHistory> getDistrictHistory(@PathVariable("districtId") UUID districtId);

  @PostMapping
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to create district entity.", description = "Endpoint to create district entity.")
  @Schema(name = "District", implementation = District.class)
  @ResponseStatus(CREATED)
  District createDistrict(@Validated @RequestBody District district);

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Tag(name = "Endpoint to update district entity.", description = "Endpoint to update district entity.")
  @Schema(name = "District", implementation = District.class)
  District updateDistrict(@PathVariable UUID id, @Validated @RequestBody District district);

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "NO CONTENT"), @ApiResponse(responseCode = "404", description = "NOT FOUND."), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Tag(name = "Endpoint to delete district entity.", description = "Endpoint to delete district entity.")
  ResponseEntity<Void> deleteDistrict(@PathVariable UUID id);

  @GetMapping
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all district entities.", description = "Endpoint to get all district entities.")
  @Schema(name = "District", implementation = District.class)
  List<District> getAllDistricts();

  @GetMapping("/{districtId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to get district contact entity.", description = "Endpoint to get district contact entity.")
  @Schema(name = "Contact", implementation = Contact.class)
  Contact getDistrictContact(@PathVariable UUID districtId, @PathVariable UUID contactId);

  @PostMapping("/{districtId}/contact")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to create district contact entity.", description = "Endpoint to create district contact entity.")
  @Schema(name = "Contact", implementation = Contact.class)
  @ResponseStatus(CREATED)
  Contact createDistrictContact(@PathVariable UUID districtId, @Validated @RequestBody Contact contact);

  @PutMapping("/{districtId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to update district contact entity.", description = "Endpoint to update district contact entity.")
  @Schema(name = "Contact", implementation = Contact.class)
  Contact updateDistrictContact(@PathVariable UUID districtId, @PathVariable UUID contactId, @Validated @RequestBody Contact contact);

  @DeleteMapping("/{districtId}/contact/{contactId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_DISTRICT_CONTACT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to delete district contact entity.", description = "Endpoint to delete district contact entity.")
  ResponseEntity<Void> deleteDistrictContact(@PathVariable UUID districtId, @PathVariable UUID contactId);

  @GetMapping("/{districtId}/address/{addressId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT_ADDRESS')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to get district address entity.", description = "Endpoint to get district address entity.")
  @Schema(name = "Address", implementation = Address.class)
  Address getDistrictAddress(@PathVariable UUID districtId, @PathVariable UUID addressId);

  @PostMapping("/{districtId}/address")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT_ADDRESS')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to create district address entity.", description = "Endpoint to create district address entity.")
  @Schema(name = "Address", implementation = Address.class)
  @ResponseStatus(CREATED)
  Address createDistrictAddress(@PathVariable UUID districtId, @Validated @RequestBody Address address);

  @PutMapping("/{districtId}/address/{addressId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT_ADDRESS')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to update district address entity.", description = "Endpoint to update district address entity.")
  @Schema(name = "Address", implementation = Address.class)
  Address updateDistrictAddress(@PathVariable UUID districtId, @PathVariable UUID addressId, @Validated @RequestBody Address address);

  @DeleteMapping("/{districtId}/address/{addressId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_DISTRICT_ADDRESS')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to delete district address entity.", description = "Endpoint to delete district address entity.")
  ResponseEntity<Void> deleteDistrictAddress(@PathVariable UUID districtId, @PathVariable UUID addressId);

  @GetMapping("/{districtId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to get district note entity.", description = "Endpoint to get district note entity.")
  @Schema(name = "Note", implementation = Note.class)
  Note getDistrictNote(@PathVariable UUID districtId, @PathVariable UUID noteId);

  @PostMapping("/{districtId}/note")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to create district note entity.", description = "Endpoint to create district note entity.")
  @Schema(name = "Note", implementation = Note.class)
  @ResponseStatus(CREATED)
  Note createDistrictNote(@PathVariable UUID districtId, @Validated @RequestBody Note note);

  @PutMapping("/{districtId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to update district note entity.", description = "Endpoint to update district note entity.")
  @Schema(name = "Note", implementation = Note.class)
  Note updateDistrictNote(@PathVariable UUID districtId, @PathVariable UUID noteId, @Validated @RequestBody Note note);

  @DeleteMapping("/{districtId}/note/{noteId}")
  @PreAuthorize("hasAuthority('SCOPE_DELETE_DISTRICT_NOTE')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to delete district note entity.", description = "Endpoint to delete district note entity.")
  ResponseEntity<Void> deleteDistrictNote(@PathVariable UUID districtId, @PathVariable UUID noteId);

}
