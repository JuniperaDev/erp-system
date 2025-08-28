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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.List;

@Schema(description = "Advanced audit event search request")
public class AuditSearchRequestDTO {

    @Schema(description = "Full-text search query")
    @JsonProperty("query")
    private String query;

    @Schema(description = "Search filters")
    @JsonProperty("filters")
    private FiltersDTO filters;

    @Schema(description = "Aggregation configurations")
    @JsonProperty("aggregations")
    private List<AggregationDTO> aggregations;

    @Schema(description = "Sort criteria")
    @JsonProperty("sort")
    private List<SortDTO> sort;

    @Schema(description = "Page number", example = "0")
    @JsonProperty("page")
    @Min(0)
    private Integer page = 0;

    @Schema(description = "Page size", example = "20")
    @JsonProperty("size")
    @Min(1)
    @Max(100)
    private Integer size = 20;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public FiltersDTO getFilters() {
        return filters;
    }

    public void setFilters(FiltersDTO filters) {
        this.filters = filters;
    }

    public List<AggregationDTO> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<AggregationDTO> aggregations) {
        this.aggregations = aggregations;
    }

    public List<SortDTO> getSort() {
        return sort;
    }

    public void setSort(List<SortDTO> sort) {
        this.sort = sort;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public static class FiltersDTO {
        @Schema(description = "Filter by entity types")
        @JsonProperty("entityTypes")
        private List<String> entityTypes;

        @Schema(description = "Filter by event types")
        @JsonProperty("eventTypes")
        private List<String> eventTypes;

        @Schema(description = "Filter by user IDs")
        @JsonProperty("userIds")
        private List<String> userIds;

        @Schema(description = "Date range filter")
        @JsonProperty("dateRange")
        private DateRangeDTO dateRange;

        @Schema(description = "Filter by IP addresses")
        @JsonProperty("ipAddresses")
        private List<String> ipAddresses;

        public List<String> getEntityTypes() {
            return entityTypes;
        }

        public void setEntityTypes(List<String> entityTypes) {
            this.entityTypes = entityTypes;
        }

        public List<String> getEventTypes() {
            return eventTypes;
        }

        public void setEventTypes(List<String> eventTypes) {
            this.eventTypes = eventTypes;
        }

        public List<String> getUserIds() {
            return userIds;
        }

        public void setUserIds(List<String> userIds) {
            this.userIds = userIds;
        }

        public DateRangeDTO getDateRange() {
            return dateRange;
        }

        public void setDateRange(DateRangeDTO dateRange) {
            this.dateRange = dateRange;
        }

        public List<String> getIpAddresses() {
            return ipAddresses;
        }

        public void setIpAddresses(List<String> ipAddresses) {
            this.ipAddresses = ipAddresses;
        }
    }

    public static class DateRangeDTO {
        @Schema(description = "Start date", example = "2024-01-01T00:00:00Z")
        @JsonProperty("from")
        private Instant from;

        @Schema(description = "End date", example = "2024-12-31T23:59:59Z")
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

    public static class AggregationDTO {
        @Schema(description = "Field to aggregate on")
        @JsonProperty("field")
        private String field;

        @Schema(description = "Aggregation type", allowableValues = {"terms", "date_histogram", "range"})
        @JsonProperty("type")
        private String type;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class SortDTO {
        @Schema(description = "Field to sort by")
        @JsonProperty("field")
        private String field;

        @Schema(description = "Sort direction", allowableValues = {"asc", "desc"})
        @JsonProperty("direction")
        private String direction;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }
}
