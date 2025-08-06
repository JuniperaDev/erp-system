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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DomainEventPublisher.class);

    @Value("${spring.kafka.topics.domain-events.topic.name:domain_events_topic}")
    private String domainEventsTopicName;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final KafkaTemplate<String, DomainEvent> kafkaTemplate;
    private final DomainEventStore eventStore;

    public DomainEventPublisher(ApplicationEventPublisher applicationEventPublisher,
                              KafkaTemplate<String, DomainEvent> kafkaTemplate,
                              DomainEventStore eventStore) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.kafkaTemplate = kafkaTemplate;
        this.eventStore = eventStore;
    }

    public void publish(DomainEvent event) {
        try {
            DomainEvent storedEvent = eventStore.store(event);
            
            log.info("Publishing domain event: {} for aggregate: {}", 
                    event.getEventType(), event.getAggregateId());

            applicationEventPublisher.publishEvent(event);

            String partitionKey = event.getAggregateType() + ":" + event.getAggregateId();
            kafkaTemplate.send(domainEventsTopicName, partitionKey, event);

            log.debug("Successfully published domain event: {}", event.getEventId());

        } catch (Exception e) {
            log.error("Failed to publish domain event: {}", event.getEventId(), e);
            throw new DomainEventPublishingException("Failed to publish domain event", e);
        }
    }
}
