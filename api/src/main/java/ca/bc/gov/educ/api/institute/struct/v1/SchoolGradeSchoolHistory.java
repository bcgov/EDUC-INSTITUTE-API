package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolGradeSchoolHistory extends BaseRequest implements Serializable {


    private static final long serialVersionUID = -8816066998462169068L;

    private String schoolGradeHistoryId;

    private String schoolHistoryId;

    @Size(max = 10)
    @NotNull(message = "school Grade Code cannot be null")
    private String schoolGradeCode;

}
