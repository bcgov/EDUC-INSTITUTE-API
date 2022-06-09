package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityRepository;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthority;
import ca.bc.gov.educ.api.institute.util.TransformUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IndependentAuthorityService {

  private static final String DISTRICT_ID_ATTR = "independentAuthorityId";

  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityRepository independentAuthorityRepository;

  private final IndependentAuthorityHistoryService independentAuthorityHistoryService;


  @Autowired
  public IndependentAuthorityService(IndependentAuthorityRepository independentAuthorityRepository, IndependentAuthorityHistoryService independentAuthorityHistoryService) {
    this.independentAuthorityRepository = independentAuthorityRepository;
    this.independentAuthorityHistoryService = independentAuthorityHistoryService;
  }

  public List<IndependentAuthorityEntity> getAllIndependentAuthoritysList() {
    return independentAuthorityRepository.findAll();
  }

  public Optional<IndependentAuthorityEntity> getIndependentAuthority(UUID independentAuthorityId) {
    return independentAuthorityRepository.findById(independentAuthorityId);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public IndependentAuthorityEntity createIndependentAuthority(IndependentAuthority independentAuthority) {
    var independentAuthorityEntity = IndependentAuthorityMapper.mapper.toModel(independentAuthority);
    TransformUtil.uppercaseFields(independentAuthorityEntity);
    independentAuthorityRepository.save(independentAuthorityEntity);
    independentAuthorityHistoryService.createIndependentAuthorityHistory(independentAuthorityEntity, independentAuthority.getCreateUser(), false);
    return independentAuthorityEntity;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteIndependentAuthority(UUID independentAuthorityId) {
    val entityOptional = independentAuthorityRepository.findById(independentAuthorityId);
    val entity = entityOptional.orElseThrow(() -> new EntityNotFoundException(IndependentAuthorityEntity.class, DISTRICT_ID_ATTR, independentAuthorityId.toString()));
    independentAuthorityHistoryService.deleteByIndependentAuthorityID(independentAuthorityId);
    independentAuthorityRepository.delete(entity);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {EntityNotFoundException.class})
  public IndependentAuthorityEntity updateIndependentAuthority(IndependentAuthority independentAuthorityUpdate, UUID independentAuthorityId) {
    var independentAuthority = IndependentAuthorityMapper.mapper.toModel(independentAuthorityUpdate);
    if (independentAuthorityId == null || !independentAuthorityId.equals(independentAuthority.getIndependentAuthorityId())) {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, DISTRICT_ID_ATTR, String.valueOf(independentAuthorityId));
    }

    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthority.getIndependentAuthorityId());

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      BeanUtils.copyProperties(independentAuthority, currentIndependentAuthorityEntity, "createDate", "createUser"); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentIndependentAuthorityEntity); // convert the input to upper case.
      independentAuthorityHistoryService.createIndependentAuthorityHistory(currentIndependentAuthorityEntity, currentIndependentAuthorityEntity.getUpdateUser(), false);
      independentAuthorityRepository.save(currentIndependentAuthorityEntity);
      return currentIndependentAuthorityEntity;
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, DISTRICT_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }
}
