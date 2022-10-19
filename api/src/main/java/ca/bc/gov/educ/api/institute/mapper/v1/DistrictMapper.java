package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.DistrictEntity;
import ca.bc.gov.educ.api.institute.model.v1.DistrictHistoryEntity;
import ca.bc.gov.educ.api.institute.struct.v1.District;
import ca.bc.gov.educ.api.institute.struct.v1.DistrictHistory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class, DistrictContactMapper.class, SchoolGradeMapper.class, NeighborhoodLearningMapper.class, NoteMapper.class, AddressMapper.class})
@SuppressWarnings("squid:S1214")
public interface DistrictMapper {

  DistrictMapper mapper = Mappers.getMapper(DistrictMapper.class);


  DistrictEntity toModel(District structure);

  District toStructure(DistrictEntity entity);


  DistrictHistoryEntity toModel(DistrictHistory structure);

  DistrictHistory toStructure(DistrictHistoryEntity entity);
}
