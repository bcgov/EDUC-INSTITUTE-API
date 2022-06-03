package ca.bc.gov.educ.api.institute.model.v1;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "COUNTRY_CODE")
public class CountryCodeEntity {
  @Id
  @Column(name = "country_code", unique = true, updatable = false)
  private String countryCode;
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
