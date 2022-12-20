package ca.bc.gov.educ.api.institute.validator;

import ca.bc.gov.educ.api.institute.model.v1.DistrictRegionCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictStatusCodeEntity;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.service.v1.DistrictService;
import ca.bc.gov.educ.api.institute.struct.v1.District;
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
public class DistrictPayloadValidator {

  public static final String DISTICT_REGION_CODE = "districtRegionCode";

  public static final String DISTICT_STATUS_CODE = "districtStatusCode";

  @Getter(AccessLevel.PRIVATE)
  private final DistrictService districtService;

  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;

  private final AddressPayloadValidator addressPayloadValidator;

  @Autowired
  public DistrictPayloadValidator(final DistrictService districtService, final CodeTableService codeTableService, AddressPayloadValidator addressPayloadValidator) {
    this.districtService = districtService;
    this.codeTableService = codeTableService;
    this.addressPayloadValidator = addressPayloadValidator;
  }

  public List<FieldError> validatePayload(District district, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    if (isCreateOperation && district.getDistrictId() != null) {
      apiValidationErrors.add(createFieldError("districtId", district.getDistrictId(), "districtId should be null for post operation."));
    }
    validateDistrictRegionCode(district, apiValidationErrors);
    validateDistrictStatusCode(district, apiValidationErrors);
    Optional.ofNullable(district.getAddresses()).orElse(Collections.emptyList()).stream().forEach(address -> addressPayloadValidator.validatePayload(address, apiValidationErrors));
    return apiValidationErrors;
  }

  public List<FieldError> validateUpdatePayload(District district) {
    return validatePayload(district, false);
  }

  public List<FieldError> validateCreatePayload(District district) {
    return validatePayload(district, true);
  }

  protected void validateDistrictRegionCode(District district, List<FieldError> apiValidationErrors) {
    if (district.getDistrictRegionCode() != null) {
      Optional<DistrictRegionCodeEntity> districtRegionCodeEntity = codeTableService.getDistrictRegionCode(district.getDistrictRegionCode());
      if (districtRegionCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(DISTICT_REGION_CODE, district.getDistrictRegionCode(), "Invalid region code."));
      } else if (districtRegionCodeEntity.get().getEffectiveDate() != null && districtRegionCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(DISTICT_REGION_CODE, district.getDistrictRegionCode(), "Region Code provided is not yet effective."));
      } else if (districtRegionCodeEntity.get().getExpiryDate() != null && districtRegionCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(DISTICT_REGION_CODE, district.getDistrictRegionCode(), "Region Code provided has expired."));
      }
    }
  }

  protected void validateDistrictStatusCode(District district, List<FieldError> apiValidationErrors) {
    if (district.getDistrictStatusCode() != null) {
      Optional<DistrictStatusCodeEntity> districtStatusCodeEntity = codeTableService.getDistrictStatusCode(district.getDistrictStatusCode());
      if (districtStatusCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(DISTICT_STATUS_CODE, district.getDistrictStatusCode(), "Invalid Status code."));
      } else if (districtStatusCodeEntity.get().getEffectiveDate() != null && districtStatusCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(DISTICT_STATUS_CODE, district.getDistrictStatusCode(), "Status Code provided is not yet effective."));
      } else if (districtStatusCodeEntity.get().getExpiryDate() != null && districtStatusCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(DISTICT_STATUS_CODE, district.getDistrictStatusCode(), "Status Code provided has expired."));
      }
    }
  }

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("district", fieldName, rejectedValue, false, null, null, message);
  }
}
