package ca.bc.gov.educ.api.institute.validator;

import ca.bc.gov.educ.api.institute.model.v1.DistrictContactTypeCodeEntity;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.struct.v1.DistrictContact;
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
public class DistrictContactPayloadValidator {

  public static final String DISTRICT_CONTACT_TYPE_CODE = "districtContactTypeCode";

  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;

  @Autowired
  public DistrictContactPayloadValidator(final CodeTableService codeTableService) {
    this.codeTableService = codeTableService;
  }

  public List<FieldError> validatePayload(DistrictContact contact, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    if (isCreateOperation && contact.getDistrictContactId() != null) {
      apiValidationErrors.add(createFieldError("districtContactId", contact.getDistrictContactId(), "districtContactId should be null for post operation."));
    }
    validateContactTypeCode(contact, apiValidationErrors);
    return apiValidationErrors;
  }

  public List<FieldError> validateUpdatePayload(DistrictContact contact) {
    return validatePayload(contact, false);
  }

  public List<FieldError> validateCreatePayload(DistrictContact contact) {
    return validatePayload(contact, true);
  }

  protected void validateContactTypeCode(DistrictContact contact, List<FieldError> apiValidationErrors) {
    if (contact.getDistrictContactTypeCode() != null) {
      Optional<DistrictContactTypeCodeEntity> contactTypeCodeEntity = codeTableService.getDistrictContactTypeCode(contact.getDistrictContactTypeCode());
      if (contactTypeCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(DISTRICT_CONTACT_TYPE_CODE, contact.getDistrictContactTypeCode(), "Invalid district contact type code."));
      } else if (contactTypeCodeEntity.get().getEffectiveDate() != null && contactTypeCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(DISTRICT_CONTACT_TYPE_CODE, contact.getDistrictContactTypeCode(), "District contact type code provided is not yet effective."));
      } else if (contactTypeCodeEntity.get().getExpiryDate() != null && contactTypeCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(DISTRICT_CONTACT_TYPE_CODE, contact.getDistrictContactTypeCode(), "District contact type code provided has expired."));
      }
    }
  }

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("contact", fieldName, rejectedValue, false, null, null, message);
  }
}
