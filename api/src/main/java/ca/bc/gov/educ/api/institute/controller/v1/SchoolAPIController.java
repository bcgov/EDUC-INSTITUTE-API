package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.SchoolAPIEndpoint;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.institute.exception.errors.ApiError;
import ca.bc.gov.educ.api.institute.mapper.v1.*;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.service.v1.SchoolHistoryService;
import ca.bc.gov.educ.api.institute.service.v1.SchoolSearchService;
import ca.bc.gov.educ.api.institute.service.v1.SchoolService;
import ca.bc.gov.educ.api.institute.struct.v1.*;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.validator.AddressPayloadValidator;
import ca.bc.gov.educ.api.institute.validator.NotePayloadValidator;
import ca.bc.gov.educ.api.institute.validator.SchoolContactPayloadValidator;
import ca.bc.gov.educ.api.institute.validator.SchoolPayloadValidator;
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

  private static final SchoolMapper mapper = SchoolMapper.mapper;

  private static final SchoolTombstoneMapper tombstoneMapper = SchoolTombstoneMapper.mapper;

  private static final SchoolContactMapper schoolContactMapper = SchoolContactMapper.mapper;

  private static final AddressMapper addressMapper = AddressMapper.mapper;

  private static final NoteMapper noteMapper = NoteMapper.mapper;

  @Getter(AccessLevel.PRIVATE)
  private final SchoolService schoolService;
  @Getter(AccessLevel.PRIVATE)
  private final SchoolHistoryService schoolHistoryService;

  @Getter(AccessLevel.PRIVATE)
  private final SchoolSearchService schoolSearchService;

  @Getter(AccessLevel.PRIVATE)
  private final SchoolPayloadValidator payloadValidator;

  private final SchoolContactPayloadValidator contactPayloadValidator;

  private final AddressPayloadValidator addressPayloadValidator;

  private final NotePayloadValidator notePayloadValidator;
  @Autowired
  public SchoolAPIController(final SchoolService schoolService, final SchoolHistoryService schoolHistoryService, SchoolSearchService schoolSearchService, final SchoolPayloadValidator payloadValidator, SchoolContactPayloadValidator contactPayloadValidator, AddressPayloadValidator addressPayloadValidator, NotePayloadValidator notePayloadValidator) {
    this.schoolService = schoolService;
    this.schoolHistoryService = schoolHistoryService;
    this.schoolSearchService = schoolSearchService;
    this.payloadValidator = payloadValidator;
    this.contactPayloadValidator = contactPayloadValidator;
    this.addressPayloadValidator = addressPayloadValidator;
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
  public School createSchool(School school) {
    validatePayload(() -> getPayloadValidator().validateCreatePayload(school));
    RequestUtil.setAuditColumnsForCreate(school);
    return mapper.toStructure(schoolService.createSchool(school));
  }

  @Override
  public School updateSchool(UUID id, School school) {
    validatePayload(() -> getPayloadValidator().validateUpdatePayload(school));
    RequestUtil.setAuditColumnsForUpdate(school);
    return mapper.toStructure(schoolService.updateSchool(school, id));
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
  public Address getSchoolAddress(UUID schoolId, UUID addressId) {
    var addressEntity = this.schoolService.getSchoolAddress(schoolId, addressId);

    if (addressEntity.isPresent()) {
      return addressMapper.toStructure(addressEntity.get());
    } else {
      throw new EntityNotFoundException();
    }
  }

  @Override
  public Address createSchoolAddress(UUID schoolId, Address address) {
    validatePayload(() -> this.addressPayloadValidator.validateCreatePayload(address));
    RequestUtil.setAuditColumnsForCreate(address);
    return addressMapper.toStructure(schoolService.createSchoolAddress(address, schoolId));
  }

  @Override
  public Address updateSchoolAddress(UUID schoolId, UUID addressId, Address address) {
    validatePayload(() -> this.addressPayloadValidator.validateUpdatePayload(address));
    RequestUtil.setAuditColumnsForUpdate(address);
    return addressMapper.toStructure(schoolService.updateSchoolAddress(address, schoolId, addressId));
  }

  @Override
  public ResponseEntity<Void> deleteSchoolAddress(UUID schoolId, UUID addressId) {
    this.schoolService.deleteSchoolAddress(schoolId, addressId);
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

}
