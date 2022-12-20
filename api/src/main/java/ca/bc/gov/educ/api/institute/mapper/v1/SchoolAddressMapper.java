package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.SchoolAddressEntity;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolAddressMapper {

  SchoolAddressMapper mapper = Mappers.getMapper(SchoolAddressMapper.class);

  SchoolAddressEntity toModel(SchoolAddress structure);

  @Mapping(target = "schoolId", source = "schoolEntity.schoolId")
  SchoolAddress toStructure(SchoolAddressEntity entity);
}
