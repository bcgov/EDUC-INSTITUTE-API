package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.model.v1.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, UUID> {
  Optional<NoteEntity> findByNoteIdAndDistrictEntity(UUID noteId, DistrictEntity districtEntity);

  void deleteByNoteIdAndDistrictEntity(UUID noteId, DistrictEntity districtEntity);
}
