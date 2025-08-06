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
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "asset_category_changed_event")
public class AssetCategoryChangedEvent extends AbstractDomainEvent {

    @Column(name = "asset_registration_number")
    private String assetRegistrationNumber;

    @Column(name = "previous_category_id")
    private Long previousCategoryId;

    @Column(name = "new_category_id")
    private Long newCategoryId;

    @Column(name = "previous_category_name")
    private String previousCategoryName;

    @Column(name = "new_category_name")
    private String newCategoryName;

    protected AssetCategoryChangedEvent() {
        super();
    }

    public AssetCategoryChangedEvent(String assetId, String assetRegistrationNumber, 
                                   Long previousCategoryId, Long newCategoryId,
                                   String previousCategoryName, String newCategoryName,
                                   UUID correlationId) {
        super(assetId, "Asset", correlationId);
        this.assetRegistrationNumber = assetRegistrationNumber;
        this.previousCategoryId = previousCategoryId;
        this.newCategoryId = newCategoryId;
        this.previousCategoryName = previousCategoryName;
        this.newCategoryName = newCategoryName;
    }

    public String getAssetRegistrationNumber() {
        return assetRegistrationNumber;
    }

    public void setAssetRegistrationNumber(String assetRegistrationNumber) {
        this.assetRegistrationNumber = assetRegistrationNumber;
    }

    public Long getPreviousCategoryId() {
        return previousCategoryId;
    }

    public void setPreviousCategoryId(Long previousCategoryId) {
        this.previousCategoryId = previousCategoryId;
    }

    public Long getNewCategoryId() {
        return newCategoryId;
    }

    public void setNewCategoryId(Long newCategoryId) {
        this.newCategoryId = newCategoryId;
    }

    public String getPreviousCategoryName() {
        return previousCategoryName;
    }

    public void setPreviousCategoryName(String previousCategoryName) {
        this.previousCategoryName = previousCategoryName;
    }

    public String getNewCategoryName() {
        return newCategoryName;
    }

    public void setNewCategoryName(String newCategoryName) {
        this.newCategoryName = newCategoryName;
    }
}
