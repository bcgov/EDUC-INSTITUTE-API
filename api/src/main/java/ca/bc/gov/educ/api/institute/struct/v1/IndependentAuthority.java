package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Student.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndependentAuthority extends BaseRequest implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private String independentAuthorityId;

  @Size(max = 4)
  private String authorityNumber;

  @Size(max = 10, min = 10)
  @Pattern(regexp = "\\d{10}", message = "Invalid phone number format")
  private String faxNumber;

  @Size(max = 10, min = 10)
  @Pattern(regexp = "\\d{10}", message = "Invalid phone number format")
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
  private List<AuthorityContact> contacts;

  @Valid
  private List<AuthorityAddress> addresses;

  @Valid
  private List<Note> notes;
}
