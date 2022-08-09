package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * The type Student.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address extends BaseRequest implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private String addressId;

  private String schoolId;

  private String districtId;

  private String independentAuthorityId;

  @Size(max = 10)
  private String phoneNumber;

  @Size(max = 255)
  @Email(message = "Email address should be a valid email address")
  private String email;

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
