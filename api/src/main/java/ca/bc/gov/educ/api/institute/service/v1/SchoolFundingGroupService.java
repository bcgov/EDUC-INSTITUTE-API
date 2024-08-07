package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.model.v1.IndependentSchoolFundingGroupEntity;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentSchoolFundingGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolFundingGroupService {

  private final IndependentSchoolFundingGroupRepository independentSchoolFundingGroupRepository;

  public SchoolFundingGroupService(IndependentSchoolFundingGroupRepository independentSchoolFundingGroupRepository) {
      this.independentSchoolFundingGroupRepository = independentSchoolFundingGroupRepository;
  }

  public List<IndependentSchoolFundingGroupEntity> getAllSchoolFundingGroups() {
    return independentSchoolFundingGroupRepository.findAll();
  }
}
