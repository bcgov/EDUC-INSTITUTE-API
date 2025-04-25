package ca.bc.gov.educ.api.institute.messaging.jetstream;

import ca.bc.gov.educ.api.institute.helpers.LogHelper;
import ca.bc.gov.educ.api.institute.properties.ApplicationProperties;
import ca.bc.gov.educ.api.institute.service.v1.EventHandlerDelegatorService;
import ca.bc.gov.educ.api.institute.struct.v1.ChoreographedEvent;
import ca.bc.gov.educ.api.institute.util.JsonUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.nats.client.Connection;
import io.nats.client.JetStreamApiException;
import io.nats.client.Message;
import io.nats.client.PushSubscribeOptions;
import io.nats.client.api.ConsumerConfiguration;
import io.nats.client.api.DeliverPolicy;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jboss.threads.EnhancedQueueExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * The type Subscriber.
 */
@Component
@DependsOn("publisher")
@Slf4j
public class Subscriber {
    private final Executor subscriberExecutor = new EnhancedQueueExecutor.Builder()
            .setThreadFactory(new ThreadFactoryBuilder().setNameFormat("jet-stream-subscriber-%d").build())
            .setCorePoolSize(2).setMaximumPoolSize(2).setKeepAliveTime(Duration.ofMillis(1000)).build();
    private final Connection natsConnection;
    private final EventHandlerDelegatorService eventHandlerDelegatorService;
    private final Map<String, List<String>> streamTopicsMap = new HashMap<>();


    @Autowired
    public Subscriber(final Connection natsConnection, EventHandlerDelegatorService eventHandlerDelegatorService) {
        this.natsConnection = natsConnection;
        this.eventHandlerDelegatorService = eventHandlerDelegatorService;
        initializeStreamTopicMap();
    }

    private void initializeStreamTopicMap() {
        final List<String> gradSchoolEventsTopics = new ArrayList<>();
        gradSchoolEventsTopics.add("PEN_SERVICES_EVENTS_TOPIC");
        final List<String> instituteEventsTopics = new ArrayList<>();
        instituteEventsTopics.add("INSTITUTE_EVENTS_TOPIC");
        this.streamTopicsMap.put("GRAD_SCHOOL_EVENTS", gradSchoolEventsTopics);
        this.streamTopicsMap.put("INSTITUTE_EVENTS", instituteEventsTopics);
    }


    /**
     * Subscribe.
     *
     * @throws IOException           the io exception
     * @throws JetStreamApiException the jet stream api exception
     */
    @PostConstruct
    public void subscribe() throws IOException, JetStreamApiException {
        val qName = ApplicationProperties.API_NAME.concat("-QUEUE");
        val autoAck = false;
        for (val entry : this.streamTopicsMap.entrySet()) {
            for (val topic : entry.getValue()) {
                final PushSubscribeOptions options = PushSubscribeOptions.builder().stream(entry.getKey())
                        .durable(ApplicationProperties.API_NAME.concat("-DURABLE"))
                        .configuration(ConsumerConfiguration.builder().deliverPolicy(DeliverPolicy.New).build()).build();
                this.natsConnection.jetStream().subscribe(topic, qName, this.natsConnection.createDispatcher(), this::onMessage,
                        autoAck, options);
            }
        }
    }

    public void onMessage(final Message message) {
        if (message != null) {
            log.info("Received message Subject:: {} , SID :: {} , sequence :: {}, pending :: {} ", message.getSubject(), message.getSID(), message.metaData().consumerSequence(), message.metaData().pendingCount());
            try {
                val eventString = new String(message.getData());
                LogHelper.logMessagingEventDetails(eventString);
                final ChoreographedEvent event = JsonUtil.getJsonObjectFromString(ChoreographedEvent.class, eventString);
                if (event.getEventPayload() == null) {
                    message.ack();
                    log.warn("payload is null, ignoring event :: {}", event);
                    return;
                }
                this.subscriberExecutor.execute(() -> {
                    try {
                        this.eventHandlerDelegatorService.handleChoreographyEvent(event, message);
                    } catch (final IOException e) {
                        log.error("IOException ", e);
                    }
                });
                log.info("received event :: {} ", event);
            } catch (final Exception ex) {
                log.error("Exception ", ex);
            }
        }
    }
}
