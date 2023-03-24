package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.mapper.v1.CodeTableMapper;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.*;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = InstituteApiResourceApplication.class)
@AutoConfigureMockMvc
public class CodeTableControllerTest {

  private static final CodeTableMapper mapper = CodeTableMapper.mapper;
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  CodeTableAPIController controller;

  @Autowired
  AddressTypeCodeRepository addressTypeCodeRepository;

  @Autowired
  AuthorityTypeCodeRepository authorityTypeCodeRepository;

  @Autowired
  DistrictContactTypeCodeRepository districtContactTypeCodeRepository;

  @Autowired
  SchoolContactTypeCodeRepository schoolContactTypeCodeRepository;

  @Autowired
  AuthorityContactTypeCodeRepository authorityContactTypeCodeRepository;

  @Autowired
  DistrictRegionCodeRepository districtRegionCodeRepository;

  @Autowired
  DistrictStatusCodeRepository districtStatusCodeRepository;

  @Autowired
  FacilityTypeCodeRepository facilityTypeCodeRepository;

  @Autowired
  NeighborhoodLearningTypeCodeRepository neighborhoodLearningTypeCodeRepository;

  @Autowired
  ProvinceCodeRepository provinceCodeRepository;

  @Autowired
  CountryCodeRepository countryCodeRepository;

  @Autowired
  SchoolGradeCodeRepository schoolGradeCodeRepository;

  @Autowired
  SchoolOrganizationCodeRepository schoolOrganizationCodeRepository;

  @Autowired
  SchoolCategoryCodeRepository schoolCategoryCodeRepository;

  @Autowired
  SchoolReportingRequirementCodeRepository schoolReportingRequirementCodeRepository;

  @Autowired
  CodeTableService codeTableService;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.addressTypeCodeRepository.save(this.createAddressTypeCodeData());
    this.authorityTypeCodeRepository.save(this.createAuthorityTypeCodeData());
    this.districtContactTypeCodeRepository.save(this.createDistrictContactTypeCodeData());
    this.schoolContactTypeCodeRepository.save(this.createSchoolContactTypeCodeData());
    this.authorityContactTypeCodeRepository.save(this.createAuthorityContactTypeCodeData());
    this.districtRegionCodeRepository.save(this.createDistrictRegionCodeData());
    this.districtStatusCodeRepository.save(this.createDistrictStatusCodeData());
    this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData());
    this.neighborhoodLearningTypeCodeRepository.save(this.createNeighborhoodLearningTypeCodeData());
    this.provinceCodeRepository.save(this.createProvinceCodeData());
    this.countryCodeRepository.save(this.createCountryCodeData());
    this.schoolGradeCodeRepository.save(this.createSchoolGradeCodeData());
    this.schoolOrganizationCodeRepository.save(this.createSchoolOrganizationCodeData());
    this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData());
    this.schoolReportingRequirementCodeRepository
      .save(this.createSchoolReportingRequirementCodeData());
  }

  /**
   * need to delete the records to make it working in unit tests assertion, else the records will keep growing and assertions will fail.
   */
  @After
  public void after() {
    this.addressTypeCodeRepository.deleteAll();
    this.authorityTypeCodeRepository.deleteAll();
    this.districtContactTypeCodeRepository.deleteAll();
    this.schoolContactTypeCodeRepository.deleteAll();
    this.authorityContactTypeCodeRepository.deleteAll();
    this.districtRegionCodeRepository.deleteAll();
    this.districtStatusCodeRepository.deleteAll();
    this.facilityTypeCodeRepository.deleteAll();
    this.neighborhoodLearningTypeCodeRepository.deleteAll();
    this.provinceCodeRepository.deleteAll();
    this.countryCodeRepository.deleteAll();
    this.schoolGradeCodeRepository.deleteAll();
    this.schoolOrganizationCodeRepository.deleteAll();
    this.schoolCategoryCodeRepository.deleteAll();
  }

  private AddressTypeCodeEntity createAddressTypeCodeData() {
    return AddressTypeCodeEntity.builder().addressTypeCode("MAILING").description("Mailing Address")
        .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Mailing Address").createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private AuthorityTypeCodeEntity createAuthorityTypeCodeData() {
    return AuthorityTypeCodeEntity.builder().authorityTypeCode("INDEPEND").description("Independent School")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Independent School").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private DistrictContactTypeCodeEntity createDistrictContactTypeCodeData() {
    return DistrictContactTypeCodeEntity.builder().districtContactTypeCode("PRINCIPAL").description("School Principal")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Principal").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolContactTypeCodeEntity createSchoolContactTypeCodeData() {
    return SchoolContactTypeCodeEntity.builder().schoolContactTypeCode("PRINCIPAL").description("School Principal")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Principal").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private AuthorityContactTypeCodeEntity createAuthorityContactTypeCodeData() {
    return AuthorityContactTypeCodeEntity.builder().authorityContactTypeCode("PRINCIPAL").description("School Principal")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Principal").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private DistrictRegionCodeEntity createDistrictRegionCodeData() {
    return DistrictRegionCodeEntity.builder().districtRegionCode("KOOTENAYS").description("Kootenays region")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Kootenays").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private DistrictStatusCodeEntity createDistrictStatusCodeData() {
    return DistrictStatusCodeEntity.builder().districtStatusCode("OPEN").description("Open District")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Open").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private FacilityTypeCodeEntity createFacilityTypeCodeData() {
    return FacilityTypeCodeEntity.builder().facilityTypeCode("STAND_SCHL").description("Standard School")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Standard School").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private ProvinceCodeEntity createProvinceCodeData() {
    return ProvinceCodeEntity.builder().provinceCode("BC").description("British Columbia")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("British Columbia").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private CountryCodeEntity createCountryCodeData() {
    return CountryCodeEntity.builder().countryCode("CA").description("Canada")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Canada").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private NeighborhoodLearningTypeCodeEntity createNeighborhoodLearningTypeCodeData() {
    return NeighborhoodLearningTypeCodeEntity.builder().neighborhoodLearningTypeCode("COMM_USE").description("Community Use")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Community Use").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolGradeCodeEntity createSchoolGradeCodeData() {
    return SchoolGradeCodeEntity.builder().schoolGradeCode("01").description("First Grade")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("First").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolReportingRequirementCodeEntity createSchoolReportingRequirementCodeData() {
    return SchoolReportingRequirementCodeEntity.builder()
      .schoolReportingRequirementCode("REGULAR")
      .description("The school submits a standard 1701 file")
      .effectiveDate(LocalDateTime.now())
      .expiryDate(LocalDateTime.MAX)
      .displayOrder(1)
      .label("Regular")
      .createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now())
      .createUser("TEST")
      .updateUser("TEST")
      .build();
  }

  private SchoolOrganizationCodeEntity createSchoolOrganizationCodeData() {
    return SchoolOrganizationCodeEntity.builder().schoolOrganizationCode("TWO_SEM").description("Two Semesters")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Two Semesters").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolCategoryCodeEntity createSchoolCategoryCodeData() {
    return SchoolCategoryCodeEntity.builder().schoolCategoryCode("PUB_SCHL").description("Public School")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Public School").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  @Test
  public void testGetAddressTypeCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.ADDRESS_TYPE_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].addressTypeCode").value("MAILING"));
  }

  @Test
  public void testGetSchoolCategoryCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.CATEGORY_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].schoolCategoryCode").value("PUB_SCHL"));
  }

  @Test
  public void testGetSchoolOrganizationCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.ORGANIZATION_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].schoolOrganizationCode").value("TWO_SEM"));
  }

  @Test
  public void testGetSchoolGradeCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.GRADE_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].schoolGradeCode").value("01"));
  }

  @Test
  public void testGetNeighborhoodLearningTypeCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.NEIGHBORHOOD_LEARNING_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].neighborhoodLearningTypeCode").value("COMM_USE"));
  }

  @Test
  public void testGetProvinceCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.PROVINCE_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].provinceCode").value("BC"));
  }

  @Test
  public void testGetCountryCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.COUNTRY_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].countryCode").value("CA"));
  }

  @Test
  public void testGetFacilityTypeCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.FACILITY_TYPE_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].facilityTypeCode").value("STAND_SCHL"));
  }

  @Test
  public void testGetDistrictRegionCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.DISTRICT_REGION_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].districtRegionCode").value("KOOTENAYS"));
  }

  @Test
  public void testGetDistrictStatusCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.DISTRICT_STATUS_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].districtStatusCode").value("OPEN"));
  }

  @Test
  public void testGetDistrictContactTypeCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.DISTRICT_CONTACT_TYPE_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].districtContactTypeCode").value("PRINCIPAL"));
  }

  @Test
  public void testGetSchoolContactTypeCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.SCHOOL_CONTACT_TYPE_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].schoolContactTypeCode").value("PRINCIPAL"));
  }

  @Test
  public void testGetAuthorityContactTypeCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.AUTHORITY_CONTACT_TYPE_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorityContactTypeCode").value("PRINCIPAL"));
  }

  @Test
  public void testGetAuthorityTypeCodes_ShouldReturnCodes() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_INSTITUTE_CODES";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL + URL.AUTHORITY_TYPE_CODES).with(mockAuthority)).andDo(print()).andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$[0].authorityTypeCode").value("INDEPEND"));
  }
}


