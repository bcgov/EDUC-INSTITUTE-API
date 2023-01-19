package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.constants.v1.EventOutcome;
import ca.bc.gov.educ.api.institute.constants.v1.EventType;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityRepository;
import ca.bc.gov.educ.api.institute.struct.v1.Event;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

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
   * The constant NO_RECORD_SAGA_ID_EVENT_TYPE.
   */
  public static final String NO_RECORD_SAGA_ID_EVENT_TYPE = "no record found for the saga id and event type combination, processing.";
  /**
   * The constant RECORD_FOUND_FOR_SAGA_ID_EVENT_TYPE.
   */
  public static final String RECORD_FOUND_FOR_SAGA_ID_EVENT_TYPE = "record found for the saga id and event type combination, might be a duplicate or replay," +
      " just updating the db status so that it will be polled and sent back again.";
  /**
   * The constant PAYLOAD_LOG.
   */
  public static final String PAYLOAD_LOG = "payload is :: {}";
  /**
   * The constant EVENT_PAYLOAD.
   */
  public static final String EVENT_PAYLOAD = "event is :: {}";
  @Getter(PRIVATE)
  private final IndependentAuthorityRepository independentAuthorityRepository;

  private static final IndependentAuthorityMapper independentAuthorityMapper = IndependentAuthorityMapper.mapper;

  @Autowired
  public EventHandlerService(IndependentAuthorityRepository independentAuthorityRepository){
    this.independentAuthorityRepository = independentAuthorityRepository;
  }

  /**
   * Saga should never be null for this type of event.
   * this method expects that the event payload contains a pen number.
   *
   * @param event         containing the student PEN.
   * @param isSynchronous the is synchronous
   * @return the byte [ ]
   * @throws JsonProcessingException the json processing exception
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public byte[] handleGetAuthorityEvent(Event event, boolean isSynchronous) throws JsonProcessingException {
    if (isSynchronous) {
      val optionalAuthorityEntity = independentAuthorityRepository.findById(UUID.fromString(event.getEventPayload()));
      if (optionalAuthorityEntity.isPresent()) {
        return JsonUtil.getJsonBytesFromObject(independentAuthorityMapper.toStructure(optionalAuthorityEntity.get()));
      } else {
        return new byte[0];
      }
    }

    log.trace(EVENT_PAYLOAD, event);
    val optionalAuthorityEntity = independentAuthorityRepository.findById(UUID.fromString(event.getEventPayload()));
    if (optionalAuthorityEntity.isPresent()) {
      var authority = independentAuthorityMapper.toStructure(optionalAuthorityEntity.get()); // need to convert to structure MANDATORY otherwise jackson will break.
      event.setEventPayload(JsonUtil.getJsonStringFromObject(authority));
      event.setEventOutcome(EventOutcome.AUTHORITY_FOUND);
    } else {
      event.setEventOutcome(EventOutcome.AUTHORITY_NOT_FOUND);
    }
    val authorityEvent = createInstituteEventRecord(event);
    return createResponseEvent(authorityEvent);
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
