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

  private final ContactTypeCodeRepository contactTypeCodeRepository;

  private final DistrictRegionCodeRepository districtRegionCodeRepository;

  private final DistrictStatusCodeRepository districtStatusCodeRepository;

  private final FacilityTypeCodeRepository facilityTypeCodeRepository;

  private final NeighborhoodLearningTypeCodeRepository neighborhoodLearningTypeCodeRepository;

  private final ProvinceCodeRepository provinceCodeRepository;

  private final SchoolGradeCodeRepository schoolGradeCodeRepository;

  private final SchoolOrganizationCodeRepository schoolOrganizationCodeRepository;

  private final SchoolCategoryCodeRepository schoolCategoryCodeRepository;

  /**
   * Instantiates a new Code table service.
   *
   * @param addressTypeCodeRepository
   * @param authorityTypeCodeRepository
   * @param contactTypeCodeRepository
   * @param districtStatusCodeRepository
   * @param facilityTypeCodeRepository
   * @param provinceCodeRepository
   * @param schoolGradeCodeRepository
   * @param schoolOrganizationCodeRepository
   * @param schoolCategoryCodeRepository
   */
  @Autowired
  public CodeTableService(AddressTypeCodeRepository addressTypeCodeRepository, AuthorityTypeCodeRepository authorityTypeCodeRepository,
                          ContactTypeCodeRepository contactTypeCodeRepository, DistrictRegionCodeRepository districtRegionCodeRepository, DistrictStatusCodeRepository districtStatusCodeRepository, FacilityTypeCodeRepository facilityTypeCodeRepository,
                          NeighborhoodLearningTypeCodeRepository neighborhoodLearningTypeCodeRepository, ProvinceCodeRepository provinceCodeRepository,
                          SchoolGradeCodeRepository schoolGradeCodeRepository, SchoolOrganizationCodeRepository schoolOrganizationCodeRepository, SchoolCategoryCodeRepository schoolCategoryCodeRepository) {
    this.addressTypeCodeRepository = addressTypeCodeRepository;
    this.authorityTypeCodeRepository = authorityTypeCodeRepository;
    this.contactTypeCodeRepository = contactTypeCodeRepository;
    this.districtRegionCodeRepository = districtRegionCodeRepository;
    this.districtStatusCodeRepository = districtStatusCodeRepository;
    this.facilityTypeCodeRepository = facilityTypeCodeRepository;
    this.neighborhoodLearningTypeCodeRepository = neighborhoodLearningTypeCodeRepository;
    this.provinceCodeRepository = provinceCodeRepository;
    this.schoolGradeCodeRepository = schoolGradeCodeRepository;
    this.schoolOrganizationCodeRepository = schoolOrganizationCodeRepository;
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

  @Cacheable("contactTypeCodes")
  public List<ContactTypeCodeEntity> getContactTypeCodesList() {
    return contactTypeCodeRepository.findAll();
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

  public Optional<ContactTypeCodeEntity> getContactTypeCode(String contactTypeCode) {
    return contactTypeCodeRepository.findById(contactTypeCode);
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
