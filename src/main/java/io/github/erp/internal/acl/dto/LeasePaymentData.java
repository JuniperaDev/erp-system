package io.github.erp.internal.acl.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LeasePaymentData {
    private final String leaseContractId;
    private final BigDecimal paymentAmount;
    private final LocalDate paymentDate;
    private final String paymentType;
    private final String currencyCode;
    private final String description;

    private LeasePaymentData(Builder builder) {
        this.leaseContractId = builder.leaseContractId;
        this.paymentAmount = builder.paymentAmount;
        this.paymentDate = builder.paymentDate;
        this.paymentType = builder.paymentType;
        this.currencyCode = builder.currencyCode;
        this.description = builder.description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getLeaseContractId() {
        return leaseContractId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder {
        private String leaseContractId;
        private BigDecimal paymentAmount;
        private LocalDate paymentDate;
        private String paymentType;
        private String currencyCode;
        private String description;

        public Builder leaseContractId(String leaseContractId) {
            this.leaseContractId = leaseContractId;
            return this;
        }

        public Builder paymentAmount(BigDecimal paymentAmount) {
            this.paymentAmount = paymentAmount;
            return this;
        }

        public Builder paymentDate(LocalDate paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        public Builder paymentType(String paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public Builder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public LeasePaymentData build() {
            return new LeasePaymentData(this);
        }
    }
}
