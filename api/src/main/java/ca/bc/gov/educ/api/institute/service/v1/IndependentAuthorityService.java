package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.AddressMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.ContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.AddressRepository;
import ca.bc.gov.educ.api.institute.repository.v1.ContactRepository;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityRepository;
import ca.bc.gov.educ.api.institute.repository.v1.NoteRepository;
import ca.bc.gov.educ.api.institute.struct.v1.Address;
import ca.bc.gov.educ.api.institute.struct.v1.Contact;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthority;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
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

  private static final String INDEPENDENT_AUTHORITY_ID_ATTR = "independentAuthorityId";

  private static final String CONTACT_ID_ATTR = "contactId";

  private static final String ADDRESS_ID_ATTR = "addressId";

  private static final String NOTE_ID_ATTR = "noteId";

  private static final String CREATE_DATE = "createDate";

  private static final String CREATE_USER = "createUser";

  private final AddressHistoryService addressHistoryService;

  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityRepository independentAuthorityRepository;

  private final IndependentAuthorityHistoryService independentAuthorityHistoryService;

  private final ContactRepository contactRepository;

  private final AddressRepository addressRepository;

  private final NoteRepository noteRepository;


  @Autowired
  public IndependentAuthorityService(AddressHistoryService addressHistoryService, IndependentAuthorityRepository independentAuthorityRepository, IndependentAuthorityHistoryService independentAuthorityHistoryService, ContactRepository contactRepository, AddressRepository addressRepository, NoteRepository noteRepository) {
    this.addressHistoryService = addressHistoryService;
    this.independentAuthorityRepository = independentAuthorityRepository;
    this.independentAuthorityHistoryService = independentAuthorityHistoryService;
    this.contactRepository = contactRepository;
    this.addressRepository = addressRepository;
    this.noteRepository = noteRepository;
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
    val entity = entityOptional.orElseThrow(() -> new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, independentAuthorityId.toString()));
    independentAuthorityHistoryService.deleteByIndependentAuthorityID(independentAuthorityId);
    independentAuthorityRepository.delete(entity);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {EntityNotFoundException.class})
  public IndependentAuthorityEntity updateIndependentAuthority(IndependentAuthority independentAuthorityUpdate, UUID independentAuthorityId) {
    var independentAuthority = IndependentAuthorityMapper.mapper.toModel(independentAuthorityUpdate);
    if (independentAuthorityId == null || !independentAuthorityId.equals(independentAuthority.getIndependentAuthorityId())) {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }

    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthority.getIndependentAuthorityId());

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      BeanUtils.copyProperties(independentAuthority, currentIndependentAuthorityEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentIndependentAuthorityEntity); // convert the input to upper case.
      independentAuthorityHistoryService.createIndependentAuthorityHistory(currentIndependentAuthorityEntity, currentIndependentAuthorityEntity.getUpdateUser(), false);
      independentAuthorityRepository.save(currentIndependentAuthorityEntity);
      return currentIndependentAuthorityEntity;
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  public Optional<ContactEntity> getIndependentAuthorityContact(UUID independentAuthorityId, UUID contactId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      return contactRepository.findByContactIdAndIndependentAuthorityEntity(contactId, currentIndependentAuthorityEntity);
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ContactEntity createIndependentAuthorityContact(Contact contact, UUID independentAuthorityId) {
    var contactEntity = ContactMapper.mapper.toModel(contact);
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      contactEntity.setIndependentAuthorityEntity(curIndependentAuthorityEntityOptional.get());
      TransformUtil.uppercaseFields(contactEntity);
      contactRepository.save(contactEntity);
      return contactEntity;
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public ContactEntity updateIndependentAuthorityContact(Contact contact, UUID independentAuthorityId, UUID contactId) {
    var contactEntity = ContactMapper.mapper.toModel(contact);
    if (contactId == null || !contactId.equals(contactEntity.getContactId())) {
      throw new EntityNotFoundException(ContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }

    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }

    Optional<ContactEntity> curContactEntityOptional = contactRepository.findById(contactEntity.getContactId());

    if (curContactEntityOptional.isPresent()) {
      if (!independentAuthorityId.equals(curContactEntityOptional.get().getIndependentAuthorityEntity().getIndependentAuthorityId())) {
        throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
      }
      final ContactEntity currentContactEntity = curContactEntityOptional.get();
      BeanUtils.copyProperties(contactEntity, currentContactEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentContactEntity); // convert the input to upper case.
      currentContactEntity.setIndependentAuthorityEntity(curIndependentAuthorityEntityOptional.get());
      contactRepository.save(currentContactEntity);
      return currentContactEntity;
    } else {
      throw new EntityNotFoundException(ContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteIndependentAuthorityContact(UUID independentAuthorityId, UUID contactId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      contactRepository.deleteByContactIdAndIndependentAuthorityEntity(contactId, currentIndependentAuthorityEntity);
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }

  }

  public Optional<AddressEntity> getIndependentAuthorityAddress(UUID independentAuthorityId, UUID addressId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      return addressRepository.findByAddressIdAndIndependentAuthorityEntity(addressId, currentIndependentAuthorityEntity);
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public AddressEntity createIndependentAuthorityAddress(Address address, UUID independentAuthorityId) {
    var addressEntity = AddressMapper.mapper.toModel(address);
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      addressEntity.setIndependentAuthorityEntity(curIndependentAuthorityEntityOptional.get());
      TransformUtil.uppercaseFields(addressEntity);
      addressRepository.save(addressEntity);
      addressHistoryService.createAddressHistory(addressEntity, address.getCreateUser(), false);
      return addressEntity;
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public AddressEntity updateIndependentAuthorityAddress(Address address, UUID independentAuthorityId, UUID addressId) {
    var addressEntity = AddressMapper.mapper.toModel(address);
    if (addressId == null || !addressId.equals(addressEntity.getAddressId())) {
      throw new EntityNotFoundException(AddressEntity.class, ADDRESS_ID_ATTR, String.valueOf(addressId));
    }

    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }

    Optional<AddressEntity> curAddressEntityOptional = addressRepository.findById(addressEntity.getAddressId());

    if (curAddressEntityOptional.isPresent()) {
      if (!independentAuthorityId.equals(curAddressEntityOptional.get().getIndependentAuthorityEntity().getIndependentAuthorityId())) {
        throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
      }
      final AddressEntity currentAddressEntity = curAddressEntityOptional.get();
      BeanUtils.copyProperties(addressEntity, currentAddressEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentAddressEntity); // convert the input to upper case.
      currentAddressEntity.setIndependentAuthorityEntity(curIndependentAuthorityEntityOptional.get());
      addressHistoryService.createAddressHistory(currentAddressEntity, currentAddressEntity.getUpdateUser(), false);
      addressRepository.save(currentAddressEntity);
      return currentAddressEntity;
    } else {
      throw new EntityNotFoundException(AddressEntity.class, ADDRESS_ID_ATTR, String.valueOf(addressId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteIndependentAuthorityAddress(UUID independentAuthorityId, UUID addressId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);
    Optional<AddressEntity> curAddressEntityOptional = addressRepository.findById(addressId);

    if (curIndependentAuthorityEntityOptional.isPresent() && curAddressEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      addressHistoryService.deleteByAddressID(addressId);
      addressRepository.deleteByAddressIdAndIndependentAuthorityEntity(addressId, currentIndependentAuthorityEntity);
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  public Optional<NoteEntity> getIndependentAuthorityNote(UUID independentAuthorityId, UUID noteId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      return noteRepository.findByNoteIdAndIndependentAuthorityEntity(noteId, currentIndependentAuthorityEntity);
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public NoteEntity createIndependentAuthorityNote(Note note, UUID independentAuthorityId) {
    var noteEntity = NoteMapper.mapper.toModel(note);
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      noteEntity.setIndependentAuthorityEntity(curIndependentAuthorityEntityOptional.get());
      TransformUtil.uppercaseFields(noteEntity);
      noteRepository.save(noteEntity);
      return noteEntity;
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public NoteEntity updateIndependentAuthorityNote(Note note, UUID independentAuthorityId, UUID noteId) {
    var noteEntity = NoteMapper.mapper.toModel(note);
    if (noteId == null || !noteId.equals(noteEntity.getNoteId())) {
      throw new EntityNotFoundException(NoteEntity.class, NOTE_ID_ATTR, String.valueOf(noteId));
    }

    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }

    Optional<NoteEntity> curNoteEntityOptional = noteRepository.findById(noteEntity.getNoteId());

    if (curNoteEntityOptional.isPresent()) {
      if (!independentAuthorityId.equals(curNoteEntityOptional.get().getIndependentAuthorityEntity().getIndependentAuthorityId())) {
        throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
      }
      final NoteEntity currentNoteEntity = curNoteEntityOptional.get();
      BeanUtils.copyProperties(noteEntity, currentNoteEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentNoteEntity); // convert the input to upper case.
      currentNoteEntity.setIndependentAuthorityEntity(curIndependentAuthorityEntityOptional.get());
      noteRepository.save(currentNoteEntity);
      return currentNoteEntity;
    } else {
      throw new EntityNotFoundException(NoteEntity.class, NOTE_ID_ATTR, String.valueOf(noteId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteIndependentAuthorityNote(UUID independentAuthorityId, UUID noteId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);
    Optional<NoteEntity> curNoteEntityOptional = noteRepository.findById(noteId);

    if (curIndependentAuthorityEntityOptional.isPresent() && curNoteEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      noteRepository.deleteByNoteIdAndIndependentAuthorityEntity(noteId, currentIndependentAuthorityEntity);
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }
}
