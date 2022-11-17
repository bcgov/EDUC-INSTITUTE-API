package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.filter.FilterOperation;
import ca.bc.gov.educ.api.institute.mapper.v1.CodeTableMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.*;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
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
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
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
  private static final CodeTableMapper mapper = CodeTableMapper.mapper;

  private static final SchoolMapper schoolMapper = SchoolMapper.mapper;
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  CodeTableAPIController controller;

  @Autowired
  DistrictRepository districtRepository;

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
  FacilityTypeCodeRepository facilityTypeCodeRepository;

  @Autowired
  SchoolContactTypeCodeRepository schoolContactTypeCodeRepository;

  @Autowired
  SchoolContactRepository schoolContactRepository;

  @Autowired
  AddressRepository addressRepository;

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
  NeighborhoodLearningTypeCodeRepository neighborhoodLearningTypeCodeRepository;

  @BeforeEach
  public void before(){
    MockitoAnnotations.openMocks(this);
    this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData());
    this.schoolOrganizationCodeRepository.save(this.createSchoolOrganizationCodeData());
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
    this.facilityTypeCodeRepository.deleteAll();
  }

  @Test
  void testAllSchools_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictEntity dist = this.districtRepository.save(this.createDistrictData());
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
    final DistrictEntity dist = this.districtRepository.save(this.createDistrictData());
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
    final DistrictEntity dist = this.districtRepository.save(this.createDistrictData());
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
    final DistrictEntity dist = this.districtRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);

    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.schoolId")
        .value(entity.getSchoolId().toString()));
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
    final DistrictEntity dist = this.districtRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);
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
  void testUpdateSchoolWithAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    final DistrictEntity dist = this.districtRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    SchoolEntity entity = this.schoolRepository.save(schoolEntity);
    entity.getAddresses().add(this.createSchoolAddressData());
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

    entity = this.schoolRepository.findById(entity.getSchoolId()).get();
    var addr = this.createSchoolAddressData();
    addr.setAddressLine1("123 TESTING");
    entity.getAddresses().iterator().next().setAddressLine1("123 TESTING");
    entity.getAddresses().add(addr);

    SchoolMapper mapper = SchoolMapper.mapper;

    var auth = mapper.toStructure(entity);

    auth.setCreateDate(null);
    auth.setUpdateDate(null);

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
    final DistrictEntity dist = this.districtRepository.save(this.createDistrictData());
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
    final DistrictEntity dist = this.districtRepository.save(this.createDistrictData());
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
  void testCreateSchool_GivenValidPayload_ShouldReturnStatusOK() throws Exception {
    final DistrictEntity dist = this.districtRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    SchoolMapper map = SchoolMapper.mapper;

    School mappedSchool = map.toStructure(schoolEntity);

    mappedSchool.setDistrictId(dist.getDistrictId().toString());
    mappedSchool.setCreateDate(null);
    mappedSchool.setUpdateDate(null);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(mappedSchool))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(mappedSchool.getDisplayName()));
  }

  @Test
  void testCreateSchool_GivenInvalidPayload_ShouldReturnStatusBadRequest() throws Exception {
    final var school = this.createSchoolData();
    school.setSchoolCategoryCode("ABCD");
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
    contactEntity.setPubliclyAvailable(true);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/contact")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(contactEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_CONTACT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(contactEntity.getLastName()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(contactEntity.getPhoneNumber()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.phoneExtension").value(contactEntity.getPhoneExtension()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.alternatePhoneNumber").value(contactEntity.getAlternatePhoneNumber()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.alternatePhoneExtension").value(contactEntity.getAlternatePhoneExtension()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.publiclyAvailable").value(contactEntity.isPubliclyAvailable()));
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
  void testCreateSchoolAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final SchoolEntity schoolEntity = this.schoolRepository.save(this.createSchoolData());
    AddressEntity addressEntity = createAddressData(schoolEntity);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/address")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(addressEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(addressEntity.getCity()));
  }

  @Test
  void testDeleteSchoolAddress_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    AddressEntity addressEntity = createAddressData(schoolEntity);
    var address = this.addressRepository.save(addressEntity);

    this.mockMvc.perform(delete(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/address/" + address.getAddressId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(schoolEntity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_SCHOOL_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedAddress = this.addressRepository.findById(address.getAddressId());
    Assertions.assertTrue(deletedAddress.isEmpty());
  }

  @Test
  void testRetrieveSchoolAddress_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_ADDRESS";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    AddressEntity addressEntity = createAddressData(schoolEntity);
    var address = this.addressRepository.save(addressEntity);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/address/" + address.getAddressId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.addressId")
        .value(address.getAddressId().toString()));
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
  void testUpdateSchoolAddress_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    var schoolEntity = this.schoolRepository.save(school);
    AddressEntity addressEntity = createAddressData(schoolEntity);
    var address = this.addressRepository.save(addressEntity);
    address.setCity("southshore");

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + schoolEntity.getSchoolId() + "/address/" + address.getAddressId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(address))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL_ADDRESS"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(address.getCity()));
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
    final DistrictEntity dist = this.districtRepository.save(this.createDistrictData());

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
    val resultActions = this.mockMvc.perform(get(URL.BASE_URL_SCHOOL).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].schoolId")
        .value(entity.getSchoolId().toString()));

    val schools = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), new TypeReference<List<School>>() {
    });
    val school = schools.get(0);
    school.setCreateDate(null);
    school.setUpdateDate(null);
    school.setGrades(List.of(createSchoolGrade(school.getSchoolId())));
    school.setNeighborhoodLearning(List.of(createNeighborhoodLearning(school.getSchoolId())));

    final DistrictEntity dist = this.districtRepository.save(this.createDistrictData());
    school.setDistrictId(dist.getDistrictId().toString());
    school.setUpdateDate(null);
    school.setCreateDate(null);

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
      .andExpect(jsonPath("$.[0].schoolGrades.[0]", is(notNullValue())))
      .andExpect(jsonPath("$.[0].neighbourhoodLearnings.[0]", is(notNullValue())));

  }

  @Test
  void testReadSchoolHistoryPaginated_givenValueNull_ShouldReturnStatusOk() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_HISTORY";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);

    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    this.schoolHistoryRepository.save(createHistorySchoolData(entity.getSchoolId()));
    val entitiesFromDB = this.schoolHistoryRepository.findAll();
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
    val entitiesFromDB = this.schoolHistoryRepository.findAll();
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
  void testReadSchoolHistoryPaginated_givenMultipleSearchCriteriaInlcudingLowerCase_ShouldReturnStatusOk() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_HISTORY";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);

    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    final SchoolHistoryEntity historyEntity = this.schoolHistoryRepository.save(createHistorySchoolData(entity.getSchoolId()));
    final SearchCriteria criteriaSchoolNumber = SearchCriteria.builder().key("schoolNumber").operation(FilterOperation.EQUAL).value(historyEntity.getSchoolNumber()).valueType(ValueType.STRING).build();
    final SearchCriteria criteriaSchoolUUID = SearchCriteria.builder().key("schoolId").operation(FilterOperation.EQUAL).value(historyEntity.getSchoolId().toString().toLowerCase()).valueType(ValueType.UUID).build();
    final SearchCriteria criteriaCreateDate = SearchCriteria.builder().key("createDate").operation(FilterOperation.LESS_THAN).value("2999-01-01T00:00:00").valueType(ValueType.DATE_TIME).condition(Condition.AND).build();
    final List<SearchCriteria> criteriaList = new ArrayList<>();
    criteriaList.add(criteriaSchoolNumber);
    criteriaList.add(criteriaSchoolUUID);
    criteriaList.add(criteriaCreateDate);
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

    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    final SchoolHistoryEntity historyEntity = this.schoolHistoryRepository.save(createHistorySchoolData(entity.getSchoolId()));
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

  private SchoolEntity createSchoolData() {
    return SchoolEntity.builder().schoolNumber("12345").displayName("School Name").openedDate(LocalDateTime.now().minusDays(1).withNano(0)).schoolCategoryCode("PUB_SCHL")
      .schoolOrganizationCode("TWO_SEM").facilityTypeCode("STAND_SCHL").website("abc@sd99.edu").createDate(LocalDateTime.now().withNano(0))
      .updateDate(LocalDateTime.now().withNano(0)).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolHistoryEntity createHistorySchoolData(UUID schoolId) {
    return SchoolHistoryEntity.builder().schoolId(schoolId).schoolNumber("003").displayName("School Name").openedDate(LocalDateTime.now().minusDays(1)).schoolCategoryCode("PUB_SCHL")
      .schoolOrganizationCode("TWO_SEM").facilityTypeCode("STAND_SCHL").website("abc@sd99.edu").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private DistrictEntity createDistrictData() {
    return DistrictEntity.builder().districtNumber("003").displayName("District Name").districtStatusCode("OPEN").districtRegionCode("KOOTENAYS")
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

  private FacilityTypeCodeEntity createFacilityTypeCodeData() {
    return FacilityTypeCodeEntity.builder().facilityTypeCode("STAND_SCHL").description("Standard School")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Standard School").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolContactEntity createContactData(SchoolEntity entity) {
    return SchoolContactEntity.builder().schoolEntity(entity).schoolContactTypeCode("PRINCIPAL").firstName("John").lastName("Wayne").createUser("TEST").updateUser("TEST").build();
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
    schoolGrade.setCreateDate(String.valueOf(LocalDateTime.now()));
    return schoolGrade;
  }
  private NeighborhoodLearning createNeighborhoodLearning(String schoolId) {
    NeighborhoodLearning neighborhoodLearning = new NeighborhoodLearning();
    neighborhoodLearning.setSchoolId(schoolId);
    neighborhoodLearning.setNeighborhoodLearningTypeCode("COMM_USE");
    neighborhoodLearning.setCreateUser("TEST");
    neighborhoodLearning.setUpdateUser("TEST");
    neighborhoodLearning.setCreateDate(String.valueOf(LocalDateTime.now()));
    return neighborhoodLearning;
  }

  private NeighborhoodLearningEntity createNeighborhoodLearningData(SchoolEntity entity) {
    return NeighborhoodLearningEntity.builder().schoolEntity(entity).neighborhoodLearningTypeCode("COMM_USE").createUser("TEST").updateUser("TEST").build();
  }

  private AddressEntity createAddressData(SchoolEntity entity) {
    return AddressEntity.builder().schoolEntity(entity).addressTypeCode("MAILING").addressLine1("123 This Street").city("Compton")
      .provinceCode("BC").countryCode("CA").postal("V1B9H2").createUser("TEST").updateUser("TEST").build();
  }

  private NoteEntity createNoteData(SchoolEntity entity) {
    return NoteEntity.builder().schoolEntity(entity).content("This is a note.").createUser("TEST").updateUser("TEST").build();
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

  private AddressEntity createSchoolAddressData() {
    return AddressEntity.builder().independentAuthorityEntity(null).addressLine1("Line 1").city("City").provinceCode("BC").countryCode("CA").postal("V1V1V2").addressTypeCode("MAILING")
      .createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private NeighborhoodLearningTypeCodeEntity createNeighborhoodLearningTypeCodeData() {
    return NeighborhoodLearningTypeCodeEntity.builder().neighborhoodLearningTypeCode("COMM_USE").description("Community Use")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Community Use").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }
}


