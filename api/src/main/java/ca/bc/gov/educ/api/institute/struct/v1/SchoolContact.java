package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Student.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolContact extends BaseRequest implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private String schoolContactId;

  private String schoolId;

  @Size(max = 10)
  @NotNull(message = "schoolContactTypeCode cannot be null")
  private String schoolContactTypeCode;

  @Size(max = 10)
  @Pattern(regexp = "^$|\\d{10}", message = "Invalid phone number format")
  private String phoneNumber;

  private String jobTitle;

  @Size(max = 10)
  private String phoneExtension;

  @Size(max = 10)
  @Pattern(regexp = "^$|\\d{10}", message = "Invalid phone number format")
  private String alternatePhoneNumber;

  @Size(max = 10)
  private String alternatePhoneExtension;

  @Size(max = 255)
  @Email(message = "Email address should be a valid email address")
  private String email;

  @Size(max = 255)
  private String firstName;

  @Size(max = 255)
  @NotNull(message = "lastName cannot be null")
  private String lastName;

  private String effectiveDate;

  private String expiryDate;

}
