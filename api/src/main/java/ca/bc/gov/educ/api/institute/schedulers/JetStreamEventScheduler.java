package ca.bc.gov.educ.api.institute.schedulers;

import ca.bc.gov.educ.api.institute.choreographer.ChoreographEventHandler;
import ca.bc.gov.educ.api.institute.constants.v1.EventType;
import ca.bc.gov.educ.api.institute.messaging.jetstream.Publisher;
import ca.bc.gov.educ.api.institute.repository.v1.InstituteEventRepository;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.apache.logging.log4j.core.Core;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

import static ca.bc.gov.educ.api.institute.constants.v1.EventStatus.DB_COMMITTED;

/**
 * This class is responsible to check the STUDENT_EVENT table periodically and publish messages to JET STREAM, if some them are not yet published
 * this is a very edge case scenario which will occur.
 */
@Component
@Slf4j
public class JetStreamEventScheduler {

  private final InstituteEventRepository instituteEventRepository;
  private final Publisher publisher;
  private final ChoreographEventHandler choreographer;

  /**
   * Instantiates a new Stan event scheduler.
   *
   * @param instituteEventRepository the student event repository
   * @param publisher              the publisher
   */
  public JetStreamEventScheduler(InstituteEventRepository instituteEventRepository, Publisher publisher, ChoreographEventHandler choreographer) {
    this.instituteEventRepository = instituteEventRepository;
    this.publisher = publisher;
    this.choreographer = choreographer;
  }

  /**
   * Find and publish student events to stan.
   */
  @Scheduled(cron = "0 0/5 * * * *") // every 5 minutes
  @SchedulerLock(name = "PUBLISH_INSTITUTE_EVENTS_TO_JET_STREAM", lockAtLeastFor = "PT4M", lockAtMostFor = "PT4M")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void findAndPublishStudentEventsToJetStream() {
    var gradSchoolEventTypes = Arrays.asList(EventType.UPDATE_GRAD_SCHOOL.toString());
    LockAssert.assertLocked();
    var results = instituteEventRepository.findByEventStatusAndEventTypeNotIn(DB_COMMITTED.toString(), gradSchoolEventTypes);
    if (!results.isEmpty()) {
      results.forEach(el -> {
        if (el.getUpdateDate().isBefore(LocalDateTime.now().minusMinutes(5))) {
          try {
            publisher.dispatchChoreographyEvent(el);
          } catch (final Exception ex) {
            log.error("Exception while trying to publish message", ex);
          }
        }
      });
    }

    final var resultsForIncoming = this.instituteEventRepository.findAllByEventStatusAndCreateDateBeforeAndEventTypeInOrderByCreateDate(DB_COMMITTED.toString(), LocalDateTime.now().minusMinutes(1), 500, gradSchoolEventTypes);
    if (!resultsForIncoming.isEmpty()) {
      log.info("Found {} grad school choreographed events which needs to be processed.", resultsForIncoming.size());
      resultsForIncoming.forEach(this.choreographer::handleEvent);
    }
  }
}
