package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.ComparableField;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@DynamicUpdate
@Table(name = "SCHOOL_MOVE_HISTORY")
public class SchoolMoveHistoryEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
    @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "school_move_history_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  @ComparableField
  private UUID schoolMoveHistoryId;

  @Basic
  @JoinColumn(name = "to_school_id", referencedColumnName = "school_id")
  @Column(name = "to_school_id", columnDefinition = "BINARY(16)", updatable = false)
  private UUID toSchoolId;

  @Basic
  @JoinColumn(name = "from_school_id", referencedColumnName = "school_id")
  @Column(name = "from_school_id", columnDefinition = "BINARY(16)", updatable = false)
  private UUID fromSchoolId;

  @Column(name = "move_date", updatable = false)
  private LocalDateTime moveDate;

  @Column(name = "create_user", updatable = false)
  private String createUser;

  @PastOrPresent
  @Column(name = "create_date", updatable = false)
  private LocalDateTime createDate;

  @Column(name = "update_user")
  private String updateUser;

  @PastOrPresent
  @Column(name = "update_date")
  private LocalDateTime updateDate;
}
