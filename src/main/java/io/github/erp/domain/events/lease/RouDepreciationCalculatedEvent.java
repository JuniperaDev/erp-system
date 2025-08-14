package io.github.erp.domain.events.lease;

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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@DiscriminatorValue("RouDepreciationCalculatedEvent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RouDepreciationCalculatedEvent extends AbstractDomainEvent {

    @Column(name = "rou_asset_identifier")
    private UUID rouAssetIdentifier;

    @Column(name = "rou_depreciation_identifier")
    private UUID rouDepreciationIdentifier;

    @Column(name = "depreciation_amount", precision = 21, scale = 2)
    private BigDecimal depreciationAmount;

    @Column(name = "outstanding_amount", precision = 21, scale = 2)
    private BigDecimal outstandingAmount;

    @Column(name = "lease_contract_id")
    private Long leaseContractId;

    @Column(name = "lease_contract_number")
    private String leaseContractNumber;

    @Column(name = "asset_category_name")
    private String assetCategoryName;

    @Column(name = "lease_period_code")
    private String leasePeriodCode;

    @Column(name = "depreciation_date")
    private LocalDate depreciationDate;

    protected RouDepreciationCalculatedEvent() {
        super();
    }

    public RouDepreciationCalculatedEvent(String rouDepreciationEntryId, UUID rouAssetIdentifier,
                                        UUID rouDepreciationIdentifier, BigDecimal depreciationAmount,
                                        BigDecimal outstandingAmount, Long leaseContractId,
                                        String leaseContractNumber, String assetCategoryName,
                                        String leasePeriodCode, LocalDate depreciationDate,
                                        UUID correlationId) {
        super(rouDepreciationEntryId, "RouDepreciationEntry", correlationId);
        this.rouAssetIdentifier = rouAssetIdentifier;
        this.rouDepreciationIdentifier = rouDepreciationIdentifier;
        this.depreciationAmount = depreciationAmount;
        this.outstandingAmount = outstandingAmount;
        this.leaseContractId = leaseContractId;
        this.leaseContractNumber = leaseContractNumber;
        this.assetCategoryName = assetCategoryName;
        this.leasePeriodCode = leasePeriodCode;
        this.depreciationDate = depreciationDate;
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

    public String getAssetCategoryName() {
        return assetCategoryName;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouDepreciationCalculatedEvent)) return false;
        if (!super.equals(o)) return false;

        RouDepreciationCalculatedEvent that = (RouDepreciationCalculatedEvent) o;

        if (rouAssetIdentifier != null ? !rouAssetIdentifier.equals(that.rouAssetIdentifier) : that.rouAssetIdentifier != null)
            return false;
        if (rouDepreciationIdentifier != null ? !rouDepreciationIdentifier.equals(that.rouDepreciationIdentifier) : that.rouDepreciationIdentifier != null)
            return false;
        return leaseContractId != null ? leaseContractId.equals(that.leaseContractId) : that.leaseContractId == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (rouAssetIdentifier != null ? rouAssetIdentifier.hashCode() : 0);
        result = 31 * result + (rouDepreciationIdentifier != null ? rouDepreciationIdentifier.hashCode() : 0);
        result = 31 * result + (leaseContractId != null ? leaseContractId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RouDepreciationCalculatedEvent{" +
                "rouAssetIdentifier=" + rouAssetIdentifier +
                ", rouDepreciationIdentifier=" + rouDepreciationIdentifier +
                ", depreciationAmount=" + depreciationAmount +
                ", outstandingAmount=" + outstandingAmount +
                ", leaseContractId=" + leaseContractId +
                ", leaseContractNumber='" + leaseContractNumber + '\'' +
                ", assetCategoryName='" + assetCategoryName + '\'' +
                ", leasePeriodCode='" + leasePeriodCode + '\'' +
                ", depreciationDate=" + depreciationDate +
                "} " + super.toString();
    }
}
