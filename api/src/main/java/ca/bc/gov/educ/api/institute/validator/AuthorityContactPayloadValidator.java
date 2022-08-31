package ca.bc.gov.educ.api.institute.validator;

import ca.bc.gov.educ.api.institute.model.v1.AuthorityContactTypeCodeEntity;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.service.v1.DistrictService;
import ca.bc.gov.educ.api.institute.struct.v1.AuthorityContact;
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
public class AuthorityContactPayloadValidator {

  public static final String AUTHORITY_CONTACT_TYPE_CODE = "authorityContactTypeCode";

  @Getter(AccessLevel.PRIVATE)
  private final DistrictService districtService;

  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;

  @Autowired
  public AuthorityContactPayloadValidator(final DistrictService districtService, final CodeTableService codeTableService) {
    this.districtService = districtService;
    this.codeTableService = codeTableService;
  }

  public List<FieldError> validatePayload(AuthorityContact contact, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    if (isCreateOperation && contact.getAuthorityContactId() != null) {
      apiValidationErrors.add(createFieldError("authorityContactId", contact.getAuthorityContactId(), "authorityContactId should be null for post operation."));
    }
    validateContactTypeCode(contact, apiValidationErrors);
    return apiValidationErrors;
  }

  public List<FieldError> validateUpdatePayload(AuthorityContact contact) {
    return validatePayload(contact, false);
  }

  public List<FieldError> validateCreatePayload(AuthorityContact contact) {
    return validatePayload(contact, true);
  }

  protected void validateContactTypeCode(AuthorityContact contact, List<FieldError> apiValidationErrors) {
    if (contact.getAuthorityContactTypeCode() != null) {
      Optional<AuthorityContactTypeCodeEntity> contactTypeCodeEntity = codeTableService.getAuthorityContactTypeCode(contact.getAuthorityContactTypeCode());
      if (contactTypeCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(AUTHORITY_CONTACT_TYPE_CODE, contact.getAuthorityContactTypeCode(), "Invalid authority contact type code."));
      } else if (contactTypeCodeEntity.get().getEffectiveDate() != null && contactTypeCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(AUTHORITY_CONTACT_TYPE_CODE, contact.getAuthorityContactTypeCode(), "Authority contact type code provided is not yet effective."));
      } else if (contactTypeCodeEntity.get().getExpiryDate() != null && contactTypeCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(AUTHORITY_CONTACT_TYPE_CODE, contact.getAuthorityContactTypeCode(), "Authority contact type code provided has expired."));
      }
    }
  }

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("contact", fieldName, rejectedValue, false, null, null, message);
  }
}
