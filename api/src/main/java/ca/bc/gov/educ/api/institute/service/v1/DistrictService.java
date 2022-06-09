package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.ContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.DistrictMapper;
import ca.bc.gov.educ.api.institute.model.v1.ContactEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.repository.v1.ContactRepository;
import ca.bc.gov.educ.api.institute.repository.v1.DistrictRepository;
import ca.bc.gov.educ.api.institute.struct.v1.Contact;
import ca.bc.gov.educ.api.institute.struct.v1.District;
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
public class DistrictService {

  private static final String DISTRICT_ID_ATTR = "districtId";

  private static final String CONTACT_ID_ATTR = "contactId";

  @Getter(AccessLevel.PRIVATE)
  private final DistrictRepository districtRepository;

  private final DistrictHistoryService districtHistoryService;

  private final ContactRepository contactRepository;


  @Autowired
  public DistrictService(DistrictRepository districtRepository, DistrictHistoryService districtHistoryService, ContactRepository contactRepository) {
    this.districtRepository = districtRepository;
    this.districtHistoryService = districtHistoryService;
    this.contactRepository = contactRepository;
  }

  public List<DistrictEntity> getAllDistrictsList() {
    return districtRepository.findAll();
  }

  public Optional<DistrictEntity> getDistrict(UUID districtId) {
    return districtRepository.findById(districtId);
  }

  public Optional<ContactEntity> getDistrictContact(UUID districtId, UUID contactId) {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      return contactRepository.findByContactIdAndDistrictEntity(contactId, currentDistrictEntity);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ContactEntity createDistrictContact(Contact contact, UUID districtId) {
    var contactEntity = ContactMapper.mapper.toModel(contact);
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      contactEntity.setDistrictEntity(curDistrictEntityOptional.get());
      TransformUtil.uppercaseFields(contactEntity);
      contactRepository.save(contactEntity);
      return contactEntity;
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ContactEntity updateDistrictContact(Contact contact, UUID districtId, UUID contactId) {
    var contactEntity = ContactMapper.mapper.toModel(contact);
    if (contactId == null || !contactId.equals(contactEntity.getContactId())) {
      throw new EntityNotFoundException(ContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }

    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }

    Optional<ContactEntity> curContactEntityOptional = contactRepository.findById(contactEntity.getContactId());

    if (curContactEntityOptional.isPresent()) {
      if (!districtId.equals(curContactEntityOptional.get().getDistrictEntity().getDistrictId())) {
        throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
      }
      final ContactEntity currentContactEntity = curContactEntityOptional.get();
      BeanUtils.copyProperties(contactEntity, currentContactEntity, "createDate", "createUser"); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentContactEntity); // convert the input to upper case.
      currentContactEntity.setDistrictEntity(curDistrictEntityOptional.get());
      contactRepository.save(currentContactEntity);
      return currentContactEntity;
    } else {
      throw new EntityNotFoundException(ContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteDistrictContact(UUID districtId, UUID contactId) {
    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(districtId);

    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      contactRepository.deleteByContactIdAndDistrictEntity(contactId, currentDistrictEntity);
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }

  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public DistrictEntity createDistrict(District district) {
    var districtEntity = DistrictMapper.mapper.toModel(district);
    TransformUtil.uppercaseFields(districtEntity);
    districtRepository.save(districtEntity);
    districtHistoryService.createDistrictHistory(districtEntity, district.getCreateUser(), false);
    return districtEntity;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteDistrict(UUID districtId) {
    val entityOptional = districtRepository.findById(districtId);
    val entity = entityOptional.orElseThrow(() -> new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, districtId.toString()));
    districtHistoryService.deleteByDistrictID(districtId);
    districtRepository.delete(entity);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {EntityNotFoundException.class})
  public DistrictEntity updateDistrict(District districtUpdate, UUID districtId) {
    var district = DistrictMapper.mapper.toModel(districtUpdate);
    if (districtId == null || !districtId.equals(district.getDistrictId())) {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }

    Optional<DistrictEntity> curDistrictEntityOptional = districtRepository.findById(district.getDistrictId());

    if (curDistrictEntityOptional.isPresent()) {
      final DistrictEntity currentDistrictEntity = curDistrictEntityOptional.get();
      BeanUtils.copyProperties(district, currentDistrictEntity, "createDate", "createUser"); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentDistrictEntity); // convert the input to upper case.
      districtHistoryService.createDistrictHistory(currentDistrictEntity, currentDistrictEntity.getUpdateUser(), false);
      districtRepository.save(currentDistrictEntity);
      return currentDistrictEntity;
    } else {
      throw new EntityNotFoundException(DistrictEntity.class, DISTRICT_ID_ATTR, String.valueOf(districtId));
    }
  }
}
