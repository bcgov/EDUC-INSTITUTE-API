package ca.bc.gov.educ.api.institute.validator;

import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictRepository;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.service.v1.SchoolService;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentSchoolFundingGroup;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import ca.bc.gov.educ.api.institute.util.ValidationUtil;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.*;


@Component
public class SchoolPayloadValidator {

  public static final String SCHOOL_ORGANIZATION_CODE = "schoolOrganizationCode";
  public static final String SCHOOL_CATEGORY_CODE = "schoolCategoryCode";
  public static final String FACILITY_TYPE_CODE = "facilityTypeCode";
  public static final String VENDOR_SOURCE_SYSTEM_CODE = "vendorSourceSystemCode";
  public static final String DISTRICT_ID = "districtID";

  @Getter(AccessLevel.PRIVATE)
  private final SchoolService schoolService;

  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;

  private final DistrictRepository districtRepository;

  private final AddressPayloadValidator addressPayloadValidator;

  @Autowired
  public SchoolPayloadValidator(final SchoolService schoolService, final CodeTableService codeTableService, DistrictRepository districtRepository, AddressPayloadValidator addressPayloadValidator) {
    this.schoolService = schoolService;
    this.codeTableService = codeTableService;
    this.districtRepository = districtRepository;
    this.addressPayloadValidator = addressPayloadValidator;
  }

  public List<FieldError> validatePayload(School school, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    if (isCreateOperation && school.getSchoolId() != null) {
      apiValidationErrors.add(createFieldError("schoolId", school.getSchoolId(), "schoolId should be null for post operation."));
    }

    if(!isCreateOperation && school.getSchoolNumber() == null) {
      apiValidationErrors.add(createFieldError("schoolNumber", school.getSchoolNumber(), "schoolNumber can not be null for a put operation."));
    }

    validateDistrict(school, apiValidationErrors);
    validateSchoolOrganizationCode(school, apiValidationErrors);
    validateSchoolCategoryCode(school, apiValidationErrors);
    validateFacilityTypeCode(school, apiValidationErrors);
    validateVendorSourceSystemCode(school, apiValidationErrors);
    validateSchoolFundingCodes(school, apiValidationErrors);
    Optional.ofNullable(school.getAddresses()).orElse(Collections.emptyList()).stream().forEach(address -> addressPayloadValidator.validatePayload(address, apiValidationErrors));
    return apiValidationErrors;
  }

  private void validateDistrict(School school, List<FieldError> apiValidationErrors){
    Optional<DistrictEntity> district = districtRepository.findById(UUID.fromString(school.getDistrictId()));
    if(district.isEmpty()) {
      apiValidationErrors.add(createFieldError(DISTRICT_ID, school.getDistrictId(), "Invalid district ID."));
    }
  }

  public List<FieldError> validateUpdatePayload(School school) {
    return validatePayload(school, false);
  }

  public List<FieldError> validateCreatePayload(School school) {
    return validatePayload(school, true);
  }

  protected void validateSchoolOrganizationCode(School school, List<FieldError> apiValidationErrors) {
    if (school.getSchoolOrganizationCode() != null) {
      Optional<SchoolOrganizationCodeEntity> schoolOrganizationCodeEntity = codeTableService.getSchoolOrganizationCode(school.getSchoolOrganizationCode());
      if (schoolOrganizationCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(SCHOOL_ORGANIZATION_CODE, school.getSchoolOrganizationCode(), "Invalid organization code."));
      } else if (schoolOrganizationCodeEntity.get().getEffectiveDate() != null && schoolOrganizationCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(SCHOOL_ORGANIZATION_CODE, school.getSchoolOrganizationCode(), "Organization Code provided is not yet effective."));
      } else if (schoolOrganizationCodeEntity.get().getExpiryDate() != null && schoolOrganizationCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(SCHOOL_ORGANIZATION_CODE, school.getSchoolOrganizationCode(), "Organization Code provided has expired."));
      }
    }
  }

  protected void validateSchoolCategoryCode(School school, List<FieldError> apiValidationErrors) {
    if (school.getSchoolCategoryCode() != null) {
      Optional<SchoolCategoryCodeEntity> schoolCategoryCodeEntity = codeTableService.getSchoolCategoryCode(school.getSchoolCategoryCode());
      if (schoolCategoryCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(SCHOOL_CATEGORY_CODE, school.getSchoolCategoryCode(), "Invalid school category code."));
      } else if (schoolCategoryCodeEntity.get().getEffectiveDate() != null && schoolCategoryCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(SCHOOL_CATEGORY_CODE, school.getSchoolCategoryCode(), "School category code provided is not yet effective."));
      } else if (schoolCategoryCodeEntity.get().getExpiryDate() != null && schoolCategoryCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(SCHOOL_CATEGORY_CODE, school.getSchoolCategoryCode(), "School category code provided has expired."));
      }
    }
  }

  protected void validateSchoolFundingCodes(School school, List<FieldError> apiValidationErrors) {
    if(school.getSchoolFundingGroups() != null) {
      for (IndependentSchoolFundingGroup independentSchoolFundingGroup : school.getSchoolFundingGroups()) {
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
  }

  protected void validateFacilityTypeCode(School school, List<FieldError> apiValidationErrors) {
    if (school.getFacilityTypeCode() != null) {
      Optional<FacilityTypeCodeEntity> schoolCategoryCodeEntity = codeTableService.getFacilityTypeCode(school.getFacilityTypeCode());
      if (schoolCategoryCodeEntity.isEmpty()) {
        apiValidationErrors.add(createFieldError(FACILITY_TYPE_CODE, school.getFacilityTypeCode(), "Invalid facility type code."));
      } else if (schoolCategoryCodeEntity.get().getEffectiveDate() != null && schoolCategoryCodeEntity.get().getEffectiveDate().isAfter(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(FACILITY_TYPE_CODE, school.getFacilityTypeCode(), "Facility type code provided is not yet effective."));
      } else if (schoolCategoryCodeEntity.get().getExpiryDate() != null && schoolCategoryCodeEntity.get().getExpiryDate().isBefore(LocalDateTime.now())) {
        apiValidationErrors.add(createFieldError(FACILITY_TYPE_CODE, school.getFacilityTypeCode(), "Facility type code provided has expired."));
      }
    }
  }

  protected void validateVendorSourceSystemCode(School school, List<FieldError> apiValidationErrors) {
    if (school.getVendorSourceSystemCode() != null) {
      Optional<VendorSourceSystemCodeEntity> vendorSourceSystemCode = codeTableService.getVendorSourceSystemCode(school.getVendorSourceSystemCode());
      if (vendorSourceSystemCode.isEmpty()) {
        apiValidationErrors.add(createFieldError(VENDOR_SOURCE_SYSTEM_CODE, school.getVendorSourceSystemCode(), "Invalid vendor source system code."));
      }
    }
  }

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("school", fieldName, rejectedValue, false, null, null, message);
  }
}
