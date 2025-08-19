package io.github.erp.internal.acl.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProjectCostData {
    private final String projectCode;
    private final String projectName;
    private final BigDecimal totalCost;
    private final BigDecimal completionPercentage;
    private final LocalDate transferDate;
    private final String costCenter;
    private final String description;

    private ProjectCostData(Builder builder) {
        this.projectCode = builder.projectCode;
        this.projectName = builder.projectName;
        this.totalCost = builder.totalCost;
        this.completionPercentage = builder.completionPercentage;
        this.transferDate = builder.transferDate;
        this.costCenter = builder.costCenter;
        this.description = builder.description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getProjectCode() {
        return projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public BigDecimal getCompletionPercentage() {
        return completionPercentage;
    }

    public LocalDate getTransferDate() {
        return transferDate;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder {
        private String projectCode;
        private String projectName;
        private BigDecimal totalCost;
        private BigDecimal completionPercentage;
        private LocalDate transferDate;
        private String costCenter;
        private String description;

        public Builder projectCode(String projectCode) {
            this.projectCode = projectCode;
            return this;
        }

        public Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder totalCost(BigDecimal totalCost) {
            this.totalCost = totalCost;
            return this;
        }

        public Builder completionPercentage(BigDecimal completionPercentage) {
            this.completionPercentage = completionPercentage;
            return this;
        }

        public Builder transferDate(LocalDate transferDate) {
            this.transferDate = transferDate;
            return this;
        }

        public Builder costCenter(String costCenter) {
            this.costCenter = costCenter;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public ProjectCostData build() {
            return new ProjectCostData(this);
        }
    }
}
