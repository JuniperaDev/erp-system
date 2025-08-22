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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetRegistration} entity.
 */
public class AssetRegistrationDTO implements Serializable {

    private Long id;

    @NotNull
    private String assetNumber;

    @NotNull
    private String assetTag;

    private String assetDetails;

    @NotNull
    private BigDecimal assetCost;

    @Lob
    private byte[] comments;

    private String commentsContentType;
    private String modelNumber;

    private String serialNumber;

    @Lob
    private String remarks;

    @NotNull
    private LocalDate capitalizationDate;

    private BigDecimal historicalCost;

    private LocalDate registrationDate;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private AssetCategoryDTO assetCategory;

    private DealerDTO dealer;

    private SettlementCurrencyDTO settlementCurrency;

    private ServiceOutletDTO mainServiceOutlet;

    private Long acquiringTransactionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    public String getAssetTag() {
        return assetTag;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public String getAssetDetails() {
        return assetDetails;
    }

    public void setAssetDetails(String assetDetails) {
        this.assetDetails = assetDetails;
    }

    public BigDecimal getAssetCost() {
        return assetCost;
    }

    public void setAssetCost(BigDecimal assetCost) {
        this.assetCost = assetCost;
    }

    public byte[] getComments() {
        return comments;
    }

    public void setComments(byte[] comments) {
        this.comments = comments;
    }

    public String getCommentsContentType() {
        return commentsContentType;
    }

    public void setCommentsContentType(String commentsContentType) {
        this.commentsContentType = commentsContentType;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDate getCapitalizationDate() {
        return capitalizationDate;
    }

    public void setCapitalizationDate(LocalDate capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public BigDecimal getHistoricalCost() {
        return historicalCost;
    }

    public void setHistoricalCost(BigDecimal historicalCost) {
        this.historicalCost = historicalCost;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public AssetCategoryDTO getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(AssetCategoryDTO assetCategory) {
        this.assetCategory = assetCategory;
    }

    public DealerDTO getDealer() {
        return dealer;
    }

    public void setDealer(DealerDTO dealer) {
        this.dealer = dealer;
    }

    public SettlementCurrencyDTO getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrencyDTO settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public ServiceOutletDTO getMainServiceOutlet() {
        return mainServiceOutlet;
    }

    public void setMainServiceOutlet(ServiceOutletDTO mainServiceOutlet) {
        this.mainServiceOutlet = mainServiceOutlet;
    }

    public Long getAcquiringTransactionId() {
        return acquiringTransactionId;
    }

    public void setAcquiringTransactionId(Long acquiringTransactionId) {
        this.acquiringTransactionId = acquiringTransactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetRegistrationDTO)) {
            return false;
        }

        AssetRegistrationDTO assetRegistrationDTO = (AssetRegistrationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetRegistrationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetRegistrationDTO{" +
            "id=" + getId() +
            ", assetNumber='" + getAssetNumber() + "'" +
            ", assetTag='" + getAssetTag() + "'" +
            ", assetDetails='" + getAssetDetails() + "'" +
            ", assetCost=" + getAssetCost() +
            ", comments='" + getComments() + "'" +
            ", modelNumber='" + getModelNumber() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", capitalizationDate='" + getCapitalizationDate() + "'" +
            ", historicalCost=" + getHistoricalCost() +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", placeholders=" + getPlaceholders() +
            ", assetCategory=" + getAssetCategory() +
            ", dealer=" + getDealer() +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", mainServiceOutlet=" + getMainServiceOutlet() +
            ", acquiringTransactionId=" + getAcquiringTransactionId() +
            "}";
    }
}
