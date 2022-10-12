package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolHistoryEntity;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class, SchoolContactMapper.class, SchoolGradeMapper.class, NeighborhoodLearningMapper.class, NoteMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolMapper {

  SchoolMapper mapper = Mappers.getMapper(SchoolMapper.class);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  SchoolEntity toModel(School structure);

  @Mapping(target = "districtId", source = "districtEntity.districtId")
  @Mapping(target = "mincode", expression = "java(entity.getDistrictEntity() != null && entity.getSchoolNumber() != null ? entity.getDistrictEntity().getDistrictNumber() + entity.getSchoolNumber() : null)")
  School toStructure(SchoolEntity entity);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  SchoolHistoryEntity toModel(SchoolHistory structure);

  SchoolHistory toStructure(SchoolHistoryEntity entity);
}
