package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.mapper.v1.CodeTableMapper;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictRegionCodeEntity;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictRegionCodeRepository;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictRepository;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = InstituteApiResourceApplication.class)
@AutoConfigureMockMvc
public class DistrictControllerTest {

  private static final CodeTableMapper mapper = CodeTableMapper.mapper;
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  CodeTableAPIController controller;

  @Autowired
  DistrictRepository districtRepository;

  @Autowired
  CodeTableService codeTableService;

  @Autowired
  DistrictRegionCodeRepository districtRegionCodeRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Before
  public void before(){
    this.districtRegionCodeRepository.save(this.createDistrictRegionCodeData());
  }

  /**
   * need to delete the records to make it working in unit tests assertion, else the records will keep growing and assertions will fail.
   */
  @After
  public void after() {
    this.districtRepository.deleteAll();
    this.districtRegionCodeRepository.deleteAll();
  }

  @Test
  public void testAllDistricts_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictEntity entity = this.districtRepository.save(this.createDistrictData());
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[0].districtId")
        .value(entity.getDistrictId().toString()));
  }

  @Test
  public void testRetrieveDistrict_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_DISTRICT";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
    final DistrictEntity entity = this.districtRepository.save(this.createDistrictData());
    this.mockMvc.perform(get(URL.BASE_URL_DISTRICT + "/" + entity.getDistrictId()).with(mockAuthority))
      .andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.districtId")
        .value(entity.getDistrictId().toString()));
  }

  @Test
  public void testDeleteDistrict_GivenValidID_ShouldReturnStatusOK() throws Exception {
    final var district = this.createDistrictData();
    var entity = this.districtRepository.save(district);

    this.mockMvc.perform(delete(URL.BASE_URL_DISTRICT + "/" + entity.getDistrictId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "DELETE_DISTRICT"))))
      .andDo(print())
      .andExpect(status().isNoContent());

    var deletedEntity = this.districtRepository.findById(entity.getDistrictId());
    Assert.assertTrue(deletedEntity.isEmpty());
  }

  @Test
  public void testUpdateDistrict_GivenValidPayload_ShouldReturnStatusCreated() throws Exception {
    final var district = this.createDistrictData();
    var entity = this.districtRepository.save(district);
    entity.setDisplayName("newdist");
    entity.setCreateDate(null);
    entity.setUpdateDate(null);

    this.mockMvc.perform(put(URL.BASE_URL_DISTRICT + "/" + entity.getDistrictId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(entity))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT"))))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(entity.getDisplayName().toUpperCase()));
  }

  @Test
  public void testCreateDistrict_GivenValidPayload_ShouldReturnStatusOK() throws Exception {
    final var district = this.createDistrictData();
    district.setCreateDate(null);
    district.setUpdateDate(null);
    this.mockMvc.perform(post(URL.BASE_URL_DISTRICT)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(asJsonString(district))
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "WRITE_DISTRICT"))))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value(district.getDisplayName().toUpperCase()));
  }

  private DistrictEntity createDistrictData() {
    return DistrictEntity.builder().districtNumber("003").displayName("District Name").openedDate(LocalDateTime.now().minusDays(1)).districtRegionCode("KOOTENAYS")
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

  private DistrictRegionCodeEntity createDistrictRegionCodeData() {
    return DistrictRegionCodeEntity.builder().districtRegionCode("KOOTENAYS").description("Kootenays region")
      .effectiveDate(LocalDateTime.now()).expiryDate(LocalDateTime.MAX).displayOrder(1).label("Kootenays").createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }
}


