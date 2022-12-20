package ca.bc.gov.educ.api.institute.struct.v1;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The type Base address.
 */
@Data
public abstract class BaseAddress extends BaseRequest {

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
