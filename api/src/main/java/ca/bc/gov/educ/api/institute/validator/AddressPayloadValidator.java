package ca.bc.gov.educ.api.institute.validator;

import ca.bc.gov.educ.api.institute.model.v1.AddressTypeCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.CountryCodeEntity;
import ca.bc.gov.educ.api.institute.model.v1.ProvinceCodeEntity;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.service.v1.DistrictService;
import ca.bc.gov.educ.api.institute.struct.v1.Address;
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
public class AddressPayloadValidator {

  public static final String ADDRESS_TYPE_CODE = "addressTypeCode";
  public static final String PROVINCE_CODE = "provinceCode";

  public static final String COUNTRY_CODE = "countryCode";

  @Getter(AccessLevel.PRIVATE)
  private final DistrictService districtService;

  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;

  @Autowired
  public AddressPayloadValidator(final DistrictService districtService, final CodeTableService codeTableService) {
    this.districtService = districtService;
    this.codeTableService = codeTableService;
  }

  public List<FieldError> validatePayload(Address address, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    if (isCreateOperation && address.getAddressId() != null) {
      apiValidationErrors.add(createFieldError("addressId", address.getAddressId(), "addressId should be null for post operation."));
    }
    validateAddressTypeCode(address, apiValidationErrors);
    validateProvinceCode(address, apiValidationErrors);
    validateCountryCode(address, apiValidationErrors);
    return apiValidationErrors;
  }

  public List<FieldError> validateUpdatePayload(Address address) {
    return validatePayload(address, false);
  }

  public List<FieldError> validateCreatePayload(Address address) {
    return validatePayload(address, true);
  }

  protected void validateAddressTypeCode(Address address, List<FieldError> apiValidationErrors) {
    if (address.getAddressTypeCode() != null) {
      Optional<AddressTypeCodeEntity> addressTypeCodeEntity = codeTableService.getAddressTypeCode(address.getAddressTypeCode());
      if (addressTypeCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(ADDRESS_TYPE_CODE, address.getAddressTypeCode(), "Invalid address type code."));
      } else if (addressTypeCodeEntity.get().getEffectiveDate() != null && addressTypeCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(ADDRESS_TYPE_CODE, address.getAddressTypeCode(), "Address type code provided is not yet effective."));
      } else if (addressTypeCodeEntity.get().getExpiryDate() != null && addressTypeCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(ADDRESS_TYPE_CODE, address.getAddressTypeCode(), "Address type code provided has expired."));
      }
    }
  }

  protected void validateProvinceCode(Address address, List<FieldError> apiValidationErrors) {
    if (address.getProvinceCode() != null) {
      Optional<ProvinceCodeEntity> provinceCodeEntity = codeTableService.getProvinceCode(address.getProvinceCode());
      if (provinceCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(PROVINCE_CODE, address.getProvinceCode(), "Invalid province code."));
      } else if (provinceCodeEntity.get().getEffectiveDate() != null && provinceCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(PROVINCE_CODE, address.getProvinceCode(), "Province code provided is not yet effective."));
      } else if (provinceCodeEntity.get().getExpiryDate() != null && provinceCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(PROVINCE_CODE, address.getProvinceCode(), "Province code provided has expired."));
      }
    }
  }

  protected void validateCountryCode(Address address, List<FieldError> apiValidationErrors) {
    if (address.getCountryCode() != null) {
      Optional<CountryCodeEntity> countryCodeEntity = codeTableService.getCountryCode(address.getCountryCode());
      if (countryCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(COUNTRY_CODE, address.getCountryCode(), "Invalid country code."));
      } else if (countryCodeEntity.get().getEffectiveDate() != null && countryCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(COUNTRY_CODE, address.getCountryCode(), "Country code provided is not yet effective."));
      } else if (countryCodeEntity.get().getExpiryDate() != null && countryCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(COUNTRY_CODE, address.getCountryCode(), "Country code provided has expired."));
      }
    }
  }

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("address", fieldName, rejectedValue, false, null, null, message);
  }
}
