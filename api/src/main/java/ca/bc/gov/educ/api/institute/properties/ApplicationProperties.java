package ca.bc.gov.educ.api.institute.properties;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationProperties {

  public static final String INSTITUTE_API = "INSTITUTE-API";

  private ApplicationProperties() {
    // This is fine
  }
}
