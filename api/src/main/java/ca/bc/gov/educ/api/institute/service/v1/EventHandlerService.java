package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.constants.v1.EventOutcome;
import ca.bc.gov.educ.api.institute.constants.v1.EventType;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityRepository;
import ca.bc.gov.educ.api.institute.struct.v1.Event;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
  @Getter(PRIVATE)
  private final IndependentAuthorityRepository independentAuthorityRepository;

  private final SchoolSearchService schoolSearchService;

  private static final SchoolMapper schoolMapper = SchoolMapper.mapper;

  private final ObjectMapper obMapper = new ObjectMapper();

  private static final IndependentAuthorityMapper independentAuthorityMapper = IndependentAuthorityMapper.mapper;

  @Autowired
  public EventHandlerService(IndependentAuthorityRepository independentAuthorityRepository, SchoolSearchService schoolSearchService){
    this.independentAuthorityRepository = independentAuthorityRepository;
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
    log.info("Running query for paginated schools: {}", schoolSpecs);
    return schoolSearchService
      .findAll(schoolSpecs, pageNumber, pageSize, sorts)
      .thenApplyAsync(schoolEntities -> schoolEntities.map(schoolMapper::toStructure))
      .thenApplyAsync(schoolEntities -> {
        try {
          log.info("Found {} schools for {}", schoolEntities.getContent().size(), event.getSagaId());
          val resBytes = obMapper.writeValueAsBytes(schoolEntities.getContent());
          log.info("Response prepared for {}, response length {}", event.getSagaId(), resBytes.length);
          return resBytes;
        } catch (JsonProcessingException e) {
          log.error("Error during get paginated schools :: {} {}", event, e);
        }
        log.info("Found no schools for paginated query with saga {}", event.getSagaId());
        return new byte[0];
      });

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
