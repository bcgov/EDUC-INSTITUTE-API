package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.IndependentAuthorityAPIEndpoint;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.institute.exception.errors.ApiError;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.service.v1.IndependentAuthorityHistoryService;
import ca.bc.gov.educ.api.institute.service.v1.IndependentAuthorityService;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthority;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthorityHistory;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.validator.IndependentAuthorityPayloadValidator;
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
public class IndependentAuthorityAPIController implements IndependentAuthorityAPIEndpoint {

  private static final IndependentAuthorityMapper mapper = IndependentAuthorityMapper.mapper;
  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityService independentAuthorityService;
  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityHistoryService independentAuthorityHistoryService;

  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityPayloadValidator payloadValidator;

  @Autowired
  public IndependentAuthorityAPIController(final IndependentAuthorityService independentAuthorityService, final IndependentAuthorityHistoryService independentAuthorityHistoryService, final IndependentAuthorityPayloadValidator payloadValidator) {
    this.independentAuthorityService = independentAuthorityService;
    this.independentAuthorityHistoryService = independentAuthorityHistoryService;
    this.payloadValidator = payloadValidator;
  }

  @Override
  public IndependentAuthority getIndependentAuthority(String independentAuthorityId) {
    UUID independentAuthorityUUID;
    try{
      independentAuthorityUUID = UUID.fromString(independentAuthorityId);
    }catch(Exception e){
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Invalid independentAuthority ID").status(BAD_REQUEST).build();
      throw new InvalidPayloadException(error);
    }

    var independentAuthority = getIndependentAuthorityService().getIndependentAuthority(independentAuthorityUUID);

    if(independentAuthority.isPresent()){
      return mapper.toStructure(independentAuthority.get());
    }else{
      throw new EntityNotFoundException();
    }
  }

  @Override
  public List<IndependentAuthorityHistory> getIndependentAuthorityHistory(String independentAuthorityId) {
    UUID independentAuthorityUUID;
    try{
      independentAuthorityUUID = UUID.fromString(independentAuthorityId);
    }catch(Exception e){
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Invalid independentAuthority history ID").status(BAD_REQUEST).build();
      throw new InvalidPayloadException(error);
    }

    return getIndependentAuthorityHistoryService().getAllIndependentAuthorityHistoryList(independentAuthorityUUID).stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public IndependentAuthority createIndependentAuthority(IndependentAuthority independentAuthority) {
    validatePayload(() -> getPayloadValidator().validateCreatePayload(independentAuthority));
    RequestUtil.setAuditColumnsForCreate(independentAuthority);
    return mapper.toStructure(independentAuthorityService.createIndependentAuthority(independentAuthority));
  }

  @Override
  public IndependentAuthority updateIndependentAuthority(UUID id, IndependentAuthority independentAuthority) {
    validatePayload(() -> getPayloadValidator().validateUpdatePayload(independentAuthority));
    RequestUtil.setAuditColumnsForUpdate(independentAuthority);
    return mapper.toStructure(independentAuthorityService.updateIndependentAuthority(independentAuthority, id));
  }

  @Override
  @Transactional
  public ResponseEntity<Void> deleteIndependentAuthority(UUID id) {
    getIndependentAuthorityService().deleteIndependentAuthority(id);
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
  public List<IndependentAuthority> getAllIndependentAuthorities() {
    return getIndependentAuthorityService().getAllIndependentAuthoritysList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }
}
