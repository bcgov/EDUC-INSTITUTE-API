package ca.bc.gov.educ.api.institute.struct.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SuppressWarnings("squid:S1700")
public class SchoolGrade implements Serializable {

  private static final long serialVersionUID = 6118916290604876032L;

  private String schoolGradeId;

  private String schoolId;

  @Size(max = 10)
  @NotNull(message = "schoolGradeCode cannot be null")
  private String schoolGradeCode;
}
