package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentSchoolFundingGroupMapper;
import ca.bc.gov.educ.api.institute.model.v1.IndependentSchoolFundingGroupEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentSchoolFundingGroupHistoryEntity;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentSchoolFundingGroupHistoryRepository;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentSchoolFundingGroupRepository;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentSchoolFundingGroup;
import ca.bc.gov.educ.api.institute.util.TransformUtil;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IndependentSchoolFundingGroupService {

  @Getter(AccessLevel.PRIVATE)
  private final IndependentSchoolFundingGroupRepository independentSchoolFundingGroupRepository;
  private final IndependentSchoolFundingGroupHistoryRepository independentSchoolFundingGroupHistoryRepository;
  private static final IndependentSchoolFundingGroupMapper independentSchoolFundingGroupMapper = IndependentSchoolFundingGroupMapper.mapper;

  @Autowired
  public IndependentSchoolFundingGroupService(IndependentSchoolFundingGroupRepository independentSchoolFundingGroupRepository, IndependentSchoolFundingGroupHistoryRepository independentSchoolFundingGroupHistoryRepository) {
    this.independentSchoolFundingGroupRepository = independentSchoolFundingGroupRepository;
      this.independentSchoolFundingGroupHistoryRepository = independentSchoolFundingGroupHistoryRepository;
  }

  public Optional<IndependentSchoolFundingGroupEntity> getIndependentSchoolFundingGroup(UUID schoolFundingGroupID) {
    return independentSchoolFundingGroupRepository.findById(schoolFundingGroupID);
  }

  public List<IndependentSchoolFundingGroupEntity> getIndependentSchoolFundingGroups(UUID schoolID) {
    return independentSchoolFundingGroupRepository.findAllBySchoolID(schoolID);
  }

  public List<IndependentSchoolFundingGroupHistoryEntity> getIndependentSchoolFundingGroupHistory(UUID schoolID) {
    return independentSchoolFundingGroupHistoryRepository.findAllBySchoolID(schoolID);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public IndependentSchoolFundingGroupEntity createIndependentSchoolFundingGroup(IndependentSchoolFundingGroup independentSchoolFundingGroup) {
    IndependentSchoolFundingGroupEntity independentSchoolFundingGroupEntity = IndependentSchoolFundingGroupMapper.mapper.toModel(independentSchoolFundingGroup);
    TransformUtil.uppercaseFields(independentSchoolFundingGroupEntity);

    var savedEntity = independentSchoolFundingGroupRepository.save(independentSchoolFundingGroupEntity);
    IndependentSchoolFundingGroupHistoryEntity historyEntity = getIndependentSchoolFundingGroupHistoryEntity(savedEntity);
    independentSchoolFundingGroupHistoryRepository.save(historyEntity);

    return savedEntity;
  }

  private static IndependentSchoolFundingGroupHistoryEntity getIndependentSchoolFundingGroupHistoryEntity(IndependentSchoolFundingGroupEntity savedEntity) {
    IndependentSchoolFundingGroupHistoryEntity historyEntity = new IndependentSchoolFundingGroupHistoryEntity();
    historyEntity.setSchoolFundingGroupID(savedEntity.getSchoolFundingGroupID());
    historyEntity.setSchoolID(savedEntity.getSchoolID());
    historyEntity.setSchoolGradeCode(savedEntity.getSchoolGradeCode());
    historyEntity.setSchoolFundingGroupCode(savedEntity.getSchoolFundingGroupCode());
    historyEntity.setCreateUser(savedEntity.getCreateUser());
    historyEntity.setCreateDate(savedEntity.getCreateDate());
    historyEntity.setUpdateUser(savedEntity.getUpdateUser());
    historyEntity.setUpdateDate(savedEntity.getUpdateDate());
    return historyEntity;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteIndependentSchoolFundingGroup(UUID schoolFundingGroupID) {
    Optional<IndependentSchoolFundingGroupEntity> entityOptional = independentSchoolFundingGroupRepository.findById(schoolFundingGroupID);
    IndependentSchoolFundingGroupEntity entity = entityOptional.orElseThrow(() -> new EntityNotFoundException(IndependentSchoolFundingGroupEntity.class, "schoolFundingGroupID", schoolFundingGroupID.toString()));
    independentSchoolFundingGroupRepository.delete(entity);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public IndependentSchoolFundingGroupEntity updateIndependentSchoolFundingGroup(IndependentSchoolFundingGroup independentSchoolFundingGroup) {
    Optional<IndependentSchoolFundingGroupEntity> independentSchoolFundingGroupEntity = independentSchoolFundingGroupRepository.findById(UUID.fromString(independentSchoolFundingGroup.getSchoolFundingGroupID()));

    if(independentSchoolFundingGroupEntity.isEmpty()) {
      throw new EntityNotFoundException(IndependentSchoolFundingGroupEntity.class, "IndependentSchoolFundingGroupEntity", independentSchoolFundingGroup.getSchoolFundingGroupID());
    }

    var curIndependentSchoolFundingGroupEntity = independentSchoolFundingGroupEntity.get();
    var incomingIndependentSchoolFundingGroupEntity = independentSchoolFundingGroupMapper.toModel(independentSchoolFundingGroup);
    BeanUtils.copyProperties(incomingIndependentSchoolFundingGroupEntity, curIndependentSchoolFundingGroupEntity, "schoolFundingGroupID", "schoolID", "createUser", "createDate");
    TransformUtil.uppercaseFields(curIndependentSchoolFundingGroupEntity);

    var savedEntity = independentSchoolFundingGroupRepository.save(curIndependentSchoolFundingGroupEntity);
    IndependentSchoolFundingGroupHistoryEntity historyEntity = getIndependentSchoolFundingGroupHistoryEntity(savedEntity);
    independentSchoolFundingGroupHistoryRepository.save(historyEntity);

    return savedEntity;
  }

}
