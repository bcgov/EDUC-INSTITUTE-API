package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * The type Student.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndependentAuthorityHistory extends BaseRequest implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private String independentAuthorityHistoryId;

  private String independentAuthorityId;

  @Size(max = 4)
  @NotNull(message = "authorityNumber can not be null.")
  private String authorityNumber;

  @Size(max = 10)
  private String faxNumber;

  @Size(max = 10)
  private String phoneNumber;

  @Size(max = 255)
  @Email(message = "Email address should be a valid email address")
  private String email;

  @Size(max = 255)
  @NotNull(message = "displayName cannot be null")
  private String displayName;

  @Size(max = 10)
  @NotNull(message = "authorityTypeCode cannot be null")
  private String authorityTypeCode;

  @NotNull(message = "openedDate cannot be null")
  private String openedDate;

  private String closedDate;

  @Valid
  private List<AuthorityAddressHistory> addresses;
}
