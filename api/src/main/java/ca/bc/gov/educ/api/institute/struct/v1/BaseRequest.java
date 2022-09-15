package ca.bc.gov.educ.api.institute.struct.v1;

import lombok.Data;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * The type Base request.
 */
@Data
public abstract class BaseRequest {
  /**
   * The Create user.
   */
  @Size(max = 32)
  public String createUser;
  /**
   * The Update user.
   */
  @Size(max = 32)
  public String updateUser;
  /**
   * The Create date.
   */
  @Null(message = "createDate should be null.")
  public String createDate;
  /**
   * The Update date.
   */
  @Null(message = "updateDate should be null.")
  public String updateDate;
}
