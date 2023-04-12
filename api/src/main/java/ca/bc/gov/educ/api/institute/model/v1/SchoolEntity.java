package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SCHOOL")
public class SchoolEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(
      name = "uuid_gen_strategy_class",
      value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
    )
  })

  @Column(name = "school_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID schoolId;

  @ManyToOne(optional = true, targetEntity = DistrictTombstoneEntity.class)
  @JoinColumn(name = "district_id", referencedColumnName = "district_id")
  DistrictTombstoneEntity districtEntity;
  @Basic
  @Column(name = "independent_authority_id", columnDefinition = "BINARY(16)")
  private UUID independentAuthorityId;

  @Basic
  @Column(
    name = "district_id",
    insertable = false,
    columnDefinition = "BINARY(16)",
    updatable = false
  )
  private UUID districtID;

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
  @Column(name = "school_reporting_requirement_code")
  @UpperCase
  private String schoolReportingRequirementCode;

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

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(
    mappedBy = "schoolEntity",
    fetch = FetchType.EAGER,
    cascade = CascadeType.DETACH,
    targetEntity = SchoolContactEntity.class
  )
  private Set<SchoolContactEntity> contacts;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(
    mappedBy = "schoolEntity",
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    targetEntity = SchoolAddressEntity.class
  )
  private Set<SchoolAddressEntity> addresses;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(
    mappedBy = "schoolID",
    fetch = FetchType.EAGER,
    cascade = CascadeType.DETACH,
    targetEntity = NoteEntity.class
  )
  private Set<NoteEntity> notes;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(
    mappedBy = "schoolEntity",
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    targetEntity = SchoolGradeEntity.class
  )
  private Set<SchoolGradeEntity> grades;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(
    mappedBy = "schoolEntity",
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    targetEntity = NeighborhoodLearningEntity.class
  )
  private Set<NeighborhoodLearningEntity> neighborhoodLearning;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "fromSchoolId",
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL,
      targetEntity = SchoolMoveHistoryEntity.class
  )
  private Set<SchoolMoveHistoryEntity> schoolMoveToHistory;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(
      mappedBy = "toSchoolId",
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL,
      targetEntity = SchoolMoveHistoryEntity.class
  )
  private Set<SchoolMoveHistoryEntity> schoolMoveFromHistory;

  public Set<NeighborhoodLearningEntity> getNeighborhoodLearning() {
    if (this.neighborhoodLearning == null) {
      this.neighborhoodLearning = new HashSet<>();
    }
    return this.neighborhoodLearning;
  }

  public Set<SchoolGradeEntity> getGrades() {
    if (this.grades == null) {
      this.grades = new HashSet<>();
    }
    return this.grades;
  }

  public Set<SchoolAddressEntity> getAddresses() {
    if (this.addresses == null) {
      this.addresses = new HashSet<>();
    }
    return this.addresses;
  }

  public Set<SchoolMoveHistoryEntity> getSchoolMoveFromHistory() {
    if (this.schoolMoveFromHistory == null) {
      this.schoolMoveFromHistory = new HashSet<>();
    }
    return this.schoolMoveFromHistory;
  }

  public Set<SchoolMoveHistoryEntity> getSchoolMoveToHistory() {
    if (this.schoolMoveToHistory == null) {
      this.schoolMoveToHistory = new HashSet<>();
    }
    return this.schoolMoveToHistory;
  }
}
