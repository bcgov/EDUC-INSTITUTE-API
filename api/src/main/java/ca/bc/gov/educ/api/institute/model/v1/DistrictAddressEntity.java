package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.ComparableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(name = "DISTRICT_ADDRESS")
public class DistrictAddressEntity extends BaseAddressEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "district_address_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  @ComparableField
  private UUID districtAddressId;

  @ManyToOne(optional = true, targetEntity = DistrictEntity.class)
  @JoinColumn(name = "district_id", referencedColumnName = "district_id")
  DistrictEntity districtEntity;

}
