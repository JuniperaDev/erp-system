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
@Table(name = "depreciation_report_read_model", indexes = {
    @Index(name = "idx_depreciation_report_period", columnList = "depreciation_period"),
    @Index(name = "idx_depreciation_report_asset", columnList = "asset_id"),
    @Index(name = "idx_depreciation_report_category", columnList = "category_name"),
    @Index(name = "idx_depreciation_report_date", columnList = "depreciation_date")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DepreciationReportReadModel implements Serializable {

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

    @Column(name = "depreciation_date")
    private LocalDate depreciationDate;

    @Column(name = "depreciation_period")
    private String depreciationPeriod;

    @Column(name = "depreciation_amount", precision = 21, scale = 2)
    private BigDecimal depreciationAmount;

    @Column(name = "accumulated_depreciation", precision = 21, scale = 2)
    private BigDecimal accumulatedDepreciation;

    @Column(name = "net_book_value", precision = 21, scale = 2)
    private BigDecimal netBookValue;

    @Column(name = "depreciation_method")
    private String depreciationMethod;

    @Column(name = "useful_life_years", precision = 21, scale = 2)
    private BigDecimal usefulLifeYears;

    @Column(name = "depreciation_rate", precision = 21, scale = 4)
    private BigDecimal depreciationRate;

    @Column(name = "service_outlet_code")
    private String serviceOutletCode;

    @Column(name = "service_outlet_id")
    private Long serviceOutletId;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "dealer_id")
    private Long dealerId;

    @Column(name = "fiscal_month_code")
    private String fiscalMonthCode;

    @Column(name = "fiscal_quarter")
    private String fiscalQuarter;

    @Column(name = "fiscal_year")
    private Integer fiscalYear;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;

    public Long getId() {
        return this.id;
    }

    public DepreciationReportReadModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetId() {
        return this.assetId;
    }

    public DepreciationReportReadModel assetId(Long assetId) {
        this.setAssetId(assetId);
        return this;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getAssetNumber() {
        return this.assetNumber;
    }

    public DepreciationReportReadModel assetNumber(String assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public DepreciationReportReadModel assetTag(String assetTag) {
        this.setAssetTag(assetTag);
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDescription() {
        return this.assetDescription;
    }

    public DepreciationReportReadModel assetDescription(String assetDescription) {
        this.setAssetDescription(assetDescription);
        return this;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public DepreciationReportReadModel categoryName(String categoryName) {
        this.setCategoryName(categoryName);
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public DepreciationReportReadModel categoryId(Long categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDate getDepreciationDate() {
        return this.depreciationDate;
    }

    public DepreciationReportReadModel depreciationDate(LocalDate depreciationDate) {
        this.setDepreciationDate(depreciationDate);
        return this;
    }

    public void setDepreciationDate(LocalDate depreciationDate) {
        this.depreciationDate = depreciationDate;
    }

    public String getDepreciationPeriod() {
        return this.depreciationPeriod;
    }

    public DepreciationReportReadModel depreciationPeriod(String depreciationPeriod) {
        this.setDepreciationPeriod(depreciationPeriod);
        return this;
    }

    public void setDepreciationPeriod(String depreciationPeriod) {
        this.depreciationPeriod = depreciationPeriod;
    }

    public BigDecimal getDepreciationAmount() {
        return this.depreciationAmount;
    }

    public DepreciationReportReadModel depreciationAmount(BigDecimal depreciationAmount) {
        this.setDepreciationAmount(depreciationAmount);
        return this;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public BigDecimal getAccumulatedDepreciation() {
        return this.accumulatedDepreciation;
    }

    public DepreciationReportReadModel accumulatedDepreciation(BigDecimal accumulatedDepreciation) {
        this.setAccumulatedDepreciation(accumulatedDepreciation);
        return this;
    }

    public void setAccumulatedDepreciation(BigDecimal accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
    }

    public BigDecimal getNetBookValue() {
        return this.netBookValue;
    }

    public DepreciationReportReadModel netBookValue(BigDecimal netBookValue) {
        this.setNetBookValue(netBookValue);
        return this;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public String getDepreciationMethod() {
        return this.depreciationMethod;
    }

    public DepreciationReportReadModel depreciationMethod(String depreciationMethod) {
        this.setDepreciationMethod(depreciationMethod);
        return this;
    }

    public void setDepreciationMethod(String depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public BigDecimal getUsefulLifeYears() {
        return this.usefulLifeYears;
    }

    public DepreciationReportReadModel usefulLifeYears(BigDecimal usefulLifeYears) {
        this.setUsefulLifeYears(usefulLifeYears);
        return this;
    }

    public void setUsefulLifeYears(BigDecimal usefulLifeYears) {
        this.usefulLifeYears = usefulLifeYears;
    }

    public BigDecimal getDepreciationRate() {
        return this.depreciationRate;
    }

    public DepreciationReportReadModel depreciationRate(BigDecimal depreciationRate) {
        this.setDepreciationRate(depreciationRate);
        return this;
    }

    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public String getServiceOutletCode() {
        return this.serviceOutletCode;
    }

    public DepreciationReportReadModel serviceOutletCode(String serviceOutletCode) {
        this.setServiceOutletCode(serviceOutletCode);
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public Long getServiceOutletId() {
        return this.serviceOutletId;
    }

    public DepreciationReportReadModel serviceOutletId(Long serviceOutletId) {
        this.setServiceOutletId(serviceOutletId);
        return this;
    }

    public void setServiceOutletId(Long serviceOutletId) {
        this.serviceOutletId = serviceOutletId;
    }

    public String getDealerName() {
        return this.dealerName;
    }

    public DepreciationReportReadModel dealerName(String dealerName) {
        this.setDealerName(dealerName);
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Long getDealerId() {
        return this.dealerId;
    }

    public DepreciationReportReadModel dealerId(Long dealerId) {
        this.setDealerId(dealerId);
        return this;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    public String getFiscalMonthCode() {
        return this.fiscalMonthCode;
    }

    public DepreciationReportReadModel fiscalMonthCode(String fiscalMonthCode) {
        this.setFiscalMonthCode(fiscalMonthCode);
        return this;
    }

    public void setFiscalMonthCode(String fiscalMonthCode) {
        this.fiscalMonthCode = fiscalMonthCode;
    }

    public String getFiscalQuarter() {
        return this.fiscalQuarter;
    }

    public DepreciationReportReadModel fiscalQuarter(String fiscalQuarter) {
        this.setFiscalQuarter(fiscalQuarter);
        return this;
    }

    public void setFiscalQuarter(String fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public Integer getFiscalYear() {
        return this.fiscalYear;
    }

    public DepreciationReportReadModel fiscalYear(Integer fiscalYear) {
        this.setFiscalYear(fiscalYear);
        return this;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public LocalDate getLastUpdated() {
        return this.lastUpdated;
    }

    public DepreciationReportReadModel lastUpdated(LocalDate lastUpdated) {
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
        if (!(o instanceof DepreciationReportReadModel)) {
            return false;
        }
        return id != null && id.equals(((DepreciationReportReadModel) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "DepreciationReportReadModel{" +
            "id=" + getId() +
            ", assetId=" + getAssetId() +
            ", assetNumber='" + getAssetNumber() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDescription='" + getAssetDescription() + "'" +
            ", categoryName='" + getCategoryName() + "'" +
            ", depreciationDate='" + getDepreciationDate() + "'" +
            ", depreciationPeriod='" + getDepreciationPeriod() + "'" +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", accumulatedDepreciation=" + getAccumulatedDepreciation() +
            ", netBookValue=" + getNetBookValue() +
            ", depreciationMethod='" + getDepreciationMethod() + "'" +
            ", usefulLifeYears=" + getUsefulLifeYears() +
            ", depreciationRate=" + getDepreciationRate() +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            ", dealerName='" + getDealerName() + "'" +
            ", fiscalMonthCode='" + getFiscalMonthCode() + "'" +
            ", fiscalQuarter='" + getFiscalQuarter() + "'" +
            ", fiscalYear=" + getFiscalYear() +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
