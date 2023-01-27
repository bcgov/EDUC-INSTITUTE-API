package ca.bc.gov.educ.api.institute.model.v1;

import ca.bc.gov.educ.api.institute.util.UpperCase;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "SCHOOL_GRADE_HISTORY")
public class SchoolGradeSchoolHistoryEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")})
    @Column(name = "SCHOOL_GRADE_HISTORY_ID", unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID schoolGradeHistoryId;

    @ManyToOne(optional = false, targetEntity = SchoolHistoryEntity.class)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "SCHOOL_HISTORY_ID", referencedColumnName = "SCHOOL_HISTORY_ID")
    SchoolHistoryEntity schoolHistoryEntity;

    @Basic
    @Column(name = "SCHOOL_GRADE_CODE")
    @UpperCase
    private String schoolGradeCode;

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
