package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
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
@Table(name = "NEIGHBOURHOOD_LEARNING_SCH_HIST")
public class NeighbourhoodLearningSchoolHistoryEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
    @Column(name = "NEIGHBOURHOOD_LEARNING_HIST_ID", unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID neighbourhoodLearningHistoryId;

    @ManyToOne(optional = false, targetEntity = SchoolHistoryEntity.class)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "SCHOOL_HISTORY_ID", referencedColumnName = "SCHOOL_HISTORY_ID")
    SchoolHistoryEntity schoolHistoryEntity;

    @Basic
    @Column(name = "NEIGHBOURHOOD_LEARNING_TYPE_CODE")
    @UpperCase
    private String neighbourhoodLearningTypeCode;

    @Column(name = "CREATE_USER", updatable = false)
    private String createUser;

    @PastOrPresent
    @Column(name = "CREATE_DATE", updatable = false)
    private LocalDateTime createDate;

    @Column(name = "UPDATE_USER")
    private String updateUser;

    @PastOrPresent
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

}
