package ca.bc.gov.educ.api.institute.mapper.v1;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.bc.gov.educ.api.institute.model.v1.SchoolEntity;
import ca.bc.gov.educ.api.institute.model.v1.SchoolMoveHistoryEntity;
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
  void testToSchool_GivenSchoolMoveHistoryToAndFromLists_ShouldReturnSchoolMoveListSortedInDescendingOrder() {
    final SchoolEntity schoolEntity = new SchoolEntity();

    final SchoolMoveHistoryEntity schoolMoveToHistory1 = SchoolMoveHistoryEntity.builder()
        .toSchoolId(UUID.randomUUID())
        .fromSchoolId(UUID.randomUUID())
        .moveDate(LocalDateTime.now().plusDays(1))
        .build();

    final SchoolMoveHistoryEntity schoolMoveToHistory2 = SchoolMoveHistoryEntity.builder()
        .toSchoolId(UUID.randomUUID())
        .fromSchoolId(UUID.randomUUID())
        .moveDate(LocalDateTime.now().plusDays(2))
        .build();

    final SchoolMoveHistoryEntity schoolMoveToHistory3 = SchoolMoveHistoryEntity.builder()
        .toSchoolId(UUID.randomUUID())
        .fromSchoolId(UUID.randomUUID())
        .moveDate(LocalDateTime.now().plusDays(3))
        .build();

    final Set<SchoolMoveHistoryEntity> schoolMoveToHistory = new HashSet<>();
    final Set<SchoolMoveHistoryEntity> schoolMoveFromHistory = new HashSet<>();

    schoolMoveToHistory.add(schoolMoveToHistory1);

    schoolMoveFromHistory.add(schoolMoveToHistory3);
    schoolMoveFromHistory.add(schoolMoveToHistory2);

    schoolEntity.setSchoolMoveFromHistory(schoolMoveFromHistory);
    schoolEntity.setSchoolMoveToHistory(schoolMoveToHistory);

    final School school = schoolMapper.toStructure(schoolEntity);

    //ensure that we are sorted in descending order
    for (int i = 0; i < school.getSchoolMoveHistory().size() - 1; i++) {
      LocalDateTime school1 = LocalDateTime.parse(school.getSchoolMoveHistory().get(i).getMoveDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      LocalDateTime school2 = LocalDateTime.parse(school.getSchoolMoveHistory().get(i + 1).getMoveDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      assertTrue(school1.compareTo(school2) >= 0);
    }
  }

}
