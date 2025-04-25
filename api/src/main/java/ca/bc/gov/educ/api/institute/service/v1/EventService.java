package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;

/**
 * The interface Event service.
 *
 * @param <T> the type parameter
 */
public interface EventService<T> {

  /**
   * Process event.
   *
   * @param request the request
   * @param event   the event
   */
  void processEvent(T request, InstituteEvent event);

  /**
   * Gets event type.
   *
   * @return the event type
   */
  String getEventType();
}
