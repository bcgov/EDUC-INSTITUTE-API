package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.DistrictAddressEntity;
import ca.bc.gov.educ.api.institute.struct.v1.DistrictAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface DistrictAddressMapper {

  DistrictAddressMapper mapper = Mappers.getMapper(DistrictAddressMapper.class);

  DistrictAddressEntity toModel(DistrictAddress structure);

  @Mapping(target = "districtId", source = "districtEntity.districtId")
  DistrictAddress toStructure(DistrictAddressEntity entity);
}
