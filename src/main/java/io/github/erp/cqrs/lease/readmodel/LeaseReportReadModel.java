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
@Table(name = "lease_report_read_model", indexes = {
    @Index(name = "idx_lease_report_date", columnList = "reporting_date"),
    @Index(name = "idx_lease_report_contract", columnList = "lease_contract_number"),
    @Index(name = "idx_lease_report_lessee", columnList = "lessee_name"),
    @Index(name = "idx_lease_report_status", columnList = "lease_status"),
    @Index(name = "idx_lease_report_fiscal_year", columnList = "fiscal_year")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LeaseReportReadModel implements Serializable {

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

    @Column(name = "lessor_name")
    private String lessorName;

    @Column(name = "lessor_id")
    private Long lessorId;

    @Column(name = "lease_amount", precision = 21, scale = 2)
    private BigDecimal leaseAmount;

    @Column(name = "lease_liability", precision = 21, scale = 2)
    private BigDecimal leaseLiability;

    @Column(name = "rou_asset_value", precision = 21, scale = 2)
    private BigDecimal rouAssetValue;

    @Column(name = "lease_commencement_date")
    private LocalDate leaseCommencementDate;

    @Column(name = "lease_expiry_date")
    private LocalDate leaseExpiryDate;

    @Column(name = "lease_term_months")
    private Integer leaseTermMonths;

    @Column(name = "lease_status")
    private String leaseStatus;

    @Column(name = "lease_type")
    private String leaseType;

    @Column(name = "reporting_date")
    private LocalDate reportingDate;

    @Column(name = "fiscal_year")
    private Integer fiscalYear;

    @Column(name = "fiscal_quarter")
    private String fiscalQuarter;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;

    @Column(name = "interest_rate", precision = 21, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "incremental_borrowing_rate", precision = 21, scale = 4)
    private BigDecimal incrementalBorrowingRate;

    @Column(name = "accumulated_depreciation", precision = 21, scale = 2)
    private BigDecimal accumulatedDepreciation;

    @Column(name = "current_rou_nbv", precision = 21, scale = 2)
    private BigDecimal currentRouNBV;

    public Long getId() {
        return this.id;
    }

    public LeaseReportReadModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLeaseId() {
        return this.leaseId;
    }

    public LeaseReportReadModel leaseId(Long leaseId) {
        this.setLeaseId(leaseId);
        return this;
    }

    public void setLeaseId(Long leaseId) {
        this.leaseId = leaseId;
    }

    public String getLeaseContractNumber() {
        return this.leaseContractNumber;
    }

    public LeaseReportReadModel leaseContractNumber(String leaseContractNumber) {
        this.setLeaseContractNumber(leaseContractNumber);
        return this;
    }

    public void setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public String getLeaseTitle() {
        return this.leaseTitle;
    }

    public LeaseReportReadModel leaseTitle(String leaseTitle) {
        this.setLeaseTitle(leaseTitle);
        return this;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public String getLesseeName() {
        return this.lesseeName;
    }

    public LeaseReportReadModel lesseeName(String lesseeName) {
        this.setLesseeName(lesseeName);
        return this;
    }

    public void setLesseeName(String lesseeName) {
        this.lesseeName = lesseeName;
    }

    public Long getLesseeId() {
        return this.lesseeId;
    }

    public LeaseReportReadModel lesseeId(Long lesseeId) {
        this.setLesseeId(lesseeId);
        return this;
    }

    public void setLesseeId(Long lesseeId) {
        this.lesseeId = lesseeId;
    }

    public String getLessorName() {
        return this.lessorName;
    }

    public LeaseReportReadModel lessorName(String lessorName) {
        this.setLessorName(lessorName);
        return this;
    }

    public void setLessorName(String lessorName) {
        this.lessorName = lessorName;
    }

    public Long getLessorId() {
        return this.lessorId;
    }

    public LeaseReportReadModel lessorId(Long lessorId) {
        this.setLessorId(lessorId);
        return this;
    }

    public void setLessorId(Long lessorId) {
        this.lessorId = lessorId;
    }

    public BigDecimal getLeaseAmount() {
        return this.leaseAmount;
    }

    public LeaseReportReadModel leaseAmount(BigDecimal leaseAmount) {
        this.setLeaseAmount(leaseAmount);
        return this;
    }

    public void setLeaseAmount(BigDecimal leaseAmount) {
        this.leaseAmount = leaseAmount;
    }

    public BigDecimal getLeaseLiability() {
        return this.leaseLiability;
    }

    public LeaseReportReadModel leaseLiability(BigDecimal leaseLiability) {
        this.setLeaseLiability(leaseLiability);
        return this;
    }

    public void setLeaseLiability(BigDecimal leaseLiability) {
        this.leaseLiability = leaseLiability;
    }

    public BigDecimal getRouAssetValue() {
        return this.rouAssetValue;
    }

    public LeaseReportReadModel rouAssetValue(BigDecimal rouAssetValue) {
        this.setRouAssetValue(rouAssetValue);
        return this;
    }

    public void setRouAssetValue(BigDecimal rouAssetValue) {
        this.rouAssetValue = rouAssetValue;
    }

    public LocalDate getLeaseCommencementDate() {
        return this.leaseCommencementDate;
    }

    public LeaseReportReadModel leaseCommencementDate(LocalDate leaseCommencementDate) {
        this.setLeaseCommencementDate(leaseCommencementDate);
        return this;
    }

    public void setLeaseCommencementDate(LocalDate leaseCommencementDate) {
        this.leaseCommencementDate = leaseCommencementDate;
    }

    public LocalDate getLeaseExpiryDate() {
        return this.leaseExpiryDate;
    }

    public LeaseReportReadModel leaseExpiryDate(LocalDate leaseExpiryDate) {
        this.setLeaseExpiryDate(leaseExpiryDate);
        return this;
    }

    public void setLeaseExpiryDate(LocalDate leaseExpiryDate) {
        this.leaseExpiryDate = leaseExpiryDate;
    }

    public Integer getLeaseTermMonths() {
        return this.leaseTermMonths;
    }

    public LeaseReportReadModel leaseTermMonths(Integer leaseTermMonths) {
        this.setLeaseTermMonths(leaseTermMonths);
        return this;
    }

    public void setLeaseTermMonths(Integer leaseTermMonths) {
        this.leaseTermMonths = leaseTermMonths;
    }

    public String getLeaseStatus() {
        return this.leaseStatus;
    }

    public LeaseReportReadModel leaseStatus(String leaseStatus) {
        this.setLeaseStatus(leaseStatus);
        return this;
    }

    public void setLeaseStatus(String leaseStatus) {
        this.leaseStatus = leaseStatus;
    }

    public String getLeaseType() {
        return this.leaseType;
    }

    public LeaseReportReadModel leaseType(String leaseType) {
        this.setLeaseType(leaseType);
        return this;
    }

    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public LeaseReportReadModel reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public Integer getFiscalYear() {
        return this.fiscalYear;
    }

    public LeaseReportReadModel fiscalYear(Integer fiscalYear) {
        this.setFiscalYear(fiscalYear);
        return this;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getFiscalQuarter() {
        return this.fiscalQuarter;
    }

    public LeaseReportReadModel fiscalQuarter(String fiscalQuarter) {
        this.setFiscalQuarter(fiscalQuarter);
        return this;
    }

    public void setFiscalQuarter(String fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public LocalDate getLastUpdated() {
        return this.lastUpdated;
    }

    public LeaseReportReadModel lastUpdated(LocalDate lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BigDecimal getInterestRate() {
        return this.interestRate;
    }

    public LeaseReportReadModel interestRate(BigDecimal interestRate) {
        this.setInterestRate(interestRate);
        return this;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getIncrementalBorrowingRate() {
        return this.incrementalBorrowingRate;
    }

    public LeaseReportReadModel incrementalBorrowingRate(BigDecimal incrementalBorrowingRate) {
        this.setIncrementalBorrowingRate(incrementalBorrowingRate);
        return this;
    }

    public void setIncrementalBorrowingRate(BigDecimal incrementalBorrowingRate) {
        this.incrementalBorrowingRate = incrementalBorrowingRate;
    }

    public BigDecimal getAccumulatedDepreciation() {
        return this.accumulatedDepreciation;
    }

    public LeaseReportReadModel accumulatedDepreciation(BigDecimal accumulatedDepreciation) {
        this.setAccumulatedDepreciation(accumulatedDepreciation);
        return this;
    }

    public void setAccumulatedDepreciation(BigDecimal accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
    }

    public BigDecimal getCurrentRouNBV() {
        return this.currentRouNBV;
    }

    public LeaseReportReadModel currentRouNBV(BigDecimal currentRouNBV) {
        this.setCurrentRouNBV(currentRouNBV);
        return this;
    }

    public void setCurrentRouNBV(BigDecimal currentRouNBV) {
        this.currentRouNBV = currentRouNBV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseReportReadModel)) {
            return false;
        }
        return id != null && id.equals(((LeaseReportReadModel) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "LeaseReportReadModel{" +
            "id=" + getId() +
            ", leaseId=" + getLeaseId() +
            ", leaseContractNumber='" + getLeaseContractNumber() + "'" +
            ", leaseTitle='" + getLeaseTitle() + "'" +
            ", lesseeName='" + getLesseeName() + "'" +
            ", leaseAmount=" + getLeaseAmount() +
            ", leaseLiability=" + getLeaseLiability() +
            ", rouAssetValue=" + getRouAssetValue() +
            ", leaseCommencementDate='" + getLeaseCommencementDate() + "'" +
            ", leaseExpiryDate='" + getLeaseExpiryDate() + "'" +
            ", reportingDate='" + getReportingDate() + "'" +
            ", fiscalYear=" + getFiscalYear() +
            "}";
    }
}
