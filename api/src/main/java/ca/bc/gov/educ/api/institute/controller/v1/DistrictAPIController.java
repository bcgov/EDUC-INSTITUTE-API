package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.DistrictAPIEndpoint;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.institute.exception.errors.ApiError;
import ca.bc.gov.educ.api.institute.mapper.v1.DistrictContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.DistrictMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.DistrictTombstoneMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.messaging.jetstream.Publisher;
import ca.bc.gov.educ.api.institute.model.v1.DistrictContactTombstoneEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import ca.bc.gov.educ.api.institute.service.v1.DistrictContactSearchService;
import ca.bc.gov.educ.api.institute.service.v1.DistrictHistoryService;
import ca.bc.gov.educ.api.institute.service.v1.DistrictSearchService;
import ca.bc.gov.educ.api.institute.service.v1.DistrictService;
import ca.bc.gov.educ.api.institute.struct.v1.District;
import ca.bc.gov.educ.api.institute.struct.v1.DistrictContact;
import ca.bc.gov.educ.api.institute.struct.v1.DistrictHistory;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.validator.DistrictContactPayloadValidator;
import ca.bc.gov.educ.api.institute.validator.DistrictPayloadValidator;
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
public class DistrictAPIController implements DistrictAPIEndpoint {

  @Getter(AccessLevel.PRIVATE)
  private final Publisher publisher;
  private static final DistrictMapper mapper = DistrictMapper.mapper;

  private static final DistrictTombstoneMapper districtTombstoneMapper = DistrictTombstoneMapper.mapper;

  private static final DistrictContactMapper districtContactMapper = DistrictContactMapper.mapper;

  private static final NoteMapper noteMapper = NoteMapper.mapper;
  private final DistrictService districtService;
  private final DistrictSearchService districtSearchService;
  private final DistrictContactSearchService districtContactSearchService;
  private final DistrictHistoryService districtHistoryService;

  private final DistrictPayloadValidator districtPayloadValidator;

  private final DistrictContactPayloadValidator districtContactPayloadValidator;

  private final NotePayloadValidator notePayloadValidator;

  @Autowired
  public DistrictAPIController(Publisher publisher, final DistrictService districtService, DistrictSearchService districtSearchService, DistrictContactSearchService districtContactSearchService, final DistrictHistoryService districtHistoryService, final DistrictPayloadValidator districtPayloadValidator, DistrictContactPayloadValidator districtContactPayloadValidator, NotePayloadValidator notePayloadValidator) {
    this.publisher = publisher;
    this.districtService = districtService;
    this.districtSearchService = districtSearchService;
    this.districtContactSearchService = districtContactSearchService;
    this.districtHistoryService = districtHistoryService;
    this.districtPayloadValidator = districtPayloadValidator;
    this.districtContactPayloadValidator = districtContactPayloadValidator;
    this.notePayloadValidator = notePayloadValidator;
  }

  @Override
  public District getDistrict(UUID districtId) {
    var district = this.districtService.getDistrict(districtId);

    if (district.isPresent()) {
      return mapper.toStructure(district.get());
    } else {
      throw new EntityNotFoundException();
    }
  }

  @Override
  public List<DistrictHistory> getDistrictHistory(UUID districtId) {
    return this.districtHistoryService.getAllDistrictHistoryList(districtId).stream().map(mapper::toStructure).toList();
  }

  @Override
  public District createDistrict(District district) throws JsonProcessingException {
    validatePayload(() -> this.districtPayloadValidator.validateCreatePayload(district));
    RequestUtil.setAuditColumnsForCreate(district);
    var pair = districtService.createDistrict(district);
    publisher.dispatchChoreographyEvent(pair.getRight());
    return mapper.toStructure(pair.getLeft());
  }

  @Override
  public District updateDistrict(UUID id, District district) throws JsonProcessingException {
    validatePayload(() -> this.districtPayloadValidator.validateUpdatePayload(district));
    RequestUtil.setAuditColumnsForUpdate(district);
    var pair = districtService.updateDistrict(district, id);
    publisher.dispatchChoreographyEvent(pair.getRight());
    return mapper.toStructure(pair.getLeft());
  }

  @Override
  @Transactional
  public ResponseEntity<Void> deleteDistrict(UUID id) {
    this.districtService.deleteDistrict(id);
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
  public List<District> getAllDistricts() {
    return this.districtService.getAllDistrictsList().stream().map(districtTombstoneMapper::toStructure).toList();
  }

  @Override
  public CompletableFuture<Page<District>> findAll(Integer pageNumber, Integer pageSize, String sortCriteriaJson, String searchCriteriaListJson) {
    final List<Sort.Order> sorts = new ArrayList<>();
    Specification<DistrictEntity> districtSpecs = districtSearchService.setSpecificationAndSortCriteria(sortCriteriaJson, searchCriteriaListJson, JsonUtil.mapper, sorts);
    return this.districtSearchService.findAll(districtSpecs, pageNumber, pageSize, sorts).thenApplyAsync(districtEntities -> districtEntities.map(mapper::toStructure));
  }

  @Override
  public CompletableFuture<Page<DistrictContact>> findAllContacts(Integer pageNumber, Integer pageSize, String sortCriteriaJson, String searchCriteriaListJson) {
    final List<Sort.Order> sorts = new ArrayList<>();
    Specification<DistrictContactTombstoneEntity> districtSpecs = districtContactSearchService.setSpecificationAndSortCriteria(sortCriteriaJson, searchCriteriaListJson, JsonUtil.mapper, sorts);
    return this.districtContactSearchService.findAll(districtSpecs, pageNumber, pageSize, sorts).thenApplyAsync(districtEntities -> districtEntities.map(districtContactMapper::toStructure));
  }

  @Override
  public DistrictContact getDistrictContact(UUID districtId, UUID contactId) {
    var contactEntity = this.districtService.getDistrictContact(districtId, contactId);

    if (contactEntity.isPresent()) {
      return districtContactMapper.toStructure(contactEntity.get());
    } else {
      throw new EntityNotFoundException();
    }
  }

  @Override
  public DistrictContact createDistrictContact(UUID districtId, DistrictContact contact) throws JsonProcessingException {
    validatePayload(() -> this.districtContactPayloadValidator.validateCreatePayload(contact));
    RequestUtil.setAuditColumnsForCreate(contact);
    var pair = districtService.createDistrictContact(contact, districtId);
    publisher.dispatchChoreographyEvent(pair.getRight());
    return districtContactMapper.toStructure(pair.getLeft());
  }

  @Override
  public DistrictContact updateDistrictContact(UUID districtId, UUID contactId, DistrictContact contact) throws JsonProcessingException {
    validatePayload(() -> this.districtContactPayloadValidator.validateUpdatePayload(contact));
    RequestUtil.setAuditColumnsForUpdate(contact);
    var pair = districtService.updateDistrictContact(contact, districtId, contactId);
    publisher.dispatchChoreographyEvent(pair.getRight());
    return districtContactMapper.toStructure(pair.getLeft());
  }

  @Override
  public ResponseEntity<Void> deleteDistrictContact(UUID districtId, UUID contactId) throws JsonProcessingException {
    InstituteEvent instituteEvent = this.districtService.deleteDistrictContact(districtId, contactId);
    if(instituteEvent != null){
      publisher.dispatchChoreographyEvent(instituteEvent);
    }
    return ResponseEntity.noContent().build();
  }

  @Override
  public Note getDistrictNote(UUID districtId, UUID noteId) {
    var noteEntity = this.districtService.getDistrictNote(districtId, noteId);

    if (noteEntity.isPresent()) {
      return noteMapper.toStructure(noteEntity.get());
    } else {
      throw new EntityNotFoundException();
    }
  }

  @Override
  public List<Note> getDistrictNotes(UUID districtId) {
    return this.districtService.getDistrictNotes(districtId).stream().map(noteMapper::toStructure).toList();
  }

  @Override
  public Note createDistrictNote(UUID districtId, Note note) {
    validatePayload(() -> this.notePayloadValidator.validateCreatePayload(note));
    RequestUtil.setAuditColumnsForCreate(note);
    return noteMapper.toStructure(districtService.createDistrictNote(note, districtId));
  }

  @Override
  public Note updateDistrictNote(UUID districtId, UUID noteId, Note note) {
    validatePayload(() -> this.notePayloadValidator.validateUpdatePayload(note));
    RequestUtil.setAuditColumnsForUpdate(note);
    return noteMapper.toStructure(districtService.updateDistrictNote(note, districtId, noteId));
  }

  @Override
  public ResponseEntity<Void> deleteDistrictNote(UUID districtId, UUID noteId) {
    this.districtService.deleteDistrictNote(districtId, noteId);
    return ResponseEntity.noContent().build();
  }

}
