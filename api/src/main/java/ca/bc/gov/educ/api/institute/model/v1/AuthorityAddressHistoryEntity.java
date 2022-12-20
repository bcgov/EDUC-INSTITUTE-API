package ca.bc.gov.educ.api.institute.model.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "INDEPENDENT_AUTHORITY_ADDRESS_HISTORY")
public class AuthorityAddressHistoryEntity extends BaseAddressEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "independent_authority_address_history_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID independentAuthorityAddressHistoryId;
  @Basic
  @Column(name = "independent_authority_id", columnDefinition = "BINARY(16)")
  private UUID independentAuthorityId;
  @ManyToOne(optional = false, targetEntity = IndependentAuthorityHistoryEntity.class)
  @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
  @JoinColumn(name = "independent_authority_history_id", referencedColumnName = "independent_authority_history_id")
  IndependentAuthorityHistoryEntity independentAuthorityHistoryEntity;
  @Basic
  @Column(name = "independent_authority_address_id", columnDefinition = "BINARY(16)")
  private UUID independentAuthorityAddressId;
}