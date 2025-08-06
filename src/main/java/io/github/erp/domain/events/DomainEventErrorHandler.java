package io.github.erp.domain.events;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class DomainEventErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(DomainEventErrorHandler.class);
    private static final int MAX_RETRY_COUNT = 3;
    private static final long BASE_RETRY_DELAY_SECONDS = 2;

    @Value("${spring.kafka.topics.domain-events-dlq.topic.name:domain_events_dlq}")
    private String deadLetterTopicName;

    private final DomainEventStore eventStore;
    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;
    private final DomainEventProcessor domainEventProcessor;

    public DomainEventErrorHandler(DomainEventStore eventStore, 
                                 KafkaTemplate<String, DomainEvent> kafkaTemplate,
                                 DomainEventProcessor domainEventProcessor) {
        this.eventStore = eventStore;
        this.kafkaTemplate = kafkaTemplate;
        this.domainEventProcessor = domainEventProcessor;
    }

    public void handleError(DomainEvent event, Exception error) {
        log.error("Error processing domain event: {} - {}", event.getEventId(), error.getMessage(), error);

        try {
            eventStore.incrementRetryCount(event.getEventId());

            if (event instanceof AbstractDomainEvent) {
                AbstractDomainEvent abstractEvent = (AbstractDomainEvent) event;
                if (abstractEvent.getRetryCount() >= MAX_RETRY_COUNT) {
                    log.error("Max retry count exceeded for domain event: {}. Moving to dead letter queue.", 
                            event.getEventId());
                    handleDeadLetter(event, error);
                } else {
                    log.info("Scheduling retry for domain event: {}. Retry count: {}", 
                            event.getEventId(), abstractEvent.getRetryCount() + 1);
                    scheduleRetry(event);
                }
            }

        } catch (Exception e) {
            log.error("Failed to handle error for domain event: {}", event.getEventId(), e);
        }
    }

    private void handleDeadLetter(DomainEvent event, Exception error) {
        try {
            String partitionKey = event.getAggregateType() + ":" + event.getAggregateId();
            kafkaTemplate.send(deadLetterTopicName, partitionKey, event);
            
            log.error("Domain event {} moved to dead letter queue due to: {}", 
                     event.getEventId(), error.getMessage());
            
            sendAlert("Domain Event DLQ", event, error);
            
        } catch (Exception e) {
            log.error("Failed to send event {} to dead letter queue", event.getEventId(), e);
        }
    }

    @Async
    public void scheduleRetry(DomainEvent event) {
        long delaySeconds = calculateBackoffDelay(event instanceof AbstractDomainEvent ? 
            ((AbstractDomainEvent) event).getRetryCount() : 0);
        
        log.info("Scheduling retry for domain event: {} with delay: {}s", 
                event.getEventId(), delaySeconds);
        
        CompletableFuture.delayedExecutor(delaySeconds, TimeUnit.SECONDS)
            .execute(() -> {
                try {
                    domainEventProcessor.processEvent(event);
                } catch (Exception e) {
                    log.error("Retry failed for domain event: {}", event.getEventId(), e);
                }
            });
    }

    private long calculateBackoffDelay(int retryCount) {
        return BASE_RETRY_DELAY_SECONDS * (long) Math.pow(2, retryCount);
    }

    private void sendAlert(String alertType, DomainEvent event, Exception error) {
        log.warn("ALERT: {} - Event: {}, Aggregate: {}, Error: {}", 
                alertType, event.getEventId(), event.getAggregateId(), error.getMessage());
    }
}
