package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "DISTRICT_CONTACT_TYPE_CODE")
public class DistrictContactTypeCodeEntity {
  @Id
  @Column(name = "district_contact_type_code", unique = true, updatable = false)
  private String districtContactTypeCode;
  @Basic
  @Column(name = "label")
  private String label;
  @Basic
  @Column(name = "description")
  private String description;
  @Basic
  @Column(name = "display_order")
  private Integer displayOrder;
  @Basic
  @Column(name = "legacy_code")
  @UpperCase
  private String legacyCode;
  @Basic
  @Column(name = "effective_date")
  private LocalDateTime effectiveDate;
  @Basic
  @Column(name = "expiry_date")
  private LocalDateTime expiryDate;
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
