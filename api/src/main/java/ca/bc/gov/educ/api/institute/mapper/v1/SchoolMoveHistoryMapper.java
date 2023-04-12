package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.SchoolMoveHistoryEntity;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolMoveHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolMoveHistoryMapper {

  SchoolMoveHistoryMapper mapper = Mappers.getMapper(SchoolMoveHistoryMapper.class);

  SchoolMoveHistoryEntity toModel(SchoolMoveHistory structure);

  SchoolMoveHistory toStructure(SchoolMoveHistoryEntity entity);
}
