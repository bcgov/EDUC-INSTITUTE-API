package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import ca.bc.gov.educ.api.institute.util.UpperCaseSearch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "AUTHORITY_CONTACT")
public class AuthorityContactEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "authority_contact_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID authorityContactId;

  @ManyToOne(optional = true, targetEntity = IndependentAuthorityEntity.class)
  @JoinColumn(name = "independent_authority_id", referencedColumnName = "independent_authority_id")
  IndependentAuthorityEntity independentAuthorityEntity;

  @Basic
  @Column(name = "first_name")
  @ColumnTransformer(read = "UPPER(first_name)")
  @UpperCaseSearch
  private String firstName;
  @Basic
  @Column(name = "last_name")
  @ColumnTransformer(read = "UPPER(last_name)")
  @UpperCaseSearch
  private String lastName;
  @Basic
  @Column(name = "job_title")
  @ColumnTransformer(read = "UPPER(job_title)")
  @UpperCaseSearch
  private String jobTitle;
  @Basic
  @Column(name = "phone_number")
  private String phoneNumber;
  @Basic
  @Column(name = "phone_extension")
  private String phoneExtension;
  @Basic
  @Column(name = "alt_phone_number")
  private String alternatePhoneNumber;
  @Basic
  @Column(name = "alt_phone_extension")
  private String alternatePhoneExtension;
  @Basic
  @Column(name = "email")
  @ColumnTransformer(read = "UPPER(email)")
  @UpperCaseSearch
  private String email;
  @Column(name = "publicly_avail")
  private boolean publiclyAvailable;
  @Basic
  @Column(name = "authority_contact_type_code")
  @UpperCase
  private String authorityContactTypeCode;
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
