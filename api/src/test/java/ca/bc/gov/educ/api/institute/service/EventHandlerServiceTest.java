package ca.bc.gov.educ.api.institute.service;

import ca.bc.gov.educ.api.institute.constants.v1.Topics;
import ca.bc.gov.educ.api.institute.filter.FilterOperation;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityRepository;
import ca.bc.gov.educ.api.institute.repository.v1.InstituteEventRepository;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolRepository;
import ca.bc.gov.educ.api.institute.service.v1.EventHandlerService;
import ca.bc.gov.educ.api.institute.struct.v1.*;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import ca.bc.gov.educ.api.institute.util.TransformUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static ca.bc.gov.educ.api.institute.constants.v1.EventOutcome.AUTHORITY_FOUND;
import static ca.bc.gov.educ.api.institute.constants.v1.EventOutcome.AUTHORITY_NOT_FOUND;
import static ca.bc.gov.educ.api.institute.constants.v1.EventType.GET_AUTHORITY;
import static ca.bc.gov.educ.api.institute.constants.v1.EventType.GET_PAGINATED_SCHOOLS;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Slf4j
public class EventHandlerServiceTest {

  public static final String INSTITUTE_API_TOPIC = Topics.INSTITUTE_API_TOPIC.toString();
  @Autowired
  private IndependentAuthorityRepository independentAuthorityRepository;
  @Autowired
  private InstituteEventRepository instituteEventRepository;
  @Autowired
  private SchoolRepository schoolRepository;
  public static final String SEARCH_CRITERIA_LIST = "searchCriteriaList";
  public static final String PAGE_SIZE = "pageSize";
  @Autowired
  private EventHandlerService eventHandlerServiceUnderTest;
  private static final IndependentAuthorityMapper independentAuthorityMapper = IndependentAuthorityMapper.mapper;
  private static final SchoolMapper schoolMapper = SchoolMapper.mapper;
  private final boolean isSynchronous = false;
  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @After
  public void tearDown() {
    independentAuthorityRepository.deleteAll();
    instituteEventRepository.deleteAll();
    schoolRepository.deleteAll();
  }

  @Test
  public void testHandleEvent_givenEventTypeGET_AUTHORITY__whenNoStudentExist_shouldHaveEventOutcomeAUTHORITY_NOT_FOUND() throws JsonProcessingException, IOException {
    var sagaId = UUID.randomUUID();
    final Event event = Event.builder().eventType(GET_AUTHORITY).sagaId(sagaId).replyTo(INSTITUTE_API_TOPIC).eventPayload(JsonUtil.getJsonStringFromObject(UUID.randomUUID().toString())).build();
    byte[] response = eventHandlerServiceUnderTest.handleGetAuthorityEvent(event, isSynchronous);
    assertThat(response).isNotEmpty();
    Event responseEvent = JsonUtil.getObjectFromJsonBytes(Event.class, response);
    assertThat(responseEvent).isNotNull();
    assertThat(responseEvent.getEventOutcome()).isEqualTo(AUTHORITY_NOT_FOUND);
  }



  @Test
  public void testHandleEvent_givenEventTypeGET_AUTHORITY__whenStudentExist_shouldHaveEventOutcomeAUTHORITY_FOUND() throws IOException {
    IndependentAuthorityEntity entity = independentAuthorityRepository.save(createIndependentAuthorityData());
    var sagaId = UUID.randomUUID();
    final Event event = Event.builder().eventType(GET_AUTHORITY).sagaId(sagaId).replyTo(INSTITUTE_API_TOPIC).eventPayload(JsonUtil.getJsonStringFromObject(entity.getIndependentAuthorityId().toString())).build();
    byte[] response = eventHandlerServiceUnderTest.handleGetAuthorityEvent(event, isSynchronous);
    assertThat(response).isNotEmpty();
    Event responseEvent = JsonUtil.getObjectFromJsonBytes(Event.class, response);
    assertThat(responseEvent).isNotNull();
    assertThat(responseEvent.getEventOutcome()).isEqualTo(AUTHORITY_FOUND);
  }


  @Test
  public void testHandleEvent_givenEventTypeGET_AUTHORITY__whenStudentExistAndSynchronousNatsMessage_shouldRespondWithAuthorityData() throws JsonProcessingException {
    IndependentAuthorityEntity entity = independentAuthorityRepository.save(createIndependentAuthorityData());
    IndependentAuthorityEntity newEntity = independentAuthorityRepository.findById(entity.getIndependentAuthorityId()).get();
    var studentBytes = JsonUtil.getJsonBytesFromObject(independentAuthorityMapper.toStructure(newEntity));
    var sagaId = UUID.randomUUID();
    final Event event = Event.builder().eventType(GET_AUTHORITY).sagaId(sagaId).eventPayload(JsonUtil.getJsonStringFromObject(entity.getIndependentAuthorityId().toString())).build();
    var response = eventHandlerServiceUnderTest.handleGetAuthorityEvent(event, true);
    assertThat(studentBytes).isEqualTo(response);
  }

  @Test
  public void testHandleEvent_givenEventTypeGET_AUTHORITY__whenAuthorityDoesNotExistAndSynchronousNatsMessage_shouldRespondWithBlankObjectData() throws JsonProcessingException {
    var studentBytes = new byte[0];
    var sagaId = UUID.randomUUID();
    final Event event = Event.builder().eventType(GET_AUTHORITY).sagaId(sagaId).eventPayload(JsonUtil.getJsonStringFromObject(UUID.randomUUID().toString())).build();
    var response = eventHandlerServiceUnderTest.handleGetAuthorityEvent(event, true);
    assertThat(studentBytes).isEqualTo(response);
  }

  @Test
  public void testHandleEvent_givenEventTypeGET_PAGINATED_SCHOOLS_BY_CRITERIA__DoesExistAndSynchronousNatsMessage_shouldRespondWithData() throws IOException, ExecutionException, InterruptedException {
    var schoolEntity = this.createNewSchoolData(null, "PUBLIC", "DISTONLINE");;
    schoolRepository.save(schoolEntity);

    SearchCriteria criteriaSchoolNumber = SearchCriteria.builder().key("schoolNumber").operation(FilterOperation.EQUAL).value(schoolEntity.getSchoolNumber()).valueType(ValueType.STRING).build();
    List<SearchCriteria> criteriaList = new LinkedList<>();
    criteriaList.add(criteriaSchoolNumber);

    List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());

    ObjectMapper objectMapper = new ObjectMapper();
    String criteriaJSON = objectMapper.writeValueAsString(searches);

    var sagaId = UUID.randomUUID();
    final Event event = Event.builder().eventType(GET_PAGINATED_SCHOOLS).sagaId(sagaId).eventPayload(SEARCH_CRITERIA_LIST.concat("=").concat(URLEncoder.encode(criteriaJSON, StandardCharsets.UTF_8)).concat("&").concat(PAGE_SIZE).concat("=").concat("100000").concat("&pageNumber=0")).build();
    var response = eventHandlerServiceUnderTest.handleGetPaginatedSchools(event).get();
    List<School> payload = new ObjectMapper().readValue(response, new TypeReference<>() {
    });
    assertThat(payload).hasSize(1);
  }

  private IndependentAuthorityEntity createIndependentAuthorityData() {
    return IndependentAuthorityEntity.builder().authorityNumber(003).displayName("IndependentAuthority Name").openedDate(LocalDateTime.now().minusDays(1))
      .authorityTypeCode("INDEPEND").createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolEntity createNewSchoolData(String schoolNumber, String schoolCategory, String facilityTypeCode) {
    return SchoolEntity.builder().schoolNumber(schoolNumber).displayName("School Name").openedDate(LocalDateTime.now().minusDays(1).withNano(0)).schoolCategoryCode(schoolCategory)
      .schoolOrganizationCode("TWO_SEM").facilityTypeCode(facilityTypeCode).website("abc@sd99.edu").createDate(LocalDateTime.now().withNano(0))
      .updateDate(LocalDateTime.now().withNano(0)).createUser("TEST").updateUser("TEST").build();
  }
}
