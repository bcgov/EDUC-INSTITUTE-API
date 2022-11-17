package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.SchoolHistoryEntity;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class, SchoolContactMapper.class, SchoolGradeMapper.class, NeighborhoodLearningMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolHistoryMapper {

  SchoolHistoryMapper mapper = Mappers.getMapper(SchoolHistoryMapper.class);

  SchoolHistoryEntity toModel(SchoolHistory structure);

  SchoolHistory toStructure(SchoolHistoryEntity entity);

}
