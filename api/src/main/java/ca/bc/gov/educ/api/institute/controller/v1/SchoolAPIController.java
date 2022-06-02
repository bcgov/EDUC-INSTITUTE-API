package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.SchoolAPIEndpoint;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.service.v1.SchoolService;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The type School api controller.
 */
@RestController
@Slf4j
public class SchoolAPIController implements SchoolAPIEndpoint {
  /**
   * The constant mapper.
   */
  private static final SchoolMapper mapper = SchoolMapper.mapper;
  @Getter(AccessLevel.PRIVATE)
  private final SchoolService service;

  /**
   * Instantiates a new School api controller.
   *
   * @param service               the service
   */
  @Autowired
  public SchoolAPIController(SchoolService service) {
    this.service = service;
  }

  @Override
  public School getSchoolByMinCode(String mincode) {
    return mapper.toStructure(getService().retrieveSchoolByMincode(mincode));
  }

  @Override
  public List<School> getAllSchools() {
    return getService().retrieveAllSchoolStructs();
  }


}
