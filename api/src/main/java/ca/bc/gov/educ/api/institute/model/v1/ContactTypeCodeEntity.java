package ca.bc.gov.educ.api.institute.model.v1;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CONTACT_TYPE_CODE")
public class ContactTypeCodeEntity {
  @Id
  @Column(name = "contact_type_code", unique = true, updatable = false)
  private String contactTypeCode;
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
