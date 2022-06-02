package ca.bc.gov.educ.api.institute.exception;

/**
 * The type School api runtime exception.
 */
public class InstituteAPIRuntimeException extends RuntimeException {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 5241655513745148898L;

  /**
   * Instantiates a new School api runtime exception.
   *
   * @param message the message
   */
  public InstituteAPIRuntimeException(String message) {
		super(message);
	}

  /**
   * Instantiates a new School api runtime exception.
   *
   * @param message the message
   * @param cause the cause of the exception
   */
  public InstituteAPIRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

}
