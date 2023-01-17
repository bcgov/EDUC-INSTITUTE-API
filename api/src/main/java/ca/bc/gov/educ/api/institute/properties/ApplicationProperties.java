package ca.bc.gov.educ.api.institute.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationProperties {

  public static final String INSTITUTE_API = "INSTITUTE-API";
  public static final String STREAM_NAME= "INSTITUTE_EVENTS";
  public static final String CORRELATION_ID = "correlationID";
  /**
   * The Stan url.
   */
  @Value("${nats.url}")
  String natsUrl;


  @Value("${nats.maxReconnect}")
  Integer natsMaxReconnect;

}
