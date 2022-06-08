package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.mapper.v1.CodeTableMapper;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.*;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = InstituteApiResourceApplication.class)
@AutoConfigureMockMvc
public class SchoolControllerTest {

  private static final CodeTableMapper mapper = CodeTableMapper.mapper;
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  CodeTableAPIController controller;

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

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Before
  public void before(){
    this.schoolCategoryCodeRepository.save(this.createSchoolCategoryCodeData());
    this.schoolOrganizationCodeRepository.save(this.createSchoolOrganizationCodeData());
    this.facilityTypeCodeRepository.save(this.createFacilityTypeCodeData());
  }

  /**
   * need to delete the records to make it working in unit tests assertion, else the records will keep growing and assertions will fail.
   */
  @After
  public void after() {
    this.schoolRepository.deleteAll();
    this.schoolHistoryRepository.deleteAll();
    this.schoolCategoryCodeRepository.deleteAll();
    this.schoolOrganizationCodeRepository.deleteAll();
    this.facilityTypeCodeRepository.deleteAll();
  }

  @Test
  public void testAllSchools_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].schoolId")
        .value(entity.getSchoolId().toString()));
  }

  @Test
  public void testRetrieveSchool_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.schoolId")
        .value(entity.getSchoolId().toString()));
  }

  @Test
  public void testRetrieveSchoolHistory_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    final SchoolHistoryEntity historyEntity = this.schoolHistoryRepository.save(this.createHistorySchoolData(entity.getSchoolId()));
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId() + "/history").with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].schoolHistoryId")
        .value(historyEntity.getSchoolHistoryId().toString()));
  }

  @Test
  public void testDeleteSchool_GivenValidID_ShouldReturnStatusOK() throws Exception {
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
    Assert.assertTrue(deletedEntity.isEmpty());
  }

  @Test
  public void testUpdateSchool_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var school = this.createSchoolData();
    var entity = this.schoolRepository.save(school);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);

    this.mockMvc.perform(put(URL.BASE_URL_SCHOOL + "/" + entity.getSchoolId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(entity.getDisplayName().toUpperCase()));
  }

  @Test
  public void testCreateSchool_GivenValidPayload_ShouldReturnStatusOK() throws Exception {
    final var school = this.createSchoolData();
    school.setCreateDate(null);
    school.setUpdateDate(null);
    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(school))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_SCHOOL"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(school.getDisplayName().toUpperCase()));
  }

  private SchoolEntity createSchoolData() {
    return SchoolEntity.builder().schoolNumber("003").displayName("School Name").openedDate(LocalDateTime.now().minusDays(1)).schoolCategoryCode("PUB_SCHL")
      .schoolOrganizationCode("TWO_SEM").facilityTypeCode("STAND_SCHL").website("abc@sd99.edu").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolHistoryEntity createHistorySchoolData(UUID schoolId) {
    return SchoolHistoryEntity.builder().schoolId(schoolId).schoolNumber("003").displayName("School Name").openedDate(LocalDateTime.now().minusDays(1)).schoolCategoryCode("PUB_SCHL")
      .schoolOrganizationCode("TWO_SEM").facilityTypeCode("STAND_SCHL").website("abc@sd99.edu").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
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
}


