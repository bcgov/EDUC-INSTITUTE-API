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


  AddressTypeCodeEntity toModel(AddressTypeCode structure);

  AddressTypeCode toStructure(AddressTypeCodeEntity entity);


  AuthorityTypeCodeEntity toModel(AuthorityTypeCode structure);

  AuthorityTypeCode toStructure(AuthorityTypeCodeEntity entity);


  DistrictContactTypeCodeEntity toModel(DistrictContactTypeCode structure);

  DistrictContactTypeCode toStructure(DistrictContactTypeCodeEntity entity);


  SchoolContactTypeCodeEntity toModel(SchoolContactTypeCode structure);

  SchoolContactTypeCode toStructure(SchoolContactTypeCodeEntity entity);


  AuthorityContactTypeCodeEntity toModel(AuthorityContactTypeCode structure);

  AuthorityContactTypeCode toStructure(AuthorityContactTypeCodeEntity entity);


  ProvinceCodeEntity toModel(ProvinceCode structure);

  ProvinceCode toStructure(ProvinceCodeEntity entity);


  CountryCodeEntity toModel(CountryCode structure);

  CountryCode toStructure(CountryCodeEntity entity);


  DistrictRegionCodeEntity toModel(DistrictRegionCode structure);

  DistrictRegionCode toStructure(DistrictRegionCodeEntity entity);


  DistrictStatusCodeEntity toModel(DistrictStatusCode structure);

  DistrictStatusCode toStructure(DistrictStatusCodeEntity entity);



  FacilityTypeCodeEntity toModel(FacilityTypeCode structure);

  FacilityTypeCode toStructure(FacilityTypeCodeEntity entity);


  NeighborhoodLearningTypeCodeEntity toModel(NeighborhoodLearningTypeCode structure);

  NeighborhoodLearningTypeCode toStructure(NeighborhoodLearningTypeCodeEntity entity);


  SchoolCategoryCodeEntity toModel(SchoolCategoryCode structure);

  SchoolCategoryCode toStructure(SchoolCategoryCodeEntity entity);


  SchoolGradeCodeEntity toModel(SchoolGradeCode structure);

  SchoolGradeCode toStructure(SchoolGradeCodeEntity entity);


  SchoolOrganizationCodeEntity toModel(SchoolOrganizationCode structure);

  SchoolOrganizationCode toStructure(SchoolOrganizationCodeEntity entity);


  SchoolReportingRequirementCodeEntity toModel(SchoolReportingRequirementCode structure);

  SchoolReportingRequirementCode toStructure(SchoolReportingRequirementCodeEntity entity);
}
