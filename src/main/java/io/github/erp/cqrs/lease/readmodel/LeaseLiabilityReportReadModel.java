package io.github.erp.cqrs.lease.readmodel;

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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lease_liability_report_read_model", indexes = {
    @Index(name = "idx_lease_liability_date", columnList = "reporting_date"),
    @Index(name = "idx_lease_liability_contract", columnList = "lease_contract_number"),
    @Index(name = "idx_lease_liability_period", columnList = "liability_period"),
    @Index(name = "idx_lease_liability_fiscal_year", columnList = "fiscal_year"),
    @Index(name = "idx_lease_liability_maturity", columnList = "maturity_classification")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LeaseLiabilityReportReadModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "lease_id", nullable = false)
    private Long leaseId;

    @Column(name = "lease_contract_number")
    private String leaseContractNumber;

    @Column(name = "lease_title")
    private String leaseTitle;

    @Column(name = "lessee_name")
    private String lesseeName;

    @Column(name = "lessee_id")
    private Long lesseeId;

    @Column(name = "opening_balance", precision = 21, scale = 2)
    private BigDecimal openingBalance;

    @Column(name = "cash_payment", precision = 21, scale = 2)
    private BigDecimal cashPayment;

    @Column(name = "principal_payment", precision = 21, scale = 2)
    private BigDecimal principalPayment;

    @Column(name = "interest_payment", precision = 21, scale = 2)
    private BigDecimal interestPayment;

    @Column(name = "outstanding_balance", precision = 21, scale = 2)
    private BigDecimal outstandingBalance;

    @Column(name = "interest_payable_opening", precision = 21, scale = 2)
    private BigDecimal interestPayableOpening;

    @Column(name = "interest_expense_accrued", precision = 21, scale = 2)
    private BigDecimal interestExpenseAccrued;

    @Column(name = "interest_payable_balance", precision = 21, scale = 2)
    private BigDecimal interestPayableBalance;

    @Column(name = "liability_period")
    private String liabilityPeriod;

    @Column(name = "reporting_date")
    private LocalDate reportingDate;

    @Column(name = "fiscal_year")
    private Integer fiscalYear;

    @Column(name = "fiscal_quarter")
    private String fiscalQuarter;

    @Column(name = "maturity_classification")
    private String maturityClassification;

    @Column(name = "current_portion", precision = 21, scale = 2)
    private BigDecimal currentPortion;

    @Column(name = "non_current_portion", precision = 21, scale = 2)
    private BigDecimal nonCurrentPortion;

    @Column(name = "interest_rate", precision = 21, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "incremental_borrowing_rate", precision = 21, scale = 4)
    private BigDecimal incrementalBorrowingRate;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;

    public Long getId() {
        return this.id;
    }

    public LeaseLiabilityReportReadModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLeaseId() {
        return this.leaseId;
    }

    public LeaseLiabilityReportReadModel leaseId(Long leaseId) {
        this.setLeaseId(leaseId);
        return this;
    }

    public void setLeaseId(Long leaseId) {
        this.leaseId = leaseId;
    }

    public String getLeaseContractNumber() {
        return this.leaseContractNumber;
    }

    public LeaseLiabilityReportReadModel leaseContractNumber(String leaseContractNumber) {
        this.setLeaseContractNumber(leaseContractNumber);
        return this;
    }

    public void setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public String getLeaseTitle() {
        return this.leaseTitle;
    }

    public LeaseLiabilityReportReadModel leaseTitle(String leaseTitle) {
        this.setLeaseTitle(leaseTitle);
        return this;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public String getLesseeName() {
        return this.lesseeName;
    }

    public LeaseLiabilityReportReadModel lesseeName(String lesseeName) {
        this.setLesseeName(lesseeName);
        return this;
    }

    public void setLesseeName(String lesseeName) {
        this.lesseeName = lesseeName;
    }

    public Long getLesseeId() {
        return this.lesseeId;
    }

    public LeaseLiabilityReportReadModel lesseeId(Long lesseeId) {
        this.setLesseeId(lesseeId);
        return this;
    }

    public void setLesseeId(Long lesseeId) {
        this.lesseeId = lesseeId;
    }

    public BigDecimal getOpeningBalance() {
        return this.openingBalance;
    }

    public LeaseLiabilityReportReadModel openingBalance(BigDecimal openingBalance) {
        this.setOpeningBalance(openingBalance);
        return this;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimal getCashPayment() {
        return this.cashPayment;
    }

    public LeaseLiabilityReportReadModel cashPayment(BigDecimal cashPayment) {
        this.setCashPayment(cashPayment);
        return this;
    }

    public void setCashPayment(BigDecimal cashPayment) {
        this.cashPayment = cashPayment;
    }

    public BigDecimal getPrincipalPayment() {
        return this.principalPayment;
    }

    public LeaseLiabilityReportReadModel principalPayment(BigDecimal principalPayment) {
        this.setPrincipalPayment(principalPayment);
        return this;
    }

    public void setPrincipalPayment(BigDecimal principalPayment) {
        this.principalPayment = principalPayment;
    }

    public BigDecimal getInterestPayment() {
        return this.interestPayment;
    }

    public LeaseLiabilityReportReadModel interestPayment(BigDecimal interestPayment) {
        this.setInterestPayment(interestPayment);
        return this;
    }

    public void setInterestPayment(BigDecimal interestPayment) {
        this.interestPayment = interestPayment;
    }

    public BigDecimal getOutstandingBalance() {
        return this.outstandingBalance;
    }

    public LeaseLiabilityReportReadModel outstandingBalance(BigDecimal outstandingBalance) {
        this.setOutstandingBalance(outstandingBalance);
        return this;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public BigDecimal getInterestPayableOpening() {
        return this.interestPayableOpening;
    }

    public LeaseLiabilityReportReadModel interestPayableOpening(BigDecimal interestPayableOpening) {
        this.setInterestPayableOpening(interestPayableOpening);
        return this;
    }

    public void setInterestPayableOpening(BigDecimal interestPayableOpening) {
        this.interestPayableOpening = interestPayableOpening;
    }

    public BigDecimal getInterestExpenseAccrued() {
        return this.interestExpenseAccrued;
    }

    public LeaseLiabilityReportReadModel interestExpenseAccrued(BigDecimal interestExpenseAccrued) {
        this.setInterestExpenseAccrued(interestExpenseAccrued);
        return this;
    }

    public void setInterestExpenseAccrued(BigDecimal interestExpenseAccrued) {
        this.interestExpenseAccrued = interestExpenseAccrued;
    }

    public BigDecimal getInterestPayableBalance() {
        return this.interestPayableBalance;
    }

    public LeaseLiabilityReportReadModel interestPayableBalance(BigDecimal interestPayableBalance) {
        this.setInterestPayableBalance(interestPayableBalance);
        return this;
    }

    public void setInterestPayableBalance(BigDecimal interestPayableBalance) {
        this.interestPayableBalance = interestPayableBalance;
    }

    public String getLiabilityPeriod() {
        return this.liabilityPeriod;
    }

    public LeaseLiabilityReportReadModel liabilityPeriod(String liabilityPeriod) {
        this.setLiabilityPeriod(liabilityPeriod);
        return this;
    }

    public void setLiabilityPeriod(String liabilityPeriod) {
        this.liabilityPeriod = liabilityPeriod;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public LeaseLiabilityReportReadModel reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public Integer getFiscalYear() {
        return this.fiscalYear;
    }

    public LeaseLiabilityReportReadModel fiscalYear(Integer fiscalYear) {
        this.setFiscalYear(fiscalYear);
        return this;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getFiscalQuarter() {
        return this.fiscalQuarter;
    }

    public LeaseLiabilityReportReadModel fiscalQuarter(String fiscalQuarter) {
        this.setFiscalQuarter(fiscalQuarter);
        return this;
    }

    public void setFiscalQuarter(String fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public String getMaturityClassification() {
        return this.maturityClassification;
    }

    public LeaseLiabilityReportReadModel maturityClassification(String maturityClassification) {
        this.setMaturityClassification(maturityClassification);
        return this;
    }

    public void setMaturityClassification(String maturityClassification) {
        this.maturityClassification = maturityClassification;
    }

    public BigDecimal getCurrentPortion() {
        return this.currentPortion;
    }

    public LeaseLiabilityReportReadModel currentPortion(BigDecimal currentPortion) {
        this.setCurrentPortion(currentPortion);
        return this;
    }

    public void setCurrentPortion(BigDecimal currentPortion) {
        this.currentPortion = currentPortion;
    }

    public BigDecimal getNonCurrentPortion() {
        return this.nonCurrentPortion;
    }

    public LeaseLiabilityReportReadModel nonCurrentPortion(BigDecimal nonCurrentPortion) {
        this.setNonCurrentPortion(nonCurrentPortion);
        return this;
    }

    public void setNonCurrentPortion(BigDecimal nonCurrentPortion) {
        this.nonCurrentPortion = nonCurrentPortion;
    }

    public BigDecimal getInterestRate() {
        return this.interestRate;
    }

    public LeaseLiabilityReportReadModel interestRate(BigDecimal interestRate) {
        this.setInterestRate(interestRate);
        return this;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getIncrementalBorrowingRate() {
        return this.incrementalBorrowingRate;
    }

    public LeaseLiabilityReportReadModel incrementalBorrowingRate(BigDecimal incrementalBorrowingRate) {
        this.setIncrementalBorrowingRate(incrementalBorrowingRate);
        return this;
    }

    public void setIncrementalBorrowingRate(BigDecimal incrementalBorrowingRate) {
        this.incrementalBorrowingRate = incrementalBorrowingRate;
    }

    public LocalDate getLastUpdated() {
        return this.lastUpdated;
    }

    public LeaseLiabilityReportReadModel lastUpdated(LocalDate lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseLiabilityReportReadModel)) {
            return false;
        }
        return id != null && id.equals(((LeaseLiabilityReportReadModel) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "LeaseLiabilityReportReadModel{" +
            "id=" + getId() +
            ", leaseId=" + getLeaseId() +
            ", leaseContractNumber='" + getLeaseContractNumber() + "'" +
            ", leaseTitle='" + getLeaseTitle() + "'" +
            ", lesseeName='" + getLesseeName() + "'" +
            ", openingBalance=" + getOpeningBalance() +
            ", outstandingBalance=" + getOutstandingBalance() +
            ", liabilityPeriod='" + getLiabilityPeriod() + "'" +
            ", reportingDate='" + getReportingDate() + "'" +
            ", fiscalYear=" + getFiscalYear() +
            "}";
    }
}
