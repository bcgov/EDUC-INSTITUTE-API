package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.mapper.LocalDateTimeMapper;
import ca.bc.gov.educ.api.institute.mapper.StringMapper;
import ca.bc.gov.educ.api.institute.mapper.UUIDMapper;
import ca.bc.gov.educ.api.institute.model.v1.NeighbourhoodLearningSchoolHistoryEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolGradeSchoolHistoryEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolHistoryEntity;
import ca.bc.gov.educ.api.institute.struct.v1.NeighbourhoodLearningSchoolHistory;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolGradeSchoolHistory;
import ca.bc.gov.educ.api.institute.struct.v1.SchoolHistory;
import org.mapstruct.DecoratedWith;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@DecoratedWith(SchoolDecorator.class)
@Mapper(uses = {UUIDMapper.class, LocalDateTimeMapper.class, StringMapper.class, SchoolContactMapper.class, SchoolGradeMapper.class, NeighborhoodLearningMapper.class, NoteMapper.class, SchoolAddressMapper.class, IndependentSchoolFundingGroupMapper.class})
@SuppressWarnings("squid:S1214")
public interface SchoolMapper {

  SchoolMapper mapper = Mappers.getMapper(SchoolMapper.class);

  @InheritInverseConfiguration
  @Mapping(target = "districtID", source = "districtId")
  @Mapping(target = "schoolMoveTo", ignore = true)
  @Mapping(target = "schoolMoveFrom", ignore = true)
  SchoolEntity toModel(School structure);

  @Mapping(target = "districtId", source = "districtEntity.districtId")
  @Mapping(target = "mincode", expression = "java(entity.getDistrictEntity() != null && entity.getSchoolNumber() != null ? entity.getDistrictEntity().getDistrictNumber() + entity.getSchoolNumber() : null)")
  @Mapping(target = "schoolMove", ignore = true)
  School toStructure(SchoolEntity entity);

  @InheritInverseConfiguration
  @Mapping(target = "districtId", source = "districtID")
  SchoolHistory toStructure(SchoolHistoryEntity entity);

  @Mapping(target = "schoolHistoryId", source = "schoolHistoryEntity.schoolHistoryId")
  SchoolGradeSchoolHistory toStructure(SchoolGradeSchoolHistoryEntity model);
  @InheritInverseConfiguration
  SchoolGradeSchoolHistoryEntity toModel(SchoolGradeSchoolHistory structure);

  @Mapping(target = "schoolHistoryId", source = "schoolHistoryEntity.schoolHistoryId")
  NeighbourhoodLearningSchoolHistory toStructure(NeighbourhoodLearningSchoolHistoryEntity model);
  @InheritInverseConfiguration
  NeighbourhoodLearningSchoolHistoryEntity toModel(NeighbourhoodLearningSchoolHistory structure);

}
