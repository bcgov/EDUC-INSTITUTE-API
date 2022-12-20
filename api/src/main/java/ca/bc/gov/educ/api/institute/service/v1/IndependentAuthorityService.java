package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.mapper.v1.AuthorityContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.repository.v1.AuthorityContactRepository;
import ca.bc.gov.educ.api.institute.repository.v1.IndependentAuthorityRepository;
import ca.bc.gov.educ.api.institute.repository.v1.NoteRepository;
import ca.bc.gov.educ.api.institute.struct.v1.AuthorityContact;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthority;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
import ca.bc.gov.educ.api.institute.util.BeanComparatorUtil;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.util.TransformUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndependentAuthorityService {

  private static final String INDEPENDENT_AUTHORITY_ID_ATTR = "independentAuthorityId";

  private static final String CONTACT_ID_ATTR = "contactId";

  private static final String NOTE_ID_ATTR = "noteId";

  private static final String CREATE_DATE = "createDate";

  private static final String CREATE_USER = "createUser";

  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityRepository independentAuthorityRepository;

  private final IndependentAuthorityHistoryService independentAuthorityHistoryService;

  private final AuthorityContactRepository authorityContactRepository;

  private final NoteRepository noteRepository;


  @Autowired
  public IndependentAuthorityService(IndependentAuthorityRepository independentAuthorityRepository, IndependentAuthorityHistoryService independentAuthorityHistoryService, AuthorityContactRepository authorityContactRepository, NoteRepository noteRepository) {
    this.independentAuthorityRepository = independentAuthorityRepository;
    this.independentAuthorityHistoryService = independentAuthorityHistoryService;
    this.authorityContactRepository = authorityContactRepository;
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

    String lastAuthorityNumber = independentAuthorityRepository.findLastAuthorityNumber();
    Integer lastNum = Integer.parseInt(lastAuthorityNumber);
    lastNum = lastNum + 1;
    independentAuthorityEntity.setAuthorityNumber(lastNum);
    independentAuthorityEntity.getAddresses().stream().forEach(addy -> {
      RequestUtil.setAuditColumnsForAddress(addy);
      TransformUtil.uppercaseFields(addy);
      addy.setIndependentAuthorityEntity(independentAuthorityEntity);
    });
    TransformUtil.uppercaseFields(independentAuthorityEntity);
    independentAuthorityRepository.save(independentAuthorityEntity);
    independentAuthorityHistoryService.createIndependentAuthorityHistory(independentAuthorityEntity, independentAuthority.getCreateUser());
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
      BeanUtils.copyProperties(independentAuthority, currentIndependentAuthorityEntity, CREATE_DATE, CREATE_USER, "addresses"); // update current student entity with incoming payload ignoring the fields.
      setAddresses(currentIndependentAuthorityEntity, independentAuthority);
      TransformUtil.uppercaseFields(currentIndependentAuthorityEntity); // convert the input to upper case.
      var savedAuthority = independentAuthorityRepository.save(currentIndependentAuthorityEntity);
      independentAuthorityHistoryService.createIndependentAuthorityHistory(savedAuthority, savedAuthority.getUpdateUser());
      return savedAuthority;
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  private void setAddresses(IndependentAuthorityEntity currentDistrictEntity, IndependentAuthorityEntity authority){
    Set<AuthorityAddressEntity> addresses = new HashSet<>(currentDistrictEntity.getAddresses());
    currentDistrictEntity.getAddresses().clear();
    authority.getAddresses().stream().forEach(address -> {
      if(address.getIndependentAuthorityAddressId() == null) {
        setupAddressForSave(address, currentDistrictEntity);
      }else{
        var currAddress = addresses.stream().filter(addy -> addy.getIndependentAuthorityAddressId().equals(address.getIndependentAuthorityAddressId())).collect(Collectors.toList()).get(0);
        if(!BeanComparatorUtil.compare(address, currAddress)){
          setupAddressForSave(address, currentDistrictEntity);
        }else{
          currentDistrictEntity.getAddresses().add(currAddress);
        }
      }
    });
  }

  private void setupAddressForSave(AuthorityAddressEntity address, IndependentAuthorityEntity currentAuthorityEntity){
    RequestUtil.setAuditColumnsForAddress(address);
    TransformUtil.uppercaseFields(address);
    address.setIndependentAuthorityEntity(currentAuthorityEntity);
    currentAuthorityEntity.getAddresses().add(address);
  }

  public Optional<AuthorityContactEntity> getIndependentAuthorityContact(UUID independentAuthorityId, UUID contactId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      return authorityContactRepository.findByAuthorityContactIdAndIndependentAuthorityEntity(contactId, currentIndependentAuthorityEntity);
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public AuthorityContactEntity createIndependentAuthorityContact(AuthorityContact contact, UUID independentAuthorityId) {
    var contactEntity = AuthorityContactMapper.mapper.toModel(contact);
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      contactEntity.setIndependentAuthorityEntity(curIndependentAuthorityEntityOptional.get());
      TransformUtil.uppercaseFields(contactEntity);
      authorityContactRepository.save(contactEntity);
      return contactEntity;
    } else {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public AuthorityContactEntity updateIndependentAuthorityContact(AuthorityContact contact, UUID independentAuthorityId, UUID contactId) {
    var contactEntity = AuthorityContactMapper.mapper.toModel(contact);
    if (contactId == null || !contactId.equals(contactEntity.getAuthorityContactId())) {
      throw new EntityNotFoundException(AuthorityContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }

    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isEmpty()) {
      throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
    }

    Optional<AuthorityContactEntity> curContactEntityOptional = authorityContactRepository.findById(contactEntity.getAuthorityContactId());

    if (curContactEntityOptional.isPresent()) {
      if (!independentAuthorityId.equals(curContactEntityOptional.get().getIndependentAuthorityEntity().getIndependentAuthorityId())) {
        throw new EntityNotFoundException(IndependentAuthorityEntity.class, INDEPENDENT_AUTHORITY_ID_ATTR, String.valueOf(independentAuthorityId));
      }
      final AuthorityContactEntity currentContactEntity = curContactEntityOptional.get();
      BeanUtils.copyProperties(contactEntity, currentContactEntity, CREATE_DATE, CREATE_USER); // update current student entity with incoming payload ignoring the fields.
      TransformUtil.uppercaseFields(currentContactEntity); // convert the input to upper case.
      currentContactEntity.setIndependentAuthorityEntity(curIndependentAuthorityEntityOptional.get());
      authorityContactRepository.save(currentContactEntity);
      return currentContactEntity;
    } else {
      throw new EntityNotFoundException(AuthorityContactEntity.class, CONTACT_ID_ATTR, String.valueOf(contactId));
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteIndependentAuthorityContact(UUID independentAuthorityId, UUID contactId) {
    Optional<IndependentAuthorityEntity> curIndependentAuthorityEntityOptional = independentAuthorityRepository.findById(independentAuthorityId);

    if (curIndependentAuthorityEntityOptional.isPresent()) {
      final IndependentAuthorityEntity currentIndependentAuthorityEntity = curIndependentAuthorityEntityOptional.get();
      authorityContactRepository.deleteByAuthorityContactIdAndIndependentAuthorityEntity(contactId, currentIndependentAuthorityEntity);
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
