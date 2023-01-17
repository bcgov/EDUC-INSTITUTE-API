package ca.bc.gov.educ.api.institute.support;

import ca.bc.gov.educ.api.institute.messaging.MessagePublisher;
import ca.bc.gov.educ.api.institute.messaging.NatsConnection;
import ca.bc.gov.educ.api.institute.messaging.jetstream.Publisher;
import ca.bc.gov.educ.api.institute.messaging.jetstream.Subscriber;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test-event")
@Configuration
public class EventTaskSchedulerMockConfiguration {
  @Bean
  @Primary
  public MessagePublisher messagePublisher() {
    return Mockito.mock(MessagePublisher.class);
  }

  @Bean
  @Primary
  public NatsConnection natsConnection() {
    return Mockito.mock(NatsConnection.class);
  }

  @Bean
  @Primary
  public Publisher publisher() {
    return Mockito.mock(Publisher.class);
  }

  @Bean
  @Primary
  public Subscriber subscriber() {
    return Mockito.mock(Subscriber.class);
  }
}
