package io.github.erp.cqrs.asset.readmodel;

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

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "asset_nbv_report_read_model", indexes = {
    @Index(name = "idx_asset_nbv_report_date", columnList = "reportingDate"),
    @Index(name = "idx_asset_nbv_report_asset", columnList = "assetId"),
    @Index(name = "idx_asset_nbv_report_category", columnList = "categoryName"),
    @Index(name = "idx_asset_nbv_report_outlet", columnList = "serviceOutletCode")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AssetNBVReportReadModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "asset_id")
    private Long assetId;

    @Column(name = "asset_number")
    private String assetNumber;

    @Column(name = "asset_tag")
    private String assetTag;

    @Column(name = "asset_description")
    private String assetDescription;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "service_outlet_code")
    private String serviceOutletCode;

    @Column(name = "service_outlet_id")
    private Long serviceOutletId;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "dealer_id")
    private Long dealerId;

    @Column(name = "acquisition_date")
    private LocalDate acquisitionDate;

    @Column(name = "capitalization_date")
    private LocalDate capitalizationDate;

    @Column(name = "historical_cost", precision = 21, scale = 2)
    private BigDecimal historicalCost;

    @Column(name = "net_book_value", precision = 21, scale = 2)
    private BigDecimal netBookValue;

    @Column(name = "depreciation_period_start_date")
    private LocalDate depreciationPeriodStartDate;

    @Column(name = "depreciation_period_end_date")
    private LocalDate depreciationPeriodEndDate;

    @Column(name = "fiscal_month_code")
    private String fiscalMonthCode;

    @Column(name = "fiscal_quarter")
    private String fiscalQuarter;

    @Column(name = "fiscal_year")
    private Integer fiscalYear;

    @Column(name = "reporting_date")
    private LocalDate reportingDate;

    @Column(name = "time_of_creation")
    private LocalDate timeOfCreation;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;

    public Long getId() {
        return this.id;
    }

    public AssetNBVReportReadModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetId() {
        return this.assetId;
    }

    public AssetNBVReportReadModel assetId(Long assetId) {
        this.setAssetId(assetId);
        return this;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getAssetNumber() {
        return this.assetNumber;
    }

    public AssetNBVReportReadModel assetNumber(String assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public AssetNBVReportReadModel assetTag(String assetTag) {
        this.setAssetTag(assetTag);
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDescription() {
        return this.assetDescription;
    }

    public AssetNBVReportReadModel assetDescription(String assetDescription) {
        this.setAssetDescription(assetDescription);
        return this;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public AssetNBVReportReadModel categoryName(String categoryName) {
        this.setCategoryName(categoryName);
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public AssetNBVReportReadModel categoryId(Long categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getServiceOutletCode() {
        return this.serviceOutletCode;
    }

    public AssetNBVReportReadModel serviceOutletCode(String serviceOutletCode) {
        this.setServiceOutletCode(serviceOutletCode);
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public Long getServiceOutletId() {
        return this.serviceOutletId;
    }

    public AssetNBVReportReadModel serviceOutletId(Long serviceOutletId) {
        this.setServiceOutletId(serviceOutletId);
        return this;
    }

    public void setServiceOutletId(Long serviceOutletId) {
        this.serviceOutletId = serviceOutletId;
    }

    public String getDealerName() {
        return this.dealerName;
    }

    public AssetNBVReportReadModel dealerName(String dealerName) {
        this.setDealerName(dealerName);
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Long getDealerId() {
        return this.dealerId;
    }

    public AssetNBVReportReadModel dealerId(Long dealerId) {
        this.setDealerId(dealerId);
        return this;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    public LocalDate getAcquisitionDate() {
        return this.acquisitionDate;
    }

    public AssetNBVReportReadModel acquisitionDate(LocalDate acquisitionDate) {
        this.setAcquisitionDate(acquisitionDate);
        return this;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public LocalDate getCapitalizationDate() {
        return this.capitalizationDate;
    }

    public AssetNBVReportReadModel capitalizationDate(LocalDate capitalizationDate) {
        this.setCapitalizationDate(capitalizationDate);
        return this;
    }

    public void setCapitalizationDate(LocalDate capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public BigDecimal getHistoricalCost() {
        return this.historicalCost;
    }

    public AssetNBVReportReadModel historicalCost(BigDecimal historicalCost) {
        this.setHistoricalCost(historicalCost);
        return this;
    }

    public void setHistoricalCost(BigDecimal historicalCost) {
        this.historicalCost = historicalCost;
    }

    public BigDecimal getNetBookValue() {
        return this.netBookValue;
    }

    public AssetNBVReportReadModel netBookValue(BigDecimal netBookValue) {
        this.setNetBookValue(netBookValue);
        return this;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public LocalDate getDepreciationPeriodStartDate() {
        return this.depreciationPeriodStartDate;
    }

    public AssetNBVReportReadModel depreciationPeriodStartDate(LocalDate depreciationPeriodStartDate) {
        this.setDepreciationPeriodStartDate(depreciationPeriodStartDate);
        return this;
    }

    public void setDepreciationPeriodStartDate(LocalDate depreciationPeriodStartDate) {
        this.depreciationPeriodStartDate = depreciationPeriodStartDate;
    }

    public LocalDate getDepreciationPeriodEndDate() {
        return this.depreciationPeriodEndDate;
    }

    public AssetNBVReportReadModel depreciationPeriodEndDate(LocalDate depreciationPeriodEndDate) {
        this.setDepreciationPeriodEndDate(depreciationPeriodEndDate);
        return this;
    }

    public void setDepreciationPeriodEndDate(LocalDate depreciationPeriodEndDate) {
        this.depreciationPeriodEndDate = depreciationPeriodEndDate;
    }

    public String getFiscalMonthCode() {
        return this.fiscalMonthCode;
    }

    public AssetNBVReportReadModel fiscalMonthCode(String fiscalMonthCode) {
        this.setFiscalMonthCode(fiscalMonthCode);
        return this;
    }

    public void setFiscalMonthCode(String fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public String getFiscalQuarter() {
        return this.fiscalQuarter;
    }

    public AssetNBVReportReadModel fiscalQuarter(String fiscalQuarter) {
        this.setFiscalQuarter(fiscalQuarter);
        return this;
    }

    public void setFiscalQuarter(String fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public Integer getFiscalYear() {
        return this.fiscalYear;
    }

    public AssetNBVReportReadModel fiscalYear(Integer fiscalYear) {
        this.setFiscalYear(fiscalYear);
        return this;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public AssetNBVReportReadModel reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public LocalDate getTimeOfCreation() {
        return this.timeOfCreation;
    }

    public AssetNBVReportReadModel timeOfCreation(LocalDate timeOfCreation) {
        this.setTimeOfCreation(timeOfCreation);
        return this;
    }

    public void setTimeOfCreation(LocalDate timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public LocalDate getLastUpdated() {
        return this.lastUpdated;
    }

    public AssetNBVReportReadModel lastUpdated(LocalDate lastUpdated) {
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
        if (!(o instanceof AssetNBVReportReadModel)) {
            return false;
        }
        return id != null && id.equals(((AssetNBVReportReadModel) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AssetNBVReportReadModel{" +
            "id=" + getId() +
            ", assetId=" + getAssetId() +
            ", assetNumber='" + getAssetNumber() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDescription='" + getAssetDescription() + "'" +
            ", categoryName='" + getCategoryName() + "'" +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            ", dealerName='" + getDealerName() + "'" +
            ", acquisitionDate='" + getAcquisitionDate() + "'" +
            ", capitalizationDate='" + getCapitalizationDate() + "'" +
            ", historicalCost=" + getHistoricalCost() +
            ", netBookValue=" + getNetBookValue() +
            ", depreciationPeriodStartDate='" + getDepreciationPeriodStartDate() + "'" +
            ", depreciationPeriodEndDate='" + getDepreciationPeriodEndDate() + "'" +
            ", fiscalMonthCode='" + getFiscalMonthCode() + "'" +
            ", fiscalQuarter='" + getFiscalQuarter() + "'" +
            ", fiscalYear=" + getFiscalYear() +
            ", reportingDate='" + getReportingDate() + "'" +
            ", timeOfCreation='" + getTimeOfCreation() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
