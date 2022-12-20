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
@Table(name = "SCHOOL_ADDRESS_HISTORY")
public class SchoolAddressHistoryEntity extends BaseAddressEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "school_address_history_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID schoolAddressHistoryId;
  @Basic
  @Column(name = "school_id", columnDefinition = "BINARY(16)")
  private UUID schoolId;
  @ManyToOne(optional = false, targetEntity = SchoolHistoryEntity.class)
  @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
  @JoinColumn(name = "SCHOOL_HISTORY_ID", referencedColumnName = "SCHOOL_HISTORY_ID")
  SchoolHistoryEntity schoolHistoryEntity;

}
