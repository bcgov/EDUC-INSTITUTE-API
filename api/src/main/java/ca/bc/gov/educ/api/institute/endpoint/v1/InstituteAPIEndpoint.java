package ca.bc.gov.educ.api.institute.endpoint.v1;

import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.struct.v1.CountryCode;
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

@RequestMapping(URL.BASE_URL)
@OpenAPIDefinition(info = @Info(title = "API to Institute CRU.", description = "This API is related to district, independent authority and school data.", version = "1"),
  security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_INSTITUTE_CODES"})})
public interface InstituteAPIEndpoint {

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.COUNTRY_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all country codes.", description = "Endpoint to get all country codes.")
  @Schema(name = "CountryCode", implementation = CountryCode.class)
  List<CountryCode> getCountryCodes();
}
