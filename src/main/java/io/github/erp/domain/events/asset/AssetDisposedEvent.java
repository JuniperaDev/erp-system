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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "asset_disposed_event")
public class AssetDisposedEvent extends AbstractDomainEvent {

    @Column(name = "asset_registration_number")
    private String assetRegistrationNumber;

    @Column(name = "asset_description")
    private String assetDescription;

    @Column(name = "disposal_date")
    private LocalDate disposalDate;

    @Column(name = "net_book_value", precision = 21, scale = 2)
    private BigDecimal netBookValue;

    @Column(name = "disposal_proceeds", precision = 21, scale = 2)
    private BigDecimal disposalProceeds;

    @Column(name = "disposal_reference")
    private String disposalReference;

    protected AssetDisposedEvent() {
        super();
    }

    public AssetDisposedEvent(String assetId, String assetRegistrationNumber, String assetDescription,
                            LocalDate disposalDate, BigDecimal netBookValue, BigDecimal disposalProceeds,
                            String disposalReference, UUID correlationId) {
        super(assetId, "Asset", correlationId);
        this.assetRegistrationNumber = assetRegistrationNumber;
        this.assetDescription = assetDescription;
        this.disposalDate = disposalDate;
        this.netBookValue = netBookValue;
        this.disposalProceeds = disposalProceeds;
        this.disposalReference = disposalReference;
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

    public LocalDate getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(LocalDate disposalDate) {
        this.disposalDate = disposalDate;
    }

    public BigDecimal getNetBookValue() {
        return netBookValue;
    }

    public void setNetBookValue(BigDecimal netBookValue) {
        this.netBookValue = netBookValue;
    }

    public BigDecimal getDisposalProceeds() {
        return disposalProceeds;
    }

    public void setDisposalProceeds(BigDecimal disposalProceeds) {
        this.disposalProceeds = disposalProceeds;
    }

    public String getDisposalReference() {
        return disposalReference;
    }

    public void setDisposalReference(String disposalReference) {
        this.disposalReference = disposalReference;
    }
}
