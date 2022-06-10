package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.IndependentAuthorityAPIEndpoint;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.institute.exception.errors.ApiError;
import ca.bc.gov.educ.api.institute.mapper.v1.AddressMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.ContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.service.v1.IndependentAuthorityHistoryService;
import ca.bc.gov.educ.api.institute.service.v1.IndependentAuthorityService;
import ca.bc.gov.educ.api.institute.struct.v1.*;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.validator.AddressPayloadValidator;
import ca.bc.gov.educ.api.institute.validator.ContactPayloadValidator;
import ca.bc.gov.educ.api.institute.validator.IndependentAuthorityPayloadValidator;
import ca.bc.gov.educ.api.institute.validator.NotePayloadValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Slf4j
public class IndependentAuthorityAPIController implements IndependentAuthorityAPIEndpoint {

  private static final IndependentAuthorityMapper mapper = IndependentAuthorityMapper.mapper;
  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityService independentAuthorityService;
  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityHistoryService independentAuthorityHistoryService;

  private static final ContactMapper contactMapper = ContactMapper.mapper;

  private static final AddressMapper addressMapper = AddressMapper.mapper;

  private static final NoteMapper noteMapper = NoteMapper.mapper;

  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityPayloadValidator payloadValidator;

  private final ContactPayloadValidator contactPayloadValidator;

  private final AddressPayloadValidator addressPayloadValidator;

  private final NotePayloadValidator notePayloadValidator;

  @Autowired
  public IndependentAuthorityAPIController(final IndependentAuthorityService independentAuthorityService, final IndependentAuthorityHistoryService independentAuthorityHistoryService, final IndependentAuthorityPayloadValidator payloadValidator, ContactPayloadValidator contactPayloadValidator, AddressPayloadValidator addressPayloadValidator, NotePayloadValidator notePayloadValidator) {
    this.independentAuthorityService = independentAuthorityService;
    this.independentAuthorityHistoryService = independentAuthorityHistoryService;
    this.payloadValidator = payloadValidator;
    this.contactPayloadValidator = contactPayloadValidator;
    this.addressPayloadValidator = addressPayloadValidator;
    this.notePayloadValidator = notePayloadValidator;
  }

  @Override
  public IndependentAuthority getIndependentAuthority(UUID independentAuthorityId) {
    var independentAuthority = getIndependentAuthorityService().getIndependentAuthority(independentAuthorityId);

    if(independentAuthority.isPresent()){
      return mapper.toStructure(independentAuthority.get());
    }else{
      throw new EntityNotFoundException();
    }
  }

  @Override
  public List<IndependentAuthorityHistory> getIndependentAuthorityHistory(UUID independentAuthorityId) {
    return getIndependentAuthorityHistoryService().getAllIndependentAuthorityHistoryList(independentAuthorityId).stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public IndependentAuthority createIndependentAuthority(IndependentAuthority independentAuthority) {
    validatePayload(() -> getPayloadValidator().validateCreatePayload(independentAuthority));
    RequestUtil.setAuditColumnsForCreate(independentAuthority);
    return mapper.toStructure(independentAuthorityService.createIndependentAuthority(independentAuthority));
  }

  @Override
  public IndependentAuthority updateIndependentAuthority(UUID id, IndependentAuthority independentAuthority) {
    validatePayload(() -> getPayloadValidator().validateUpdatePayload(independentAuthority));
    RequestUtil.setAuditColumnsForUpdate(independentAuthority);
    return mapper.toStructure(independentAuthorityService.updateIndependentAuthority(independentAuthority, id));
  }

  @Override
  @Transactional
  public ResponseEntity<Void> deleteIndependentAuthority(UUID id) {
    getIndependentAuthorityService().deleteIndependentAuthority(id);
    return ResponseEntity.noContent().build();
  }

  private void validatePayload(Supplier<List<FieldError>> validator) {
    val validationResult = validator.get();
    if (!validationResult.isEmpty()) {
      ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message("Payload contains invalid data.").status(BAD_REQUEST).build();
      error.addValidationErrors(validationResult);
      throw new InvalidPayloadException(error);
    }
  }

  @Override
  public List<IndependentAuthority> getAllIndependentAuthorities() {
    return getIndependentAuthorityService().getAllIndependentAuthoritysList().stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public Contact getIndependentAuthorityContact(UUID independentAuthorityId, UUID contactId) {
    var contactEntity = this.independentAuthorityService.getIndependentAuthorityContact(independentAuthorityId, contactId);

    if (contactEntity.isPresent()) {
      return contactMapper.toStructure(contactEntity.get());
    } else {
      throw new EntityNotFoundException();
    }
  }

  @Override
  public Contact createIndependentAuthorityContact(UUID independentAuthorityId, Contact contact) {
    validatePayload(() -> this.contactPayloadValidator.validateCreatePayload(contact));
    RequestUtil.setAuditColumnsForCreate(contact);
    return contactMapper.toStructure(independentAuthorityService.createIndependentAuthorityContact(contact, independentAuthorityId));
  }

  @Override
  public Contact updateIndependentAuthorityContact(UUID independentAuthorityId, UUID contactId, Contact contact) {
    validatePayload(() -> this.contactPayloadValidator.validateUpdatePayload(contact));
    RequestUtil.setAuditColumnsForUpdate(contact);
    return contactMapper.toStructure(independentAuthorityService.updateIndependentAuthorityContact(contact, independentAuthorityId, contactId));
  }

  @Override
  public ResponseEntity<Void> deleteIndependentAuthorityContact(UUID independentAuthorityId, UUID contactId) {
    this.independentAuthorityService.deleteIndependentAuthorityContact(independentAuthorityId, contactId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public Address getIndependentAuthorityAddress(UUID independentAuthorityId, UUID addressId) {
    var addressEntity = this.independentAuthorityService.getIndependentAuthorityAddress(independentAuthorityId, addressId);

    if (addressEntity.isPresent()) {
      return addressMapper.toStructure(addressEntity.get());
    } else {
      throw new EntityNotFoundException();
    }
  }

  @Override
  public Address createIndependentAuthorityAddress(UUID independentAuthorityId, Address address) {
    validatePayload(() -> this.addressPayloadValidator.validateCreatePayload(address));
    RequestUtil.setAuditColumnsForCreate(address);
    return addressMapper.toStructure(independentAuthorityService.createIndependentAuthorityAddress(address, independentAuthorityId));
  }

  @Override
  public Address updateIndependentAuthorityAddress(UUID independentAuthorityId, UUID addressId, Address address) {
    validatePayload(() -> this.addressPayloadValidator.validateUpdatePayload(address));
    RequestUtil.setAuditColumnsForUpdate(address);
    return addressMapper.toStructure(independentAuthorityService.updateIndependentAuthorityAddress(address, independentAuthorityId, addressId));
  }

  @Override
  public ResponseEntity<Void> deleteIndependentAuthorityAddress(UUID independentAuthorityId, UUID addressId) {
    this.independentAuthorityService.deleteIndependentAuthorityAddress(independentAuthorityId, addressId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public Note getIndependentAuthorityNote(UUID independentAuthorityId, UUID noteId) {
    var noteEntity = this.independentAuthorityService.getIndependentAuthorityNote(independentAuthorityId, noteId);

    if (noteEntity.isPresent()) {
      return noteMapper.toStructure(noteEntity.get());
    } else {
      throw new EntityNotFoundException();
    }
  }

  @Override
  public Note createIndependentAuthorityNote(UUID independentAuthorityId, Note note) {
    validatePayload(() -> this.notePayloadValidator.validateCreatePayload(note));
    RequestUtil.setAuditColumnsForCreate(note);
    return noteMapper.toStructure(independentAuthorityService.createIndependentAuthorityNote(note, independentAuthorityId));
  }

  @Override
  public Note updateIndependentAuthorityNote(UUID independentAuthorityId, UUID noteId, Note note) {
    validatePayload(() -> this.notePayloadValidator.validateUpdatePayload(note));
    RequestUtil.setAuditColumnsForUpdate(note);
    return noteMapper.toStructure(independentAuthorityService.updateIndependentAuthorityNote(note, independentAuthorityId, noteId));
  }

  @Override
  public ResponseEntity<Void> deleteIndependentAuthorityNote(UUID independentAuthorityId, UUID noteId) {
    this.independentAuthorityService.deleteIndependentAuthorityNote(independentAuthorityId, noteId);
    return ResponseEntity.noContent().build();
  }
}
