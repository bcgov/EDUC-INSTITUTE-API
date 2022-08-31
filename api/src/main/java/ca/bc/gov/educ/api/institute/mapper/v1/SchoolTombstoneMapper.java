package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.SchoolTombstoneEntity;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolTombstone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class, SchoolContactMapper.class, SchoolGradeMapper.class, NeighborhoodLearningMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolTombstoneMapper {

  SchoolTombstoneMapper mapper = Mappers.getMapper(SchoolTombstoneMapper.class);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  SchoolTombstoneEntity toModel(SchoolTombstone structure);

  @Mapping(target = "districtId", source = "districtEntity.districtId")
  @Mapping(target = "mincode", expression = "java(entity.getDistrictEntity() != null && entity.getSchoolNumber() != null ? entity.getDistrictEntity().getDistrictNumber() + entity.getSchoolNumber() : null)")
  SchoolTombstone toStructure(SchoolTombstoneEntity entity);

}
