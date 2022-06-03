package ca.bc.gov.educ.api.institute.model.v1;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "SCHOOL")
public class SchoolEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  private UUID schoolId;
  @Basic
  @Column(name = "district_id")
  private Object districtId;
  @Basic
  @Column(name = "independent_authority_id")
  private Object independentAuthorityId;
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
  @Column(name = "asset_number")
  private String assetNumber;
  @Basic
  @Column(name = "school_organization_code")
  private String schoolOrganizationCode;
  @Basic
  @Column(name = "school_category_code")
  private String schoolCategoryCode;
  @Basic
  @Column(name = "facility_type_code")
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
