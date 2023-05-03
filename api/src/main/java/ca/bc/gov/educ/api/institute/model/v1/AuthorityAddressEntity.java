package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.ComparableField;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@DynamicUpdate
@Table(name = "AUTHORITY_ADDRESS")
@EqualsAndHashCode(callSuper = true)
public class AuthorityAddressEntity extends BaseAddressEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "independent_authority_address_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  @ComparableField
  private UUID independentAuthorityAddressId;

  @ManyToOne(optional = true, targetEntity = IndependentAuthorityEntity.class)
  @JoinColumn(name = "independent_authority_id", referencedColumnName = "independent_authority_id")
  IndependentAuthorityEntity independentAuthorityEntity;

}
