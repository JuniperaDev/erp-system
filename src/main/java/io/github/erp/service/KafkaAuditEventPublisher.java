package io.github.erp.service;

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

import io.github.erp.domain.events.audit.AuditTrailEvent;
import io.github.erp.domain.events.audit.ComplianceAuditEvent;

import java.util.concurrent.CompletableFuture;

/**
 * Service interface for publishing audit events to Kafka topics.
 * Provides methods for publishing different types of audit events to their respective topics.
 */
public interface KafkaAuditEventPublisher {

    /**
     * Publishes a business audit event to the business events topic.
     *
     * @param event the audit trail event to publish
     * @return CompletableFuture that completes when the event is published
     */
    CompletableFuture<Void> publishBusinessEvent(AuditTrailEvent event);

    /**
     * Publishes a compliance audit event to the compliance events topic.
     *
     * @param event the compliance audit event to publish
     * @return CompletableFuture that completes when the event is published
     */
    CompletableFuture<Void> publishComplianceEvent(ComplianceAuditEvent event);

    /**
     * Publishes a security audit event to the security events topic.
     *
     * @param event the audit trail event to publish
     * @return CompletableFuture that completes when the event is published
     */
    CompletableFuture<Void> publishSecurityEvent(AuditTrailEvent event);

    /**
     * Publishes a system audit event to the system events topic.
     *
     * @param event the audit trail event to publish
     * @return CompletableFuture that completes when the event is published
     */
    CompletableFuture<Void> publishSystemEvent(AuditTrailEvent event);

    /**
     * Publishes an audit event to the appropriate topic based on event type.
     *
     * @param event the audit trail event to publish
     * @param eventCategory the category of the event (BUSINESS, COMPLIANCE, SECURITY, SYSTEM)
     * @return CompletableFuture that completes when the event is published
     */
    CompletableFuture<Void> publishAuditEvent(AuditTrailEvent event, String eventCategory);

    /**
     * Publishes a compliance audit event to the appropriate topic based on event type.
     *
     * @param event the compliance audit event to publish
     * @param eventCategory the category of the event (COMPLIANCE)
     * @return CompletableFuture that completes when the event is published
     */
    CompletableFuture<Void> publishComplianceAuditEvent(ComplianceAuditEvent event, String eventCategory);
}
