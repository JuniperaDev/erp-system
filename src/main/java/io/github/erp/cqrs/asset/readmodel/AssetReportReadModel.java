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
@Table(name = "asset_report_read_model", indexes = {
    @Index(name = "idx_asset_report_date", columnList = "reportingDate"),
    @Index(name = "idx_asset_report_category", columnList = "categoryName"),
    @Index(name = "idx_asset_report_number", columnList = "assetNumber"),
    @Index(name = "idx_asset_report_dealer", columnList = "dealerName")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AssetReportReadModel implements Serializable {

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

    @Column(name = "asset_cost", precision = 21, scale = 2)
    private BigDecimal assetCost;

    @Column(name = "current_nbv", precision = 21, scale = 2)
    private BigDecimal currentNBV;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "dealer_id")
    private Long dealerId;

    @Column(name = "capitalization_date")
    private LocalDate capitalizationDate;

    @Column(name = "reporting_date")
    private LocalDate reportingDate;

    @Column(name = "total_depreciation", precision = 21, scale = 2)
    private BigDecimal totalDepreciation;

    @Column(name = "remaining_useful_life", precision = 21, scale = 2)
    private BigDecimal remainingUsefulLife;

    @Column(name = "depreciation_method")
    private String depreciationMethod;

    @Column(name = "useful_life_years", precision = 21, scale = 2)
    private BigDecimal usefulLifeYears;

    @Column(name = "service_outlet_code")
    private String serviceOutletCode;

    @Column(name = "service_outlet_id")
    private Long serviceOutletId;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;

    public Long getId() {
        return this.id;
    }

    public AssetReportReadModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetId() {
        return this.assetId;
    }

    public AssetReportReadModel assetId(Long assetId) {
        this.setAssetId(assetId);
        return this;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getAssetNumber() {
        return this.assetNumber;
    }

    public AssetReportReadModel assetNumber(String assetNumber) {
        this.setAssetNumber(assetNumber);
        return this;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetTag() {
        return this.assetTag;
    }

    public AssetReportReadModel assetTag(String assetTag) {
        this.setAssetTag(assetTag);
        return this;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDescription() {
        return this.assetDescription;
    }

    public AssetReportReadModel assetDescription(String assetDescription) {
        this.setAssetDescription(assetDescription);
        return this;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public BigDecimal getAssetCost() {
        return this.assetCost;
    }

    public AssetReportReadModel assetCost(BigDecimal assetCost) {
        this.setAssetCost(assetCost);
        return this;
    }

    public void setAssetCost(BigDecimal assetCost) {
        this.assetCost = assetCost;
    }

    public BigDecimal getCurrentNBV() {
        return this.currentNBV;
    }

    public AssetReportReadModel currentNBV(BigDecimal currentNBV) {
        this.setCurrentNBV(currentNBV);
        return this;
    }

    public void setCurrentNBV(BigDecimal currentNBV) {
        this.currentNBV = currentNBV;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public AssetReportReadModel categoryName(String categoryName) {
        this.setCategoryName(categoryName);
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public AssetReportReadModel categoryId(Long categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDealerName() {
        return this.dealerName;
    }

    public AssetReportReadModel dealerName(String dealerName) {
        this.setDealerName(dealerName);
        return this;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public Long getDealerId() {
        return this.dealerId;
    }

    public AssetReportReadModel dealerId(Long dealerId) {
        this.setDealerId(dealerId);
        return this;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    public LocalDate getCapitalizationDate() {
        return this.capitalizationDate;
    }

    public AssetReportReadModel capitalizationDate(LocalDate capitalizationDate) {
        this.setCapitalizationDate(capitalizationDate);
        return this;
    }

    public void setCapitalizationDate(LocalDate capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public LocalDate getReportingDate() {
        return this.reportingDate;
    }

    public AssetReportReadModel reportingDate(LocalDate reportingDate) {
        this.setReportingDate(reportingDate);
        return this;
    }

    public void setReportingDate(LocalDate reportingDate) {
        this.reportingDate = reportingDate;
    }

    public BigDecimal getTotalDepreciation() {
        return this.totalDepreciation;
    }

    public AssetReportReadModel totalDepreciation(BigDecimal totalDepreciation) {
        this.setTotalDepreciation(totalDepreciation);
        return this;
    }

    public void setTotalDepreciation(BigDecimal totalDepreciation) {
        this.totalDepreciation = totalDepreciation;
    }

    public BigDecimal getRemainingUsefulLife() {
        return this.remainingUsefulLife;
    }

    public AssetReportReadModel remainingUsefulLife(BigDecimal remainingUsefulLife) {
        this.setRemainingUsefulLife(remainingUsefulLife);
        return this;
    }

    public void setRemainingUsefulLife(BigDecimal remainingUsefulLife) {
        this.remainingUsefulLife = remainingUsefulLife;
    }

    public String getDepreciationMethod() {
        return this.depreciationMethod;
    }

    public AssetReportReadModel depreciationMethod(String depreciationMethod) {
        this.setDepreciationMethod(depreciationMethod);
        return this;
    }

    public void setDepreciationMethod(String depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public BigDecimal getUsefulLifeYears() {
        return this.usefulLifeYears;
    }

    public AssetReportReadModel usefulLifeYears(BigDecimal usefulLifeYears) {
        this.setUsefulLifeYears(usefulLifeYears);
        return this;
    }

    public void setUsefulLifeYears(BigDecimal usefulLifeYears) {
        this.usefulLifeYears = usefulLifeYears;
    }

    public String getServiceOutletCode() {
        return this.serviceOutletCode;
    }

    public AssetReportReadModel serviceOutletCode(String serviceOutletCode) {
        this.setServiceOutletCode(serviceOutletCode);
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public Long getServiceOutletId() {
        return this.serviceOutletId;
    }

    public AssetReportReadModel serviceOutletId(Long serviceOutletId) {
        this.setServiceOutletId(serviceOutletId);
        return this;
    }

    public void setServiceOutletId(Long serviceOutletId) {
        this.serviceOutletId = serviceOutletId;
    }

    public LocalDate getLastUpdated() {
        return this.lastUpdated;
    }

    public AssetReportReadModel lastUpdated(LocalDate lastUpdated) {
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
        if (!(o instanceof AssetReportReadModel)) {
            return false;
        }
        return id != null && id.equals(((AssetReportReadModel) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AssetReportReadModel{" +
            "id=" + getId() +
            ", assetId=" + getAssetId() +
            ", assetNumber='" + getAssetNumber() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDescription='" + getAssetDescription() + "'" +
            ", assetCost=" + getAssetCost() +
            ", currentNBV=" + getCurrentNBV() +
            ", categoryName='" + getCategoryName() + "'" +
            ", dealerName='" + getDealerName() + "'" +
            ", capitalizationDate='" + getCapitalizationDate() + "'" +
            ", reportingDate='" + getReportingDate() + "'" +
            ", totalDepreciation=" + getTotalDepreciation() +
            ", remainingUsefulLife=" + getRemainingUsefulLife() +
            ", depreciationMethod='" + getDepreciationMethod() + "'" +
            ", usefulLifeYears=" + getUsefulLifeYears() +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
