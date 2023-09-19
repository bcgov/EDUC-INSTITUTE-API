package ca.bc.gov.educ.api.institute.struct.v1;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSchoolSagaData {
  private School school;
  private Optional<EdxUser> initialEdxUser;
}
