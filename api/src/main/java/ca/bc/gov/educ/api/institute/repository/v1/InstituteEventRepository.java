package ca.bc.gov.educ.api.institute.repository.v1;

import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InstituteEventRepository extends JpaRepository<InstituteEvent, UUID> {

  Optional<InstituteEvent> findByEventId(UUID eventId);
  /**
   * Find by saga id optional.
   *
   * @param sagaId the saga id
   * @return the optional
   */
  Optional<InstituteEvent> findBySagaId(UUID sagaId);

  /**
   * Find by saga id and event type optional.
   *
   * @param sagaId    the saga id
   * @param eventType the event type
   * @return the optional
   */
  Optional<InstituteEvent> findBySagaIdAndEventType(UUID sagaId, String eventType);

  /**
   * Find by event status list.
   *
   * @param eventStatus the event status
   * @return the list
   */
  List<InstituteEvent> findByEventStatus(String eventStatus);

  @Transactional
  @Modifying
  @Query("delete from InstituteEvent where createDate <= :createDate")
  void deleteByCreateDateBefore(LocalDateTime createDate);
}
