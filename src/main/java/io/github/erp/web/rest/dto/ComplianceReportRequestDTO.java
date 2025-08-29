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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Schema(description = "Compliance report generation request")
public class ComplianceReportRequestDTO {

    @Schema(description = "Type of compliance report", allowableValues = {"SOX", "GDPR", "IFRS16", "CUSTOM"})
    @NotNull
    @JsonProperty("reportType")
    private String reportType;

    @Schema(description = "Custom report template ID")
    @JsonProperty("templateId")
    private String templateId;

    @Schema(description = "Report start date", example = "2024-01-01T00:00:00Z")
    @NotNull
    @JsonProperty("fromDate")
    private Instant fromDate;

    @Schema(description = "Report end date", example = "2024-12-31T23:59:59Z")
    @NotNull
    @JsonProperty("toDate")
    private Instant toDate;

    @Schema(description = "Filter by specific entity types")
    @JsonProperty("entityTypes")
    private List<String> entityTypes;

    @Schema(description = "Include detailed event information", example = "true")
    @JsonProperty("includeDetails")
    private Boolean includeDetails = true;

    @Schema(description = "Output format", allowableValues = {"PDF", "CSV", "JSON", "XML"}, example = "PDF")
    @JsonProperty("format")
    private String format = "PDF";

    @Schema(description = "Delivery method", allowableValues = {"DOWNLOAD", "EMAIL"}, example = "DOWNLOAD")
    @JsonProperty("deliveryMethod")
    private String deliveryMethod = "DOWNLOAD";

    @Schema(description = "Email recipients for report delivery")
    @JsonProperty("emailRecipients")
    private List<@Email String> emailRecipients;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public List<String> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<String> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public Boolean getIncludeDetails() {
        return includeDetails;
    }

    public void setIncludeDetails(Boolean includeDetails) {
        this.includeDetails = includeDetails;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public List<String> getEmailRecipients() {
        return emailRecipients;
    }

    public void setEmailRecipients(List<String> emailRecipients) {
        this.emailRecipients = emailRecipients;
    }
}
