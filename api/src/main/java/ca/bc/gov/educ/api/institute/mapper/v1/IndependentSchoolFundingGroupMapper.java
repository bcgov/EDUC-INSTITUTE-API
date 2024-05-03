package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.IndependentSchoolFundingGroupEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentSchoolFundingGroupSchoolHistoryEntity;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentSchoolFundingGroup;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentSchoolFundingGroupHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class})
@SuppressWarnings("squid:S1214")
public interface IndependentSchoolFundingGroupMapper {

  IndependentSchoolFundingGroupMapper mapper = Mappers.getMapper(IndependentSchoolFundingGroupMapper.class);

  IndependentSchoolFundingGroupEntity toModel(IndependentSchoolFundingGroup structure);

  IndependentSchoolFundingGroup toStructure(IndependentSchoolFundingGroupEntity entity);
  IndependentSchoolFundingGroupSchoolHistoryEntity toModel(IndependentSchoolFundingGroupHistory structure);

  IndependentSchoolFundingGroupHistory toStructure(IndependentSchoolFundingGroupSchoolHistoryEntity entity);
}
