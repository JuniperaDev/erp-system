package io.github.erp.domain.events.asset;

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

import io.github.erp.domain.events.AbstractDomainEvent;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@DiscriminatorValue("AssetCreatedEvent")
public class AssetCreatedEvent extends AbstractDomainEvent {

    @Column(name = "asset_registration_number")
    private String assetRegistrationNumber;

    @Column(name = "asset_description")
    private String assetDescription;

    @Column(name = "asset_cost", precision = 21, scale = 2)
    private BigDecimal assetCost;

    @Column(name = "asset_category_id")
    private Long assetCategoryId;

    protected AssetCreatedEvent() {
        super();
    }

    public AssetCreatedEvent(String assetId, String assetRegistrationNumber, String assetDescription, 
                           BigDecimal assetCost, Long assetCategoryId, UUID correlationId) {
        super(assetId, "Asset", correlationId);
        this.assetRegistrationNumber = assetRegistrationNumber;
        this.assetDescription = assetDescription;
        this.assetCost = assetCost;
        this.assetCategoryId = assetCategoryId;
    }

    public String getAssetRegistrationNumber() {
        return assetRegistrationNumber;
    }

    public void setAssetRegistrationNumber(String assetRegistrationNumber) {
        this.assetRegistrationNumber = assetRegistrationNumber;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public BigDecimal getAssetCost() {
        return assetCost;
    }

    public void setAssetCost(BigDecimal assetCost) {
        this.assetCost = assetCost;
    }

    public Long getAssetCategoryId() {
        return assetCategoryId;
    }

    public void setAssetCategoryId(Long assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
    }
}
