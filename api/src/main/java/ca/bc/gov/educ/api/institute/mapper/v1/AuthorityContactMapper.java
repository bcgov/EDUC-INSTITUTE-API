package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.AuthorityContactEntity;
import ca.bc.gov.educ.api.institute.struct.v1.AuthorityContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface AuthorityContactMapper {

  AuthorityContactMapper mapper = Mappers.getMapper(AuthorityContactMapper.class);

  AuthorityContactEntity toModel(AuthorityContact structure);

  @Mapping(target = "independentAuthorityId", source = "independentAuthorityEntity.independentAuthorityId")
  AuthorityContact toStructure(AuthorityContactEntity entity);
}
