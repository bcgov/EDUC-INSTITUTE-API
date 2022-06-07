package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Student.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndependentAuthorityHistory extends BaseRequest implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private String independentAuthorityHistoryId;

  private String independentAuthorityId;

  @Size(max = 3)
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
  @NotNull(message = "authorityGroupCode cannot be null")
  private String authorityGroupCode;

  @Size(max = 10)
  @NotNull(message = "authorityTypeCode cannot be null")
  private String authorityTypeCode;

  @Size(max = 10)
  @NotNull(message = "openedDate cannot be null")
  private LocalDateTime openedDate;

  @Size(max = 10)
  private LocalDateTime closedDate;

  private List<Contact> contacts;

  private List<Address> addresses;

  private List<Address> notes;
}
