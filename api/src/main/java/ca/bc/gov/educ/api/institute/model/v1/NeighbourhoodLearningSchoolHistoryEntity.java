package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "NEIGHBOURHOOD_LEARNING_SCHOOL_HISTORY")
public class NeighbourhoodLearningSchoolHistoryEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
    @Column(name = "neighbourhood_learning_hist_id", unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID neighbourhoodLearningHistoryId;

    @ManyToOne(optional = true, targetEntity = SchoolHistoryEntity.class)
    @JoinColumn(name = "school_history_id", referencedColumnName = "school_history_id")
    @JsonIgnoreProperties("neighbourhoodLearnings")
    SchoolHistoryEntity schoolHistoryEntity;

    @Basic
    @Column(name = "neighbourhood_learning_type_code")
    @UpperCase
    private String neighbourhoodLearningTypeCode;

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
