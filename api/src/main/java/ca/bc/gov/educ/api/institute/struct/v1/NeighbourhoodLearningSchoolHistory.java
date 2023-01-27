package ca.bc.gov.educ.api.institute.struct.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeighbourhoodLearningSchoolHistory extends BaseRequest implements Serializable {


    private static final long serialVersionUID = -774459743115777058L;

    private String neighbourhoodLearningHistoryId;

    private String schoolHistoryId;

    @Size(max = 10)
    @NotNull(message = "neighbourhoodLearningTypeCode cannot be null")
    private String neighbourhoodLearningTypeCode;

}
