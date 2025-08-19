package io.github.erp.internal.acl.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AssetAcquisitionData {
    private final String assetNumber;
    private final String assetTag;
    private final BigDecimal acquisitionCost;
    private final LocalDate acquisitionDate;
    private final String vendorName;
    private final String vendorCode;
    private final String description;
    private final String currencyCode;

    private AssetAcquisitionData(Builder builder) {
        this.assetNumber = builder.assetNumber;
        this.assetTag = builder.assetTag;
        this.acquisitionCost = builder.acquisitionCost;
        this.acquisitionDate = builder.acquisitionDate;
        this.vendorName = builder.vendorName;
        this.vendorCode = builder.vendorCode;
        this.description = builder.description;
        this.currencyCode = builder.currencyCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public BigDecimal getAcquisitionCost() {
        return acquisitionCost;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public static class Builder {
        private String assetNumber;
        private String assetTag;
        private BigDecimal acquisitionCost;
        private LocalDate acquisitionDate;
        private String vendorName;
        private String vendorCode;
        private String description;
        private String currencyCode;

        public Builder assetNumber(String assetNumber) {
            this.assetNumber = assetNumber;
            return this;
        }

        public Builder assetTag(String assetTag) {
            this.assetTag = assetTag;
            return this;
        }

        public Builder acquisitionCost(BigDecimal acquisitionCost) {
            this.acquisitionCost = acquisitionCost;
            return this;
        }

        public Builder acquisitionDate(LocalDate acquisitionDate) {
            this.acquisitionDate = acquisitionDate;
            return this;
        }

        public Builder vendorName(String vendorName) {
            this.vendorName = vendorName;
            return this;
        }

        public Builder vendorCode(String vendorCode) {
            this.vendorCode = vendorCode;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public AssetAcquisitionData build() {
            return new AssetAcquisitionData(this);
        }
    }
}
