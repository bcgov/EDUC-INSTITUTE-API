package ca.bc.gov.educ.api.institute.service.v1;

import ca.bc.gov.educ.api.institute.choreographer.ChoreographEventHandler;
import ca.bc.gov.educ.api.institute.exception.BusinessException;
import ca.bc.gov.educ.api.institute.messaging.MessagePublisher;
import ca.bc.gov.educ.api.institute.messaging.jetstream.Publisher;
import ca.bc.gov.educ.api.institute.model.v1.InstituteEvent;
import ca.bc.gov.educ.api.institute.struct.v1.ChoreographedEvent;
import ca.bc.gov.educ.api.institute.struct.v1.Event;
import io.nats.client.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static ca.bc.gov.educ.api.institute.service.v1.EventHandlerService.PAYLOAD_LOG;

/**
 * The type Event handler service.
 */
@Service
@Slf4j
@SuppressWarnings({"java:S3864", "java:S3776"})
public class EventHandlerDelegatorService {


    /**
     * The constant RESPONDING_BACK_TO_NATS_ON_CHANNEL.
     */
    public static final String RESPONDING_BACK_TO_NATS_ON_CHANNEL = "responding back to NATS on {} channel ";
    private final MessagePublisher messagePublisher;
    private final EventHandlerService eventHandlerService;
    private final Publisher publisher;
    private final ChoreographedEventPersistenceService choreographedEventPersistenceService;
    private final ChoreographEventHandler choreographer;

    /**
     * Instantiates a new Event handler delegator service.
     *
     * @param messagePublisher    the message publisher
     * @param eventHandlerService the event handler service
     * @param publisher
     */
    @Autowired
    public EventHandlerDelegatorService(MessagePublisher messagePublisher, EventHandlerService eventHandlerService, Publisher publisher, ChoreographedEventPersistenceService choreographedEventPersistenceService, ChoreographEventHandler choreographer) {
        this.messagePublisher = messagePublisher;
        this.eventHandlerService = eventHandlerService;
        this.publisher = publisher;
        this.choreographedEventPersistenceService = choreographedEventPersistenceService;
        this.choreographer = choreographer;
    }

    public void handleChoreographyEvent(@NonNull final ChoreographedEvent choreographedEvent, final Message message) throws IOException {
        try {
            final var persistedEvent = this.choreographedEventPersistenceService.persistEventToDB(choreographedEvent);
            message.ack(); // acknowledge to Jet Stream that api got the message and it is now in DB.
            log.info("acknowledged to Jet Stream...");
            this.choreographer.handleEvent(persistedEvent);
        } catch (final BusinessException businessException) {
            message.ack(); // acknowledge to Jet Stream that api got the message already...
            log.info("acknowledged to Jet Stream...");
        }
    }

    /**
     * Handle event.
     *
     * @param event   the event
     * @param message the message
     */
    public void handleEvent(final Event event, final Message message) {
        byte[] response;
        boolean isSynchronous = message.getReplyTo() != null;
        try {
            log.trace("Handling event {}, in try block", event.getEventType());
            switch (event.getEventType()) {
                case GET_AUTHORITY:
                    log.info("Received GET_AUTHORITY event :: {}", event.getSagaId());
                    log.trace(PAYLOAD_LOG, event.getEventPayload());
                    response = eventHandlerService.handleGetAuthorityEvent(event, isSynchronous);
                    log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
                    publishToNATS(event, message, isSynchronous, response);
                    break;
                case GET_PAGINATED_SCHOOLS:
                    log.info("Received GET_PAGINATED_SCHOOLS event :: {}", event.getSagaId());
                    log.trace(PAYLOAD_LOG, event.getEventPayload());
                    eventHandlerService
                            .handleGetPaginatedSchools(event)
                            .thenAcceptAsync(resBytes -> {
                                log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
                                publishToNATS(event, message, isSynchronous, resBytes);
                            });
                    break;
                case GET_PAGINATED_AUTHORITIES:
                    log.info("Received GET_PAGINATED_AUTHORITIES event :: {}", event.getSagaId());
                    log.trace(PAYLOAD_LOG, event.getEventPayload());
                    eventHandlerService
                            .handleGetPaginatedAuthorities(event)
                            .thenAcceptAsync(resBytes -> {
                                log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
                                publishToNATS(event, message, isSynchronous, resBytes);
                            });
                    break;
                case CREATE_SCHOOL:
                    log.info("Received CREATE_SCHOOL event :: {}", event.getSagaId());
                    log.trace(PAYLOAD_LOG, event.getEventPayload());
                    Pair<byte[], InstituteEvent> pair = eventHandlerService.handleCreateSchoolEvent(event);
                    log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
                    publishToNATS(event, message, isSynchronous, pair.getLeft());
                    publishToJetStream(pair.getRight());
                    break;
                case GET_SCHOOL:
                    log.info("Received GET_SCHOOL event :: {}", event.getSagaId());
                    log.trace(PAYLOAD_LOG, event.getEventPayload());
                    byte[] school = eventHandlerService.handleGetSchoolFromIdEvent(event);
                    log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
                    publishToNATS(event, message, true, school);
                    break;
                case UPDATE_SCHOOL:
                    log.info("Received UPDATE_SCHOOL event :: {}", event.getSagaId());
                    log.trace(PAYLOAD_LOG, event.getEventPayload());
                    Pair<byte[], InstituteEvent> updatePair = eventHandlerService.handleUpdateSchoolEvent(event);
                    log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
                    publishToNATS(event, message, isSynchronous, updatePair.getLeft());
                    publishToJetStream(updatePair.getRight());
                    break;
                case MOVE_SCHOOL:
                    log.info("Received MOVE_SCHOOL event :: {}", event.getSagaId());
                    log.trace(PAYLOAD_LOG, event.getEventPayload());
                    Pair<byte[], InstituteEvent> movePair = eventHandlerService.handleMoveSchoolEvent(event);
                    log.info(RESPONDING_BACK_TO_NATS_ON_CHANNEL, message.getReplyTo() != null ? message.getReplyTo() : event.getReplyTo());
                    publishToNATS(event, message, isSynchronous, movePair.getLeft());
                    publishToJetStream(movePair.getRight());
                    break;
                default:
                    log.info("silently ignoring other events :: {}", event);
                    break;
            }
        } catch (final Exception e) {
            log.error("Exception", e);
        }
    }

    private void publishToNATS(Event event, Message message, boolean isSynchronous, byte[] left) {
        log.info("Publishing event to NATS :: {}", event);
        if (isSynchronous) { // sync, req/reply pattern of nats
            messagePublisher.dispatchMessage(message.getReplyTo(), left);
        } else { // async, pub/sub
            messagePublisher.dispatchMessage(event.getReplyTo(), left);
        }
    }

    private void publishToJetStream(final InstituteEvent event) {
        publisher.dispatchChoreographyEvent(event);
    }

}
