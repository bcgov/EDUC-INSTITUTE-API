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
public class SchoolGrade extends BaseRequest implements Serializable {

  private static final long serialVersionUID = 6118916290604876032L;

  private String schoolGradeId;

  private String schoolId;

  @Size(max = 10)
  @NotNull(message = "schoolGradeCode cannot be null")
  private String schoolGradeCode;
}
