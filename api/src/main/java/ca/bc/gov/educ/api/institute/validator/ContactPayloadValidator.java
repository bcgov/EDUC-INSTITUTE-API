package ca.bc.gov.educ.api.institute.validator;

import ca.bc.gov.educ.api.institute.model.v1.ContactTypeCodeEntity;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.service.v1.DistrictService;
import ca.bc.gov.educ.api.institute.struct.v1.Contact;
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
public class ContactPayloadValidator {

  public static final String CONTACT_TYPE_CODE = "contactTypeCode";

  @Getter(AccessLevel.PRIVATE)
  private final DistrictService districtService;

  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;

  @Autowired
  public ContactPayloadValidator(final DistrictService districtService, final CodeTableService codeTableService) {
    this.districtService = districtService;
    this.codeTableService = codeTableService;
  }

  public List<FieldError> validatePayload(Contact contact, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    if (isCreateOperation && contact.getContactId() != null) {
      apiValidationErrors.add(createFieldError("contactId", contact.getContactId(), "contactId should be null for post operation."));
    }
    validateContactTypeCode(contact, apiValidationErrors);
    return apiValidationErrors;
  }

  public List<FieldError> validateUpdatePayload(Contact contact) {
    return validatePayload(contact, false);
  }

  public List<FieldError> validateCreatePayload(Contact contact) {
    return validatePayload(contact, true);
  }

  protected void validateContactTypeCode(Contact contact, List<FieldError> apiValidationErrors) {
    if (contact.getContactTypeCode() != null) {
      Optional<ContactTypeCodeEntity> contactTypeCodeEntity = codeTableService.getContactTypeCode(contact.getContactTypeCode());
      if (contactTypeCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(CONTACT_TYPE_CODE, contact.getContactTypeCode(), "Invalid contact type code."));
      } else if (contactTypeCodeEntity.get().getEffectiveDate() != null && contactTypeCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(CONTACT_TYPE_CODE, contact.getContactTypeCode(), "Contact type code provided is not yet effective."));
      } else if (contactTypeCodeEntity.get().getExpiryDate() != null && contactTypeCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(CONTACT_TYPE_CODE, contact.getContactTypeCode(), "Contact type code provided has expired."));
      }
    }
  }

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("contact", fieldName, rejectedValue, false, null, null, message);
  }
}
