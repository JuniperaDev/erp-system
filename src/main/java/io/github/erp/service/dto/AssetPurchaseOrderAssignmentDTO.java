package io.github.erp.service.dto;

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
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetPurchaseOrderAssignment} entity.
 */
public class AssetPurchaseOrderAssignmentDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate assignmentDate;

    private String assignmentStatus;

    private BigDecimal allocatedAmount;

    private String notes;

    private Long assetRegistrationId;

    private String assetRegistrationAssetNumber;

    private Long purchaseOrderId;

    private String purchaseOrderPurchaseOrderNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public String getAssignmentStatus() {
        return assignmentStatus;
    }

    public void setAssignmentStatus(String assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getAssetRegistrationId() {
        return assetRegistrationId;
    }

    public void setAssetRegistrationId(Long assetRegistrationId) {
        this.assetRegistrationId = assetRegistrationId;
    }

    public String getAssetRegistrationAssetNumber() {
        return assetRegistrationAssetNumber;
    }

    public void setAssetRegistrationAssetNumber(String assetRegistrationAssetNumber) {
        this.assetRegistrationAssetNumber = assetRegistrationAssetNumber;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getPurchaseOrderPurchaseOrderNumber() {
        return purchaseOrderPurchaseOrderNumber;
    }

    public void setPurchaseOrderPurchaseOrderNumber(String purchaseOrderPurchaseOrderNumber) {
        this.purchaseOrderPurchaseOrderNumber = purchaseOrderPurchaseOrderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetPurchaseOrderAssignmentDTO)) {
            return false;
        }

        AssetPurchaseOrderAssignmentDTO assetPurchaseOrderAssignmentDTO = (AssetPurchaseOrderAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetPurchaseOrderAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "AssetPurchaseOrderAssignmentDTO{" +
            "id=" + getId() +
            ", assignmentDate='" + getAssignmentDate() + "'" +
            ", assignmentStatus='" + getAssignmentStatus() + "'" +
            ", allocatedAmount=" + getAllocatedAmount() +
            ", notes='" + getNotes() + "'" +
            ", assetRegistrationId=" + getAssetRegistrationId() +
            ", purchaseOrderId=" + getPurchaseOrderId() +
            "}";
    }
}
