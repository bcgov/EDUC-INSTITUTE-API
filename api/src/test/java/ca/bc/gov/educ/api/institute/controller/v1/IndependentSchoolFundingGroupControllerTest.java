package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.*;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentSchoolFundingGroup;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentSchoolFundingGroupHistory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.OidcLoginRequestPostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {InstituteApiResourceApplication.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
class IndependentSchoolFundingGroupControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  IndependentSchoolFundingGroupController independentSchoolFundingGroupController;
  @Autowired
  IndependentSchoolFundingGroupRepository independentSchoolFundingGroupRepository;
  @Autowired
  SchoolFundingGroupCodeRepository schoolFundingGroupCodeRepository;
  @Autowired
  SchoolGradeCodeRepository schoolGradeCodeRepository;
  @Autowired
  DistrictTombstoneRepository districtTombstoneRepository;
  @Autowired
  SchoolRepository schoolRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  protected final static ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

  @BeforeEach
  public void before() {
    SchoolFundingGroupCodeEntity group = new SchoolFundingGroupCodeEntity();
    group.setSchoolFundingGroupCode("GROUP1");
    group.setLabel("Group 1");
    group.setDescription("Group 1");
    group.setCreateDate(LocalDateTime.now());
    group.setCreateUser("ABC");
    schoolFundingGroupCodeRepository.save(group);

    SchoolGradeCodeEntity grade = new SchoolGradeCodeEntity();
    grade.setSchoolGradeCode("GRADE01");
    grade.setLabel("Grade 1");
    grade.setDescription("Grade 1");
    grade.setCreateDate(LocalDateTime.now());
    grade.setCreateUser("ABC");
    schoolGradeCodeRepository.save(grade);

    SchoolGradeCodeEntity grade2 = new SchoolGradeCodeEntity();
    grade2.setSchoolGradeCode("GRADE02");
    grade2.setLabel("Grade 2");
    grade2.setDescription("Grade 2");
    grade2.setCreateDate(LocalDateTime.now());
    grade2.setCreateUser("ABC");
    schoolGradeCodeRepository.save(grade2);
  }

  @AfterEach
  public void after() {
    this.schoolFundingGroupCodeRepository.deleteAll();
    this.schoolGradeCodeRepository.deleteAll();
  }

  @Test
  void testGetSchoolFundingGroup_WithWrongScope_ShouldReturnForbidden() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "WRONG_SCOPE";
    final OidcLoginRequestPostProcessor mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL_FUNDING + "/" + UUID.randomUUID()).with(mockAuthority))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void testGetSchoolFundingGroup_WithWrongID_ShouldReturnStatusNotFound() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL_FUNDING + "/" + UUID.randomUUID()).with(mockAuthority))
        .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void testGetSchoolFundingGroupByID_ShouldReturnFundingGroup() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority = oidcLogin().authorities(grantedAuthority);

    final var independentSchoolFundingGroupEntity = this.independentSchoolFundingGroupRepository.save(this.createMockIndependentSchoolFundingGroupEntity(UUID.randomUUID()));

    this.mockMvc.perform(
            get(URL.BASE_URL_SCHOOL_FUNDING + "/" + independentSchoolFundingGroupEntity.getSchoolFundingGroupID()).with(mockAuthority))
        .andDo(print()).andExpect(status().isOk()).andExpect(
            MockMvcResultMatchers.jsonPath("$.schoolFundingGroupID",
                equalTo(independentSchoolFundingGroupEntity.getSchoolFundingGroupID().toString())));
  }
  
  @Test
  void testGetSchoolFundingGroupByCreateUser_ShouldReturnFundingGroup() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority = oidcLogin().authorities(grantedAuthority);

    final var independentSchoolFundingGroupEntity = this.independentSchoolFundingGroupRepository.save(this.createMockIndependentSchoolFundingGroupEntity(UUID.randomUUID()));

    var resultActions = this.mockMvc.perform(
                    get(URL.BASE_URL_SCHOOL_FUNDING + "/search/" + independentSchoolFundingGroupEntity.getSchoolID()).with(mockAuthority))
            .andDo(print()).andExpect(status().isOk());

    val summary = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), new TypeReference<List>() {
    });

    assertThat(summary).hasSize(1);

    var independentSchoolFundingGroupEntity1 = this.createMockIndependentSchoolFundingGroupEntity(independentSchoolFundingGroupEntity.getSchoolID());
    independentSchoolFundingGroupEntity1.setSchoolGradeCode("GRADE02");
    this.independentSchoolFundingGroupRepository.save(independentSchoolFundingGroupEntity1);

    var resultActions1 = this.mockMvc.perform(
                    get(URL.BASE_URL_SCHOOL_FUNDING + "/search/" + independentSchoolFundingGroupEntity.getSchoolID()).with(mockAuthority))
            .andDo(print()).andExpect(status().isOk());

    val summary1 = objectMapper.readValue(resultActions1.andReturn().getResponse().getContentAsByteArray(), new TypeReference<List>() {
    });

    assertThat(summary1).hasSize(2);
  }

  @Test
  void testUpdateSchoolFundingGroupByCreateUserWithHistory_ShouldReturnFundingGroup() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority = oidcLogin().authorities(grantedAuthority);

    final var independentSchoolFundingGroupEntity = this.independentSchoolFundingGroupRepository.save(this.createMockIndependentSchoolFundingGroupEntity(UUID.randomUUID()));

    var resultActions = this.mockMvc.perform(
                    get(URL.BASE_URL_SCHOOL_FUNDING + "/search/" + independentSchoolFundingGroupEntity.getSchoolID()).with(mockAuthority))
            .andDo(print()).andExpect(status().isOk());

    val summary = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), new TypeReference<List<IndependentSchoolFundingGroupEntity>>() {
    });

    assertThat(summary).hasSize(1);

    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);

    independentSchoolFundingGroupEntity.setSchoolGradeCode("GRADE02");
    independentSchoolFundingGroupEntity.setSchoolID(entity.getSchoolId());
    independentSchoolFundingGroupEntity.setUpdateDate(null);
    independentSchoolFundingGroupEntity.setUpdateUser(null);
    independentSchoolFundingGroupEntity.setCreateDate(null);
    independentSchoolFundingGroupEntity.setCreateUser(null);

    final GrantedAuthority grantedAuthority2 = () -> "SCOPE_WRITE_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority2 = oidcLogin().authorities(grantedAuthority2);

    var resultActions2 = this.mockMvc.perform(put(URL.BASE_URL_SCHOOL_FUNDING + "/" + independentSchoolFundingGroupEntity.getSchoolFundingGroupID()).contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON).content(asJsonString(independentSchoolFundingGroupEntity)).with(mockAuthority2))
            .andDo(print()).andExpect(status().isOk());

    val summary2 = objectMapper.readValue(resultActions2.andReturn().getResponse().getContentAsByteArray(), new TypeReference<IndependentSchoolFundingGroup>() {
    });

    assertThat(summary2).isNotNull();
    assertThat(summary2.getSchoolGradeCode()).isEqualTo("GRADE02");

    final GrantedAuthority grantedAuthority3 = () -> "SCOPE_READ_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority3 = oidcLogin().authorities(grantedAuthority3);

    var resultActions3 = this.mockMvc.perform(get(URL.BASE_URL_SCHOOL_FUNDING + "/search/" + independentSchoolFundingGroupEntity.getSchoolID() + "/history").contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON).content(asJsonString(independentSchoolFundingGroupEntity)).with(mockAuthority3))
            .andDo(print()).andExpect(status().isOk());

    val summary3 = objectMapper.readValue(resultActions3.andReturn().getResponse().getContentAsByteArray(), new TypeReference<List<IndependentSchoolFundingGroupHistory>>() {
    });

    assertThat(summary3).hasSize(1);
  }

  @Test
  void testCreateSchoolFundingGroup_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_WRITE_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority = oidcLogin().authorities(grantedAuthority);

    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);

    final var independentSchoolFundingGroupEntity = this.createMockIndependentSchoolFundingGroupEntity(entity.getSchoolId());
    independentSchoolFundingGroupEntity.setCreateDate(null);
    independentSchoolFundingGroupEntity.setUpdateDate(null);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL_FUNDING).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(asJsonString(independentSchoolFundingGroupEntity)).with(mockAuthority))
        .andDo(print()).andExpect(status().isCreated());
  }

  @Test
  void testCreateSchoolFundingGroup_GivenInvalidPayload_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_WRITE_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority = oidcLogin().authorities(grantedAuthority);

    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);

    final var independentSchoolFundingGroupEntity = this.createMockIndependentSchoolFundingGroupEntity(entity.getSchoolId());
    independentSchoolFundingGroupEntity.setCreateDate(null);
    independentSchoolFundingGroupEntity.setUpdateDate(null);
    independentSchoolFundingGroupEntity.setSchoolFundingGroupID(UUID.randomUUID());

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL_FUNDING).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(asJsonString(independentSchoolFundingGroupEntity)).with(mockAuthority))
        .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void testCreateSchoolFundingGroup_GivenInValidGrade_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_WRITE_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority = oidcLogin().authorities(grantedAuthority);

    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);

    final var independentSchoolFundingGroupEntity = this.createMockIndependentSchoolFundingGroupEntity(entity.getSchoolId());
    independentSchoolFundingGroupEntity.setCreateDate(null);
    independentSchoolFundingGroupEntity.setUpdateDate(null);
    independentSchoolFundingGroupEntity.setSchoolGradeCode("ABC");

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL_FUNDING).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(asJsonString(independentSchoolFundingGroupEntity)).with(mockAuthority))
        .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void testCreateSchoolFundingGroup_GivenInValidFundingCode_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_WRITE_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority = oidcLogin().authorities(grantedAuthority);

    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createSchoolData();
    schoolEntity.setDistrictEntity(dist);
    final SchoolEntity entity = this.schoolRepository.save(schoolEntity);

    final var independentSchoolFundingGroupEntity = this.createMockIndependentSchoolFundingGroupEntity(entity.getSchoolId());
    independentSchoolFundingGroupEntity.setCreateDate(null);
    independentSchoolFundingGroupEntity.setUpdateDate(null);
    independentSchoolFundingGroupEntity.setSchoolFundingGroupCode("ABC");

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL_FUNDING).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(asJsonString(independentSchoolFundingGroupEntity)).with(mockAuthority))
        .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void testCreateSchoolFundingGroup_GivenInValidSchoolID_ShouldReturnStatusBadRequest() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_WRITE_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority = oidcLogin().authorities(grantedAuthority);

    final var independentSchoolFundingGroupEntity = this.createMockIndependentSchoolFundingGroupEntity(UUID.randomUUID());
    independentSchoolFundingGroupEntity.setCreateDate(null);
    independentSchoolFundingGroupEntity.setUpdateDate(null);

    this.mockMvc.perform(post(URL.BASE_URL_SCHOOL_FUNDING).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).content(asJsonString(independentSchoolFundingGroupEntity)).with(mockAuthority))
        .andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  void testDeleteCollection_GivenValidPayload_ShouldReturnNoContent() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_DELETE_SCHOOL_FUNDING_GROUP";
    final OidcLoginRequestPostProcessor mockAuthority = oidcLogin().authorities(grantedAuthority);

    final var independentSchoolFundingGroupEntity = this.independentSchoolFundingGroupRepository.save(this.createMockIndependentSchoolFundingGroupEntity(UUID.randomUUID()));

    this.mockMvc.perform(
            delete(URL.BASE_URL_SCHOOL_FUNDING + "/" + independentSchoolFundingGroupEntity.getSchoolFundingGroupID().toString()).contentType(
                    MediaType.APPLICATION_JSON)
                .with(mockAuthority))
        .andDo(print()).andExpect(status().isNoContent());

    var independentSchoolFundingGroupRepositoryAll =  this.independentSchoolFundingGroupRepository.findAll();
    assertThat(independentSchoolFundingGroupRepositoryAll).isEmpty();
  }

  public IndependentSchoolFundingGroupEntity createMockIndependentSchoolFundingGroupEntity(UUID schoolID){
    IndependentSchoolFundingGroupEntity independentSchoolFundingGroupEntity = new IndependentSchoolFundingGroupEntity();
    independentSchoolFundingGroupEntity.setSchoolID(schoolID);
    independentSchoolFundingGroupEntity.setSchoolFundingGroupCode("GROUP1");
    independentSchoolFundingGroupEntity.setSchoolGradeCode("GRADE01");
    independentSchoolFundingGroupEntity.setCreateUser("ABC");
    independentSchoolFundingGroupEntity.setCreateDate(LocalDateTime.now());
    independentSchoolFundingGroupEntity.setUpdateUser("ABC");
    independentSchoolFundingGroupEntity.setUpdateDate(LocalDateTime.now());
    return independentSchoolFundingGroupEntity;
  }

  private DistrictTombstoneEntity createDistrictData() {
    return DistrictTombstoneEntity.builder().districtNumber("003").displayName("District Name").districtStatusCode("OPEN").districtRegionCode("KOOTENAYS")
            .website("abc@sd99.edu").createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
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

  public static String asJsonString(final Object obj) {
    try {
      ObjectMapper om = new ObjectMapper();
      om.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      return om.writeValueAsString(obj);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

}
