package ca.bc.gov.educ.api.institute.controller.v1;

import ca.bc.gov.educ.api.institute.endpoint.v1.DistrictAPIEndpoint;
import ca.bc.gov.educ.api.institute.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.institute.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.institute.exception.errors.ApiError;
import ca.bc.gov.educ.api.institute.mapper.v1.DistrictContactMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.DistrictMapper;
import ca.bc.gov.educ.api.institute.mapper.v1.NoteMapper;
import ca.bc.gov.educ.api.institute.service.v1.DistrictHistoryService;
import ca.bc.gov.educ.api.institute.service.v1.DistrictService;
import ca.bc.gov.educ.api.institute.struct.v1.District;
import ca.bc.gov.educ.api.institute.struct.v1.DistrictContact;
import ca.bc.gov.educ.api.institute.struct.v1.DistrictHistory;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
import ca.bc.gov.educ.api.institute.util.RequestUtil;
import ca.bc.gov.educ.api.institute.validator.DistrictContactPayloadValidator;
import ca.bc.gov.educ.api.institute.validator.DistrictPayloadValidator;
import ca.bc.gov.educ.api.institute.validator.NotePayloadValidator;
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
public class DistrictAPIController implements DistrictAPIEndpoint {

  private static final DistrictMapper mapper = DistrictMapper.mapper;

  private static final DistrictContactMapper districtContactMapper = DistrictContactMapper.mapper;

  private static final NoteMapper noteMapper = NoteMapper.mapper;
  private final DistrictService districtService;
  private final DistrictHistoryService districtHistoryService;

  private final DistrictPayloadValidator districtPayloadValidator;

  private final DistrictContactPayloadValidator districtContactPayloadValidator;

  private final NotePayloadValidator notePayloadValidator;

  @Autowired
  public DistrictAPIController(final DistrictService districtService, final DistrictHistoryService districtHistoryService, final DistrictPayloadValidator districtPayloadValidator, DistrictContactPayloadValidator districtContactPayloadValidator, NotePayloadValidator notePayloadValidator) {
    this.districtService = districtService;
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
    return this.districtHistoryService.getAllDistrictHistoryList(districtId).stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public District createDistrict(District district) {
    validatePayload(() -> this.districtPayloadValidator.validateCreatePayload(district));
    RequestUtil.setAuditColumnsForCreate(district);
    return mapper.toStructure(districtService.createDistrict(district));
  }

  @Override
  public District updateDistrict(UUID id, District district) {
    validatePayload(() -> this.districtPayloadValidator.validateUpdatePayload(district));
    RequestUtil.setAuditColumnsForUpdate(district);
    return mapper.toStructure(districtService.updateDistrict(district, id));
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
    return this.districtService.getAllDistrictsList().stream().map(mapper::toStructure).collect(Collectors.toList());
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
  public DistrictContact createDistrictContact(UUID districtId, DistrictContact contact) {
    validatePayload(() -> this.districtContactPayloadValidator.validateCreatePayload(contact));
    RequestUtil.setAuditColumnsForCreate(contact);
    return districtContactMapper.toStructure(districtService.createDistrictContact(contact, districtId));
  }

  @Override
  public DistrictContact updateDistrictContact(UUID districtId, UUID contactId, DistrictContact contact) {
    validatePayload(() -> this.districtContactPayloadValidator.validateUpdatePayload(contact));
    RequestUtil.setAuditColumnsForUpdate(contact);
    return districtContactMapper.toStructure(districtService.updateDistrictContact(contact, districtId, contactId));
  }

  @Override
  public ResponseEntity<Void> deleteDistrictContact(UUID districtId, UUID contactId) {
    this.districtService.deleteDistrictContact(districtId, contactId);
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
