package ca.bc.gov.educ.api.institute.model.v1;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "DISTRICT_ADDRESS_HISTORY")
public class DistrictAddressHistoryEntity extends BaseAddressEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "district_address_history_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID districtAddressHistoryId;
  @Basic
  @Column(name = "district_id", columnDefinition = "BINARY(16)")
  private UUID districtId;
  @ManyToOne(optional = false, targetEntity = DistrictHistoryEntity.class)
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  @JoinColumn(name = "district_history_id", referencedColumnName = "district_history_id")
  DistrictHistoryEntity districtHistoryEntity;
}
