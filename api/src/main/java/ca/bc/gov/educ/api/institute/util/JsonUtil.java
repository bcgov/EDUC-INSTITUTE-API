package ca.bc.gov.educ.api.institute.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

/**
 * The type Json util.
 *
 * @author OM
 */
@Slf4j
public class JsonUtil {
  /**
   * Instantiates a new Json util.
   */
  private JsonUtil(){
  }

  /**
   * Gets json string from object.
   *
   * @param payload the payload
   * @return the json string from object
   * @throws JsonProcessingException the json processing exception
   */
  public static String getJsonStringFromObject(Object payload) throws JsonProcessingException {
    var mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper.writeValueAsString(payload);
  }

  /**
   * Gets json object from string.
   *
   * @param <T>     the type parameter
   * @param clazz   the clazz
   * @param payload the payload
   * @return the json object from string
   * @throws JsonProcessingException the json processing exception
   */
  public static <T> T getJsonObjectFromString(Class<T> clazz,  String payload) throws JsonProcessingException {
    return new ObjectMapper().readValue(payload,clazz);
  }

  /**
   * Gets json object from string.
   *
   * @param <T>     the type parameter
   * @param clazz   the clazz
   * @param payload the payload
   * @return the json object from string
   * @throws IOException the io exception
   */
  public static <T> T getJsonObjectFromByteArray(Class<T> clazz,  byte[] payload) throws IOException {
    return new ObjectMapper().readValue(payload,clazz);
  }

  /**
   * Get json string optional.
   *
   * @param payload the payload
   * @return the optional
   */
  public static Optional<String> getJsonString(Object payload){
    try{
      return Optional.ofNullable(new ObjectMapper().writeValueAsString(payload));
    }catch (final Exception ex){
      log.error("Exception while converting object to JSON String :: {}", payload);
    }
    return Optional.empty();
  }
}
