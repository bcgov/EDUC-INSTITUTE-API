package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "DISTRICT")
public class DistrictEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "district_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID districtId;
  @Basic
  @Column(name = "district_number")
  private String districtNumber;
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
  @Column(name = "district_region_code")
  @UpperCase
  private String districtRegionCode;
  @Basic
  @Column(name = "district_status_code")
  @UpperCase
  private String districtStatusCode;
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
  @OneToMany(mappedBy = "districtEntity", fetch = FetchType.EAGER, cascade = CascadeType.DETACH, targetEntity = DistrictContactEntity.class)
  @JsonIgnoreProperties("districtEntity")
  private Set<DistrictContactEntity> contacts;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "districtEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = DistrictAddressEntity.class)
  private Set<DistrictAddressEntity> addresses;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "districtID", fetch = FetchType.EAGER, cascade = CascadeType.DETACH, targetEntity = NoteEntity.class)
  private Set<NoteEntity> notes;

  public Set<DistrictAddressEntity> getAddresses() {
    if(this.addresses== null){
      this.addresses = new HashSet<>();
    }
    return this.addresses;
  }
}
