package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.NeighborhoodLearningEntity;
import ca.bc.gov.educ.api.institute.struct.v1.NeighborhoodLearning;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface NeighborhoodLearningMapper {

  NeighborhoodLearningMapper mapper = Mappers.getMapper(NeighborhoodLearningMapper.class);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  NeighborhoodLearningEntity toModel(NeighborhoodLearning structure);

  NeighborhoodLearning toStructure(NeighborhoodLearningEntity entity);
}
