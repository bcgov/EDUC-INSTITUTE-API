package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.ComparableField;
import ca.bc.gov.educ.api.institute.util.UpperCase;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class BaseAddressEntity {
  @Basic
  @Column(name = "address_line_1")
  @ComparableField
  private String addressLine1;
  @Basic
  @Column(name = "address_line_2")
  @ComparableField
  private String addressLine2;
  @Basic
  @Column(name = "city")
  @ComparableField
  private String city;
  @Basic
  @Column(name = "postal")
  @UpperCase
  @ComparableField
  private String postal;
  @Basic
  @Column(name = "province_code")
  @UpperCase
  @ComparableField
  private String provinceCode;
  @Basic
  @Column(name = "country_code")
  @UpperCase
  @ComparableField
  private String countryCode;
  @Basic
  @Column(name = "address_type_code")
  @UpperCase
  @ComparableField
  private String addressTypeCode;
  @Column(name = "CREATE_USER", updatable = false)
  private String createUser;
  @PastOrPresent
  @Column(name = "CREATE_DATE", updatable = false)
  private LocalDateTime createDate;
  @Column(name = "update_user")
  private String updateUser;
  @PastOrPresent
  @Column(name = "update_date")
  private LocalDateTime updateDate;

}
