package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class School extends BaseRequest implements Serializable {
  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  private String schoolId;
  @NotNull(message = "districtId can not be null.")
  private String districtId;

  private String mincode;

  private String independentAuthorityId;

  @Size(max = 5)
  private String schoolNumber;

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
  @NotNull(message = "schoolOrganizationCode cannot be null")
  private String schoolOrganizationCode;

  @Size(max = 10)
  @NotNull(message = "schoolCategoryCode cannot be null")
  private String schoolCategoryCode;

  @Size(max = 10)
  @NotNull(message = "facilityTypeCode cannot be null")
  private String facilityTypeCode;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime openedDate;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime closedDate;

  @Valid
  private List<SchoolContact> contacts;

  @Valid
  private List<SchoolAddress> addresses;

  @Valid
  private List<Note> notes;

  @Valid
  private List<SchoolGrade> grades;

  @Valid
  private List<NeighborhoodLearning> neighborhoodLearning;

}
