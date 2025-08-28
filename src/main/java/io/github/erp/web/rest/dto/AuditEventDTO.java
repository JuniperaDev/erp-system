package io.github.erp.web.rest.dto;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Schema(description = "Base audit event structure")
public class AuditEventDTO {

    @Schema(description = "Unique event identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonProperty("eventId")
    private UUID eventId;

    @Schema(description = "Type of audit event", allowableValues = {"CREATED", "UPDATED", "DELETED", "ACCESSED", "PROCESSED", "APPROVED", "REJECTED"})
    @NotNull
    @JsonProperty("eventType")
    private String eventType;

    @Schema(description = "Entity type being audited", example = "Payment")
    @NotNull
    @JsonProperty("aggregateType")
    private String aggregateType;

    @Schema(description = "Entity instance identifier", example = "12345")
    @NotNull
    @JsonProperty("aggregateId")
    private String aggregateId;

    @Schema(description = "Event timestamp", example = "2024-01-15T10:30:00Z")
    @NotNull
    @JsonProperty("occurredOn")
    private Instant occurredOn;

    @Schema(description = "Event version for ordering")
    @JsonProperty("version")
    private Integer version;

    @Schema(description = "Transaction correlation identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonProperty("correlationId")
    private UUID correlationId;

    @Schema(description = "User performing the action", example = "john.doe")
    @NotNull
    @JsonProperty("userId")
    private String userId;

    @Schema(description = "User session identifier")
    @JsonProperty("sessionId")
    private String sessionId;

    @Schema(description = "Client IP address", example = "192.168.1.100")
    @JsonProperty("ipAddress")
    private String ipAddress;

    @Schema(description = "Client user agent")
    @JsonProperty("userAgent")
    private String userAgent;

    @Schema(description = "Entity state changes")
    @JsonProperty("changes")
    private ChangesDTO changes;

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }

    public void setOccurredOn(Instant occurredOn) {
        this.occurredOn = occurredOn;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public ChangesDTO getChanges() {
        return changes;
    }

    public void setChanges(ChangesDTO changes) {
        this.changes = changes;
    }

    public static class ChangesDTO {
        @Schema(description = "Previous entity state")
        @JsonProperty("oldValues")
        private Map<String, Object> oldValues;

        @Schema(description = "New entity state")
        @JsonProperty("newValues")
        private Map<String, Object> newValues;

        @Schema(description = "List of changed field names")
        @JsonProperty("changedFields")
        private String[] changedFields;

        public Map<String, Object> getOldValues() {
            return oldValues;
        }

        public void setOldValues(Map<String, Object> oldValues) {
            this.oldValues = oldValues;
        }

        public Map<String, Object> getNewValues() {
            return newValues;
        }

        public void setNewValues(Map<String, Object> newValues) {
            this.newValues = newValues;
        }

        public String[] getChangedFields() {
            return changedFields;
        }

        public void setChangedFields(String[] changedFields) {
            this.changedFields = changedFields;
        }
    }
}
