package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "CONTACT")
public class ContactEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "contact_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID contactId;

  @ManyToOne(optional = true, targetEntity = SchoolEntity.class)
  @JoinColumn(name = "school_id", referencedColumnName = "school_id")
  SchoolEntity schoolEntity;

  @ManyToOne(optional = true, targetEntity = DistrictEntity.class)
  @JoinColumn(name = "district_id", referencedColumnName = "district_id")
  @JsonIgnoreProperties("contacts")
  DistrictEntity districtEntity;

  @ManyToOne(optional = true, targetEntity = IndependentAuthorityEntity.class)
  @JoinColumn(name = "independent_authority_id", referencedColumnName = "independent_authority_id")
  IndependentAuthorityEntity independentAuthorityEntity;

  @Basic
  @Column(name = "first_name")
  @UpperCase
  private String firstName;
  @Basic
  @Column(name = "last_name")
  @UpperCase
  private String lastName;
  @Basic
  @Column(name = "phone_number")
  private String phoneNumber;
  @Basic
  @Column(name = "email")
  private String email;
  @Basic
  @Column(name = "contact_type_code")
  @UpperCase
  private String contactTypeCode;
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
