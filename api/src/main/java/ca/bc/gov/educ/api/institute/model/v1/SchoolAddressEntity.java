package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.ComparableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@DynamicUpdate
@EqualsAndHashCode(callSuper = true)
@Table(name = "SCHOOL_ADDRESS")
public class SchoolAddressEntity extends BaseAddressEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "school_address_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  @ComparableField
  private UUID schoolAddressId;

  @ManyToOne(optional = true, targetEntity = SchoolEntity.class)
  @JoinColumn(name = "school_id", referencedColumnName = "school_id")
  SchoolEntity schoolEntity;
}
