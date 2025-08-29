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
import java.util.List;

@Schema(description = "Event replay response")
public class EventReplayResponseDTO {

    @Schema(description = "Aggregate ID")
    @JsonProperty("aggregateId")
    private String aggregateId;

    @Schema(description = "Replay start time")
    @JsonProperty("fromTime")
    private Instant fromTime;

    @Schema(description = "Replay end time")
    @JsonProperty("toTime")
    private Instant toTime;

    @Schema(description = "Replayed events")
    @JsonProperty("events")
    private List<AuditEventDTO> events;

    @Schema(description = "Number of events replayed")
    @JsonProperty("eventCount")
    private Integer eventCount;

    @Schema(description = "Replay execution time in milliseconds")
    @JsonProperty("replayDuration")
    private Integer replayDuration;

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public Instant getFromTime() {
        return fromTime;
    }

    public void setFromTime(Instant fromTime) {
        this.fromTime = fromTime;
    }

    public Instant getToTime() {
        return toTime;
    }

    public void setToTime(Instant toTime) {
        this.toTime = toTime;
    }

    public List<AuditEventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<AuditEventDTO> events) {
        this.events = events;
    }

    public Integer getEventCount() {
        return eventCount;
    }

    public void setEventCount(Integer eventCount) {
        this.eventCount = eventCount;
    }

    public Integer getReplayDuration() {
        return replayDuration;
    }

    public void setReplayDuration(Integer replayDuration) {
        this.replayDuration = replayDuration;
    }
}
