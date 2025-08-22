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
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "rou_asset_report_read_model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RouAssetReportReadModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "rou_asset_identifier")
    private UUID rouAssetIdentifier;

    @Column(name = "rou_depreciation_identifier")
    private UUID rouDepreciationIdentifier;

    @Column(name = "lease_contract_id")
    private Long leaseContractId;

    @Column(name = "lease_contract_number")
    private String leaseContractNumber;

    @Column(name = "lease_title")
    private String leaseTitle;

    @Column(name = "asset_category_name")
    private String assetCategoryName;

    @Column(name = "asset_category_id")
    private Long assetCategoryId;

    @Column(name = "depreciation_amount", precision = 21, scale = 2)
    private BigDecimal depreciationAmount;

    @Column(name = "outstanding_amount", precision = 21, scale = 2)
    private BigDecimal outstandingAmount;

    @Column(name = "accumulated_depreciation", precision = 21, scale = 2)
    private BigDecimal accumulatedDepreciation;

    @Column(name = "net_book_value", precision = 21, scale = 2)
    private BigDecimal netBookValue;

    @Column(name = "lease_period_code")
    private String leasePeriodCode;

    @Column(name = "depreciation_date")
    private LocalDate depreciationDate;

    @Column(name = "fiscal_month_code")
    private String fiscalMonthCode;

    @Column(name = "fiscal_quarter")
    private String fiscalQuarter;

    @Column(name = "fiscal_year")
    private Integer fiscalYear;

    @Column(name = "reporting_date", nullable = false)
    private LocalDate reportingDate;

    @Column(name = "last_updated", nullable = false)
    private LocalDate lastUpdated;

    public RouAssetReportReadModel() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRouAssetIdentifier() {
        return rouAssetIdentifier;
    }

    public void setRouAssetIdentifier(UUID rouAssetIdentifier) {
        this.rouAssetIdentifier = rouAssetIdentifier;
    }

    public UUID getRouDepreciationIdentifier() {
        return rouDepreciationIdentifier;
    }

    public void setRouDepreciationIdentifier(UUID rouDepreciationIdentifier) {
        this.rouDepreciationIdentifier = rouDepreciationIdentifier;
    }

    public Long getLeaseContractId() {
        return leaseContractId;
    }

    public void setLeaseContractId(Long leaseContractId) {
        this.leaseContractId = leaseContractId;
    }

    public String getLeaseContractNumber() {
        return leaseContractNumber;
    }

    public void setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public String getLeaseTitle() {
        return leaseTitle;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public String getAssetCategoryName() {
        return assetCategoryName;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public Long getAssetCategoryId() {
        return assetCategoryId;
    }

    public void setAssetCategoryId(Long assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public BigDecimal getAccumulatedDepreciation() {
        return accumulatedDepreciation;
    }

    public void setAccumulatedDepreciation(BigDecimal accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
    }

    public BigDecimal getNetBookValue() {
        return netBookValue;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public String getLeasePeriodCode() {
        return leasePeriodCode;
    }

    public void setLeasePeriodCode(String leasePeriodCode) {
        this.leasePeriodCode = leasePeriodCode;
    }

    public LocalDate getDepreciationDate() {
        return depreciationDate;
    }

    public void setDepreciationDate(LocalDate depreciationDate) {
        this.depreciationDate = depreciationDate;
    }

    public String getFiscalMonthCode() {
        return fiscalMonthCode;
    }

    public void setFiscalMonthCode(String fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public String getFiscalQuarter() {
        return fiscalQuarter;
    }

    public void setFiscalQuarter(String fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public Integer getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public LocalDate getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
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
        if (!(o instanceof RouAssetReportReadModel)) return false;

        RouAssetReportReadModel that = (RouAssetReportReadModel) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RouAssetReportReadModel{" +
                "id=" + id +
                ", rouAssetIdentifier=" + rouAssetIdentifier +
                ", rouDepreciationIdentifier=" + rouDepreciationIdentifier +
                ", leaseContractId=" + leaseContractId +
                ", leaseContractNumber='" + leaseContractNumber + '\'' +
                ", leaseTitle='" + leaseTitle + '\'' +
                ", assetCategoryName='" + assetCategoryName + '\'' +
                ", depreciationAmount=" + depreciationAmount +
                ", outstandingAmount=" + outstandingAmount +
                ", accumulatedDepreciation=" + accumulatedDepreciation +
                ", netBookValue=" + netBookValue +
                ", leasePeriodCode='" + leasePeriodCode + '\'' +
                ", depreciationDate=" + depreciationDate +
                ", fiscalYear=" + fiscalYear +
                ", reportingDate=" + reportingDate +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
