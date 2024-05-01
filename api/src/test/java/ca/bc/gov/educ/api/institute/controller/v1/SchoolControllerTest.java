package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.filter.FilterOperation;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.*;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.service.v1.SchoolNumberGenerationService;
import ca.bc.gov.educ.api.institute.struct.v1.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { InstituteApiResourceApplication.class })
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SchoolControllerTest {
  protected final static ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  CodeTableAPIController controller;

  @Autowired
  DistrictTombstoneRepository districtTombstoneRepository;

  @Autowired
  SchoolRepository schoolRepository;

  @Autowired
  SchoolHistoryRepository schoolHistoryRepository;

  @Autowired
  CodeTableService codeTableService;

  @Autowired
  SchoolCategoryCodeRepository schoolCategoryCodeRepository;

  @Autowired
  SchoolOrganizationCodeRepository schoolOrganizationCodeRepository;

  @Autowired
  SchoolReportingRequirementCodeRepository schoolReportingRequirementCodeRepository;

  @Autowired
  FacilityTypeCodeRepository facilityTypeCodeRepository;

  @Autowired
  SchoolContactTypeCodeRepository schoolContactTypeCodeRepository;

  @Autowired
  SchoolContactRepository schoolContactRepository;

  @Autowired
  SchoolAddressRepository addressRepository;

  @Autowired
  NoteRepository noteRepository;

  @Autowired
  AddressTypeCodeRepository addressTypeCodeRepository;

  @Autowired
  ProvinceCodeRepository provinceCodeRepository;

  @Autowired
  CountryCodeRepository countryCodeRepository;

  @Autowired
  SchoolGradeCodeRepository schoolGradeCodeRepository;

  @Autowired
  SchoolNumberGenerationService schoolNumberGenerationService;

  @Autowired
  NeighborhoodLearningTypeCodeRepository neighborhoodLearningTypeCodeRepository;

  @BeforeEach
  public void before(){
    MockitoAnnotations.openMocks(this);
    this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData());
    this.schoolOrganizationCodeRepository.save(this.createSchoolOrganizationCodeData());
    this.schoolReportingRequirementCodeRepository
      .save(this.createSchoolReportingRequirementCodeData());
    this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData());
    this.schoolContactTypeCodeRepository.save(this.createContactTypeCodeData());
    this.addressTypeCodeRepository.save(this.createAddressTypeCodeData());
    this.provinceCodeRepository.save(this.createProvinceCodeData());
    this.countryCodeRepository.save(this.createCountryCodeData());
    this.schoolGradeCodeRepository.save(this.createSchoolGradeCodeData());
    this.neighborhoodLearningTypeCodeRepository.save(this.createNeighborhoodLearningTypeCodeData());
  }

  /**
   * need to delete the records to make it working in unit tests assertion, else the records will keep growing and assertions will fail.
   */
  @AfterEach
  public void after() {
    this.addressRepository.deleteAll();
    this.schoolContactRepository.deleteAll();
    this.noteRepository.deleteAll();
    this.schoolRepository.deleteAll();
    this.schoolHistoryRepository.deleteAll();
    this.schoolGradeCodeRepository.deleteAll();
    this.neighborhoodLearningTypeCodeRepository.deleteAll();
    this.schoolCategoryCodeRepository.deleteAll();
    this.schoolOrganizationCodeRepository.deleteAll();
    this.schoolReportingRequirementCodeRepository.deleteAll();
    this.facilityTypeCodeRepository.deleteAll();
  }

  @Test
  void testAllSchools_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].schoolId")
        .value(entity.getSchoolId().toString()));
  }

  @Test
  void testAllSchoolsCheckDistrict_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].schoolId")
        .value(entity.getSchoolId().toString())).andExpect(MockMvcResultMatchers.jsonPath("$.[0].mincode")
        .value(entity.getDistrictEntity().getDistrictNumber() + entity.getSchoolNumber()));
  }

  @Test
  void testRetrieveSchool_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.schoolId")
        .value(entity.getSchoolId().toString()));
  }

  @Test
  void testRetrieveSchoolWithAddress_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);

    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.schoolId")
        .value(entity.getSchoolId().toString()));
  }

    @Test
  void testRetrieveSchoolWithDisplayNameNoSpecChars_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    schoolEntity.setDisplayNameNoSpecialChars("NOSPECCHAR");
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);

    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.displayNameNoSpecialChars")
        .value(entity.getDisplayNameNoSpecialChars()));
  }

  @Test
  void testRetrieveSchool_GivenInvalidID_ShouldReturnStatusNotFound() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + UUID.randomUUID()).with(mockAuthority))
      .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void testRetrieveSchoolHistory_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    final SchoolHistoryEntity historyEntity = this.schoolHistoryRepository.save(this.createHistorySchoolData(entity.getSchoolId()));
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId() + "/history").with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].schoolHistoryId")
        .value(historyEntity.getSchoolHistoryId().toString()));
  }

  @Test
  void testRetrieveSchoolHistory_GivenInvalidID_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/abc/history").with(mockAuthority))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void testRetrieveSchool_GivenInvalidID_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/abc").with(mockAuthority))
      .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void testDeleteSchool_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var school = this.createSchoolData();
    var entity = this.schoolRepository.save(school);

    this.mockMvc.perform(delete(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedEntity = this.schoolRepository.findById(entity.getSchoolId());
    Assertions.assertTrue(deletedEntity.isEmpty());
  }

  @Test
  void testUpdateSchool_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    entity.setPhoneNumber("");
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);

    var schoolStruct = SchoolMapper.mapper.toStructure(entity);
    schoolStruct.setDistrictId(dist.getDistrictId().toString());

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(schoolStruct))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(entity.getDisplayName()));
  }

  @Test
  void testUpdateSchool_GivenValidPayloadWithNoSpecCharDisplayName_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    entity.setPhoneNumber("");
    entity.setDisplayName("newdist");
    entity.setDisplayNameNoSpecialChars("nochars");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);

    var schoolStruct = SchoolMapper.mapper.toStructure(entity);
    schoolStruct.setDistrictId(dist.getDistrictId().toString());

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(schoolStruct))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayNameNoSpecialChars").value(entity.getDisplayNameNoSpecialChars()));
  }

  @Test
  void testUpdateSchool_GivenInvalidURLSchoolId_ShouldReturnNotFound() throws Exception {
    final var school = this.createSchoolData();
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);

    var schoolStruct = SchoolMapper.mapper.toStructure(entity);
    schoolStruct.setDistrictId(dist.getDistrictId().toString());

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(schoolStruct))
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void testUpdateSchool_GivenInvalidPayload_ShouldReturnNotFound() throws Exception {
    UUID badSchoolId = UUID.randomUUID();
    final var school = this.createSchoolData();
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);

    var schoolStruct = SchoolMapper.mapper.toStructure(entity);
    schoolStruct.setSchoolId(badSchoolId.toString());
    schoolStruct.setDistrictId(dist.getDistrictId().toString());

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + badSchoolId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(schoolStruct))
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
        .andDo(print())
        .andExpect(status().isNotFound());
  }
  @Test
  void testUpdateSchoolWithAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    entity.getAddresses().add(this.createSchoolAddressData());
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);

    entity.getAddresses().stream().forEach(addy -> {
        addy.setCreateDate(null);
        addy.setUpdateDate(null);
      }
    );

    var schoolStruct = SchoolMapper.mapper.toStructure(entity);
    schoolStruct.setDistrictId(dist.getDistrictId().toString());

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(schoolStruct))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(entity.getDisplayName()));

    entity = this.schoolRepository.findById(entity.getSchoolId()).get();
    var addr = this.createSchoolAddressData();
    addr.setAddressLine1("123 TESTING");
    entity.getAddresses().iterator().next().setAddressLine1("123 TESTING");
    entity.getAddresses().add(addr);

    SchoolMapper mapper = SchoolMapper.mapper;

    var auth = mapper.toStructure(entity);

    auth.setCreateDate(null);
    auth.setUpdateDate(null);

    auth.getAddresses().stream().forEach(addy -> {
        addy.setCreateDate(null);
        addy.setUpdateDate(null);
      }
    );

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(auth))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.addresses.[1].addressLine1").value("123 TESTING"));
  }

  @Test
  void testAddSchoolGrade_GivenValidPayload_ShouldReturnStatusOk() throws Exception {
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);
    entity.getGrades().add(createSchoolGradeData(entity));

    SchoolMapper map = SchoolMapper.mapper;

    School mappedSchool = map.toStructure(entity);

    mappedSchool.setCreateDate(null);
    mappedSchool.setUpdateDate(null);

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(mappedSchool))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.grades[0].schoolGradeCode").value("01"));
  }

  @Test
  void testAddSchoolNeighborhoodLearning_GivenValidPayload_ShouldReturnStatusOk() throws Exception {
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);
    entity.getNeighborhoodLearning().add(createNeighborhoodLearningData(entity));

    var school = SchoolMapper.mapper.toStructure(entity);
    school.setDistrictId(dist.getDistrictId().toString());

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(school))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.neighborhoodLearning[0].neighborhoodLearningTypeCode").value("COMM_USE"));
  }

  @Test
  void testUpdateSchoolNeighborhoodLearning_GivenValidPayload_ShouldReturnStatusOk() throws Exception {
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);
    entity.getNeighborhoodLearning().add(createNeighborhoodLearningData(entity));

    var school = SchoolMapper.mapper.toStructure(entity);
    school.setDistrictId(dist.getDistrictId().toString());

    var resultActions = this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(school))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.neighborhoodLearning[0].neighborhoodLearningTypeCode").value("COMM_USE"));

    val schoolReturn = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), new TypeReference<School>() {
    });

    schoolReturn.setCreateDate(null);
    schoolReturn.setUpdateDate(null);
    schoolReturn.getNeighborhoodLearning().get(0).setUpdateDate(null);
    schoolReturn.getNeighborhoodLearning().get(0).setCreateDate(null);

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(schoolReturn))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.neighborhoodLearning[0].neighborhoodLearningId").value(schoolReturn.getNeighborhoodLearning().get(0).getNeighborhoodLearningId()));
  }

  @Test
  void testCreateSchool_GivenValidPayload_ShouldReturnStatusOK() throws Exception {
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());

    var schoolEntity = this.createNewSchoolData(null, "PUBLIC", "DISTONLINE");
    schoolEntity.setDisplayNameNoSpecialChars("NOSPECCHAR");
    SchoolMapper map = SchoolMapper.mapper;

    School mappedSchool = map.toStructure(schoolEntity);

    mappedSchool.setDistrictId(dist.getDistrictId().toString());
    mappedSchool.setCreateDate(null);
    mappedSchool.setUpdateDate(null);
    mappedSchool.setSchoolNumber(null);
    mappedSchool.setGrades(List.of(createSchoolGrade()));
    mappedSchool.setNeighborhoodLearning(List.of(createNeighborhoodLearning()));
    mappedSchool.setAddresses(List.of(createSchoolAddress()));
    mappedSchool.setCanIssueCertificates(true);
    mappedSchool.setCanIssueTranscripts(true);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(mappedSchool))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.schoolNumber").exists())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(mappedSchool.getDisplayName()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.canIssueCertificates").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.canIssueTranscripts").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayNameNoSpecialChars").value(mappedSchool.getDisplayNameNoSpecialChars()));
  }

  @Test
  void testCreateSchool_GivenInvalidPayload_ShouldReturnStatusBadRequest() throws Exception {
    final var school = this.createSchoolData();
    school.setSchoolCategoryCode("ABCD");
    school.setPhoneNumber("123");
    school.setFaxNumber("noletterss");
    school.setCreateDate(null);
    school.setUpdateDate(null);
    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(school))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }
  @Test
  void testCreateSchoolContact_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    SchoolContactEntity contactEntity = createContactData(schoolEntity);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(contactEntity.getLastName()));
  }

  @Test
  void testCreateSchoolContact_GivenValidPayloadNoFirstName_ShouldReturnStatusCreated() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    SchoolContactEntity contactEntity = createContactData(schoolEntity);
    contactEntity.setFirstName(null);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(contactEntity.getLastName()));
  }


  @Test
  void testCreateSchoolContactExtFields_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    SchoolContactEntity contactEntity = createContactData(schoolEntity);
    contactEntity.setPhoneNumber("9876541234");
    contactEntity.setPhoneExtension("321");
    contactEntity.setAlternatePhoneNumber("1234567891");
    contactEntity.setAlternatePhoneExtension("123");
    contactEntity.setJobTitle("Painter");

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(contactEntity.getLastName()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.jobTitle").value(contactEntity.getJobTitle()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(contactEntity.getPhoneNumber()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phoneExtension").value(contactEntity.getPhoneExtension()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.alternatePhoneNumber").value(contactEntity.getAlternatePhoneNumber()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.alternatePhoneExtension").value(contactEntity.getAlternatePhoneExtension()));
  }


  @Test
  void testCreateSchoolContact_GivenInvalidPayload_ShouldReturnStatusCreated() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    SchoolContactEntity contactEntity = createContactData(schoolEntity);
    contactEntity.setSchoolContactId(UUID.randomUUID());

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void testCreateSchoolContact_GivenInvalidTypeCodePayload_ShouldReturnStatusCreated() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    SchoolContactEntity contactEntity = createContactData(schoolEntity);
    contactEntity.setSchoolContactTypeCode("TESTER");

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
      .andDo(print())
      .andExpect(status().isBadRequest());
  }

  @Test
  void testCreateSchoolContact_GivenInvalidSchoolIdURL_ShouldReturnNotFound() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    SchoolContactEntity contactEntity = createContactData(schoolEntity);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + UUID.randomUUID() + "/contact")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(contactEntity))
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteSchoolContact_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    SchoolContactEntity contactEntity = createContactData(schoolEntity);
    var contact = this.schoolContactRepository.save(contactEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact/" + contact.getSchoolContactId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(schoolEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_SCHOOL_CONTACT"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedContact = this.schoolContactRepository.findById(contact.getSchoolContactId());
    Assertions.assertTrue(deletedContact.isEmpty());
  }

  @Test
  void testRetrieveSchoolContact_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_CONTACT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    SchoolContactEntity contactEntity = createContactData(schoolEntity);
    var contact = this.schoolContactRepository.save(contactEntity);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact/" + contact.getSchoolContactId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.schoolContactId")
        .value(contact.getSchoolContactId().toString()));
  }

  @Test
  void testUpdateSchoolContact_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    SchoolContactEntity contactEntity = createContactData(schoolEntity);
    var contact = this.schoolContactRepository.save(contactEntity);
    contact.setFirstName("pete");

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact/" + contact.getSchoolContactId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contact))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(contact.getFirstName()));
  }

  @Test
  void testUpdateSchoolContact_GivenInvalidSchoolIdURL_ShouldReturnNotFound() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    SchoolContactEntity contactEntity = createContactData(schoolEntity);
    var contact = this.schoolContactRepository.save(contactEntity);
    contact.setFirstName("pete");

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + UUID.randomUUID() + "/contact/" + contact.getSchoolContactId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(contact))
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void testUpdateSchoolContact_GivenInvalidContactIdURL_ShouldReturnNotFound() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    SchoolContactEntity contactEntity = createContactData(schoolEntity);
    var contact = this.schoolContactRepository.save(contactEntity);
    contact.setFirstName("pete");

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact/" + UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(contact))
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
        .andDo(print())
        .andExpect(status().isNotFound());
  }
  @ParameterizedTest
  @CsvSource(value = {"SCOPE_READ_SCHOOL_CONTACT:contact", "SCOPE_READ_SCHOOL_ADDRESS:address", "SCOPE_READ_SCHOOL_NOTE:note"}, delimiter = ':')
  void testRetrieveDistrictInstitute_GivenInvalidID_ShouldReturnStatusNotFound(String scope, String path) throws Exception {
    final GrantedAuthority grantedAuthority = () -> scope;
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);

    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/" + path + "/" + UUID.randomUUID()).with(mockAuthority))
      .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void testCreateSchoolNote_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    NoteEntity noteEntity = createNoteData(schoolEntity);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/note")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(noteEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_NOTE"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(noteEntity.getContent()));
  }

  @Test
  void testCreateSchoolNote_GivenInvalidSchoolIdURL_ShouldReturnNotFound() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    NoteEntity noteEntity = createNoteData(schoolEntity);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + UUID.randomUUID() + "/note")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(noteEntity))
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_NOTE"))))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteSchoolNote_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    NoteEntity noteEntity = createNoteData(schoolEntity);
    var note = this.noteRepository.save(noteEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/note/" + note.getNoteId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(schoolEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_SCHOOL_NOTE"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedNote = this.noteRepository.findById(note.getNoteId());
    Assertions.assertTrue(deletedNote.isEmpty());
  }

  @Test
  void testRetrieveSchoolNote_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_NOTE";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    NoteEntity noteEntity = createNoteData(schoolEntity);
    var note = this.noteRepository.save(noteEntity);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/note/" + note.getNoteId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.noteId")
        .value(note.getNoteId().toString()));
  }

  @Test
  void testRetrieveSchoolNotes_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_NOTE";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    NoteEntity noteEntity = createNoteData(schoolEntity);
    var note = this.noteRepository.save(noteEntity);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/note").with(mockAuthority))
            .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].noteId")
                    .value(note.getNoteId().toString()));
  }

  @Test
  void testUpdateSchoolNote_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    NoteEntity noteEntity = createNoteData(schoolEntity);
    var note = this.noteRepository.save(noteEntity);
    note.setContent("southshore");

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/note/" + note.getNoteId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(note))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_NOTE"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(note.getContent()));
  }

  @Test
  void testReadSchoolPaginated_givenValueNull_ShouldReturnStatusOk() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);

    this.schoolRepository.save(createSchoolData());
    val entitiesFromDB = this.schoolRepository.findAll();
    final SearchCriteria criteria = SearchCriteria.builder().key("website").operation(FilterOperation.EQUAL).value(null).valueType(ValueType.STRING).build();
    final List<SearchCriteria> criteriaList = new ArrayList<>();
    criteriaList.add(criteria);
    final List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    final ObjectMapper objectMapper = new ObjectMapper();
    final String criteriaJSON = objectMapper.writeValueAsString(searches);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/paginated").with(mockAuthority).param("searchCriteriaList", criteriaJSON)
      .contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
  }

  @Test
  void testReadStudentPaginated_GivenSchoolNameFilter_ShouldReturnStatusOk() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);

    final ObjectMapper objectMapper = new ObjectMapper();
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());

    var schoolData = createSchoolData();
    schoolData.setDistrictEntity(dist);
    this.schoolRepository.save(schoolData);
    final SearchCriteria criteria = SearchCriteria.builder().key("displayName").operation(FilterOperation.EQUAL).value(schoolData.getDisplayName()).valueType(ValueType.STRING).build();
    final List<SearchCriteria> criteriaList = new ArrayList<>();
    criteriaList.add(criteria);
    final List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    final String criteriaJSON = objectMapper.writeValueAsString(searches);

    final MvcResult result = this.mockMvc
      .perform(get(URL.BASE_URL_SCHOOL + "/paginated").with(mockAuthority).param("searchCriteriaList", criteriaJSON)
        .contentType(APPLICATION_JSON))
      .andReturn();
    this.mockMvc.perform(asyncDispatch(result)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  void testCreateSchoolWithGradeAndNeighbourhoodLearning_GivenValidData_SchoolHistoryShouldBeCreatedWithGradeCodeAndNeighbourhoodLearningDataAndShouldReturnStatusOK() throws Exception {
    var schoolEntity = this.createSchoolData();
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    val resultActions = this.mockMvc
      .perform(get(URL.BASE_URL_SCHOOL).with(mockAuthority))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers
          .jsonPath("$.[0].schoolId")
          .value(entity.getSchoolId().toString()));

    val schools = objectMapper.readValue(
      resultActions.andReturn().getResponse().getContentAsByteArray(),
      new TypeReference<List<School>>() {}
    );

    val school = schools.get(0);
    school.setCreateDate(null);
    school.setUpdateDate(null);
    school.setGrades(List.of(createSchoolGrade(school.getSchoolId())));
    school.setNeighborhoodLearning(List.of(createNeighborhoodLearning(school.getSchoolId())));

    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    school.setDistrictId(dist.getDistrictId().toString());
    school.setUpdateDate(null);
    school.setCreateDate(null);
    school.setDisplayNameNoSpecialChars("NOSPECCHAR");

    String updatedSchool = asJsonString(school);
    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(updatedSchool)
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(entity.getDisplayName()));

    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId() + "/history")
        .with(mockAuthority))
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("$.[0].schoolId").value(entity.getSchoolId().toString()))
      .andExpect(jsonPath("$.[0].displayNameNoSpecialChars").value(school.getDisplayNameNoSpecialChars()))
      .andExpect(jsonPath("$.[0].schoolGrades.[0]", is(notNullValue())))
      .andExpect(jsonPath("$.[0].neighbourhoodLearnings.[0]", is(notNullValue())));

  }

  @Test
  void testReadSchoolHistoryPaginated_givenValueNull_ShouldReturnStatusOk() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_HISTORY";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);

    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    this.schoolHistoryRepository.save(createHistorySchoolData(entity.getSchoolId()));
    final SearchCriteria criteria = SearchCriteria.builder().key("website").operation(FilterOperation.EQUAL).value(null).valueType(ValueType.STRING).build();
    final List<SearchCriteria> criteriaList = new ArrayList<>();
    criteriaList.add(criteria);
    final List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    final ObjectMapper objectMapper = new ObjectMapper();
    final String criteriaJSON = objectMapper.writeValueAsString(searches);
    final MvcResult result = this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/history/paginated").with(mockAuthority).param("searchCriteriaList", criteriaJSON)
        .contentType(APPLICATION_JSON)).andReturn();
    this.mockMvc.perform(asyncDispatch(result)).andDo(print()).andExpect(status().isOk());
  }

  @Test
  void testReadSchoolHistoryPaginated_givenWrongRole_ShouldReturnStatusForbidden() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "UNAUTHORIZED";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);

    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    this.schoolHistoryRepository.save(createHistorySchoolData(entity.getSchoolId()));
    final SearchCriteria criteria = SearchCriteria.builder().key("website").operation(FilterOperation.EQUAL).value(null).valueType(ValueType.STRING).build();
    final List<SearchCriteria> criteriaList = new ArrayList<>();
    criteriaList.add(criteria);
    final List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    final ObjectMapper objectMapper = new ObjectMapper();
    final String criteriaJSON = objectMapper.writeValueAsString(searches);
    final ResultActions result =  this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/history/paginated").with(mockAuthority).param("searchCriteriaList", criteriaJSON)
        .contentType(APPLICATION_JSON)).andDo(print());
    result.andExpect(status().isForbidden());
  }

  @Test
  void testReadSchoolHistoryPaginated_givenMultipleSearchCriteriaIncludingLowerCase_ShouldReturnStatusOk() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_HISTORY";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);

    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    final SchoolHistoryEntity historyEntity = this.schoolHistoryRepository.save(createHistorySchoolData(entity.getSchoolId()));
    final SearchCriteria criteriaSchoolNumber = SearchCriteria.builder().key("schoolNumber").operation(FilterOperation.EQUAL).value(historyEntity.getSchoolNumber()).valueType(ValueType.STRING).build();
    final SearchCriteria criteriaSchoolUUID = SearchCriteria.builder().key("schoolId").operation(FilterOperation.EQUAL).value(historyEntity.getSchoolId().toString()).valueType(ValueType.UUID).build();
    final SearchCriteria criteriaCreateDate = SearchCriteria.builder().key("createDate").operation(FilterOperation.LESS_THAN).value("2999-01-01T00:00:00").valueType(ValueType.DATE_TIME).condition(Condition.AND).build();
    final SearchCriteria criteriaSchoolOrganizationCode = SearchCriteria.builder().key("schoolOrganizationCode").operation(FilterOperation.EQUAL).value(historyEntity.getSchoolOrganizationCode().toLowerCase()).valueType(ValueType.STRING).condition(Condition.OR).build();
    final SearchCriteria criteriaSchoolPhoneNumber = SearchCriteria.builder().key("phoneNumber").operation(FilterOperation.EQUAL).value(historyEntity.getPhoneNumber()).valueType(ValueType.STRING).build();
    final List<SearchCriteria> criteriaList = new ArrayList<>();
    criteriaList.add(criteriaSchoolNumber);
    criteriaList.add(criteriaSchoolUUID);
    criteriaList.add(criteriaCreateDate);
    criteriaList.add(criteriaSchoolOrganizationCode);
    criteriaList.add(criteriaSchoolPhoneNumber);
    final List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    final ObjectMapper objectMapper = new ObjectMapper();
    final String criteriaJSON = objectMapper.writeValueAsString(searches);
    final MvcResult result = this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/history/paginated").with(mockAuthority).param("searchCriteriaList", criteriaJSON)
        .contentType(APPLICATION_JSON)).andReturn();
    this.mockMvc.perform(asyncDispatch(result)).andDo(print()).andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  void testReadSchoolHistoryPaginated_givenInvalidSearchCriteria_ShouldThrowException() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_HISTORY";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);

    this.schoolRepository.save(this.createSchoolData());
    final SearchCriteria invalidCriteria = SearchCriteria.builder().key(null).operation(null).value(null).valueType(null).build();
    final List<SearchCriteria> criteriaList = new ArrayList<>();
    criteriaList.add(invalidCriteria);
    final List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    final ObjectMapper objectMapper = new ObjectMapper();
    final String criteriaJSON = objectMapper.writeValueAsString(searches);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/history/paginated").with(mockAuthority).param("searchCriteriaList", criteriaJSON)
        .contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void testReadSchoolContactPaginated_givenValueFirstName_ShouldReturnStatusOk() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_CONTACT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);

    var schoolEntity = this.schoolRepository.save(createSchoolData());
    this.schoolContactRepository.save(createContactData(schoolEntity));
    final SearchCriteria criteria = SearchCriteria.builder().key("firstName").operation(FilterOperation.EQUAL).value("JOHN").valueType(ValueType.STRING).build();
    final List<SearchCriteria> criteriaList = new ArrayList<>();
    criteriaList.add(criteria);
    final List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    final ObjectMapper objectMapper = new ObjectMapper();
    final String criteriaJSON = objectMapper.writeValueAsString(searches);
    final MvcResult result = this.mockMvc
            .perform(get(URL.BASE_URL_SCHOOL + "/contact/paginated").with(mockAuthority).param("searchCriteriaList", criteriaJSON)
                    .contentType(APPLICATION_JSON))
            .andReturn();
    this.mockMvc.perform(asyncDispatch(result)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  void testReadSchoolContactPaginated_givenValueSchoolID_ShouldReturnStatusOk() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_CONTACT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);

    var schoolEntity = this.schoolRepository.save(createSchoolData());
    val contactEntity = this.schoolContactRepository.save(createContactData(schoolEntity));
    this.schoolRepository.findAll();
    final SearchCriteria criteria = SearchCriteria.builder().key("schoolContactTypeCode").operation(FilterOperation.EQUAL).value(contactEntity.getSchoolContactTypeCode()).valueType(ValueType.STRING).build();
    final List<SearchCriteria> criteriaList = new ArrayList<>();
    criteriaList.add(criteria);
    final List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    final ObjectMapper objectMapper = new ObjectMapper();
    final String criteriaJSON = objectMapper.writeValueAsString(searches);
    final MvcResult result = this.mockMvc
            .perform(get(URL.BASE_URL_SCHOOL + "/contact/paginated").with(mockAuthority).param("searchCriteriaList", criteriaJSON)
                    .contentType(APPLICATION_JSON))
            .andReturn();
    this.mockMvc.perform(asyncDispatch(result)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(1)));
  }

  private SchoolEntity createSchoolData() {
    return SchoolEntity
      .builder()
      .schoolNumber("12345")
      .displayName("School Name")
      .openedDate(LocalDateTime.now().minusDays(1).withNano(0))
      .schoolCategoryCode("PUBLIC")
      .schoolOrganizationCode("TWO_SEM")
      .schoolReportingRequirementCode("REGULAR")
      .facilityTypeCode("DISTONLINE")
      .website("abc@sd99.edu")
      .createDate(LocalDateTime.now().withNano(0))
      .updateDate(LocalDateTime.now().withNano(0))
      .createUser("TEST")
      .updateUser("TEST")
      .build();
  }

  private SchoolEntity createNewSchoolData(String schoolNumber, String schoolCategory, String facilityTypeCode) {
    return SchoolEntity
      .builder()
      .schoolNumber(schoolNumber)
      .displayName("School Name")
      .openedDate(LocalDateTime.now().minusDays(1).withNano(0))
      .schoolCategoryCode(schoolCategory)
      .schoolOrganizationCode("TWO_SEM")
      .schoolReportingRequirementCode("REGULAR")
      .facilityTypeCode(facilityTypeCode)
      .website("abc@sd99.edu")
      .createDate(LocalDateTime.now().withNano(0))
      .updateDate(LocalDateTime.now().withNano(0))
      .createUser("TEST")
      .updateUser("TEST")
      .build();
  }

  private SchoolHistoryEntity createHistorySchoolData(UUID schoolId) {
    return SchoolHistoryEntity.builder().schoolId(schoolId).schoolNumber("003").displayName("School Name").openedDate(LocalDateTime.now().minusDays(1)).schoolCategoryCode("PUBLIC")
      .schoolOrganizationCode("TWO_SEM").phoneNumber("1112223333").facilityTypeCode("DISTONLINE").website("abc@sd99.edu").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private DistrictTombstoneEntity createDistrictData() {
    return DistrictTombstoneEntity.builder().districtNumber("003").displayName("District Name").districtStatusCode("OPEN").districtRegionCode("KOOTENAYS")
      .website("abc@sd99.edu").createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  public static String asJsonString(final Object obj) {
    try {
      ObjectMapper om = new ObjectMapper();
      om.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      return om.writeValueAsString(obj);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
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
    return SchoolCategoryCodeEntity.builder().schoolCategoryCode("PUBLIC").description("Public School")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Public School").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private FacilityTypeCodeEntity createFacilityTypeCodeData() {
    return FacilityTypeCodeEntity.builder().facilityTypeCode("DISTONLINE").description("Standard School")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Standard School").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolContactEntity createContactData(SchoolEntity entity) {
    return SchoolContactEntity.builder().schoolEntity(entity).schoolContactTypeCode("PRINCIPAL").firstName("JOHN").lastName("WAYNE").createUser("TEST").updateUser("TEST").build();
  }

  private SchoolGradeEntity createSchoolGradeData(SchoolEntity entity) {
    return SchoolGradeEntity.builder().schoolEntity(entity).schoolGradeCode("01").createUser("TEST").updateUser("TEST").build();
  }

  private SchoolGrade createSchoolGrade(String schoolId) {
    SchoolGrade schoolGrade = new SchoolGrade();
    schoolGrade.setSchoolGradeCode("01");
    schoolGrade.setSchoolId(schoolId);
    schoolGrade.setCreateUser("TEST");
    schoolGrade.setUpdateUser("TEST");
    return schoolGrade;
  }
  private SchoolGrade createSchoolGrade() {
    SchoolGrade schoolGrade = new SchoolGrade();
    schoolGrade.setSchoolGradeCode("01");
    schoolGrade.setCreateUser("TEST");
    schoolGrade.setUpdateUser("TEST");
    return schoolGrade;
  }
  private NeighborhoodLearning createNeighborhoodLearning(String schoolId) {
    NeighborhoodLearning neighborhoodLearning = new NeighborhoodLearning();
    neighborhoodLearning.setSchoolId(schoolId);
    neighborhoodLearning.setNeighborhoodLearningTypeCode("COMM_USE");
    neighborhoodLearning.setCreateUser("TEST");
    neighborhoodLearning.setUpdateUser("TEST");
    return neighborhoodLearning;
  }
  private NeighborhoodLearning createNeighborhoodLearning() {
    NeighborhoodLearning neighborhoodLearning = new NeighborhoodLearning();
    neighborhoodLearning.setNeighborhoodLearningTypeCode("COMM_USE");
    neighborhoodLearning.setCreateUser("TEST");
    neighborhoodLearning.setUpdateUser("TEST");
    return neighborhoodLearning;
  }
  private SchoolAddress createSchoolAddress() {
    SchoolAddress schoolAddress = new SchoolAddress();
    schoolAddress.setAddressTypeCode("MAILING");
    schoolAddress.setAddressLine1("123 This Street");
    schoolAddress.setCity("Compton");
    schoolAddress.setProvinceCode("BC");
    schoolAddress.setCountryCode("CA");
    schoolAddress.setPostal("v1B9H2");

    return schoolAddress;
  }

  private NeighborhoodLearningEntity createNeighborhoodLearningData(SchoolEntity entity) {
    return NeighborhoodLearningEntity.builder().schoolEntity(entity).neighborhoodLearningTypeCode("COMM_USE").createUser("TEST").updateUser("TEST").build();
  }

  private SchoolAddressEntity createAddressData(SchoolEntity entity) {
    return SchoolAddressEntity.builder().schoolEntity(entity).addressTypeCode("MAILING").addressLine1("123 This Street").city("Compton")
      .provinceCode("BC").countryCode("CA").postal("V1B9H2").build();
  }

  private NoteEntity createNoteData(SchoolEntity entity) {
    return NoteEntity.builder().schoolID(entity.getSchoolId()).content("This is a note.").createUser("TEST").updateUser("TEST").build();
  }

  private SchoolContactTypeCodeEntity createContactTypeCodeData() {
    return SchoolContactTypeCodeEntity.builder().schoolContactTypeCode("PRINCIPAL").description("School Principal")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Principal").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private AddressTypeCodeEntity createAddressTypeCodeData() {
    return AddressTypeCodeEntity.builder().addressTypeCode("MAILING").description("Mailing Address")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Mailing Address").createDate(LocalDateTime.now())
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

  private SchoolGradeCodeEntity createSchoolGradeCodeData() {
    return SchoolGradeCodeEntity.builder().schoolGradeCode("01").description("First Grade")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("First").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolAddressEntity createSchoolAddressData() {
    return SchoolAddressEntity.builder().addressLine1("Line 1").city("City").provinceCode("BC").countryCode("CA").postal("V1V1V2").addressTypeCode("MAILING")
      .createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();
  }

  private NeighborhoodLearningTypeCodeEntity createNeighborhoodLearningTypeCodeData() {
    return NeighborhoodLearningTypeCodeEntity.builder().neighborhoodLearningTypeCode("COMM_USE").description("Community Use")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Community Use").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }
}


