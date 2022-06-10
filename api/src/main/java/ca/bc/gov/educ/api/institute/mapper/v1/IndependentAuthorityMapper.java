package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityHistoryEntity;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthority;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthorityHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class, ContactMapper.class, SchoolGradeMapper.class, NeighborhoodLearningMapper.class})
@SuppressWarnings("squid:S1214")
public interface IndependentAuthorityMapper {

  IndependentAuthorityMapper mapper = Mappers.getMapper(IndependentAuthorityMapper.class);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  IndependentAuthorityEntity toModel(IndependentAuthority structure);

  IndependentAuthority toStructure(IndependentAuthorityEntity entity);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  IndependentAuthorityHistoryEntity toModel(IndependentAuthorityHistory structure);

  IndependentAuthorityHistory toStructure(IndependentAuthorityHistoryEntity entity);
}
