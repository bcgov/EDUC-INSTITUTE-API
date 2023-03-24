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
@OpenAPIDefinition(info = @Info(title = "API to Institute CRUD.", description = "This API is related to district, independent authority and school data.", version = "1"),
  security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_INSTITUTE_CODES", "READ_DISTRICT", "WRITE_DISTRICT", "DELETE_DISTRICT",
    "READ_DISTRICT_CONTACT", "WRITE_DISTRICT_CONTACT", "DELETE_DISTRICT_CONTACT",
    "READ_DISTRICT_ADDRESS", "WRITE_DISTRICT_ADDRESS", "DELETE_DISTRICT_ADDRESS",
    "READ_DISTRICT_NOTE", "WRITE_DISTRICT_NOTE", "DELETE_DISTRICT_NOTE", "READ_INDEPENDENT_AUTHORITY", "WRITE_INDEPENDENT_AUTHORITY", "DELETE_INDEPENDENT_AUTHORITY",
    "READ_INDEPENDENT_AUTHORITY_CONTACT", "WRITE_INDEPENDENT_AUTHORITY_CONTACT", "DELETE_INDEPENDENT_AUTHORITY_CONTACT",
    "READ_INDEPENDENT_AUTHORITY_ADDRESS", "WRITE_INDEPENDENT_AUTHORITY_ADDRESS", "DELETE_INDEPENDENT_AUTHORITY_ADDRESS",
    "READ_INDEPENDENT_AUTHORITY_NOTE", "WRITE_INDEPENDENT_AUTHORITY_NOTE", "DELETE_INDEPENDENT_AUTHORITY_NOTE", "READ_SCHOOL", "WRITE_SCHOOL", "DELETE_SCHOOL",
    "READ_SCHOOL_CONTACT", "WRITE_SCHOOL_CONTACT", "DELETE_SCHOOL_CONTACT",
    "READ_SCHOOL_ADDRESS", "WRITE_SCHOOL_ADDRESS", "DELETE_SCHOOL_ADDRESS",
    "READ_SCHOOL_NOTE", "WRITE_SCHOOL_NOTE", "DELETE_SCHOOL_NOTE"})})
public interface CodeTableAPIEndpoint {

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.PROVINCE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "ProvinceCode", implementation = ProvinceCode.class)
  List<ProvinceCode> getProvinceCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.COUNTRY_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "CountryCode", implementation = CountryCode.class)
  List<CountryCode> getCountryCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.DISTRICT_REGION_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "DistrictRegionCode", implementation = DistrictRegionCode.class)
  List<DistrictRegionCode> getDistrictRegionCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.DISTRICT_STATUS_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "DistrictStatusCode", implementation = DistrictStatusCode.class)
  List<DistrictStatusCode> getDistrictStatusCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.DISTRICT_CONTACT_TYPE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "DistrictContactTypeCode", implementation = DistrictContactTypeCode.class)
  List<DistrictContactTypeCode> getDistrictContactTypeCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.SCHOOL_CONTACT_TYPE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "SchoolContactTypeCode", implementation = SchoolContactTypeCode.class)
  List<SchoolContactTypeCode> getSchoolContactTypeCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.AUTHORITY_CONTACT_TYPE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "AuthorityContactTypeCode", implementation = AuthorityContactTypeCode.class)
  List<AuthorityContactTypeCode> getAuthorityContactTypeCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.AUTHORITY_TYPE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "AuthorityTypeCode", implementation = AuthorityTypeCode.class)
  List<AuthorityTypeCode> getAuthorityTypeCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.ADDRESS_TYPE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "AddressTypeCode", implementation = AddressTypeCode.class)
  List<AddressTypeCode> getAddressTypeCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.NEIGHBORHOOD_LEARNING_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "NeighborhoodLearningTypeCode", implementation = NeighborhoodLearningTypeCode.class)
  List<NeighborhoodLearningTypeCode> getNeighborhoodLearningCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.FACILITY_TYPE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "FacilityTypeCode", implementation = FacilityTypeCode.class)
  List<FacilityTypeCode> getFacilityTypeCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.CATEGORY_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "SchoolCategoryCode", implementation = SchoolCategoryCode.class)
  List<SchoolCategoryCode> getSchoolCategoryCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.ORGANIZATION_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "SchoolOrganizationCode", implementation = SchoolOrganizationCode.class)
  List<SchoolOrganizationCode> getSchoolOrganizationCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.REPORTING_REQUIREMENT_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(
    name = "Reporting Requirement Codes",
    description = "Endpoints to get school reporting requirement codes"
  )
  @Schema(
    name = "SchoolReportingRequirementCode",
    implementation = SchoolReportingRequirementCode.class
  )
  List<SchoolReportingRequirementCode> getSchoolReportingRequirementCodes();

  @PreAuthorize("hasAuthority('SCOPE_READ_INSTITUTE_CODES')")
  @GetMapping(URL.GRADE_CODES)
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  @Transactional(readOnly = true)
  @Tag(name = "Institute Codes", description = "Endpoints to get institute codes.")
  @Schema(name = "SchoolGradeCode", implementation = SchoolGradeCode.class)
  List<SchoolGradeCode> getSchoolGradeCodes();
}
