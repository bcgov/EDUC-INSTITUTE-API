package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.SchoolAPIEndpoint;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.institute.exception.errors.ApiError;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.service.v1.SchoolHistoryService;
import ca.bc.gov.educ.api.institute.service.v1.SchoolService;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolHistory;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.validator.SchoolPayloadValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Slf4j
public class SchoolAPIController implements SchoolAPIEndpoint {

  private static final SchoolMapper mapper = SchoolMapper.mapper;
  @Getter(AccessLevel.PRIVATE)
  private final SchoolService schoolService;
  @Getter(AccessLevel.PRIVATE)
  private final SchoolHistoryService schoolHistoryService;

  @Getter(AccessLevel.PRIVATE)
  private final SchoolPayloadValidator payloadValidator;

  @Autowired
  public SchoolAPIController(final SchoolService schoolService, final SchoolHistoryService schoolHistoryService, final SchoolPayloadValidator payloadValidator) {
    this.schoolService = schoolService;
    this.schoolHistoryService = schoolHistoryService;
    this.payloadValidator = payloadValidator;
  }

  @Override
  public School getSchool(String schoolId) {
    UUID schoolUUID;
    try{
      schoolUUID = UUID.fromString(schoolId);
    }catch(Exception e){
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Invalid school ID").status(BAD_REQUEST).build();
      throw new InvalidPayloadException(error);
    }

    var school = getSchoolService().getSchool(schoolUUID);

    if(school.isPresent()){
      return mapper.toStructure(school.get());
    }else{
      throw new EntityNotFoundException();
    }
  }

  @Override
  public List<SchoolHistory> getSchoolHistory(String schoolId) {
    UUID schoolUUID;
    try{
      schoolUUID = UUID.fromString(schoolId);
    }catch(Exception e){
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Invalid school history ID").status(BAD_REQUEST).build();
      throw new InvalidPayloadException(error);
    }

    return getSchoolHistoryService().getAllSchoolHistoryList(schoolUUID).stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public School createSchool(School school) {
    validatePayload(() -> getPayloadValidator().validateCreatePayload(school));
    RequestUtil.setAuditColumnsForCreate(school);
    return mapper.toStructure(schoolService.createSchool(school));
  }

  @Override
  public School updateSchool(UUID id, School school) {
    validatePayload(() -> getPayloadValidator().validateUpdatePayload(school));
    RequestUtil.setAuditColumnsForUpdate(school);
    return mapper.toStructure(schoolService.updateSchool(school, id));
  }

  @Override
  @Transactional
  public ResponseEntity<Void> deleteSchool(UUID id) {
    getSchoolService().deleteSchool(id);
    return ResponseEntity.noContent().build();
  }

  private void validatePayload(Supplier<List<FieldError>> validator) {
    val validationResult = validator.get();
    if (!validationResult.isEmpty()) {
      ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Payload contains invalid data.").status(BAD_REQUEST).build();
      error.addValidationErrors(validationResult);
      throw new InvalidPayloadException(error);
    }
  }

  @Override
  public List<School> getAllSchools() {
    return getSchoolService().getAllSchoolsList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }
}
