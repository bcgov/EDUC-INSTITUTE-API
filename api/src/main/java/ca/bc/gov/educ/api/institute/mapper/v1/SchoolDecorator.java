package ca.bc.gov.educ.api.institute.mapper.v1;

import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolMoveEntity;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.val;

public abstract class SchoolDecorator implements SchoolMapper {

  private final SchoolMapper delegate;

  private static final SchoolMoveMapper schoolMoveMapper = SchoolMoveMapper.mapper;

  protected SchoolDecorator(final SchoolMapper delegate) {
    this.delegate = delegate;
  }

  @Override
  public School toStructure(SchoolEntity schoolEntity) {
    School school = this.delegate.toStructure(schoolEntity);

    Set<SchoolMoveEntity> schoolMoveToEntity = schoolEntity.getSchoolMoveTo();
    Set<SchoolMoveEntity> schoolMoveFromEntity = schoolEntity.getSchoolMoveFrom();

    Set<SchoolMoveEntity> mergedSchoolMoveEntity = new HashSet<>(schoolMoveToEntity);
    mergedSchoolMoveEntity.addAll(schoolMoveFromEntity);

    school.setSchoolMove(new ArrayList<>());

    if (!mergedSchoolMoveEntity.isEmpty()) {
      for (val schoolMoveEntity : mergedSchoolMoveEntity) {
        school.getSchoolMove().add(schoolMoveMapper.toStructure(schoolMoveEntity));
      }

      Collections.sort(school.getSchoolMove(), (schoolHistory1, schoolHistory2) -> {
        LocalDateTime dt1 = LocalDateTime.parse(schoolHistory1.getMoveDate(),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime dt2 = LocalDateTime.parse(schoolHistory2.getMoveDate(),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return dt2.compareTo(dt1); // Sort in descending order
      });
    }

    return school;
  }


}
