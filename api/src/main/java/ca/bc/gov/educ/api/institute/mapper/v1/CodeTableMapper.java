package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.model.v1.*;
import ca.bc.gov.educ.api.institute.struct.v1.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface Student mapper.
 */
@Mapper(uses = {LocalDateTimeMapper.class, StringMapper.class})
@SuppressWarnings("squid:S1214")
public interface CodeTableMapper {

  CodeTableMapper mapper = Mappers.getMapper(CodeTableMapper.class);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  AddressTypeCodeEntity toModel(AddressTypeCode structure);

  AddressTypeCode toStructure(AddressTypeCodeEntity entity);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  AuthorityTypeCodeEntity toModel(AuthorityTypeCode structure);

  AuthorityTypeCode toStructure(AuthorityTypeCodeEntity entity);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  ContactTypeCodeEntity toModel(ContactTypeCode structure);

  ContactTypeCode toStructure(ContactTypeCodeEntity entity);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  ProvinceCodeEntity toModel(ProvinceCode structure);

  ProvinceCode toStructure(ProvinceCodeEntity entity);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  DistrictRegionCodeEntity toModel(DistrictRegionCode structure);

  DistrictRegionCode toStructure(DistrictRegionCodeEntity entity);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  DistrictStatusCodeEntity toModel(DistrictStatusCode structure);

  DistrictStatusCode toStructure(DistrictStatusCodeEntity entity);


  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  FacilityTypeCodeEntity toModel(FacilityTypeCode structure);

  FacilityTypeCode toStructure(FacilityTypeCodeEntity entity);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  NeighborhoodLearningTypeCodeEntity toModel(NeighborhoodLearningTypeCode structure);

  NeighborhoodLearningTypeCode toStructure(NeighborhoodLearningTypeCodeEntity entity);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  SchoolCategoryCodeEntity toModel(SchoolCategoryCode structure);

  SchoolCategoryCode toStructure(SchoolCategoryCodeEntity entity);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  SchoolGradeCodeEntity toModel(SchoolGradeCode structure);

  SchoolGradeCode toStructure(SchoolGradeCodeEntity entity);

  @Mapping(target = "updateUser", ignore = true)
  @Mapping(target = "updateDate", ignore = true)
  @Mapping(target = "createUser", ignore = true)
  @Mapping(target = "createDate", ignore = true)
  SchoolOrganizationCodeEntity toModel(SchoolOrganizationCode structure);

  SchoolOrganizationCode toStructure(SchoolOrganizationCodeEntity entity);
}
