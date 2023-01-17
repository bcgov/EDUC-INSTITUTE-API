package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, UUID> {
  Optional<NoteEntity> findByNoteIdAndDistrictID(UUID noteId, UUID districtID);

  void deleteByNoteIdAndDistrictID(UUID noteId, UUID districtID);

  Optional<NoteEntity> findByNoteIdAndSchoolID(UUID noteId, UUID schoolID);

  void deleteByNoteIdAndSchoolID(UUID noteId, UUID schoolID);

  Optional<NoteEntity> findByNoteIdAndIndependentAuthorityID(UUID noteId, UUID independentAuthorityID);

  void deleteByNoteIdAndIndependentAuthorityID(UUID noteId, UUID independentAuthorityID);
}
