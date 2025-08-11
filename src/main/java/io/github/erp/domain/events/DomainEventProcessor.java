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
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DomainEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(DomainEventProcessor.class);

    private final Map<String, List<DomainEventHandlerMethodWrapper>> handlerRegistry = new ConcurrentHashMap<>();
    private final DomainEventStore eventStore;
    private final DomainEventErrorHandler errorHandler;

    public DomainEventProcessor(DomainEventStore eventStore, @Lazy DomainEventErrorHandler errorHandler) {
        this.eventStore = eventStore;
        this.errorHandler = errorHandler;
    }

    @EventListener
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void handleLocalEvent(DomainEvent event) {
        processEvent(event);
    }

    @KafkaListener(topics = "domain_events_topic", groupId = "erp-system-domain-events", concurrency = "4")
    public void handleDistributedEvent(DomainEvent event, Acknowledgment acknowledgment) {
        try {
            processEvent(event);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process distributed domain event: {}", event.getEventId(), e);
            errorHandler.handleError(event, e);
        }
    }

    public void processEvent(DomainEvent event) {
        try {
            log.info("Processing domain event: {} for aggregate: {}", 
                    event.getEventType(), event.getAggregateId());

            List<DomainEventHandlerMethodWrapper> handlers = handlerRegistry.get(event.getEventType());
            if (handlers != null && !handlers.isEmpty()) {
                for (DomainEventHandlerMethodWrapper handler : handlers) {
                    try {
                        handler.handle(event);
                    } catch (Exception e) {
                        log.error("Handler {} failed for event {}", handler.getMethodName(), event.getEventId(), e);
                        errorHandler.handleError(event, e);
                    }
                }
            } else {
                log.warn("No handlers found for event type: {}", event.getEventType());
            }

            eventStore.markAsProcessed(event.getEventId());

        } catch (Exception e) {
            log.error("Failed to process domain event: {}", event.getEventId(), e);
            throw e;
        }
    }

    public void registerHandler(String eventType, DomainEventHandlerMethodWrapper handler) {
        handlerRegistry.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }
}
