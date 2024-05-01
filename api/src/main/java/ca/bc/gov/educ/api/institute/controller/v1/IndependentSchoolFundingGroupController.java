package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.IndependentSchoolFundingGroupEndpoint;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.institute.exception.errors.ApiError;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentSchoolFundingGroupMapper;
import ca.bc.gov.educ.api.institute.model.v1.IndependentSchoolFundingGroupEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentSchoolFundingGroupHistoryEntity;
import ca.bc.gov.educ.api.institute.service.v1.IndependentSchoolFundingGroupService;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentSchoolFundingGroup;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentSchoolFundingGroupHistory;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.util.ValidationUtil;
import ca.bc.gov.educ.api.institute.validator.IndependentSchoolFundingGroupPayloadValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Slf4j
public class IndependentSchoolFundingGroupController implements IndependentSchoolFundingGroupEndpoint {

  private static final IndependentSchoolFundingGroupMapper independentSchoolFundingGroupMapper = IndependentSchoolFundingGroupMapper.mapper;
  private final IndependentSchoolFundingGroupPayloadValidator independentSchoolFundingGroupPayloadValidator;
  private final IndependentSchoolFundingGroupService independentSchoolFundingGroupService;

  @Autowired
  public IndependentSchoolFundingGroupController(IndependentSchoolFundingGroupPayloadValidator independentSchoolFundingGroupPayloadValidator, IndependentSchoolFundingGroupService independentSchoolFundingGroupService) {
    this.independentSchoolFundingGroupPayloadValidator = independentSchoolFundingGroupPayloadValidator;
    this.independentSchoolFundingGroupService = independentSchoolFundingGroupService;
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
  public IndependentSchoolFundingGroup getIndependentSchoolFundingGroup(UUID schoolFundingGroupID) {
    Optional<IndependentSchoolFundingGroupEntity> independentSchoolFundingGroup = this.independentSchoolFundingGroupService.getIndependentSchoolFundingGroup(schoolFundingGroupID);

    if (independentSchoolFundingGroup.isPresent()) {
      return independentSchoolFundingGroupMapper.toStructure(independentSchoolFundingGroup.get());
    } else {
      throw new EntityNotFoundException();
    }
  }

  @Override
  public List<IndependentSchoolFundingGroup> getIndependentSchoolFundingGroups(UUID schoolID) {
    List<IndependentSchoolFundingGroupEntity> independentSchoolFundingGroups = this.independentSchoolFundingGroupService.getIndependentSchoolFundingGroups(schoolID);

    List<IndependentSchoolFundingGroup> independentSchoolFundingGroupList = new ArrayList<>();
    for(IndependentSchoolFundingGroupEntity entity: independentSchoolFundingGroups){
      independentSchoolFundingGroupList.add(independentSchoolFundingGroupMapper.toStructure(entity));
    }

    return independentSchoolFundingGroupList;
  }

  @Override
  public List<IndependentSchoolFundingGroupHistory> getIndependentSchoolFundingGroupHistory(UUID schoolID) {
    List<IndependentSchoolFundingGroupHistoryEntity> independentSchoolFundingGroups = this.independentSchoolFundingGroupService.getIndependentSchoolFundingGroupHistory(schoolID);

    List<IndependentSchoolFundingGroupHistory> independentSchoolFundingGroupList = new ArrayList<>();
    for(IndependentSchoolFundingGroupHistoryEntity entity: independentSchoolFundingGroups){
      independentSchoolFundingGroupList.add(independentSchoolFundingGroupMapper.toStructure(entity));
    }

    return independentSchoolFundingGroupList;
  }

  @Override
  public IndependentSchoolFundingGroup createIndependentSchoolFundingGroup(IndependentSchoolFundingGroup fundingGroup) throws JsonProcessingException {
    validatePayload(() -> this.independentSchoolFundingGroupPayloadValidator.validateCreatePayload(fundingGroup));
    RequestUtil.setAuditColumnsForCreate(fundingGroup);

    return independentSchoolFundingGroupMapper.toStructure(independentSchoolFundingGroupService.createIndependentSchoolFundingGroup(fundingGroup));
  }

  @Override
  public IndependentSchoolFundingGroup updateIndependentSchoolFundingGroup(UUID schoolFundingGroupID, IndependentSchoolFundingGroup independentSchoolFundingGroup) {
    ValidationUtil.validatePayload(() -> this.independentSchoolFundingGroupPayloadValidator.validatePayload(independentSchoolFundingGroup, false));
    RequestUtil.setAuditColumnsForUpdate(independentSchoolFundingGroup);
    return independentSchoolFundingGroupMapper.toStructure(independentSchoolFundingGroupService.updateIndependentSchoolFundingGroup(independentSchoolFundingGroup));
  }

  @Override
  @Transactional
  public ResponseEntity<Void> deleteIndependentSchoolFundingGroup(UUID schoolFundingGroupID) {
    this.independentSchoolFundingGroupService.deleteIndependentSchoolFundingGroup(schoolFundingGroupID);
    return ResponseEntity.noContent().build();
  }
}
