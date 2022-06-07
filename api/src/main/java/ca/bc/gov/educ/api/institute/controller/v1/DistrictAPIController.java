package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.CodeTableAPIEndpoint;
import ca.bc.gov.educ.api.institute.endpoint.v1.DistrictAPIEndpoint;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.institute.exception.errors.ApiError;
import ca.bc.gov.educ.api.institute.mapper.v1.CodeTableMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.DistrictMapper;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.service.v1.DistrictService;
import ca.bc.gov.educ.api.institute.struct.v1.*;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.validator.DistrictPayloadValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class DistrictAPIController implements DistrictAPIEndpoint {

  private static final DistrictMapper mapper = DistrictMapper.mapper;
  @Getter(AccessLevel.PRIVATE)
  private final DistrictService service;

  @Getter(AccessLevel.PRIVATE)
  private final DistrictPayloadValidator payloadValidator;

  @Autowired
  public DistrictAPIController(final DistrictService service, final DistrictPayloadValidator payloadValidator) {
    this.service = service;
    this.payloadValidator = payloadValidator;
  }

  @Override
  public District getDistrict(String districtId) {
    UUID districtUUID = null;
    try{
      districtUUID = UUID.fromString(districtId);
    }catch(Exception e){
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Invalid district ID").status(BAD_REQUEST).build();
      throw new InvalidPayloadException(error);
    }

    var district = getService().getDistrict(districtUUID);

    if(district.isPresent()){
      return mapper.toStructure(district.get());
    }else{
      throw new EntityNotFoundException();
    }
  }

  @Override
  public District createDistrict(District district) throws JsonProcessingException {
    validatePayload(() -> getPayloadValidator().validateCreatePayload(district));
    RequestUtil.setAuditColumnsForCreate(district);
    return mapper.toStructure(service.createDistrict(district));
  }

  @Override
  public District updateDistrict(UUID id, District district) throws JsonProcessingException {
    validatePayload(() -> getPayloadValidator().validateUpdatePayload(district));
    RequestUtil.setAuditColumnsForUpdate(district);
    return mapper.toStructure(service.updateDistrict(district, id));
  }

  @Override
  @Transactional
  public ResponseEntity<Void> deleteDistrict(UUID id) {
    getService().deleteDistrict(id);
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
  public List<District> getAllDistricts() {
    return getService().getAllDistrictsList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }
}
