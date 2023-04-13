package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.SchoolMoveEntity;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolMove;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolMoveMapper {

  SchoolMoveMapper mapper = Mappers.getMapper(SchoolMoveMapper.class);

  SchoolMoveEntity toModel(SchoolMove structure);

  SchoolMove toStructure(SchoolMoveEntity entity);
}
