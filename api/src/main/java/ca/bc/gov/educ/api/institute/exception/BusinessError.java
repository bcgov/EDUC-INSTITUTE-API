package ca.bc.gov.educ.api.institute.exception;

import lombok.Getter;

/**
 * The enum Business error.
 */
public enum BusinessError {
  /**
   * The Event already persisted.
   */
  EVENT_ALREADY_PERSISTED("Event with event id :: $? , is already persisted in DB, a duplicate message from Jet Stream.");

  @Getter
  private final String code;

  BusinessError(final String code) {
    this.code = code;

  }
}
