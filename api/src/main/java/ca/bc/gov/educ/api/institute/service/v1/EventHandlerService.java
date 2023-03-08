package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.constants.v1.EventOutcome;
import ca.bc.gov.educ.api.institute.constants.v1.EventType;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityRepository;
import ca.bc.gov.educ.api.institute.repository.v1.InstituteEventRepository;
import ca.bc.gov.educ.api.institute.struct.v1.Event;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static ca.bc.gov.educ.api.institute.constants.v1.EventStatus.MESSAGE_PUBLISHED;
import static lombok.AccessLevel.PRIVATE;

/**
 * The type Event handler service.
 */
@Service
@Slf4j
@SuppressWarnings("java:S3864")
public class EventHandlerService {

  /**
   * The constant PAYLOAD_LOG.
   */
  public static final String PAYLOAD_LOG = "payload is :: {}";

  public static final String EVENT_PAYLOAD = "event is :: {}";

  public static final String SEARCH_CRITERIA_LIST = "searchCriteriaList";

  public static final String PAGE_SIZE = "pageSize";


  public static final String PAGE_NUMBER = "pageNumber";

  public static final String SORT_CRITERIA = "sortCriteriaJson";

  /**
   * The constant NO_RECORD_SAGA_ID_EVENT_TYPE.
   */
  public static final String NO_RECORD_SAGA_ID_EVENT_TYPE = "no record found for the saga id and event type combination, processing.";
  /**
   * The constant RECORD_FOUND_FOR_SAGA_ID_EVENT_TYPE.
   */
  public static final String RECORD_FOUND_FOR_SAGA_ID_EVENT_TYPE = "record found for the saga id and event type combination, might be a duplicate or replay," +
          " just updating the db status so that it will be polled and sent back again.";
  @Getter(PRIVATE)
  private final IndependentAuthorityRepository independentAuthorityRepository;

  @Getter(PRIVATE)
  private final InstituteEventRepository instituteEventRepository;

  @Getter(AccessLevel.PRIVATE)
  private final SchoolService schoolService;

  private final SchoolSearchService schoolSearchService;

  private static final SchoolMapper schoolMapper = SchoolMapper.mapper;

  private final ObjectMapper obMapper = new ObjectMapper();

  private static final IndependentAuthorityMapper independentAuthorityMapper = IndependentAuthorityMapper.mapper;

  @Autowired
  public EventHandlerService(IndependentAuthorityRepository independentAuthorityRepository, InstituteEventRepository instituteEventRepository, SchoolService schoolService, SchoolSearchService schoolSearchService){
    this.independentAuthorityRepository = independentAuthorityRepository;
    this.instituteEventRepository = instituteEventRepository;
    this.schoolService = schoolService;
    this.schoolSearchService = schoolSearchService;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public byte[] handleGetAuthorityEvent(Event event, boolean isSynchronous) throws JsonProcessingException {
    UUID authorityID = obMapper.readValue(event.getEventPayload(), new TypeReference<>() { });
    if (isSynchronous) {
      val optionalAuthorityEntity = independentAuthorityRepository.findById(authorityID);
      if (optionalAuthorityEntity.isPresent()) {
        return JsonUtil.getJsonBytesFromObject(independentAuthorityMapper.toStructure(optionalAuthorityEntity.get()));
      } else {
        return new byte[0];
      }
    }

    log.trace(EVENT_PAYLOAD, event);
    val optionalAuthorityEntity = independentAuthorityRepository.findById(authorityID);
    if (optionalAuthorityEntity.isPresent()) {
      var authority = independentAuthorityMapper.toStructure(optionalAuthorityEntity.get()); // need to convert to structure MANDATORY otherwise jackson will break.
      event.setEventPayload(JsonUtil.getJsonStringFromObject(authority));
      event.setEventOutcome(EventOutcome.AUTHORITY_FOUND);
    } else {
      event.setEventOutcome(EventOutcome.AUTHORITY_NOT_FOUND);
    }
    val authorityEvent = createInstituteEventRecord(event);
    log.info("Event outcome for saga ID :: {}, is :: {}", event.getSagaId(), event.getEventOutcome());
    return createResponseEvent(authorityEvent);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public CompletableFuture<byte[]> handleGetPaginatedSchools(Event event) {
    String sortCriteriaJson = null;
    String searchCriteriaListJson = null;
    var pageNumber = 0;
    var pageSize = 100000;
    var params = event.getEventPayload().split("&");
    for (String param : params) {
      if (param != null) {
        var keyValPair = param.split("=");
        if (SEARCH_CRITERIA_LIST.equalsIgnoreCase(keyValPair[0])) {
          searchCriteriaListJson = URLDecoder.decode(keyValPair[1], StandardCharsets.UTF_8);
        } else if (PAGE_SIZE.equalsIgnoreCase(keyValPair[0])) {
          pageSize = Integer.parseInt(keyValPair[1]);
        } else if (PAGE_NUMBER.equalsIgnoreCase(keyValPair[0])) {
          pageNumber = Integer.parseInt(keyValPair[1]);
        } else if (SORT_CRITERIA.equalsIgnoreCase(keyValPair[0])) {
          sortCriteriaJson = keyValPair[1];
        }
      }
    }

    final List<Sort.Order> sorts = new ArrayList<>();
    Specification<SchoolEntity> schoolSpecs = schoolSearchService.setSpecificationAndSortCriteria(sortCriteriaJson, searchCriteriaListJson, obMapper, sorts);
    log.trace("Running query for paginated schools: {}", schoolSpecs);
    return schoolSearchService
      .findAll(schoolSpecs, pageNumber, pageSize, sorts)
      .thenApplyAsync(schoolEntities -> {
        log.trace("Performing paginated school mapping: {}", schoolSpecs);
        var schoolMap = schoolEntities.map(schoolMapper::toStructure);
        log.trace("Mapping complete");
        return schoolMap;
      })
      .thenApplyAsync(schoolEntities -> {
        try {
          log.trace("Found {} schools for {}", schoolEntities.getContent().size(), event.getSagaId());
          val resBytes = obMapper.writeValueAsBytes(schoolEntities.getContent());
          log.trace("Response prepared for {}, response length {}", event.getSagaId(), resBytes.length);
          return resBytes;
        } catch (JsonProcessingException e) {
          log.error("Error during get paginated schools :: {} {}", event, e);
        }
        log.trace("Found no schools for paginated query with saga {}", event.getSagaId());
        return new byte[0];
      });

  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Pair<byte[], InstituteEvent> handleCreateSchoolEvent(Event event) throws JsonProcessingException {
    Optional<InstituteEvent> instituteEventOptional = getInstituteEventRepository().findBySagaIdAndEventType(event.getSagaId(), event.getEventType().toString());
    InstituteEvent schoolEvent;
    InstituteEvent choreographyEvent = null;
    if (instituteEventOptional.isEmpty()) {
      log.info(NO_RECORD_SAGA_ID_EVENT_TYPE);
      log.trace(EVENT_PAYLOAD, event);
      School school = JsonUtil.getJsonObjectFromString(School.class, event.getEventPayload());
      RequestUtil.setAuditColumnsForCreate(school);
      Pair<SchoolEntity, InstituteEvent> schoolPair = getSchoolService().createSchool(school);
      choreographyEvent = schoolPair.getRight();
      event.setEventOutcome(EventOutcome.SCHOOL_CREATED);
      event.setEventPayload(JsonUtil.getJsonStringFromObject(schoolMapper.toStructure(schoolPair.getLeft())));
      schoolEvent = createInstituteEventRecord(event);
    } else {
      log.info(RECORD_FOUND_FOR_SAGA_ID_EVENT_TYPE);
      log.trace(EVENT_PAYLOAD, event);
      schoolEvent = instituteEventOptional.get();
      schoolEvent.setUpdateDate(LocalDateTime.now());
    }

    getInstituteEventRepository().save(schoolEvent);
    return Pair.of(createResponseEvent(schoolEvent), choreographyEvent);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Pair<byte[], InstituteEvent> handleUpdateSchoolEvent(Event event) throws JsonProcessingException {
    InstituteEvent choreographyEvent = null;
    log.trace(EVENT_PAYLOAD, event);
    School school = JsonUtil.getJsonObjectFromString(School.class, event.getEventPayload());
    RequestUtil.setAuditColumnsForCreate(school);
    try {
      Pair<SchoolEntity, InstituteEvent> schoolPair = getSchoolService().updateSchool(school, UUID.fromString(school.getSchoolId()));
      choreographyEvent = schoolPair.getRight();
      event.setEventOutcome(EventOutcome.SCHOOL_UPDATED);
      event.setEventPayload(JsonUtil.getJsonStringFromObject(schoolMapper.toStructure(schoolPair.getLeft())));
    } catch (EntityNotFoundException ex) {
      event.setEventOutcome(EventOutcome.SCHOOL_NOT_FOUND);
    }

    InstituteEvent schoolEvent = createInstituteEventRecord(event);
    getInstituteEventRepository().save(schoolEvent);
    return Pair.of(createResponseEvent(schoolEvent), choreographyEvent);
  }

  private InstituteEvent createInstituteEventRecord(Event event) {
    return InstituteEvent.builder()
        .createDate(LocalDateTime.now())
        .updateDate(LocalDateTime.now())
        .createUser(event.getEventType().toString()) //need to discuss what to put here.
        .updateUser(event.getEventType().toString())
        .eventPayload(event.getEventPayload())
        .eventType(event.getEventType().toString())
        .sagaId(event.getSagaId())
        .eventStatus(MESSAGE_PUBLISHED.toString())
        .eventOutcome(event.getEventOutcome().toString())
        .replyChannel(event.getReplyTo())
        .build();
  }

  private byte[] createResponseEvent(InstituteEvent event) throws JsonProcessingException {
    val responseEvent = Event.builder()
        .sagaId(event.getSagaId())
        .eventType(EventType.valueOf(event.getEventType()))
        .eventOutcome(EventOutcome.valueOf(event.getEventOutcome()))
        .eventPayload(event.getEventPayload()).build();
    return JsonUtil.getJsonBytesFromObject(responseEvent);
  }

}
