package ca.bc.gov.educ.api.institute.validator;

import ca.bc.gov.educ.api.institute.service.v1.CodeTableService;
import ca.bc.gov.educ.api.institute.service.v1.DistrictService;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;


@Component
public class NotePayloadValidator {

  @Getter(AccessLevel.PRIVATE)
  private final DistrictService districtService;

  @Getter(AccessLevel.PRIVATE)
  private final CodeTableService codeTableService;

  @Autowired
  public NotePayloadValidator(final DistrictService districtService, final CodeTableService codeTableService) {
    this.districtService = districtService;
    this.codeTableService = codeTableService;
  }

  public List<FieldError> validatePayload(Note note, boolean isCreateOperation) {
    final List<FieldError> apiValidationErrors = new ArrayList<>();
    if (isCreateOperation && note.getNoteId() != null) {
      apiValidationErrors.add(createFieldError("noteId", note.getNoteId(), "noteId should be null for post operation."));
    }
    return apiValidationErrors;
  }

  public List<FieldError> validateUpdatePayload(Note note) {
    return validatePayload(note, false);
  }

  public List<FieldError> validateCreatePayload(Note note) {
    return validatePayload(note, true);
  }

  private FieldError createFieldError(String fieldName, Object rejectedValue, String message) {
    return new FieldError("note", fieldName, rejectedValue, false, null, null, message);
  }
}
