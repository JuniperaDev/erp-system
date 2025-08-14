package io.github.erp.cqrs.financial.readmodel;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_report_read_model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FinancialReportReadModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_number")
    private String transactionNumber;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "transaction_amount", precision = 21, scale = 2)
    private BigDecimal transactionAmount;

    @Column(name = "settlement_amount", precision = 21, scale = 2)
    private BigDecimal settlementAmount;

    @Column(name = "outstanding_amount", precision = 21, scale = 2)
    private BigDecimal outstandingAmount;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "description")
    private String description;

    @Column(name = "reporting_date")
    private LocalDate reportingDate;

    @Column(name = "fiscal_year")
    private Integer fiscalYear;

    @Column(name = "fiscal_month")
    private Integer fiscalMonth;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;

    public FinancialReportReadModel() {}

    public FinancialReportReadModel id(Long id) {
        this.setId(id);
        return this;
    }

    public FinancialReportReadModel transactionId(Long transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public FinancialReportReadModel transactionType(String transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public FinancialReportReadModel transactionNumber(String transactionNumber) {
        this.setTransactionNumber(transactionNumber);
        return this;
    }

    public FinancialReportReadModel transactionDate(LocalDate transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public FinancialReportReadModel transactionAmount(BigDecimal transactionAmount) {
        this.setTransactionAmount(transactionAmount);
        return this;
    }

    public FinancialReportReadModel settlementAmount(BigDecimal settlementAmount) {
        this.setSettlementAmount(settlementAmount);
        return this;
    }

    public FinancialReportReadModel outstandingAmount(BigDecimal outstandingAmount) {
        this.setOutstandingAmount(outstandingAmount);
        return this;
    }

    public FinancialReportReadModel currencyCode(String currencyCode) {
        this.setCurrencyCode(currencyCode);
        return this;
    }

    public FinancialReportReadModel dealerName(String dealerName) {
        this.setDealerName(dealerName);
        return this;
    }

    public FinancialReportReadModel paymentReference(String paymentReference) {
        this.setPaymentReference(paymentReference);
        return this;
    }

    public FinancialReportReadModel description(String description) {
        this.setDescription(description);
        return this;
    }

    public FinancialReportReadModel reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public FinancialReportReadModel fiscalYear(Integer fiscalYear) {
        this.setFiscalYear(fiscalYear);
        return this;
    }

    public FinancialReportReadModel fiscalMonth(Integer fiscalMonth) {
        this.setFiscalMonth(fiscalMonth);
        return this;
    }

    public FinancialReportReadModel lastUpdated(LocalDate lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(BigDecimal settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public Integer getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Integer getFiscalMonth() {
        return fiscalMonth;
    }

    public void setFiscalMonth(Integer fiscalMonth) {
        this.fiscalMonth = fiscalMonth;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinancialReportReadModel)) return false;
        FinancialReportReadModel that = (FinancialReportReadModel) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "FinancialReportReadModel{" +
            "id=" + id +
            ", transactionId=" + transactionId +
            ", transactionType='" + transactionType + '\'' +
            ", transactionNumber='" + transactionNumber + '\'' +
            ", transactionDate=" + transactionDate +
            ", transactionAmount=" + transactionAmount +
            ", settlementAmount=" + settlementAmount +
            ", outstandingAmount=" + outstandingAmount +
            ", currencyCode='" + currencyCode + '\'' +
            ", dealerName='" + dealerName + '\'' +
            ", paymentReference='" + paymentReference + '\'' +
            ", description='" + description + '\'' +
            ", reportingDate=" + reportingDate +
            ", fiscalYear=" + fiscalYear +
            ", fiscalMonth=" + fiscalMonth +
            ", lastUpdated=" + lastUpdated +
            '}';
    }
}
