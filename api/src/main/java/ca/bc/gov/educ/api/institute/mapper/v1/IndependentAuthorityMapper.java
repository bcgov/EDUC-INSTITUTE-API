package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityEntity;
import ca.bc.gov.educ.api.institute.model.v1.IndependentAuthorityHistoryEntity;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthority;
import ca.bc.gov.educ.api.institute.struct.v1.IndependentAuthorityHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class, AuthorityContactMapper.class, SchoolGradeMapper.class, NeighborhoodLearningMapper.class, NoteMapper.class, AddressMapper.class})
@SuppressWarnings("squid:S1214")
public interface IndependentAuthorityMapper {

  IndependentAuthorityMapper mapper = Mappers.getMapper(IndependentAuthorityMapper.class);


  IndependentAuthorityEntity toModel(IndependentAuthority structure);

  IndependentAuthority toStructure(IndependentAuthorityEntity entity);


  IndependentAuthorityHistoryEntity toModel(IndependentAuthorityHistory structure);

  IndependentAuthorityHistory toStructure(IndependentAuthorityHistoryEntity entity);
}
