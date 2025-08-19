package io.github.erp.internal.acl.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DepreciationData {
    private final String assetId;
    private final BigDecimal depreciationAmount;
    private final LocalDate depreciationDate;
    private final String depreciationMethod;
    private final BigDecimal netBookValue;
    private final String fiscalPeriod;

    private DepreciationData(Builder builder) {
        this.assetId = builder.assetId;
        this.depreciationAmount = builder.depreciationAmount;
        this.depreciationDate = builder.depreciationDate;
        this.depreciationMethod = builder.depreciationMethod;
        this.netBookValue = builder.netBookValue;
        this.fiscalPeriod = builder.fiscalPeriod;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getAssetId() {
        return assetId;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public LocalDate getDepreciationDate() {
        return depreciationDate;
    }

    public String getDepreciationMethod() {
        return depreciationMethod;
    }

    public BigDecimal getNetBookValue() {
        return netBookValue;
    }

    public String getFiscalPeriod() {
        return fiscalPeriod;
    }

    public static class Builder {
        private String assetId;
        private BigDecimal depreciationAmount;
        private LocalDate depreciationDate;
        private String depreciationMethod;
        private BigDecimal netBookValue;
        private String fiscalPeriod;

        public Builder assetId(String assetId) {
            this.assetId = assetId;
            return this;
        }

        public Builder depreciationAmount(BigDecimal depreciationAmount) {
            this.depreciationAmount = depreciationAmount;
            return this;
        }

        public Builder depreciationDate(LocalDate depreciationDate) {
            this.depreciationDate = depreciationDate;
            return this;
        }

        public Builder depreciationMethod(String depreciationMethod) {
            this.depreciationMethod = depreciationMethod;
            return this;
        }

        public Builder netBookValue(BigDecimal netBookValue) {
            this.netBookValue = netBookValue;
            return this;
        }

        public Builder fiscalPeriod(String fiscalPeriod) {
            this.fiscalPeriod = fiscalPeriod;
            return this;
        }

        public DepreciationData build() {
            return new DepreciationData(this);
        }
    }
}
