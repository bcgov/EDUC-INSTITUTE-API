package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * The type Student.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressHistory extends BaseRequest implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private String addressHistoryId;

  private String schoolId;

  private String districtId;

  private String independentAuthorityId;

  @Size(max = 255)
  @NotNull(message = "addressLine1 cannot be null")
  private String addressLine1;

  @Size(max = 255)
  private String addressLine2;

  @Size(max = 255)
  @NotNull(message = "city cannot be null")
  private String city;

  @Size(max = 255)
  @NotNull(message = "postal cannot be null")
  private String postal;

  @Size(max = 10)
  @NotNull(message = "addressTypeCode cannot be null")
  private String addressTypeCode;

  @Size(max = 10)
  @NotNull(message = "provinceCode cannot be null")
  private String provinceCode;

  @Size(max = 10)
  @NotNull(message = "countryCode cannot be null")
  private String countryCode;

}
