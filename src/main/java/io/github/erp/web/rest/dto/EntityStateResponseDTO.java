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

import java.time.Instant;
import java.util.Map;

@Schema(description = "Entity state reconstruction response")
public class EntityStateResponseDTO {

    @Schema(description = "Entity type")
    @JsonProperty("entityType")
    private String entityType;

    @Schema(description = "Entity ID")
    @JsonProperty("entityId")
    private String entityId;

    @Schema(description = "Point in time for reconstruction")
    @JsonProperty("asOfDate")
    private Instant asOfDate;

    @Schema(description = "Reconstructed entity state")
    @JsonProperty("state")
    private Map<String, Object> state;

    @Schema(description = "Number of events used for reconstruction")
    @JsonProperty("eventCount")
    private Integer eventCount;

    @Schema(description = "Date of last event used")
    @JsonProperty("lastEventDate")
    private Instant lastEventDate;

    @Schema(description = "Entity version")
    @JsonProperty("version")
    private Integer version;

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Instant getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Instant asOfDate) {
        this.asOfDate = asOfDate;
    }

    public Map<String, Object> getState() {
        return state;
    }

    public void setState(Map<String, Object> state) {
        this.state = state;
    }

    public Integer getEventCount() {
        return eventCount;
    }

    public void setEventCount(Integer eventCount) {
        this.eventCount = eventCount;
    }

    public Instant getLastEventDate() {
        return lastEventDate;
    }

    public void setLastEventDate(Instant lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
