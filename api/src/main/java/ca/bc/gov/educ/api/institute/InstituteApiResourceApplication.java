package ca.bc.gov.educ.api.institute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The type School api resource application.
 */
@SpringBootApplication
@EnableCaching
@EnableRetry
@EnableScheduling
public class InstituteApiResourceApplication {
  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(InstituteApiResourceApplication.class, args);
  }


  /**
   * The type Web security configuration.
   * Add security exceptions for swagger UI and prometheus.
   */
  @Configuration
  @EnableMethodSecurity
  static
  class WebSecurityConfiguration {

    /**
     * Instantiates a new Web security configuration.
     * This makes sure that security context is propagated to async threads as well.
     */
    public WebSecurityConfiguration() {
      super();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
      return web -> web.ignoring().requestMatchers("/v3/api-docs/**",
        "/actuator/health", "/actuator/prometheus","/actuator/**",
        "/swagger-ui/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
          .anyRequest().authenticated()
        )
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
      return http.build();
    }

  }

}
