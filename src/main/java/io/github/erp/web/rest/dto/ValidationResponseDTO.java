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

import java.util.List;

@Schema(description = "Event validation response")
public class ValidationResponseDTO {

    @Schema(description = "Aggregate ID that was validated")
    @JsonProperty("aggregateId")
    private String aggregateId;

    @Schema(description = "Whether validation passed")
    @JsonProperty("valid")
    private Boolean valid;

    @Schema(description = "Number of events validated")
    @JsonProperty("eventCount")
    private Integer eventCount;

    @Schema(description = "List of validation errors")
    @JsonProperty("validationErrors")
    private List<ValidationErrorDTO> validationErrors;

    @Schema(description = "Validation summary statistics")
    @JsonProperty("validationSummary")
    private ValidationSummaryDTO validationSummary;

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Integer getEventCount() {
        return eventCount;
    }

    public void setEventCount(Integer eventCount) {
        this.eventCount = eventCount;
    }

    public List<ValidationErrorDTO> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationErrorDTO> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public ValidationSummaryDTO getValidationSummary() {
        return validationSummary;
    }

    public void setValidationSummary(ValidationSummaryDTO validationSummary) {
        this.validationSummary = validationSummary;
    }

    public static class ValidationErrorDTO {
        @Schema(description = "Event ID with validation error")
        @JsonProperty("eventId")
        private String eventId;

        @Schema(description = "Type of validation error", allowableValues = {"ORDERING_VIOLATION", "VERSION_MISMATCH", "INTEGRITY_FAILURE"})
        @JsonProperty("errorType")
        private String errorType;

        @Schema(description = "Error message")
        @JsonProperty("message")
        private String message;

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getErrorType() {
            return errorType;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ValidationSummaryDTO {
        @Schema(description = "Total number of events")
        @JsonProperty("totalEvents")
        private Integer totalEvents;

        @Schema(description = "Number of valid events")
        @JsonProperty("validEvents")
        private Integer validEvents;

        @Schema(description = "Number of invalid events")
        @JsonProperty("invalidEvents")
        private Integer invalidEvents;

        @Schema(description = "Number of warnings")
        @JsonProperty("warningCount")
        private Integer warningCount;

        public Integer getTotalEvents() {
            return totalEvents;
        }

        public void setTotalEvents(Integer totalEvents) {
            this.totalEvents = totalEvents;
        }

        public Integer getValidEvents() {
            return validEvents;
        }

        public void setValidEvents(Integer validEvents) {
            this.validEvents = validEvents;
        }

        public Integer getInvalidEvents() {
            return invalidEvents;
        }

        public void setInvalidEvents(Integer invalidEvents) {
            this.invalidEvents = invalidEvents;
        }

        public Integer getWarningCount() {
            return warningCount;
        }

        public void setWarningCount(Integer warningCount) {
            this.warningCount = warningCount;
        }
    }
}
