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
@Table(name = "asset_revalued_event")
public class AssetRevaluedEvent extends AbstractDomainEvent {

    @Column(name = "asset_registration_number")
    private String assetRegistrationNumber;

    @Column(name = "asset_description")
    private String assetDescription;

    @Column(name = "revaluation_date")
    private LocalDate revaluationDate;

    @Column(name = "previous_value", precision = 21, scale = 2)
    private BigDecimal previousValue;

    @Column(name = "revalued_amount", precision = 21, scale = 2)
    private BigDecimal revaluedAmount;

    @Column(name = "revaluation_reference")
    private String revaluationReference;

    protected AssetRevaluedEvent() {
        super();
    }

    public AssetRevaluedEvent(String assetId, String assetRegistrationNumber, String assetDescription,
                            LocalDate revaluationDate, BigDecimal previousValue, BigDecimal revaluedAmount,
                            String revaluationReference, UUID correlationId) {
        super(assetId, "Asset", correlationId);
        this.assetRegistrationNumber = assetRegistrationNumber;
        this.assetDescription = assetDescription;
        this.revaluationDate = revaluationDate;
        this.previousValue = previousValue;
        this.revaluedAmount = revaluedAmount;
        this.revaluationReference = revaluationReference;
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

    public LocalDate getRevaluationDate() {
        return revaluationDate;
    }

    public void setRevaluationDate(LocalDate revaluationDate) {
        this.revaluationDate = revaluationDate;
    }

    public BigDecimal getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(BigDecimal previousValue) {
        this.previousValue = previousValue;
    }

    public BigDecimal getRevaluedAmount() {
        return revaluedAmount;
    }

    public void setRevaluedAmount(BigDecimal revaluedAmount) {
        this.revaluedAmount = revaluedAmount;
    }

    public String getRevaluationReference() {
        return revaluationReference;
    }

    public void setRevaluationReference(String revaluationReference) {
        this.revaluationReference = revaluationReference;
    }
}
