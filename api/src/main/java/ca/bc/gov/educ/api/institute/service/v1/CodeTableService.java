package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CodeTableService {

  private final AddressTypeCodeRepository addressTypeCodeRepository;

  private final AuthorityTypeCodeRepository authorityTypeCodeRepository;

  private final DistrictContactTypeCodeRepository districtContactTypeCodeRepository;

  private final SchoolContactTypeCodeRepository schoolContactTypeCodeRepository;

  private final AuthorityContactTypeCodeRepository authorityContactTypeCodeRepository;

  private final DistrictRegionCodeRepository districtRegionCodeRepository;

  private final DistrictStatusCodeRepository districtStatusCodeRepository;

  private final FacilityTypeCodeRepository facilityTypeCodeRepository;

  private final NeighborhoodLearningTypeCodeRepository neighborhoodLearningTypeCodeRepository;

  private final ProvinceCodeRepository provinceCodeRepository;

  private final CountryCodeRepository countryCodeRepository;

  private final SchoolGradeCodeRepository schoolGradeCodeRepository;

  private final SchoolOrganizationCodeRepository schoolOrganizationCodeRepository;

  private final SchoolReportingRequirementCodeRepository schoolReportingRequirementCodeRepository;

  private final SchoolCategoryCodeRepository schoolCategoryCodeRepository;

  /**
   * Instantiates a new Code table service.
   *
   * @param addressTypeCodeRepository
   * @param authorityTypeCodeRepository
   * @param districtContactTypeCodeRepository
   * @param schoolContactTypeCodeRepository
   * @param authorityContactTypeCodeRepository
   * @param districtStatusCodeRepository
   * @param facilityTypeCodeRepository
   * @param provinceCodeRepository
   * @param countryCodeRepository
   * @param schoolGradeCodeRepository
   * @param schoolOrganizationCodeRepository
   * @param schoolCategoryCodeRepository
   */
  @Autowired
  public CodeTableService(
    AddressTypeCodeRepository addressTypeCodeRepository,
    AuthorityTypeCodeRepository authorityTypeCodeRepository,
    DistrictContactTypeCodeRepository districtContactTypeCodeRepository,
    SchoolContactTypeCodeRepository schoolContactTypeCodeRepository,
    AuthorityContactTypeCodeRepository authorityContactTypeCodeRepository,
    DistrictRegionCodeRepository districtRegionCodeRepository,
    DistrictStatusCodeRepository districtStatusCodeRepository,
    FacilityTypeCodeRepository facilityTypeCodeRepository,
    NeighborhoodLearningTypeCodeRepository neighborhoodLearningTypeCodeRepository,
    ProvinceCodeRepository provinceCodeRepository,
    CountryCodeRepository countryCodeRepository,
    SchoolGradeCodeRepository schoolGradeCodeRepository,
    SchoolOrganizationCodeRepository schoolOrganizationCodeRepository,
    SchoolCategoryCodeRepository schoolCategoryCodeRepository,
    SchoolReportingRequirementCodeRepository schoolReportingRequirementCodeRepository
  ) {
    this.addressTypeCodeRepository = addressTypeCodeRepository;
    this.authorityTypeCodeRepository = authorityTypeCodeRepository;
    this.districtContactTypeCodeRepository = districtContactTypeCodeRepository;
    this.schoolContactTypeCodeRepository = schoolContactTypeCodeRepository;
    this.authorityContactTypeCodeRepository = authorityContactTypeCodeRepository;
    this.districtRegionCodeRepository = districtRegionCodeRepository;
    this.districtStatusCodeRepository = districtStatusCodeRepository;
    this.facilityTypeCodeRepository = facilityTypeCodeRepository;
    this.neighborhoodLearningTypeCodeRepository = neighborhoodLearningTypeCodeRepository;
    this.provinceCodeRepository = provinceCodeRepository;
    this.countryCodeRepository = countryCodeRepository;
    this.schoolGradeCodeRepository = schoolGradeCodeRepository;
    this.schoolOrganizationCodeRepository = schoolOrganizationCodeRepository;
    this.schoolReportingRequirementCodeRepository = schoolReportingRequirementCodeRepository;
    this.schoolCategoryCodeRepository = schoolCategoryCodeRepository;
  }

  @Cacheable("addressTypeCodes")
  public List<AddressTypeCodeEntity> getAddressTypeCodesList() {
    return addressTypeCodeRepository.findAll();
  }

  @Cacheable("authorityTypeCodes")
  public List<AuthorityTypeCodeEntity> getAuthorityTypeCodesList() {
    return authorityTypeCodeRepository.findAll();
  }

  @Cacheable("districtContactTypeCodes")
  public List<DistrictContactTypeCodeEntity> getDistrictContactTypeCodesList() {
    return districtContactTypeCodeRepository.findAll();
  }

  @Cacheable("schoolContactTypeCodes")
  public List<SchoolContactTypeCodeEntity> getSchoolContactTypeCodesList() {
    return schoolContactTypeCodeRepository.findAll();
  }

  @Cacheable("authorityContactTypeCodes")
  public List<AuthorityContactTypeCodeEntity> getAuthorityContactTypeCodesList() {
    return authorityContactTypeCodeRepository.findAll();
  }

  @Cacheable("districtRegionCodes")
  public List<DistrictRegionCodeEntity> getDistrictRegionCodesList() {
    return districtRegionCodeRepository.findAll();
  }

  @Cacheable("districtStatusCodes")
  public List<DistrictStatusCodeEntity> getDistrictStatusCodesList() {
    return districtStatusCodeRepository.findAll();
  }

  @Cacheable("facilityTypeCodes")
  public List<FacilityTypeCodeEntity> getFacilityTypeCodesList() {
    return facilityTypeCodeRepository.findAll();
  }

  @Cacheable("neighborhoodLearningTypeCodes")
  public List<NeighborhoodLearningTypeCodeEntity> getNeighborhoodLearningTypeCodesList() {
    return neighborhoodLearningTypeCodeRepository.findAll();
  }

  @Cacheable("provinceCodes")
  public List<ProvinceCodeEntity> getProvinceCodesList() {
    return provinceCodeRepository.findAll();
  }

  @Cacheable("countryCodes")
  public List<CountryCodeEntity> getCountryCodesList() {
    return countryCodeRepository.findAll();
  }

  @Cacheable("schoolGradeCodes")
  public List<SchoolGradeCodeEntity> getSchoolGradeCodesList() {
    return schoolGradeCodeRepository.findAll();
  }

  @Cacheable("schoolOrganizationCodes")
  public List<SchoolOrganizationCodeEntity> getSchoolOrganizationCodesList() {
    return schoolOrganizationCodeRepository.findAll();
  }

  @Cacheable("schoolCategoryCodes")
  public List<SchoolCategoryCodeEntity> getSchoolCategoryCodesList() {
    return schoolCategoryCodeRepository.findAll();
  }

  public Optional<AddressTypeCodeEntity> getAddressTypeCode(String addressTypeCode) {
    return addressTypeCodeRepository.findById(addressTypeCode);
  }

  public Optional<AuthorityTypeCodeEntity> getAuthorityTypeCode(String authorityTypeCode) {
    return authorityTypeCodeRepository.findById(authorityTypeCode);
  }

  public Optional<DistrictContactTypeCodeEntity> getDistrictContactTypeCode(String districtContactTypeCode) {
    return districtContactTypeCodeRepository.findById(districtContactTypeCode);
  }

  public Optional<SchoolContactTypeCodeEntity> getSchoolContactTypeCode(String schoolContactTypeCode) {
    return schoolContactTypeCodeRepository.findById(schoolContactTypeCode);
  }

  public Optional<AuthorityContactTypeCodeEntity> getAuthorityContactTypeCode(String authorityContactTypeCode) {
    return authorityContactTypeCodeRepository.findById(authorityContactTypeCode);
  }

  public Optional<DistrictRegionCodeEntity> getDistrictRegionCode(String districtRegionCode) {
    return districtRegionCodeRepository.findById(districtRegionCode);
  }

  public Optional<DistrictStatusCodeEntity> getDistrictStatusCode(String districtStatusCode) {
    return districtStatusCodeRepository.findById(districtStatusCode);
  }

  public Optional<FacilityTypeCodeEntity> getFacilityTypeCode(String facilityTypeCode) {
    return facilityTypeCodeRepository.findById(facilityTypeCode);
  }

  public Optional<NeighborhoodLearningTypeCodeEntity> getNeighborhoodLearningTypeCode(String neighborhoodLearningTypeCode) {
    return neighborhoodLearningTypeCodeRepository.findById(neighborhoodLearningTypeCode);
  }

  public Optional<ProvinceCodeEntity> getProvinceCode(String provinceCode) {
    return provinceCodeRepository.findById(provinceCode);
  }

  public Optional<CountryCodeEntity> getCountryCode(String countryCode) {
    return countryCodeRepository.findById(countryCode);
  }

  public Optional<SchoolGradeCodeEntity> getSchoolGradeCode(String schoolGradeCode) {
    return schoolGradeCodeRepository.findById(schoolGradeCode);
  }

  public Optional<SchoolOrganizationCodeEntity> getSchoolOrganizationCode(String schoolOrganizationCode) {
    return schoolOrganizationCodeRepository.findById(schoolOrganizationCode);
  }

  public Optional<SchoolCategoryCodeEntity> getSchoolCategoryCode(String schoolCategoryCode) {
    return schoolCategoryCodeRepository.findById(schoolCategoryCode);
  }
}
