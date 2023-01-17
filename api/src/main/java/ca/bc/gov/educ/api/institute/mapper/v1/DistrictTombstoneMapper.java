package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.DistrictTombstoneEntity;
import ca.bc.gov.educ.api.institute.struct.v1.District;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface DistrictTombstoneMapper {

  DistrictTombstoneMapper mapper = Mappers.getMapper(DistrictTombstoneMapper.class);


  DistrictTombstoneEntity toModel(District structure);

  District toStructure(DistrictTombstoneEntity entity);

}
