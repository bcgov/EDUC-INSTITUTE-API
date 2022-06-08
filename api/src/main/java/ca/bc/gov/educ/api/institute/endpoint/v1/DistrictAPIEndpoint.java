package ca.bc.gov.educ.api.institute.endpoint.v1;

import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.struct.v1.District;
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

@RequestMapping(URL.BASE_URL_DISTRICT)
@OpenAPIDefinition(info = @Info(title = "API to District CRUD.", description = "This API is related to district data.", version = "1"),
  security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_DISTRICT", "WRITE_DISTRICT", "DELETE_DISTRICT"})})
public interface DistrictAPIEndpoint {

  @GetMapping("/{districtId}")
  @PreAuthorize("hasAuthority('SCOPE_READ_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get district entity.", description = "Endpoint to get district entity by ID.")
  @Schema(name = "District", implementation = District.class)
  District getDistrict(@PathVariable("districtId")  String districtId);

  @PostMapping
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "CREATED"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
  @Tag(name = "Endpoint to create district entity.", description = "Endpoint to create district entity.")
  @Schema(name = "District", implementation = District.class)
  @ResponseStatus(CREATED)
  District createDistrict(@Validated @RequestBody District district) throws JsonProcessingException;

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('SCOPE_WRITE_DISTRICT')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST"), @ApiResponse(responseCode = "404", description = "NOT FOUND")})
  @Tag(name = "Endpoint to update district entity.", description = "Endpoint to update district entity.")
  @Schema(name = "District", implementation = District.class)
  District updateDistrict(@PathVariable UUID id, @Validated @RequestBody District district) throws JsonProcessingException;

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

}
