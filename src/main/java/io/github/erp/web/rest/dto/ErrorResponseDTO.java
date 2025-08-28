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

@Schema(description = "Standard error response")
public class ErrorResponseDTO {

    @Schema(description = "Error timestamp", example = "2024-01-15T10:30:00Z")
    @JsonProperty("timestamp")
    private Instant timestamp;

    @Schema(description = "HTTP status code", example = "400")
    @JsonProperty("status")
    private Integer status;

    @Schema(description = "Error type", example = "Bad Request")
    @JsonProperty("error")
    private String error;

    @Schema(description = "Error message", example = "Invalid date format. Expected ISO 8601 format.")
    @JsonProperty("message")
    private String message;

    @Schema(description = "Request path", example = "/api/v1/audit-trail/events/Payment/123")
    @JsonProperty("path")
    private String path;

    @Schema(description = "Request trace ID for debugging", example = "abc123def456")
    @JsonProperty("traceId")
    private String traceId;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
