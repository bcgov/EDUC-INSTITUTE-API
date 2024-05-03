package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Entity
@Builder
@Table(name = "INDEPENDENT_SCHOOL_FUNDING_GROUP_HISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndependentSchoolFundingGroupSchoolHistoryEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
      @Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
  @Column(name = "SCHOOL_FUNDING_GROUP_HISTORY_ID", unique = true, updatable = false, columnDefinition = "BINARY(16)")
  private UUID schoolFundingGroupHistoryID;

  @ManyToOne(optional = false, targetEntity = SchoolHistoryEntity.class)
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  @JoinColumn(name = "SCHOOL_HISTORY_ID", referencedColumnName = "SCHOOL_HISTORY_ID")
  SchoolHistoryEntity schoolHistoryEntity;

  @Basic
  @Column(name = "SCHOOL_ID", columnDefinition = "BINARY(16)")
  private UUID schoolID;

  @Column(name = "SCHOOL_GRADE_CODE", nullable = false, length = 10)
  @UpperCase
  private String schoolGradeCode;

  @Column(name = "SCHOOL_FUNDING_GROUP_CODE", nullable = false, length = 10)
  @UpperCase
  private String schoolFundingGroupCode;

  @Column(name = "CREATE_USER", updatable = false , length = 32)
  private String createUser;

  @PastOrPresent
  @Column(name = "CREATE_DATE", updatable = false)
  private LocalDateTime createDate;

  @Column(name = "UPDATE_USER", length = 32)
  private String updateUser;

  @PastOrPresent
  @Column(name = "UPDATE_DATE")
  private LocalDateTime updateDate;

}
