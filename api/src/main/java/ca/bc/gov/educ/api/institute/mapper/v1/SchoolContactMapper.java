package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.SchoolContactEntity;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolContactMapper {

  SchoolContactMapper mapper = Mappers.getMapper(SchoolContactMapper.class);

  SchoolContactEntity toModel(SchoolContact structure);

  @Mapping(target = "schoolId", source = "schoolEntity.schoolId")
  SchoolContact toStructure(SchoolContactEntity entity);
}
