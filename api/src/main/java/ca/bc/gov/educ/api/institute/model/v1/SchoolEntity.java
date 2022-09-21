package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import ca.bc.gov.educ.api.institute.util.UpperCaseSearch;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "school_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID schoolId;
  @ManyToOne(optional = true,  targetEntity = DistrictEntity.class)
  @JoinColumn(name = "district_id", referencedColumnName = "district_id")
  DistrictEntity districtEntity;
  @Basic
  @Column(name = "independent_authority_id", columnDefinition = "BINARY(16)")
  private UUID independentAuthorityId;
  @Basic
  @Column(name = "district_id", insertable=false, columnDefinition = "BINARY(16)", updatable = false)
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
  @ColumnTransformer(read = "UPPER(email)")
  @UpperCaseSearch
  private String email;
  @Basic
  @Column(name = "website")
  @ColumnTransformer(read = "UPPER(website)")
  @UpperCaseSearch
  private String website;
  @Basic
  @Column(name = "display_name")
  @ColumnTransformer(read = "UPPER(display_name)")
  @UpperCaseSearch
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

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "schoolEntity", fetch = FetchType.EAGER, cascade = CascadeType.DETACH, targetEntity = SchoolContactEntity.class)
  private Set<SchoolContactEntity> contacts;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "schoolEntity", fetch = FetchType.EAGER, cascade = CascadeType.DETACH, targetEntity = AddressEntity.class)
  private Set<AddressEntity> addresses;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "schoolEntity", fetch = FetchType.EAGER, cascade = CascadeType.DETACH, targetEntity = NoteEntity.class)
  private Set<NoteEntity> notes;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "schoolEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = SchoolGradeEntity.class)
  private Set<SchoolGradeEntity> grades;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "schoolEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = NeighborhoodLearningEntity.class)
  private Set<NeighborhoodLearningEntity> neighborhoodLearning;

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
}
