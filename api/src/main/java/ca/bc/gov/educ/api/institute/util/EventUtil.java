package ca.bc.gov.educ.api.institute.util;

import ca.bc.gov.educ.api.institute.constants.v1.EventOutcome;
import ca.bc.gov.educ.api.institute.constants.v1.EventType;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;

import java.time.LocalDateTime;

import static ca.bc.gov.educ.api.institute.constants.v1.EventStatus.DB_COMMITTED;

public class EventUtil {
  private EventUtil() {
  }

  public static InstituteEvent createInstituteEvent(String createUser, String updateUser, String jsonString, EventType eventType, EventOutcome eventOutcome) {
    return InstituteEvent.builder()
      .createDate(LocalDateTime.now())
      .updateDate(LocalDateTime.now())
      .createUser(createUser)
      .updateUser(updateUser)
      .eventPayload(jsonString)
      .eventType(eventType.toString())
      .eventStatus(DB_COMMITTED.toString())
      .eventOutcome(eventOutcome.toString())
      .build();
  }
}
