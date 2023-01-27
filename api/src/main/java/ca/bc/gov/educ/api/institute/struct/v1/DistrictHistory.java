package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * The type Student.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistrictHistory extends BaseRequest implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private String districtHistoryId;

  private String districtId;

  @Size(max = 3)
  @NotNull(message = "districtNumber can not be null.")
  private String districtNumber;

  @Size(max = 10)
  private String faxNumber;

  @Size(max = 10)
  private String phoneNumber;

  @Size(max = 255)
  @Email(message = "Email address should be a valid email address")
  private String email;

  @Size(max = 255)
  private String website;

  @Size(max = 255)
  @NotNull(message = "displayName cannot be null")
  private String displayName;

  @Size(max = 10)
  @NotNull(message = "districtRegionCode cannot be null")
  private String districtRegionCode;

  @Size(max = 10)
  @NotNull(message = "districtStatusCode cannot be null")
  private String districtStatusCode;

  @Valid
  private List<DistrictAddressHistory> addresses;
}
