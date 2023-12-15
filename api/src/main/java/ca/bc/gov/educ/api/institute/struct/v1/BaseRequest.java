package ca.bc.gov.educ.api.institute.struct.v1;

import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * The type Base request.
 */
@Data
public abstract class BaseRequest {
  /**
   * The Create user.
   */
  @Size(max = 100)
  public String createUser;
  /**
   * The Update user.
   */
  @Size(max = 100)
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
