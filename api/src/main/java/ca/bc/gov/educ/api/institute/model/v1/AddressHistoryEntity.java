package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
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
@Table(name = "ADDRESS_HISTORY")
public class AddressHistoryEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "address_history_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID addressHistoryId;
  @Basic
  @Column(name = "school_id", columnDefinition = "BINARY(16)")
  private UUID schoolId;
  @Basic
  @Column(name = "district_id", columnDefinition = "BINARY(16)")
  private UUID districtId;
  @Basic
  @Column(name = "independent_authority_id", columnDefinition = "BINARY(16)")
  private UUID independentAuthorityId;
  @Basic
  @Column(name = "address_line_1")
  @UpperCase
  private String addressLine1;
  @Basic
  @Column(name = "address_line_2")
  @UpperCase
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
