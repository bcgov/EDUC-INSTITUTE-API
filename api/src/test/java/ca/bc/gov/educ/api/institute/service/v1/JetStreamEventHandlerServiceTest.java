package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.constants.v1.EventType;
import ca.bc.gov.educ.api.institute.repository.v1.InstituteEventRepository;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import ca.bc.gov.educ.api.institute.struct.v1.ChoreographedEvent;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static ca.bc.gov.educ.api.institute.constants.v1.EventOutcome.SCHOOL_UPDATED;
import static ca.bc.gov.educ.api.institute.constants.v1.EventStatus.DB_COMMITTED;
import static ca.bc.gov.educ.api.institute.constants.v1.EventStatus.MESSAGE_PUBLISHED;
import static ca.bc.gov.educ.api.institute.constants.v1.EventType.UPDATE_SCHOOL;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class JetStreamEventHandlerServiceTest {

  @Autowired
  JetStreamEventHandlerService jetStreamEventHandlerService;

  @Autowired
  InstituteEventRepository instituteEventRepository;

  @After
  public void tearDown() {
    instituteEventRepository.deleteAll();
  }

  @Test
  public void testUpdateEventStatus_givenNoDataInDB_shouldDONothing() throws JsonProcessingException {
    ChoreographedEvent choreographedEvent = new ChoreographedEvent();
    choreographedEvent.setEventID(UUID.randomUUID().toString());
    choreographedEvent.setEventOutcome(SCHOOL_UPDATED);
    choreographedEvent.setEventType(UPDATE_SCHOOL);
    choreographedEvent.setEventPayload(JsonUtil.getJsonStringFromObject(new School()));
    jetStreamEventHandlerService.updateEventStatus(choreographedEvent);
    var results = instituteEventRepository.findByEventStatusAndEventTypeNotIn(MESSAGE_PUBLISHED.toString(), Arrays.asList(EventType.UPDATE_GRAD_SCHOOL.toString()));
    assertThat(results).isEmpty();
  }

  @Test
  public void testUpdateEventStatus_givenEventIdNull_shouldDONothing() throws JsonProcessingException {
    ChoreographedEvent choreographedEvent = new ChoreographedEvent();
    choreographedEvent.setEventOutcome(SCHOOL_UPDATED);
    choreographedEvent.setEventType(UPDATE_SCHOOL);
    choreographedEvent.setEventPayload(JsonUtil.getJsonStringFromObject(new School()));
    jetStreamEventHandlerService.updateEventStatus(choreographedEvent);
    var results = instituteEventRepository.findByEventStatusAndEventTypeNotIn(MESSAGE_PUBLISHED.toString(), Arrays.asList(EventType.UPDATE_GRAD_SCHOOL.toString()));
    assertThat(results).isEmpty();
  }

  @Test
  public void testUpdateEventStatus_givenChoreographedEventNull_shouldDONothing() {
    jetStreamEventHandlerService.updateEventStatus(null);
    var results = instituteEventRepository.findByEventStatusAndEventTypeNotIn(MESSAGE_PUBLISHED.toString(),Arrays.asList(EventType.UPDATE_GRAD_SCHOOL.toString()));
    assertThat(results).isEmpty();
  }

  @Test
  public void testUpdateEventStatus_givenDataInDB_shouldUpdateStatus() throws JsonProcessingException {
    var studentEvent = instituteEventRepository.save(createStudentEvent());
    ChoreographedEvent choreographedEvent = new ChoreographedEvent();
    choreographedEvent.setEventID(studentEvent.getEventId().toString());
    choreographedEvent.setEventOutcome(SCHOOL_UPDATED);
    choreographedEvent.setEventType(UPDATE_SCHOOL);
    choreographedEvent.setEventPayload(JsonUtil.getJsonStringFromObject(new School()));
    jetStreamEventHandlerService.updateEventStatus(choreographedEvent);
    var results = instituteEventRepository.findByEventStatusAndEventTypeNotIn(MESSAGE_PUBLISHED.toString(), Arrays.asList(EventType.UPDATE_GRAD_SCHOOL.toString()));
    assertThat(results).hasSize(1);
    assertThat(results.get(0)).isNotNull();
  }

  private InstituteEvent createStudentEvent() throws JsonProcessingException {
    return InstituteEvent.builder()
        .eventId(UUID.randomUUID())
        .createDate(LocalDateTime.now())
        .createUser("TEST")
        .eventOutcome(SCHOOL_UPDATED.toString())
        .eventStatus(DB_COMMITTED.toString())
        .eventType(UPDATE_SCHOOL.toString())
        .eventPayload(JsonUtil.getJsonStringFromObject(new School()))
        .updateDate(LocalDateTime.now())
        .updateUser("TEST")
        .build();
  }
}
