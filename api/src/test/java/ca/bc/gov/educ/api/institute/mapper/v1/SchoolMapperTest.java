package ca.bc.gov.educ.api.institute.mapper.v1;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolMoveEntity;
import ca.bc.gov.educ.api.institute.struct.v1.School;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SchoolMapperTest {

  private static final SchoolMapper schoolMapper = SchoolMapper.mapper;

  @Test
  void testToSchool_GivenSchoolMoveToAndFromLists_ShouldReturnSchoolMoveListSortedInDescendingOrder() {
    final SchoolEntity schoolEntity = new SchoolEntity();

    final SchoolMoveEntity schoolMoveTo1 = SchoolMoveEntity.builder()
        .toSchoolId(UUID.randomUUID())
        .fromSchoolId(UUID.randomUUID())
        .moveDate(LocalDateTime.now().plusDays(1))
        .build();

    final SchoolMoveEntity schoolMoveTo2 = SchoolMoveEntity.builder()
        .toSchoolId(UUID.randomUUID())
        .fromSchoolId(UUID.randomUUID())
        .moveDate(LocalDateTime.now().plusDays(2))
        .build();

    final SchoolMoveEntity schoolMoveTo3 = SchoolMoveEntity.builder()
        .toSchoolId(UUID.randomUUID())
        .fromSchoolId(UUID.randomUUID())
        .moveDate(LocalDateTime.now().plusDays(3))
        .build();

    final Set<SchoolMoveEntity> schoolMoveTo = new HashSet<>();
    final Set<SchoolMoveEntity> schoolMoveFrom = new HashSet<>();

    schoolMoveTo.add(schoolMoveTo1);

    schoolMoveFrom.add(schoolMoveTo3);
    schoolMoveFrom.add(schoolMoveTo2);

    schoolEntity.setSchoolMoveFrom(schoolMoveFrom);
    schoolEntity.setSchoolMoveTo(schoolMoveTo);

    final School school = schoolMapper.toStructure(schoolEntity);

    //ensure that we are sorted in descending order
    for (int i = 0; i < school.getSchoolMove().size() - 1; i++) {
      LocalDateTime school1 = LocalDateTime.parse(school.getSchoolMove().get(i).getMoveDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      LocalDateTime school2 = LocalDateTime.parse(school.getSchoolMove().get(i + 1).getMoveDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      assertTrue(school1.compareTo(school2) >= 0);
    }
  }

}
