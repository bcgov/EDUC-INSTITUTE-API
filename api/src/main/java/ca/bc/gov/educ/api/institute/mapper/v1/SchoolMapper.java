package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface School mapper.
 */
@Mapper(uses = {LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolMapper {

  /**
   * The constant mapper.
   */
  SchoolMapper mapper = Mappers.getMapper(SchoolMapper.class);

  /**
   * To structure school.
   *
   * @param schoolEntity the school entity
   * @return the school
   */
  @Mapping(target = "distNo", source = "schoolEntity.mincode.distNo")
  @Mapping(target = "schlNo", source = "schoolEntity.mincode.schlNo")
  School toStructure(SchoolEntity schoolEntity);

}
