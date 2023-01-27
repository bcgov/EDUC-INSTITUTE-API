package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SCHOOL")
public class SchoolTombstoneEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "school_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID schoolId;
  @ManyToOne(optional = true,  targetEntity = DistrictTombstoneEntity.class)
  @JoinColumn(name = "district_id", referencedColumnName = "district_id")
  DistrictTombstoneEntity districtEntity;
  @Basic
  @Column(name = "independent_authority_id", columnDefinition = "BINARY(16)")
  private UUID independentAuthorityId;
  @Basic
  @Column(name = "school_number")
  private String schoolNumber;
  @Basic
  @Column(name = "fax_number")
  private String faxNumber;
  @Basic
  @Column(name = "phone_number")
  private String phoneNumber;
  @Basic
  @Column(name = "email")
  private String email;
  @Basic
  @Column(name = "website")
  private String website;
  @Basic
  @Column(name = "display_name")
  private String displayName;
  @Basic
  @Column(name = "school_organization_code")
  @UpperCase
  private String schoolOrganizationCode;
  @Basic
  @Column(name = "school_category_code")
  @UpperCase
  private String schoolCategoryCode;
  @Basic
  @Column(name = "facility_type_code")
  @UpperCase
  private String facilityTypeCode;
  @Basic
  @Column(name = "opened_date")
  private LocalDateTime openedDate;
  @Basic
  @Column(name = "closed_date")
  private LocalDateTime closedDate;
  @Column(name = "CREATE_USER", updatable = false)
  String createUser;
  @PastOrPresent
  @Column(name = "CREATE_DATE", updatable = false)
  LocalDateTime createDate;
  @Column(name = "update_user")
  String updateUser;
  @PastOrPresent
  @Column(name = "update_date")
  LocalDateTime updateDate;

}
