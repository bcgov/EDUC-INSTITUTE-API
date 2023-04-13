package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolMove extends BaseRequest implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private String schoolMoveId;

  @NotNull(message = "toSchoolId cannot be null.")
  private String toSchoolId;

  @NotNull(message = "fromSchoolId cannot be null.")
  private String fromSchoolId;

  @NotNull(message = "moveDate cannot be null.")
  private String moveDate;

}