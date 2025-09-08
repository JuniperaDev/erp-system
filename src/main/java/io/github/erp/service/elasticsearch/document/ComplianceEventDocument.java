package io.github.erp.service.elasticsearch.document;

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

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.util.Map;

@Document(indexName = "compliance-events-*")
public class ComplianceEventDocument {

    @Id
    private String eventId;

    @Field(type = FieldType.Keyword)
    private String eventType;

    @Field(type = FieldType.Keyword)
    private String aggregateType;

    @Field(type = FieldType.Keyword)
    private String aggregateId;

    @Field(type = FieldType.Date)
    private Instant occurredOn;

    @Field(type = FieldType.Integer)
    private Integer version;

    @Field(type = FieldType.Keyword)
    private String correlationId;

    @Field(type = FieldType.Keyword)
    private String complianceType;

    @Field(type = FieldType.Keyword)
    private String regulationReference;

    @Field(type = FieldType.Keyword)
    private String complianceStatus;

    @Field(type = FieldType.Text)
    private String auditDetails;

    @Field(type = FieldType.Keyword)
    private String auditorId;

    @Field(type = FieldType.Keyword)
    private String riskLevel;

    @Field(type = FieldType.Boolean)
    private Boolean remediationRequired;

    @Field(type = FieldType.Boolean)
    private Boolean processed;

    @Field(type = FieldType.Integer)
    private Integer retryCount;

    @Field(type = FieldType.Object)
    private Map<String, Object> metadata;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String searchableText;

    public ComplianceEventDocument() {}

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
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

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getComplianceType() {
        return complianceType;
    }

    public void setComplianceType(String complianceType) {
        this.complianceType = complianceType;
    }

    public String getRegulationReference() {
        return regulationReference;
    }

    public void setRegulationReference(String regulationReference) {
        this.regulationReference = regulationReference;
    }

    public String getComplianceStatus() {
        return complianceStatus;
    }

    public void setComplianceStatus(String complianceStatus) {
        this.complianceStatus = complianceStatus;
    }

    public String getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(String auditDetails) {
        this.auditDetails = auditDetails;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Boolean getRemediationRequired() {
        return remediationRequired;
    }

    public void setRemediationRequired(Boolean remediationRequired) {
        this.remediationRequired = remediationRequired;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getSearchableText() {
        return searchableText;
    }

    public void setSearchableText(String searchableText) {
        this.searchableText = searchableText;
    }
}
