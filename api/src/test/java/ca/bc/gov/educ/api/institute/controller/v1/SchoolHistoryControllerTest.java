package ca.bc.gov.educ.api.institute.controller.v1;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ca.bc.gov.educ.api.institute.InstituteApiResourceApplication;
import ca.bc.gov.educ.api.institute.constants.v1.URL;
import ca.bc.gov.educ.api.institute.filter.FilterOperation;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolHistoryEntity;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolHistoryRepository;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolRepository;
import ca.bc.gov.educ.api.institute.struct.v1.Search;
import ca.bc.gov.educ.api.institute.struct.v1.SearchCriteria;
import ca.bc.gov.educ.api.institute.struct.v1.ValueType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(classes = { InstituteApiResourceApplication.class })
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SchoolHistoryControllerTest {
  protected final static ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  SchoolRepository schoolRepository;

  @Autowired
  SchoolHistoryRepository schoolHistoryRepository;

  @BeforeEach
  public void before(){
    MockitoAnnotations.openMocks(this);
  }

  /**
   * need to delete the records to make it working in unit tests assertion, else the records will keep growing and assertions will fail.
   */
  @AfterEach
  public void after() {
    this.schoolRepository.deleteAll();
    this.schoolHistoryRepository.deleteAll();
  }

//  @Test
//  void testReadSchoolHistoryPaginated_givenValueNull_ShouldReturnStatusOk() throws Exception {
//    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_HISTORY";
//    final var mockAuthority = oidcLogin().authorities(grantedAuthority);
//
//    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
//    this.schoolHistoryRepository.save(createHistorySchoolData(entity.getSchoolId()));
//    val entitiesFromDB = this.schoolHistoryRepository.findAll();
//    final SearchCriteria criteria = SearchCriteria.builder().key("website").operation(FilterOperation.EQUAL).value(null).valueType(ValueType.STRING).build();
//    final List<SearchCriteria> criteriaList = new ArrayList<>();
//    criteriaList.add(criteria);
//    final List<Search> searches = new LinkedList<>();
//    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
//    final ObjectMapper objectMapper = new ObjectMapper();
//    final String criteriaJSON = objectMapper.writeValueAsString(searches);
//    this.mockMvc.perform(get(URL.BASE_URL_SCHOOL_HISTORY + "/paginated").with(mockAuthority).param("searchCriteriaList", criteriaJSON)
//        .contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
//  }

  @Test
  void testReadSchoolHistoryPaginated_givenSchoolNumber_ShouldReturnStatusOk() throws Exception {
    final GrantedAuthority grantedAuthority = () -> "SCOPE_READ_SCHOOL_HISTORY";
    final var mockAuthority = oidcLogin().authorities(grantedAuthority);

    final SchoolEntity entity = this.schoolRepository.save(this.createSchoolData());
    final SchoolHistoryEntity historyEntity = this.schoolHistoryRepository.save(createHistorySchoolData(entity.getSchoolId()));
    val entitiesFromDB = this.schoolHistoryRepository.findAll();
    final SearchCriteria criteria = SearchCriteria.builder().key("schoolNumber").operation(FilterOperation.EQUAL).value(historyEntity.getSchoolNumber()).valueType(ValueType.STRING).build();
    final List<SearchCriteria> criteriaList = new ArrayList<>();
    criteriaList.add(criteria);
    final List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    final ObjectMapper objectMapper = new ObjectMapper();
    final String criteriaJSON = objectMapper.writeValueAsString(searches);
    final MvcResult result = this.mockMvc.perform(get(URL.BASE_URL_SCHOOL_HISTORY + "/paginated").with(mockAuthority).param("searchCriteriaList", criteriaJSON)
        .contentType(APPLICATION_JSON)).andReturn();
    this.mockMvc.perform(asyncDispatch(result)).andDo(print()).andExpect(jsonPath("$.content", hasSize(1)));
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
}


