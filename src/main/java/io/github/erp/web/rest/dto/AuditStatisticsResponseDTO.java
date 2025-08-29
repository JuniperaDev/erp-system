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
import java.util.Map;

@Schema(description = "Audit event statistics response")
public class AuditStatisticsResponseDTO {

    @Schema(description = "Statistics period")
    @JsonProperty("period")
    private PeriodDTO period;

    @Schema(description = "Total number of events")
    @JsonProperty("totalEvents")
    private Long totalEvents;

    @Schema(description = "Event counts by type")
    @JsonProperty("eventsByType")
    private Map<String, Long> eventsByType;

    @Schema(description = "Event counts by user")
    @JsonProperty("eventsByUser")
    private Map<String, Long> eventsByUser;

    @Schema(description = "Event counts by entity type")
    @JsonProperty("eventsByEntity")
    private Map<String, Long> eventsByEntity;

    @Schema(description = "Timeline data")
    @JsonProperty("timeline")
    private List<TimelinePointDTO> timeline;

    @Schema(description = "Top active users")
    @JsonProperty("topUsers")
    private List<TopUserDTO> topUsers;

    @Schema(description = "Trend analysis")
    @JsonProperty("trends")
    private TrendsDTO trends;

    public PeriodDTO getPeriod() {
        return period;
    }

    public void setPeriod(PeriodDTO period) {
        this.period = period;
    }

    public Long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(Long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public Map<String, Long> getEventsByType() {
        return eventsByType;
    }

    public void setEventsByType(Map<String, Long> eventsByType) {
        this.eventsByType = eventsByType;
    }

    public Map<String, Long> getEventsByUser() {
        return eventsByUser;
    }

    public void setEventsByUser(Map<String, Long> eventsByUser) {
        this.eventsByUser = eventsByUser;
    }

    public Map<String, Long> getEventsByEntity() {
        return eventsByEntity;
    }

    public void setEventsByEntity(Map<String, Long> eventsByEntity) {
        this.eventsByEntity = eventsByEntity;
    }

    public List<TimelinePointDTO> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<TimelinePointDTO> timeline) {
        this.timeline = timeline;
    }

    public List<TopUserDTO> getTopUsers() {
        return topUsers;
    }

    public void setTopUsers(List<TopUserDTO> topUsers) {
        this.topUsers = topUsers;
    }

    public TrendsDTO getTrends() {
        return trends;
    }

    public void setTrends(TrendsDTO trends) {
        this.trends = trends;
    }

    public static class PeriodDTO {
        @Schema(description = "Period start date")
        @JsonProperty("from")
        private Instant from;

        @Schema(description = "Period end date")
        @JsonProperty("to")
        private Instant to;

        public Instant getFrom() {
            return from;
        }

        public void setFrom(Instant from) {
            this.from = from;
        }

        public Instant getTo() {
            return to;
        }

        public void setTo(Instant to) {
            this.to = to;
        }
    }

    public static class TimelinePointDTO {
        @Schema(description = "Timestamp")
        @JsonProperty("timestamp")
        private Instant timestamp;

        @Schema(description = "Event count at this point")
        @JsonProperty("count")
        private Long count;

        public Instant getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }
    }

    public static class TopUserDTO {
        @Schema(description = "User ID")
        @JsonProperty("userId")
        private String userId;

        @Schema(description = "Number of events by this user")
        @JsonProperty("eventCount")
        private Long eventCount;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Long getEventCount() {
            return eventCount;
        }

        public void setEventCount(Long eventCount) {
            this.eventCount = eventCount;
        }
    }

    public static class TrendsDTO {
        @Schema(description = "Event growth rate percentage")
        @JsonProperty("growthRate")
        private Double growthRate;

        @Schema(description = "Hour with most activity (0-23)")
        @JsonProperty("peakHour")
        private Integer peakHour;

        @Schema(description = "Average events per day")
        @JsonProperty("averageEventsPerDay")
        private Double averageEventsPerDay;

        public Double getGrowthRate() {
            return growthRate;
        }

        public void setGrowthRate(Double growthRate) {
            this.growthRate = growthRate;
        }

        public Integer getPeakHour() {
            return peakHour;
        }

        public void setPeakHour(Integer peakHour) {
            this.peakHour = peakHour;
        }

        public Double getAverageEventsPerDay() {
            return averageEventsPerDay;
        }

        public void setAverageEventsPerDay(Double averageEventsPerDay) {
            this.averageEventsPerDay = averageEventsPerDay;
        }
    }
}
