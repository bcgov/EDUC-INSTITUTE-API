package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.SchoolAPIEndpoint;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.institute.exception.errors.ApiError;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.SchoolTombstoneMapper;
import ca.bc.gov.educ.api.institute.messaging.jetstream.Publisher;
import ca.bc.gov.educ.api.institute.model.v1.SchoolContactTombstoneEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolHistoryEntity;
import ca.bc.gov.educ.api.institute.service.v1.*;
import ca.bc.gov.educ.api.institute.struct.v1.*;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.validator.NotePayloadValidator;
import ca.bc.gov.educ.api.institute.validator.SchoolContactPayloadValidator;
import ca.bc.gov.educ.api.institute.validator.SchoolPayloadValidator;
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
public class SchoolAPIController implements SchoolAPIEndpoint {

  @Getter(AccessLevel.PRIVATE)
  private final Publisher publisher;

  private static final SchoolMapper mapper = SchoolMapper.mapper;

  private static final SchoolTombstoneMapper tombstoneMapper = SchoolTombstoneMapper.mapper;

  private static final SchoolContactMapper schoolContactMapper = SchoolContactMapper.mapper;

  private static final NoteMapper noteMapper = NoteMapper.mapper;

  @Getter(AccessLevel.PRIVATE)
  private final SchoolService schoolService;

  @Getter(AccessLevel.PRIVATE)
  private final SchoolContactSearchService schoolContactSearchService;

  @Getter(AccessLevel.PRIVATE)
  private final SchoolHistoryService schoolHistoryService;

  private final SchoolHistorySearchService schoolHistorySearchService;

  private final SchoolSearchService schoolSearchService;

  @Getter(AccessLevel.PRIVATE)
  private final SchoolPayloadValidator payloadValidator;

  private final SchoolContactPayloadValidator contactPayloadValidator;

  private final NotePayloadValidator notePayloadValidator;
  @Autowired
  public SchoolAPIController(Publisher publisher, final SchoolService schoolService, SchoolContactSearchService schoolContactSearchService, final SchoolHistoryService schoolHistoryService, SchoolSearchService schoolSearchService, SchoolHistorySearchService schoolHistorySearchService, final SchoolPayloadValidator payloadValidator, SchoolContactPayloadValidator contactPayloadValidator, NotePayloadValidator notePayloadValidator) {
    this.publisher = publisher;
    this.schoolService = schoolService;
    this.schoolContactSearchService = schoolContactSearchService;
    this.schoolHistoryService = schoolHistoryService;
    this.schoolSearchService = schoolSearchService;
    this.schoolHistorySearchService = schoolHistorySearchService;
    this.payloadValidator = payloadValidator;
    this.contactPayloadValidator = contactPayloadValidator;
    this.notePayloadValidator = notePayloadValidator;
  }

  @Override
  public School getSchool(UUID schoolId) {
    var school = getSchoolService().getSchool(schoolId);

    if(school.isPresent()){
      return mapper.toStructure(school.get());
    }else{
      throw new EntityNotFoundException();
    }
  }

  @Override
  public List<SchoolHistory> getSchoolHistory(UUID schoolId) {
    return getSchoolHistoryService().getAllSchoolHistoryList(schoolId).stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public School createSchool(School school) throws JsonProcessingException {
    validatePayload(() -> getPayloadValidator().validateCreatePayload(school));
    RequestUtil.setAuditColumnsForCreate(school);
    var pair = schoolService.createSchool(school);
    publisher.dispatchChoreographyEvent(pair.getRight());
    return mapper.toStructure(pair.getLeft());
  }

  @Override
  public School updateSchool(UUID id, School school) throws JsonProcessingException {
    validatePayload(() -> getPayloadValidator().validateUpdatePayload(school));
    RequestUtil.setAuditColumnsForUpdate(school);
    var pair = schoolService.updateSchool(school, id);
    publisher.dispatchChoreographyEvent(pair.getRight());
    return mapper.toStructure(pair.getLeft());
  }

  @Override
  @Transactional
  public ResponseEntity<Void> deleteSchool(UUID id) {
    getSchoolService().deleteSchool(id);
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
  public List<SchoolTombstone> getAllSchools() {
    return getSchoolService().getAllSchoolsList().stream().map(tombstoneMapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public SchoolContact getSchoolContact(UUID schoolId, UUID contactId) {
    var contactEntity = this.schoolService.getSchoolContact(schoolId, contactId);

    if (contactEntity.isPresent()) {
      return schoolContactMapper.toStructure(contactEntity.get());
    } else {
      throw new EntityNotFoundException();
    }
  }

  @Override
  public SchoolContact createSchoolContact(UUID schoolId, SchoolContact contact) {
    validatePayload(() -> this.contactPayloadValidator.validateCreatePayload(contact));
    RequestUtil.setAuditColumnsForCreate(contact);
    return schoolContactMapper.toStructure(schoolService.createSchoolContact(contact, schoolId));
  }

  @Override
  public SchoolContact updateSchoolContact(UUID schoolId, UUID contactId, SchoolContact contact) {
    validatePayload(() -> this.contactPayloadValidator.validateUpdatePayload(contact));
    RequestUtil.setAuditColumnsForUpdate(contact);
    return schoolContactMapper.toStructure(schoolService.updateSchoolContact(contact, schoolId, contactId));
  }

  @Override
  public ResponseEntity<Void> deleteSchoolContact(UUID schoolId, UUID contactId) {
    this.schoolService.deleteSchoolContact(schoolId, contactId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public Note getSchoolNote(UUID schoolId, UUID noteId) {
    var noteEntity = this.schoolService.getSchoolNote(schoolId, noteId);

    if (noteEntity.isPresent()) {
      return noteMapper.toStructure(noteEntity.get());
    } else {
      throw new EntityNotFoundException();
    }
  }

  @Override
  public Note createSchoolNote(UUID schoolId, Note note) {
    validatePayload(() -> this.notePayloadValidator.validateCreatePayload(note));
    RequestUtil.setAuditColumnsForCreate(note);
    return noteMapper.toStructure(schoolService.createSchoolNote(note, schoolId));
  }

  @Override
  public CompletableFuture<Page<SchoolContact>> findAllContacts(Integer pageNumber, Integer pageSize, String sortCriteriaJson, String searchCriteriaListJson) {
    final List<Sort.Order> sorts = new ArrayList<>();
    Specification<SchoolContactTombstoneEntity> schoolSpecs = schoolContactSearchService.setSpecificationAndSortCriteria(sortCriteriaJson, searchCriteriaListJson, JsonUtil.mapper, sorts);
    return this.schoolContactSearchService.findAll(schoolSpecs, pageNumber, pageSize, sorts).thenApplyAsync(schoolEntities -> schoolEntities.map(schoolContactMapper::toStructure));
  }

  @Override
  public Note updateSchoolNote(UUID schoolId, UUID noteId, Note note) {
    validatePayload(() -> this.notePayloadValidator.validateUpdatePayload(note));
    RequestUtil.setAuditColumnsForUpdate(note);
    return noteMapper.toStructure(schoolService.updateSchoolNote(note, schoolId, noteId));
  }

  @Override
  public ResponseEntity<Void> deleteSchoolNote(UUID schoolId, UUID noteId) {
    this.schoolService.deleteSchoolNote(schoolId, noteId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public CompletableFuture<Page<School>> findAll(Integer pageNumber, Integer pageSize, String sortCriteriaJson, String searchCriteriaListJson) {
    final List<Sort.Order> sorts = new ArrayList<>();
    Specification<SchoolEntity> studentSpecs = schoolSearchService.setSpecificationAndSortCriteria(sortCriteriaJson, searchCriteriaListJson, JsonUtil.mapper, sorts);
    return this.schoolSearchService.findAll(studentSpecs, pageNumber, pageSize, sorts).thenApplyAsync(schoolEntities -> schoolEntities.map(mapper::toStructure));
  }

  @Override
  public CompletableFuture<Page<SchoolHistory>> schoolHistoryFindAll(Integer pageNumber, Integer pageSize, String sortCriteriaJson, String searchCriteriaListJson) {
    final List<Sort.Order> sorts = new ArrayList<>();
    Specification<SchoolHistoryEntity> schoolHistorySpecs = schoolHistorySearchService.setSpecificationAndSortCriteria(sortCriteriaJson, searchCriteriaListJson, JsonUtil.mapper, sorts);
    return this.schoolHistorySearchService.findAll(schoolHistorySpecs, pageNumber, pageSize, sorts).thenApplyAsync(schoolHistoryEntities -> schoolHistoryEntities.map(mapper::toStructure));
  }

}
