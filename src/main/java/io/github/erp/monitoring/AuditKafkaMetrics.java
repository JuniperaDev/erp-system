package io.github.erp.monitoring;

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

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class AuditKafkaMetrics {

    private final Counter auditEventsPublished;
    private final Counter auditEventsConsumed;
    private final Counter auditEventPublishFailures;
    private final Counter auditEventConsumptionFailures;
    private final Timer auditEventPublishTime;
    private final Timer auditEventProcessingTime;

    public AuditKafkaMetrics(MeterRegistry meterRegistry) {
        this.auditEventsPublished = Counter.builder("audit.events.published")
            .description("Number of audit events published to Kafka")
            .register(meterRegistry);
        
        this.auditEventsConsumed = Counter.builder("audit.events.consumed")
            .description("Number of audit events consumed from Kafka")
            .register(meterRegistry);
        
        this.auditEventPublishFailures = Counter.builder("audit.events.publish.failures")
            .description("Number of audit event publish failures")
            .register(meterRegistry);
        
        this.auditEventConsumptionFailures = Counter.builder("audit.events.consumption.failures")
            .description("Number of audit event consumption failures")
            .register(meterRegistry);
        
        this.auditEventPublishTime = Timer.builder("audit.events.publish.time")
            .description("Time taken to publish audit events")
            .register(meterRegistry);
        
        this.auditEventProcessingTime = Timer.builder("audit.events.processing.time")
            .description("Time taken to process audit events")
            .register(meterRegistry);
    }

    public void incrementPublishedEvents() {
        auditEventsPublished.increment();
    }

    public void incrementConsumedEvents() {
        auditEventsConsumed.increment();
    }

    public void incrementPublishFailures() {
        auditEventPublishFailures.increment();
    }

    public void incrementConsumptionFailures() {
        auditEventConsumptionFailures.increment();
    }

    public Timer.Sample startPublishTimer() {
        return Timer.start(auditEventPublishTime);
    }

    public Timer.Sample startProcessingTimer() {
        return Timer.start(auditEventProcessingTime);
    }
}
