package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.BusinessError;
import ca.bc.gov.educ.api.institute.exception.BusinessException;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import ca.bc.gov.educ.api.institute.repository.v1.InstituteEventRepository;
import ca.bc.gov.educ.api.institute.struct.v1.ChoreographedEvent;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static ca.bc.gov.educ.api.institute.constants.v1.EventStatus.DB_COMMITTED;


/**
 * The type Choreographed event persistence service.
 */
@Service
@Slf4j
public class ChoreographedEventPersistenceService {
  private final InstituteEventRepository eventRepository;

  /**
   * Instantiates a new Choreographed event persistence service.
   *
   * @param eventRepository the event repository
   */
  @Autowired
  public ChoreographedEventPersistenceService(final InstituteEventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public InstituteEvent persistEventToDB(final ChoreographedEvent choreographedEvent) throws BusinessException {
    final var eventOptional = this.eventRepository.findByEventId(UUID.fromString(choreographedEvent.getEventID()));
    if (eventOptional.isPresent()) {
      throw new BusinessException(BusinessError.EVENT_ALREADY_PERSISTED, choreographedEvent.getEventID().toString());
    }
    val event = InstituteEvent.builder()
      .eventType(choreographedEvent.getEventType().toString())
      .eventId(UUID.fromString(choreographedEvent.getEventID()))
      .eventOutcome(choreographedEvent.getEventOutcome().toString())
      .eventPayload(choreographedEvent.getEventPayload())
      .eventStatus(DB_COMMITTED.toString())
      .createUser(StringUtils.isBlank(choreographedEvent.getCreateUser()) ? "INSTITUTE-API" : choreographedEvent.getCreateUser())
      .updateUser(StringUtils.isBlank(choreographedEvent.getUpdateUser()) ? "INSTITUTE-API" : choreographedEvent.getUpdateUser())
      .createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now())
      .build();
    return this.eventRepository.save(event);
  }
}
