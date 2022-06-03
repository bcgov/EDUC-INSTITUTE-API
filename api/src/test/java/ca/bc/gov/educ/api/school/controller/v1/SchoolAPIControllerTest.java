package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolRepository;
import ca.bc.gov.educ.api.institute.service.v1.SchoolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The type School api controller test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {InstituteApiResourceApplication.class})
//@ActiveProfiles("test")
@Slf4j
@SuppressWarnings({"java:S112", "java:S100", "java:S1192", "java:S2699"})
@AutoConfigureMockMvc
public class SchoolAPIControllerTest {

  /**
   * The Controller.
   */
  @Autowired
  SchoolAPIController controller;
  /**
   * The Mock mvc.
   */
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  SchoolRepository schoolRepository;

  private SchoolEntity schoolEntity;
  @Autowired
  SchoolService schoolService;

  /**
   * Sets up.
   */
  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    schoolEntity = schoolRepository.save(createSchool("123", "45678"));

    final File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("mock-pen-coordinator.json")).getFile());
  }

  /**
   * need to delete the records to make it working in unit tests assertion, else the records will keep growing and assertions will fail.
   */
  @After
  public void after() {
    schoolRepository.deleteAll();
  }

  @Test
  public void testGetSchool_GivenValidMincode_ShouldReturnStatusOK() throws Exception {
    schoolService.reloadCache();
    GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get("/api/v1/schools/12345678").with(mockAuthority)).andDo(print()).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.schoolName").value(schoolEntity.getSchoolName()));
  }


  @Test
  public void testGetAllSchool_GivenNoInput_ShouldReturnStatusOK() throws Exception {
    schoolService.reloadCache();
    GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get("/api/v1/schools").with(mockAuthority)).andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$").isArray()).andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  public void testGetSchool_GivenNotExistMincode_ShouldReturnStatusNotFound() throws Exception {
    GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get("/api/v1/schools/12345670").with(mockAuthority)).andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void testGetSchool_GivenInvalidMincode_ShouldReturnStatusNotFound() throws Exception {
    GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL";
    var mockAuthority = oidcLogin().authorities(grantedAuthority);
    this.mockMvc.perform(get("/api/v1/schools/12").with(mockAuthority)).andDo(print()).andExpect(status().isNotFound());
  }

  private SchoolEntity createSchool(String distNo, String schoolNo) {
    var mincode = Mincode.builder().distNo(distNo).schlNo(schoolNo).build();
    return SchoolEntity.builder()
        .mincode(mincode)
        .schoolName("Victoria High School")
        .build();
  }

  private SchoolEntity createClosedSchool(String distNo, String schoolNo) {
    var mincode = Mincode.builder().distNo(distNo).schlNo(schoolNo).build();
    return SchoolEntity.builder()
      .mincode(mincode)
      .closedDate(LocalDate.now().minus(1, ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
      .schoolName("Victoria High School")
      .build();
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
