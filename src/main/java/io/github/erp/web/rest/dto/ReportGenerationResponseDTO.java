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
import java.util.UUID;

@Schema(description = "Report generation response")
public class ReportGenerationResponseDTO {

    @Schema(description = "Report generation job ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonProperty("jobId")
    private UUID jobId;

    @Schema(description = "Job status", allowableValues = {"QUEUED", "IN_PROGRESS", "COMPLETED", "FAILED"})
    @JsonProperty("status")
    private String status;

    @Schema(description = "Estimated completion time", example = "2024-01-15T10:45:00Z")
    @JsonProperty("estimatedCompletionTime")
    private Instant estimatedCompletionTime;

    @Schema(description = "URL to check job status", example = "/api/v1/audit-trail/compliance-reports/123e4567-e89b-12d3-a456-426614174000")
    @JsonProperty("statusUrl")
    private String statusUrl;

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getEstimatedCompletionTime() {
        return estimatedCompletionTime;
    }

    public void setEstimatedCompletionTime(Instant estimatedCompletionTime) {
        this.estimatedCompletionTime = estimatedCompletionTime;
    }

    public String getStatusUrl() {
        return statusUrl;
    }

    public void setStatusUrl(String statusUrl) {
        this.statusUrl = statusUrl;
    }
}
