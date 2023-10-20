package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.IndependentAuthorityAPIEndpoint;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.institute.exception.errors.ApiError;
import ca.bc.gov.educ.api.institute.mapper.v1.AuthorityContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.IndependentAuthorityMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.messaging.jetstream.Publisher;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import ca.bc.gov.educ.api.institute.service.v1.AuthoritySearchService;
import ca.bc.gov.educ.api.institute.service.v1.IndependentAuthorityHistoryService;
import ca.bc.gov.educ.api.institute.service.v1.IndependentAuthorityService;
import ca.bc.gov.educ.api.institute.struct.v1.AuthorityContact;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthority;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthorityHistory;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.validator.AuthorityContactPayloadValidator;
import ca.bc.gov.educ.api.institute.validator.IndependentAuthorityPayloadValidator;
import ca.bc.gov.educ.api.institute.validator.NotePayloadValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Slf4j
public class IndependentAuthorityAPIController implements IndependentAuthorityAPIEndpoint {

  @Getter(AccessLevel.PRIVATE)
  private final Publisher publisher;
  private static final IndependentAuthorityMapper mapper = IndependentAuthorityMapper.mapper;
  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityService independentAuthorityService;
  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityHistoryService independentAuthorityHistoryService;

  private static final AuthorityContactMapper authorityContactMapper = AuthorityContactMapper.mapper;

  private static final NoteMapper noteMapper = NoteMapper.mapper;

  @Getter(AccessLevel.PRIVATE)
  private final IndependentAuthorityPayloadValidator payloadValidator;

  private final AuthorityContactPayloadValidator authorityContactPayloadValidator;

  private final NotePayloadValidator notePayloadValidator;

  private final AuthoritySearchService authoritySearchService;

  @Autowired
  public IndependentAuthorityAPIController(Publisher publisher, final IndependentAuthorityService independentAuthorityService, final IndependentAuthorityHistoryService independentAuthorityHistoryService, final IndependentAuthorityPayloadValidator payloadValidator, AuthorityContactPayloadValidator authorityContactPayloadValidator, NotePayloadValidator notePayloadValidator, AuthoritySearchService authoritySearchService) {
    this.publisher = publisher;
    this.independentAuthorityService = independentAuthorityService;
    this.independentAuthorityHistoryService = independentAuthorityHistoryService;
    this.payloadValidator = payloadValidator;
    this.authorityContactPayloadValidator = authorityContactPayloadValidator;
    this.notePayloadValidator = notePayloadValidator;
    this.authoritySearchService = authoritySearchService;
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
  public IndependentAuthority createIndependentAuthority(IndependentAuthority independentAuthority) throws JsonProcessingException {
    validatePayload(() -> getPayloadValidator().validateCreatePayload(independentAuthority));
    RequestUtil.setAuditColumnsForCreate(independentAuthority);
    var pair = independentAuthorityService.createIndependentAuthority(independentAuthority);
    publisher.dispatchChoreographyEvent(pair.getRight());
    return mapper.toStructure(pair.getLeft());
  }

  @Override
  public IndependentAuthority updateIndependentAuthority(UUID id, IndependentAuthority independentAuthority) throws JsonProcessingException {
    validatePayload(() -> getPayloadValidator().validateUpdatePayload(independentAuthority));
    RequestUtil.setAuditColumnsForUpdate(independentAuthority);
    var pair = independentAuthorityService.updateIndependentAuthority(independentAuthority, id);
    publisher.dispatchChoreographyEvent(pair.getRight());
    return mapper.toStructure(pair.getLeft());
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
  public AuthorityContact getIndependentAuthorityContact(UUID independentAuthorityId, UUID contactId) {
    var contactEntity = this.independentAuthorityService.getIndependentAuthorityContact(independentAuthorityId, contactId);

    if (contactEntity.isPresent()) {
      return authorityContactMapper.toStructure(contactEntity.get());
    } else {
      throw new EntityNotFoundException();
    }
  }

  @Override
  public AuthorityContact createIndependentAuthorityContact(UUID independentAuthorityId, AuthorityContact contact) {
    validatePayload(() -> this.authorityContactPayloadValidator.validateCreatePayload(contact));
    RequestUtil.setAuditColumnsForCreate(contact);
    return authorityContactMapper.toStructure(independentAuthorityService.createIndependentAuthorityContact(contact, independentAuthorityId));
  }

  @Override
  public AuthorityContact updateIndependentAuthorityContact(UUID independentAuthorityId, UUID contactId, AuthorityContact contact) {
    validatePayload(() -> this.authorityContactPayloadValidator.validateUpdatePayload(contact));
    RequestUtil.setAuditColumnsForUpdate(contact);
    return authorityContactMapper.toStructure(independentAuthorityService.updateIndependentAuthorityContact(contact, independentAuthorityId, contactId));
  }

  @Override
  public ResponseEntity<Void> deleteIndependentAuthorityContact(UUID independentAuthorityId, UUID contactId) {
    this.independentAuthorityService.deleteIndependentAuthorityContact(independentAuthorityId, contactId);
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
  public List<Note> getIndependentAuthorityNotes(UUID independentAuthorityId) {
    return this.independentAuthorityService.getIndependentAuthorityNotes(independentAuthorityId).stream().map(noteMapper::toStructure).toList();
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

  @Override
  public CompletableFuture<Page<IndependentAuthority>> findAll(Integer pageNumber, Integer pageSize, String sortCriteriaJson, String searchCriteriaListJson) {
    final List<Sort.Order> sorts = new ArrayList<>();
    Specification<IndependentAuthorityEntity> studentSpecs = authoritySearchService.setSpecificationAndSortCriteria(sortCriteriaJson, searchCriteriaListJson, JsonUtil.mapper, sorts);
    return this.authoritySearchService.findAll(studentSpecs, pageNumber, pageSize, sorts).thenApplyAsync(schoolEntities -> schoolEntities.map(mapper::toStructure));
  }
}
