package ca.bc.gov.educ.api.institute.service.v1;


import ca.bc.gov.educ.api.institute.constants.v1.EventStatus;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import ca.bc.gov.educ.api.institute.repository.v1.InstituteEventRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


/**
 * The type Base service.
 *
 * @param <T> the type parameter
 */
@Slf4j
public abstract class BaseService<T> implements EventService<T> {
    private final InstituteEventRepository instituteEventRepository;

    protected BaseService(InstituteEventRepository instituteEventRepository) {
        this.instituteEventRepository = instituteEventRepository;
    }

    protected void updateEvent(final InstituteEvent event) {
        this.instituteEventRepository.findByEventId(event.getEventId()).ifPresent(existingEvent -> {
            existingEvent.setEventStatus(EventStatus.PROCESSED.toString());
            existingEvent.setUpdateDate(LocalDateTime.now());
            this.instituteEventRepository.save(existingEvent);
        });
    }

}
