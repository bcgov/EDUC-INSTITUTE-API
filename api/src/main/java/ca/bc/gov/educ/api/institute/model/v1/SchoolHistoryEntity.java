package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SCHOOL_HISTORY")
public class SchoolHistoryEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "school_history_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID schoolHistoryId;

  @Basic
  @Column(name = "school_id", columnDefinition = "BINARY(16)")
  private UUID schoolId;

  @Basic
  @Column(name = "district_id", columnDefinition = "BINARY(16)")
  private UUID districtID;

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
  @Column(name = "display_name_no_spec_chars")
  private String displayNameNoSpecialChars;

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
  @Column(name = "school_reporting_requirement_code")
  @UpperCase
  private String schoolReportingRequirementCode;

  @Basic
  @Column(name = "opened_date")
  private LocalDateTime openedDate;

  @Basic
  @Column(name = "closed_date")
  private LocalDateTime closedDate;

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

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "schoolHistoryEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = SchoolGradeSchoolHistoryEntity.class)
  private Set<SchoolGradeSchoolHistoryEntity> schoolGrades;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "schoolHistoryEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = NeighbourhoodLearningSchoolHistoryEntity.class)
  private Set<NeighbourhoodLearningSchoolHistoryEntity> neighbourhoodLearnings;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "schoolHistoryEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = SchoolAddressHistoryEntity.class)
  private Set<SchoolAddressHistoryEntity> addresses;

  public Set<SchoolGradeSchoolHistoryEntity> getSchoolGrades() {
    if (this.schoolGrades == null) {
      this.schoolGrades = new HashSet<>();
    }
    return this.schoolGrades;
  }

  public Set<NeighbourhoodLearningSchoolHistoryEntity> getNeighbourhoodLearnings() {
    if (this.neighbourhoodLearnings == null) {
      this.neighbourhoodLearnings = new HashSet<>();
    }
    return this.neighbourhoodLearnings;
  }

  public Set<SchoolAddressHistoryEntity> getAddresses() {
    if(this.addresses== null){
      this.addresses = new HashSet<>();
    }
    return this.addresses;
  }
}
