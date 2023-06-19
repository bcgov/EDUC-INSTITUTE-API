package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.NoteEntity;
import ca.bc.gov.educ.api.institute.struct.v1.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface NoteMapper {

  NoteMapper mapper = Mappers.getMapper(NoteMapper.class);

  @Mapping(target = "schoolID", source = "structure.schoolId")
  @Mapping(target = "districtID", source = "structure.districtId")
  @Mapping(target = "independentAuthorityID", source = "structure.independentAuthorityId")
  NoteEntity toModel(Note structure);

  @Mapping(target = "schoolId", source = "entity.schoolID")
  @Mapping(target = "districtId", source = "entity.districtID")
  @Mapping(target = "independentAuthorityId", source = "entity.independentAuthorityID")
  Note toStructure(NoteEntity entity);
}
