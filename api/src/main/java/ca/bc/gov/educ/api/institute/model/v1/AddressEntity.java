package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import lombok.*;
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
@Table(name = "ADDRESS")
public class AddressEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "address_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID addressId;

  @ManyToOne(optional = true, targetEntity = SchoolEntity.class)
  @JoinColumn(name = "school_id", referencedColumnName = "school_id")
  SchoolEntity schoolEntity;

  @ManyToOne(optional = true, targetEntity = DistrictEntity.class)
  @JoinColumn(name = "district_id", referencedColumnName = "district_id")
  DistrictEntity districtEntity;

  @ManyToOne(optional = true, targetEntity = IndependentAuthorityEntity.class)
  @JoinColumn(name = "independent_authority_id", referencedColumnName = "independent_authority_id")
  IndependentAuthorityEntity independentAuthorityEntity;

  @Basic
  @Column(name = "address_line_1")
  private String addressLine1;
  @Basic
  @Column(name = "address_line_2")
  private String addressLine2;
  @Basic
  @Column(name = "city")
  @UpperCase
  private String city;
  @Basic
  @Column(name = "postal")
  @UpperCase
  private String postal;
  @Basic
  @Column(name = "province_code")
  @UpperCase
  private String provinceCode;
  @Basic
  @Column(name = "country_code")
  @UpperCase
  private String countryCode;
  @Basic
  @Column(name = "address_type_code")
  @UpperCase
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
