package ca.bc.gov.educ.api.institute.validator;

import ca.bc.gov.educ.api.institute.model.v1.AuthorityGroupCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.AuthorityTypeCodeEntity;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.service.v1.IndependentAuthorityService;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthority;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class IndependentAuthorityPayloadValidator {

  public static final String AUTHORITY_TYPE_CODE = "authorityTypeCode";
  public static final String AUTHORITY_GROUP_CODE = "authorityGroupCode";

  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityService independentAuthorityService;

  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;

  @Autowired
  public IndependentAuthorityPayloadValidator(final IndependentAuthorityService independentAuthorityService, final CodeTableService codeTableService) {
    this.independentAuthorityService = independentAuthorityService;
    this.codeTableService = codeTableService;
  }

  public List<FieldError> validatePayload(IndependentAuthority independentAuthority, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    if (isCreateOperation && independentAuthority.getIndependentAuthorityId() != null) {
      apiValidationErrors.add(createFieldError("independentAuthorityId", independentAuthority.getIndependentAuthorityId(), "independentAuthorityId should be null for post operation."));
    }
    validateIndependentAuthorityTypeCode(independentAuthority, apiValidationErrors);
    validateIndependentAuthorityGroupCode(independentAuthority, apiValidationErrors);
    return apiValidationErrors;
  }

  public List<FieldError> validateUpdatePayload(IndependentAuthority independentAuthority) {
    return validatePayload(independentAuthority, false);
  }

  public List<FieldError> validateCreatePayload(IndependentAuthority independentAuthority) {
    return validatePayload(independentAuthority, true);
  }

  protected void validateIndependentAuthorityTypeCode(IndependentAuthority independentAuthority, List<FieldError> apiValidationErrors) {
    if (independentAuthority.getAuthorityTypeCode() != null) {
      Optional<AuthorityTypeCodeEntity> authorityTypeCode = codeTableService.getAuthorityTypeCode(independentAuthority.getAuthorityTypeCode());
      if (authorityTypeCode.isEmpty()) {
        apiValidationErrors.add(createFieldError(AUTHORITY_TYPE_CODE, independentAuthority.getAuthorityTypeCode(), "Invalid authority type code."));
      } else if (authorityTypeCode.get().getEffectiveDate() != null && authorityTypeCode.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(AUTHORITY_TYPE_CODE, independentAuthority.getAuthorityTypeCode(), "Authority type code provided is not yet effective."));
      } else if (authorityTypeCode.get().getExpiryDate() != null && authorityTypeCode.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(AUTHORITY_TYPE_CODE, independentAuthority.getAuthorityTypeCode(), "Authority type code provided has expired."));
      }
    }
  }

  protected void validateIndependentAuthorityGroupCode(IndependentAuthority independentAuthority, List<FieldError> apiValidationErrors) {
    if (independentAuthority.getAuthorityGroupCode() != null) {
      Optional<AuthorityGroupCodeEntity> authorityTypeCode = codeTableService.getAuthorityGroupCode(independentAuthority.getAuthorityGroupCode());
      if (authorityTypeCode.isEmpty()) {
        apiValidationErrors.add(createFieldError(AUTHORITY_GROUP_CODE, independentAuthority.getAuthorityGroupCode(), "Invalid authority group code."));
      } else if (authorityTypeCode.get().getEffectiveDate() != null && authorityTypeCode.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(AUTHORITY_GROUP_CODE, independentAuthority.getAuthorityGroupCode(), "Authority group code provided is not yet effective."));
      } else if (authorityTypeCode.get().getExpiryDate() != null && authorityTypeCode.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(AUTHORITY_GROUP_CODE, independentAuthority.getAuthorityGroupCode(), "Authority group code provided has expired."));
      }
    }
  }

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("authority", fieldName, rejectedValue, false, null, null, message);
  }
}
