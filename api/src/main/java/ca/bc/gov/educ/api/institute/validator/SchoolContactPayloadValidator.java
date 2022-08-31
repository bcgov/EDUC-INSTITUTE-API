package ca.bc.gov.educ.api.institute.validator;

import ca.bc.gov.educ.api.institute.model.v1.SchoolContactTypeCodeEntity;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolContact;
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
public class SchoolContactPayloadValidator {

  public static final String SCHOOL_CONTACT_TYPE_CODE = "schoolContactTypeCode";

  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;

  @Autowired
  public SchoolContactPayloadValidator(final CodeTableService codeTableService) {
    this.codeTableService = codeTableService;
  }

  public List<FieldError> validatePayload(SchoolContact contact, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    if (isCreateOperation && contact.getSchoolContactId() != null) {
      apiValidationErrors.add(createFieldError("schoolContactId", contact.getSchoolContactId(), "schoolContactId should be null for post operation."));
    }
    validateContactTypeCode(contact, apiValidationErrors);
    return apiValidationErrors;
  }

  public List<FieldError> validateUpdatePayload(SchoolContact contact) {
    return validatePayload(contact, false);
  }

  public List<FieldError> validateCreatePayload(SchoolContact contact) {
    return validatePayload(contact, true);
  }

  protected void validateContactTypeCode(SchoolContact contact, List<FieldError> apiValidationErrors) {
    if (contact.getSchoolContactTypeCode() != null) {
      Optional<SchoolContactTypeCodeEntity> contactTypeCodeEntity = codeTableService.getSchoolContactTypeCode(contact.getSchoolContactTypeCode());
      if (contactTypeCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(SCHOOL_CONTACT_TYPE_CODE, contact.getSchoolContactTypeCode(), "Invalid school contact type code."));
      } else if (contactTypeCodeEntity.get().getEffectiveDate() != null && contactTypeCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(SCHOOL_CONTACT_TYPE_CODE, contact.getSchoolContactTypeCode(), "School contact type code provided is not yet effective."));
      } else if (contactTypeCodeEntity.get().getExpiryDate() != null && contactTypeCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(SCHOOL_CONTACT_TYPE_CODE, contact.getSchoolContactTypeCode(), "School contact type code provided has expired."));
      }
    }
  }

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("contact", fieldName, rejectedValue, false, null, null, message);
  }
}
