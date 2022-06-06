package ca.bc.gov.educ.api.institute.endpoint.v1;

import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.struct.v1.*;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(URL.BASE_URL)
@OpenAPIDefinition(info = @Info(title = "API to Institute CRU.", description = "This API is related to district, independent authority and school data.", version = "1"),
  security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_INSTITUTE_CODES"})})
public interface CodeTableAPIEndpoint {

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.COUNTRY_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all country codes.", description = "Endpoint to get all country codes.")
  @Schema(name = "CountryCode", implementation = CountryCode.class)
  List<CountryCode> getCountryCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.PROVINCE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all province codes.", description = "Endpoint to get all province codes.")
  @Schema(name = "ProvinceCode", implementation = ProvinceCode.class)
  List<ProvinceCode> getProvinceCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.DISTRICT_REGION_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all district region codes.", description = "Endpoint to get all district region codes.")
  @Schema(name = "DistrictRegionCode", implementation = DistrictRegionCode.class)
  List<DistrictRegionCode> getDistrictRegionCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.CONTACT_TYPE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all contact type codes.", description = "Endpoint to get all contact type codes.")
  @Schema(name = "ContactTypeCode", implementation = ContactTypeCode.class)
  List<ContactTypeCode> getContactTypeCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.AUTHORITY_TYPE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all authority type codes.", description = "Endpoint to get all authority type codes.")
  @Schema(name = "AuthorityTypeCode", implementation = AuthorityTypeCode.class)
  List<AuthorityTypeCode> getAuthorityTypeCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.AUTHORITY_GROUP_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all authority group codes.", description = "Endpoint to get all authority group codes.")
  @Schema(name = "AuthorityGroupCode", implementation = AuthorityGroupCode.class)
  List<AuthorityGroupCode> getAuthorityGroupCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.ADDRESS_TYPE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all address type codes.", description = "Endpoint to get all address type codes.")
  @Schema(name = "AddressTypeCode", implementation = AddressTypeCode.class)
  List<AddressTypeCode> getAddressTypeCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.NEIGHBORHOOD_LEARNING_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all neighborhood learning type codes.", description = "Endpoint to get all neighborhood learning type codes.")
  @Schema(name = "NeighborhoodLearningTypeCode", implementation = NeighborhoodLearningTypeCode.class)
  List<NeighborhoodLearningTypeCode> getNeighborhoodLearningCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.FACILITY_TYPE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all school facility type codes.", description = "Endpoint to get all school facility type codes.")
  @Schema(name = "FacilityTypeCode", implementation = FacilityTypeCode.class)
  List<FacilityTypeCode> getFacilityTypeCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.CATEGORY_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all school category codes.", description = "Endpoint to get all school category codes.")
  @Schema(name = "SchoolCategoryCode", implementation = SchoolCategoryCode.class)
  List<SchoolCategoryCode> getSchoolCategoryCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.ORGANIZATION_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all school organization codes.", description = "Endpoint to get all school organization codes.")
  @Schema(name = "SchoolOrganizationCode", implementation = SchoolOrganizationCode.class)
  List<SchoolOrganizationCode> getSchoolOrganizationCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.GRADE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to get all school grade codes.", description = "Endpoint to get all school grade codes.")
  @Schema(name = "SchoolGradeCode", implementation = SchoolGradeCode.class)
  List<SchoolGradeCode> getSchoolGradeCodes();
}
