package ca.bc.gov.educ.api.institute.service;

import static ca.bc.gov.educ.api.institute.constants.v1.EventOutcome.AUTHORITY_FOUND;
import static ca.bc.gov.educ.api.institute.constants.v1.EventOutcome.AUTHORITY_NOT_FOUND;
import static ca.bc.gov.educ.api.institute.constants.v1.EventOutcome.SCHOOL_CREATED;
import static ca.bc.gov.educ.api.institute.constants.v1.EventOutcome.SCHOOL_MOVED;
import static ca.bc.gov.educ.api.institute.constants.v1.EventOutcome.SCHOOL_UPDATED;
import static ca.bc.gov.educ.api.institute.constants.v1.EventStatus.MESSAGE_PUBLISHED;
import static ca.bc.gov.educ.api.institute.constants.v1.EventType.*;
import static org.assertj.core.api.Assertions.assertThat;

import ca.bc.gov.educ.api.institute.constants.v1.Topics;
import ca.bc.gov.educ.api.institute.exception.ConflictFoundException;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.filter.FilterOperation;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.model.v1.DistrictTombstoneEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import ca.bc.gov.educ.api.institute.model.v1.SchoolAddressEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictTombstoneRepository;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityRepository;
import ca.bc.gov.educ.api.institute.repository.v1.InstituteEventRepository;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolMoveRepository;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolRepository;
import ca.bc.gov.educ.api.institute.service.v1.EventHandlerService;
import ca.bc.gov.educ.api.institute.struct.v1.*;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
  DistrictTombstoneRepository districtTombstoneRepository;
  @Autowired
  private SchoolRepository schoolRepository;
  @Autowired
  private SchoolMoveRepository schoolMoveRepository;

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
    schoolMoveRepository.deleteAll();
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

    @Test
  public void testHandleEvent_givenEventTypeGET_PAGINATED_AUTHORITIES_BY_CRITERIA__DoesExistAndSynchronousNatsMessage_shouldRespondWithData() throws IOException, ExecutionException, InterruptedException {
    var independentAuthorityEntity = this.createIndependentAuthorityData();
    independentAuthorityRepository.save(independentAuthorityEntity);

    SearchCriteria criteriaSchoolNumber = SearchCriteria.builder().key("authorityNumber").operation(FilterOperation.EQUAL).value(independentAuthorityEntity.getAuthorityNumber().toString()).valueType(ValueType.INTEGER).build();
    List<SearchCriteria> criteriaList = new LinkedList<>();
    criteriaList.add(criteriaSchoolNumber);

    List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());

    ObjectMapper objectMapper = new ObjectMapper();
    String criteriaJSON = objectMapper.writeValueAsString(searches);

    var sagaId = UUID.randomUUID();
    final Event event = Event.builder().eventType(GET_PAGINATED_AUTHORITIES).sagaId(sagaId).eventPayload(SEARCH_CRITERIA_LIST.concat("=").concat(URLEncoder.encode(criteriaJSON, StandardCharsets.UTF_8)).concat("&").concat(PAGE_SIZE).concat("=").concat("100000").concat("&pageNumber=0")).build();
    var response = eventHandlerServiceUnderTest.handleGetPaginatedAuthorities(event).get();
    List<IndependentAuthority> payload = new ObjectMapper().readValue(response, new TypeReference<>() {
    });
    assertThat(payload).hasSize(1);
  }

  @Test
  public void testHandleEvent_givenEventTypeCREATE_SCHOOL_WITH_NEW_SCHOOL_NUMBER__DoesExistAndSynchronousNatsMessage_shouldRespondWithData() throws IOException, ExecutionException, InterruptedException {
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());

    var schoolEntity = this.createNewSchoolData(null, "PUBLIC", "DISTONLINE");

    SchoolMapper map = SchoolMapper.mapper;

    School mappedSchool = map.toStructure(schoolEntity);

    mappedSchool.setDistrictId(dist.getDistrictId().toString());
    mappedSchool.setCreateDate(null);
    mappedSchool.setUpdateDate(null);
    mappedSchool.setSchoolNumber(null);
    mappedSchool.setGrades(List.of(createSchoolGrade()));
    mappedSchool.setNeighborhoodLearning(List.of(createNeighborhoodLearning()));
    mappedSchool.setAddresses(List.of(createSchoolAddress()));

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    var sagaId = UUID.randomUUID();
    final Event event = Event.builder()
      .eventType(CREATE_SCHOOL)
      .eventOutcome(SCHOOL_CREATED)
      .sagaId(sagaId)
      .eventPayload(objectMapper.writeValueAsString(mappedSchool)).build();
    eventHandlerServiceUnderTest.handleCreateSchoolEvent(event).getLeft();
    var schoolCreatedEvent = instituteEventRepository.findBySagaIdAndEventType(sagaId, CREATE_SCHOOL.toString());
    assertThat(schoolCreatedEvent).isPresent();
    assertThat(schoolCreatedEvent.get().getEventStatus()).isEqualTo(MESSAGE_PUBLISHED.toString());
    assertThat(schoolCreatedEvent.get().getEventOutcome()).isEqualTo(SCHOOL_CREATED.toString());
  }

  @Test
  public void testHandleEvent_givenEventTypeCREATE_SCHOOL_WITH_EXISTING_SCHOOL_NUMBER__DoesExistAndSynchronousNatsMessage_shouldRespondWithData() throws IOException, ExecutionException, InterruptedException {
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());

    var schoolEntity = this.createNewSchoolData("99000", "PUBLIC", "DISTONLINE");

    SchoolMapper map = SchoolMapper.mapper;

    School mappedSchool = map.toStructure(schoolEntity);

    mappedSchool.setDistrictId(dist.getDistrictId().toString());
    mappedSchool.setCreateDate(null);
    mappedSchool.setUpdateDate(null);
    mappedSchool.setGrades(List.of(createSchoolGrade()));
    mappedSchool.setNeighborhoodLearning(List.of(createNeighborhoodLearning()));
    mappedSchool.setAddresses(List.of(createSchoolAddress()));

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    var sagaId = UUID.randomUUID();
    final Event event = Event.builder()
      .eventType(CREATE_SCHOOL)
      .eventOutcome(SCHOOL_CREATED)
      .sagaId(sagaId)
      .eventPayload(objectMapper.writeValueAsString(mappedSchool)).build();
    eventHandlerServiceUnderTest.handleCreateSchoolEvent(event).getLeft();
    var schoolCreatedEvent = instituteEventRepository.findBySagaIdAndEventType(sagaId, CREATE_SCHOOL.toString());
    assertThat(schoolCreatedEvent).isPresent();
    assertThat(schoolCreatedEvent.get().getEventStatus()).isEqualTo(MESSAGE_PUBLISHED.toString());
    assertThat(schoolCreatedEvent.get().getEventOutcome()).isEqualTo(SCHOOL_CREATED.toString());
  }

  @Test(expected = ConflictFoundException.class)
  public void testHandleEvent_givenEventTypeCREATE_SCHOOL_WITH_CONFLICTING_SCHOOL_NUMBER__DoesExistAndSynchronousNatsMessage_shouldThrowError() throws IOException, ExecutionException, InterruptedException {
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var existingSchoolEntity = this.createNewSchoolData("99000", "PUBLIC", "DISTONLINE");
    existingSchoolEntity.setDistrictEntity(dist);
    schoolRepository.save(existingSchoolEntity);

    var schoolEntity = this.createNewSchoolData("99000", "PUBLIC", "DISTONLINE");

    SchoolMapper map = SchoolMapper.mapper;

    School mappedSchool = map.toStructure(schoolEntity);

    mappedSchool.setDistrictId(dist.getDistrictId().toString());
    mappedSchool.setCreateDate(null);
    mappedSchool.setUpdateDate(null);
    mappedSchool.setGrades(List.of(createSchoolGrade()));
    mappedSchool.setNeighborhoodLearning(List.of(createNeighborhoodLearning()));
    mappedSchool.setAddresses(List.of(createSchoolAddress()));

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    var sagaId = UUID.randomUUID();
    final Event event = Event.builder().eventType(CREATE_SCHOOL).sagaId(sagaId).eventPayload(objectMapper.writeValueAsString(mappedSchool)).build();
    eventHandlerServiceUnderTest.handleCreateSchoolEvent(event);
  }

  @Test
  public void testHandleEvent_givenEventTypeUPDATE_SCHOOL__DoesExistAndSynchronousNatsMessage_shouldRespondWithData() throws IOException, ExecutionException, InterruptedException {
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    var schoolEntity = this.createNewSchoolData("99000", "PUBLIC", "DISTONLINE");
    schoolEntity.setDistrictEntity(dist);
    schoolRepository.save(schoolEntity);

    School school = new School();
    SchoolMapper map = SchoolMapper.mapper;
    school.setSchoolId(schoolEntity.getSchoolId().toString());

    BeanUtils.copyProperties(map.toStructure(schoolEntity), school);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    var sagaId = UUID.randomUUID();
    final Event event = Event.builder().eventType(UPDATE_SCHOOL).sagaId(sagaId).eventPayload(objectMapper.writeValueAsString(school)).build();
    eventHandlerServiceUnderTest.handleUpdateSchoolEvent(event).getLeft();
    var schoolUpdatedEvent = instituteEventRepository.findBySagaIdAndEventType(sagaId, UPDATE_SCHOOL.toString());
    assertThat(schoolUpdatedEvent).isPresent();
    assertThat(schoolUpdatedEvent.get().getEventStatus()).isEqualTo(MESSAGE_PUBLISHED.toString());
    assertThat(schoolUpdatedEvent.get().getEventOutcome()).isEqualTo(SCHOOL_UPDATED.toString());
  }

  @Test
  public void testHandleEvent_givenEventTypeMOVE_SCHOOL__DoesExistAndSynchronousNatsMessage_shouldRespondWithDataAndCreateMoveHistory() throws IOException, ExecutionException, InterruptedException {
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    SchoolEntity toSchoolEntity = this.createNewSchoolData("99000", "PUBLIC", "DISTONLINE");
    SchoolEntity fromSchoolEntity = this.createNewSchoolData("99000", "PUBLIC", "DISTONLINE");
    LocalDateTime moveDate = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.MILLIS);

    Set<SchoolAddressEntity> schoolAddressSet = new HashSet<>();
    SchoolAddressEntity fromSchoolAddressEntity = SchoolAddressEntity.builder()
        .addressTypeCode("MAILING").addressLine1("123 This Street").city("Victoria")
        .provinceCode("BC").countryCode("CA").postal("V1V2V3").schoolEntity(fromSchoolEntity).build();
    schoolAddressSet.add(fromSchoolAddressEntity);

    toSchoolEntity.setDistrictEntity(dist);
    fromSchoolEntity.setDistrictEntity(dist);
    fromSchoolEntity.setAddresses(schoolAddressSet);
    schoolRepository.save(fromSchoolEntity);

    School toSchool = new School();
    SchoolMapper map = SchoolMapper.mapper;

    BeanUtils.copyProperties(map.toStructure(toSchoolEntity), toSchool);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    MoveSchoolData moveSchoolData = createMoveSchoolData(toSchool, fromSchoolEntity.getSchoolId(), moveDate);

    UUID sagaId = UUID.randomUUID();
    final Event event = Event.builder().eventType(MOVE_SCHOOL).sagaId(sagaId).eventPayload(objectMapper.writeValueAsString(moveSchoolData)).build();

    eventHandlerServiceUnderTest.handleMoveSchoolEvent(event).getLeft();
    Optional<InstituteEvent> schoolUpdatedEvent = instituteEventRepository.findBySagaIdAndEventType(sagaId, MOVE_SCHOOL.toString());
    assertThat(schoolUpdatedEvent).isPresent();
    assertThat(schoolUpdatedEvent.get().getEventStatus()).isEqualTo(MESSAGE_PUBLISHED.toString());
    assertThat(schoolUpdatedEvent.get().getEventOutcome()).isEqualTo(SCHOOL_MOVED.toString());

    //check that data in event payload is updated
    MoveSchoolData moveSchoolEventData = JsonUtil.getJsonObjectFromString(MoveSchoolData.class, event.getEventPayload());
    assertThat(moveSchoolEventData.getToSchool().getSchoolId()).isNotNull();
    assertThat(moveSchoolEventData.getToSchool().getSchoolNumber()).isNotEqualTo("99000"); //new school number since school number already exists in district

    //2 schools = 1 that was created + 1 that was closed for the move.
    assertThat(schoolRepository.findAll()).hasSize(2);

    //confirm that running the same event twice will not create a new school.
    eventHandlerServiceUnderTest.handleMoveSchoolEvent(event).getLeft();
    assertThat(schoolRepository.findAll()).hasSize(2);

    SchoolEntity fromSchoolEntityComplete = schoolRepository.findById(fromSchoolEntity.getSchoolId()).orElseThrow();
    SchoolEntity toSchoolEntityComplete = schoolRepository.findById(UUID.fromString(moveSchoolEventData.getToSchool().getSchoolId())).orElseThrow();

    //confirm previous school has closed and address information saved
    assertThat(fromSchoolEntityComplete.getClosedDate()).isEqualTo(moveDate);
    assertThat(fromSchoolEntityComplete.getAddresses().stream().toList().get(0).getCity()).isEqualTo(fromSchoolAddressEntity.getCity());

    assertThat(fromSchoolEntityComplete.getSchoolMoveTo().stream().toList().get(0).getToSchoolId()).isEqualTo(toSchoolEntityComplete.getSchoolId());
    assertThat(toSchoolEntityComplete.getSchoolMoveFrom().stream().toList().get(0).getFromSchoolId()).isEqualTo(fromSchoolEntityComplete.getSchoolId());
  }

  @Test
  public void testHandleEvent_givenEventTypeMOVE_SCHOOL__ToSchoolIdDoesNotExist_shouldThrowEntitynotFoundException() throws IOException, ExecutionException, InterruptedException {
    final DistrictTombstoneEntity dist = this.districtTombstoneRepository.save(this.createDistrictData());
    SchoolEntity toSchoolEntity = this.createNewSchoolData("99000", "PUBLIC", "DISTONLINE");
    LocalDateTime moveDate = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.MILLIS);

    toSchoolEntity.setDistrictEntity(dist);

    School toSchool = new School();
    SchoolMapper map = SchoolMapper.mapper;

    BeanUtils.copyProperties(map.toStructure(toSchoolEntity), toSchool);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    MoveSchoolData moveSchoolData = createMoveSchoolData(toSchool, UUID.randomUUID(), moveDate);

    UUID sagaId = UUID.randomUUID();
    final Event event = Event.builder().eventType(MOVE_SCHOOL).sagaId(sagaId).eventPayload(objectMapper.writeValueAsString(moveSchoolData)).build();

    try {
      eventHandlerServiceUnderTest.handleMoveSchoolEvent(event).getLeft();
    } catch (EntityNotFoundException e) {
      assertThat(e.getMessage()).contains("SchoolEntity was not found for parameters");
      assertThat(schoolRepository.findAll()).isEmpty(); //make sure school isn't created when there is an error.
    }
  }

  private IndependentAuthorityEntity createIndependentAuthorityData() {
    return IndependentAuthorityEntity.builder().authorityNumber(003).displayName("IndependentAuthority Name").email("fake@email.com").openedDate(LocalDateTime.now().minusDays(1))
      .authorityTypeCode("INDEPEND").createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }

  private SchoolEntity createNewSchoolData(String schoolNumber, String schoolCategory, String facilityTypeCode) {
    return SchoolEntity.builder().schoolNumber(schoolNumber).displayName("School Name").openedDate(LocalDateTime.now().minusDays(1).withNano(0)).schoolCategoryCode(schoolCategory)
      .schoolOrganizationCode("TWO_SEM").facilityTypeCode(facilityTypeCode).website("abc@sd99.edu").createDate(LocalDateTime.now().withNano(0))
      .updateDate(LocalDateTime.now().withNano(0)).createUser("TEST").updateUser("TEST").build();
  }
  private DistrictTombstoneEntity createDistrictData() {
    return DistrictTombstoneEntity.builder().districtNumber("003").displayName("District Name").districtStatusCode("OPEN").districtRegionCode("KOOTENAYS")
            .website("abc@sd99.edu").createDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).createUser("TEST").updateUser("TEST").build();
  }
  private SchoolGrade createSchoolGrade() {
    SchoolGrade schoolGrade = new SchoolGrade();
    schoolGrade.setSchoolGradeCode("01");
    schoolGrade.setCreateUser("TEST");
    schoolGrade.setUpdateUser("TEST");
    return schoolGrade;
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

  private MoveSchoolData createMoveSchoolData(School toSchool, UUID fromSchoolId, LocalDateTime moveDate) {
    MoveSchoolData moveSchoolData = MoveSchoolData.builder().toSchool(toSchool).fromSchoolId(fromSchoolId.toString()).moveDate(moveDate.toString()).build();

    return moveSchoolData;
  }
}
