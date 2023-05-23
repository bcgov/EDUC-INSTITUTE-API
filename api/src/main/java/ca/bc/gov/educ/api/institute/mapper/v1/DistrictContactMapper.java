package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.DistrictContactEntity;
import ca.bc.gov.educ.api.institute.struct.v1.DistrictContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface DistrictContactMapper {

  DistrictContactMapper mapper = Mappers.getMapper(DistrictContactMapper.class);


  DistrictContactEntity toModel(DistrictContact structure);

  @Mapping(target = "districtId", source = "districtEntity.districtId")
  DistrictContact toStructure(DistrictContactEntity entity);
}
