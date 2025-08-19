package io.github.erp.internal.acl.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class ReportingData {
    private final String entityId;
    private final String entityType;
    private final LocalDate reportingDate;
    private final Map<String, BigDecimal> financialMetrics;
    private final Map<String, String> attributes;
    private final String reportingPeriod;

    private ReportingData(Builder builder) {
        this.entityId = builder.entityId;
        this.entityType = builder.entityType;
        this.reportingDate = builder.reportingDate;
        this.financialMetrics = builder.financialMetrics;
        this.attributes = builder.attributes;
        this.reportingPeriod = builder.reportingPeriod;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEntityId() {
        return entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public LocalDate getReportingDate() {
        return reportingDate;
    }

    public Map<String, BigDecimal> getFinancialMetrics() {
        return financialMetrics;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getReportingPeriod() {
        return reportingPeriod;
    }

    public static class Builder {
        private String entityId;
        private String entityType;
        private LocalDate reportingDate;
        private Map<String, BigDecimal> financialMetrics;
        private Map<String, String> attributes;
        private String reportingPeriod;

        public Builder entityId(String entityId) {
            this.entityId = entityId;
            return this;
        }

        public Builder entityType(String entityType) {
            this.entityType = entityType;
            return this;
        }

        public Builder reportingDate(LocalDate reportingDate) {
            this.reportingDate = reportingDate;
            return this;
        }

        public Builder financialMetrics(Map<String, BigDecimal> financialMetrics) {
            this.financialMetrics = financialMetrics;
            return this;
        }

        public Builder attributes(Map<String, String> attributes) {
            this.attributes = attributes;
            return this;
        }

        public Builder reportingPeriod(String reportingPeriod) {
            this.reportingPeriod = reportingPeriod;
            return this;
        }

        public ReportingData build() {
            return new ReportingData(this);
        }
    }
}
