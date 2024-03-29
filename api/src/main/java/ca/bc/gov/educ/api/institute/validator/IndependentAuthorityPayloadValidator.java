package ca.bc.gov.educ.api.institute.validator;

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
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Component
public class IndependentAuthorityPayloadValidator {

  public static final String AUTHORITY_TYPE_CODE = "authorityTypeCode";

  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityService independentAuthorityService;

  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;

  private final AddressPayloadValidator addressPayloadValidator;

  @Autowired
  public IndependentAuthorityPayloadValidator(final IndependentAuthorityService independentAuthorityService, final CodeTableService codeTableService, AddressPayloadValidator addressPayloadValidator) {
    this.independentAuthorityService = independentAuthorityService;
    this.codeTableService = codeTableService;
    this.addressPayloadValidator = addressPayloadValidator;
  }

  public List<FieldError> validatePayload(IndependentAuthority independentAuthority, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    if (isCreateOperation && independentAuthority.getIndependentAuthorityId() != null) {
      apiValidationErrors.add(createFieldError("independentAuthorityId", independentAuthority.getIndependentAuthorityId(), "independentAuthorityId should be null for post operation."));
    }

    if (isCreateOperation && independentAuthority.getAuthorityNumber() != null) {
      apiValidationErrors.add(createFieldError("authorityNumber", independentAuthority.getAuthorityNumber(), "authorityNumber should be null for post operation."));
    }
    else if(!isCreateOperation && independentAuthority.getAuthorityNumber() == null) {
        apiValidationErrors.add(createFieldError("authorityNumber", independentAuthority.getAuthorityNumber(), "authorityNumber can not be null for a put operation."));
    }
    validateIndependentAuthorityTypeCode(independentAuthority, apiValidationErrors);
    Optional.ofNullable(independentAuthority.getAddresses()).orElse(Collections.emptyList()).stream().forEach(address -> addressPayloadValidator.validatePayload(address, apiValidationErrors));
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

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("authority", fieldName, rejectedValue, false, null, null, message);
  }
}
