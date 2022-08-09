package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.CodeTableAPIEndpoint;
import ca.bc.gov.educ.api.institute.mapper.v1.CodeTableMapper;
import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.struct.v1.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class CodeTableAPIController implements CodeTableAPIEndpoint {

  private static final CodeTableMapper mapper = CodeTableMapper.mapper;
  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService service;

  @Autowired
  public CodeTableAPIController(CodeTableService service) {
    this.service = service;
  }

  public List<AddressTypeCode> getAddressTypeCodes() {
    return getService().getAddressTypeCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public List<ProvinceCode> getProvinceCodes() {
    return getService().getProvinceCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public List<CountryCode> getCountryCodes() { return getService().getCountryCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public List<DistrictRegionCode> getDistrictRegionCodes() {
    return getService().getDistrictRegionCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public List<DistrictStatusCode> getDistrictStatusCodes() {
    return getService().getDistrictStatusCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public List<ContactTypeCode> getContactTypeCodes() {
    return getService().getContactTypeCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public List<AuthorityTypeCode> getAuthorityTypeCodes() {
    return getService().getAuthorityTypeCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public List<NeighborhoodLearningTypeCode> getNeighborhoodLearningCodes() {
    return getService().getNeighborhoodLearningTypeCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public List<FacilityTypeCode> getFacilityTypeCodes() {
    return getService().getFacilityTypeCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public List<SchoolCategoryCode> getSchoolCategoryCodes() {
    return getService().getSchoolCategoryCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public List<SchoolOrganizationCode> getSchoolOrganizationCodes() {
    return getService().getSchoolOrganizationCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public List<SchoolGradeCode> getSchoolGradeCodes() {
    return getService().getSchoolGradeCodesList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

}
