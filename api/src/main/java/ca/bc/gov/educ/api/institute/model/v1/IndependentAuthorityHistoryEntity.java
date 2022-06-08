package ca.bc.gov.educ.api.institute.model.v1;

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
@Table(name = "INDEPENDENT_AUTHORITY_HISTORY")
public class IndependentAuthorityHistoryEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "independent_authority_history_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID independentAuthorityHistoryId;
  @Basic
  @Column(name = "independent_authority_id", columnDefinition = "BINARY(16)")
  private UUID independentAuthorityId;
  @Basic
  @Column(name = "authority_number")
  private String authorityNumber;
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
  @Column(name = "display_name")
  private String displayName;
  @Basic
  @Column(name = "asset_number")
  private String assetNumber;
  @Basic
  @Column(name = "authority_group_code")
  private String authorityGroupCode;
  @Basic
  @Column(name = "authority_type_code")
  private String authorityTypeCode;
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
}
