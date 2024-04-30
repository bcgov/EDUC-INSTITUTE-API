package ca.bc.gov.educ.api.institute.validator;

import ca.bc.gov.educ.api.institute.model.v1.SchoolFundingGroupCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolGradeCodeEntity;
import ca.bc.gov.educ.api.institute.repository.v1.SchoolRepository;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.service.v1.IndependentSchoolFundingGroupService;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentSchoolFundingGroup;
import ca.bc.gov.educ.api.institute.util.ValidationUtil;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class IndependentSchoolFundingGroupPayloadValidator {

  @Getter(AccessLevel.PRIVATE)
  private final IndependentSchoolFundingGroupService independentSchoolFundingGroupService;

  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;
  private final SchoolRepository schoolRepository;


  @Autowired
  public IndependentSchoolFundingGroupPayloadValidator(final IndependentSchoolFundingGroupService independentSchoolFundingGroupService, final CodeTableService codeTableService, SchoolRepository schoolRepository) {
    this.independentSchoolFundingGroupService = independentSchoolFundingGroupService;
    this.codeTableService = codeTableService;
    this.schoolRepository = schoolRepository;
  }

  public List<FieldError> validatePayload(IndependentSchoolFundingGroup independentSchoolFundingGroup, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    if (isCreateOperation && independentSchoolFundingGroup.getSchoolFundingGroupID() != null) {
      apiValidationErrors.add(ValidationUtil.createFieldError("schoolFundingGroupID", independentSchoolFundingGroup.getSchoolFundingGroupID(), "schoolFundingGroupID should be null for post operation."));
    }
    if (independentSchoolFundingGroup.getSchoolID() != null && !schoolRepository.findById(UUID.fromString(independentSchoolFundingGroup.getSchoolID())).isPresent()) {
      apiValidationErrors.add(ValidationUtil.createFieldError("schoolID", independentSchoolFundingGroup.getSchoolID(), "schoolID was not found"));
    }
    validateCollectionCodePayload(independentSchoolFundingGroup, apiValidationErrors);

    return apiValidationErrors;
  }

  public List<FieldError> validateCreatePayload(IndependentSchoolFundingGroup independentSchoolFundingGroup) {
    return validatePayload(independentSchoolFundingGroup, true);
  }

  protected void validateCollectionCodePayload(IndependentSchoolFundingGroup independentSchoolFundingGroup, List<FieldError> apiValidationErrors) {
    List<SchoolFundingGroupCodeEntity> schoolFundingGroupCodes = codeTableService.getAllSchoolFundingGroupCodes();
    if (StringUtils.isNotEmpty(independentSchoolFundingGroup.getSchoolFundingGroupCode()) && schoolFundingGroupCodes.stream().noneMatch(fundingGroupCode -> fundingGroupCode.getSchoolFundingGroupCode().equals(independentSchoolFundingGroup.getSchoolFundingGroupCode()))) {
        apiValidationErrors.add(ValidationUtil.createFieldError("schoolFundingGroupCode", independentSchoolFundingGroup.getSchoolFundingGroupCode(), "Invalid School Funding Group Code."));
    }

    List<SchoolGradeCodeEntity> schoolGradeCodes = codeTableService.getSchoolGradeCodesList();
    if (StringUtils.isNotEmpty(independentSchoolFundingGroup.getSchoolGradeCode()) && schoolGradeCodes.stream().noneMatch(gradeCode -> gradeCode.getSchoolGradeCode().equals(independentSchoolFundingGroup.getSchoolGradeCode()))) {
        apiValidationErrors.add(ValidationUtil.createFieldError("schoolGradeCode", independentSchoolFundingGroup.getSchoolGradeCode(), "Invalid School Grade Code."));
    }
  }

}
