package io.github.erp.service.criteria;

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
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.erp.domain.AssetRegistration} entity. This class is used
 * in {@link io.github.erp.web.rest.AssetRegistrationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-registrations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AssetRegistrationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter assetNumber;

    private StringFilter assetTag;

    private StringFilter assetDetails;

    private BigDecimalFilter assetCost;

    private StringFilter modelNumber;

    private StringFilter serialNumber;

    private LocalDateFilter capitalizationDate;

    private BigDecimalFilter historicalCost;

    private LocalDateFilter registrationDate;

    private LongFilter placeholderId;

    private LongFilter assetCategoryId;

    private LongFilter dealerId;

    private LongFilter settlementCurrencyId;

    private LongFilter mainServiceOutletId;

    private LongFilter acquiringTransactionId;

    private Boolean distinct;

    public AssetRegistrationCriteria() {}

    public AssetRegistrationCriteria(AssetRegistrationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.assetNumber = other.assetNumber == null ? null : other.assetNumber.copy();
        this.assetTag = other.assetTag == null ? null : other.assetTag.copy();
        this.assetDetails = other.assetDetails == null ? null : other.assetDetails.copy();
        this.assetCost = other.assetCost == null ? null : other.assetCost.copy();
        this.modelNumber = other.modelNumber == null ? null : other.modelNumber.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.capitalizationDate = other.capitalizationDate == null ? null : other.capitalizationDate.copy();
        this.historicalCost = other.historicalCost == null ? null : other.historicalCost.copy();
        this.registrationDate = other.registrationDate == null ? null : other.registrationDate.copy();
        this.placeholderId = other.placeholderId == null ? null : other.placeholderId.copy();
        this.assetCategoryId = other.assetCategoryId == null ? null : other.assetCategoryId.copy();
        this.dealerId = other.dealerId == null ? null : other.dealerId.copy();
        this.settlementCurrencyId = other.settlementCurrencyId == null ? null : other.settlementCurrencyId.copy();
        this.mainServiceOutletId = other.mainServiceOutletId == null ? null : other.mainServiceOutletId.copy();
        this.acquiringTransactionId = other.acquiringTransactionId == null ? null : other.acquiringTransactionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssetRegistrationCriteria copy() {
        return new AssetRegistrationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAssetNumber() {
        return assetNumber;
    }

    public StringFilter assetNumber() {
        if (assetNumber == null) {
            assetNumber = new StringFilter();
        }
        return assetNumber;
    }

    public void setAssetNumber(StringFilter assetNumber) {
        this.assetNumber = assetNumber;
    }

    public StringFilter getAssetTag() {
        return assetTag;
    }

    public StringFilter assetTag() {
        if (assetTag == null) {
            assetTag = new StringFilter();
        }
        return assetTag;
    }

    public void setAssetTag(StringFilter assetTag) {
        this.assetTag = assetTag;
    }

    public StringFilter getAssetDetails() {
        return assetDetails;
    }

    public StringFilter assetDetails() {
        if (assetDetails == null) {
            assetDetails = new StringFilter();
        }
        return assetDetails;
    }

    public void setAssetDetails(StringFilter assetDetails) {
        this.assetDetails = assetDetails;
    }

    public BigDecimalFilter getAssetCost() {
        return assetCost;
    }

    public BigDecimalFilter assetCost() {
        if (assetCost == null) {
            assetCost = new BigDecimalFilter();
        }
        return assetCost;
    }

    public void setAssetCost(BigDecimalFilter assetCost) {
        this.assetCost = assetCost;
    }

    public StringFilter getModelNumber() {
        return modelNumber;
    }

    public StringFilter modelNumber() {
        if (modelNumber == null) {
            modelNumber = new StringFilter();
        }
        return modelNumber;
    }

    public void setModelNumber(StringFilter modelNumber) {
        this.modelNumber = modelNumber;
    }

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public StringFilter serialNumber() {
        if (serialNumber == null) {
            serialNumber = new StringFilter();
        }
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDateFilter getCapitalizationDate() {
        return capitalizationDate;
    }

    public LocalDateFilter capitalizationDate() {
        if (capitalizationDate == null) {
            capitalizationDate = new LocalDateFilter();
        }
        return capitalizationDate;
    }

    public void setCapitalizationDate(LocalDateFilter capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public BigDecimalFilter getHistoricalCost() {
        return historicalCost;
    }

    public BigDecimalFilter historicalCost() {
        if (historicalCost == null) {
            historicalCost = new BigDecimalFilter();
        }
        return historicalCost;
    }

    public void setHistoricalCost(BigDecimalFilter historicalCost) {
        this.historicalCost = historicalCost;
    }

    public LocalDateFilter getRegistrationDate() {
        return registrationDate;
    }

    public LocalDateFilter registrationDate() {
        if (registrationDate == null) {
            registrationDate = new LocalDateFilter();
        }
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LongFilter getPlaceholderId() {
        return placeholderId;
    }

    public LongFilter placeholderId() {
        if (placeholderId == null) {
            placeholderId = new LongFilter();
        }
        return placeholderId;
    }

    public void setPlaceholderId(LongFilter placeholderId) {
        this.placeholderId = placeholderId;
    }




    public LongFilter getAssetCategoryId() {
        return assetCategoryId;
    }

    public LongFilter assetCategoryId() {
        if (assetCategoryId == null) {
            assetCategoryId = new LongFilter();
        }
        return assetCategoryId;
    }

    public void setAssetCategoryId(LongFilter assetCategoryId) {
        this.assetCategoryId = assetCategoryId;
    }




    public LongFilter getDealerId() {
        return dealerId;
    }

    public LongFilter dealerId() {
        if (dealerId == null) {
            dealerId = new LongFilter();
        }
        return dealerId;
    }

    public void setDealerId(LongFilter dealerId) {
        this.dealerId = dealerId;
    }


    public LongFilter getSettlementCurrencyId() {
        return settlementCurrencyId;
    }

    public LongFilter settlementCurrencyId() {
        if (settlementCurrencyId == null) {
            settlementCurrencyId = new LongFilter();
        }
        return settlementCurrencyId;
    }

    public void setSettlementCurrencyId(LongFilter settlementCurrencyId) {
        this.settlementCurrencyId = settlementCurrencyId;
    }


    public LongFilter getMainServiceOutletId() {
        return mainServiceOutletId;
    }

    public LongFilter mainServiceOutletId() {
        if (mainServiceOutletId == null) {
            mainServiceOutletId = new LongFilter();
        }
        return mainServiceOutletId;
    }

    public void setMainServiceOutletId(LongFilter mainServiceOutletId) {
        this.mainServiceOutletId = mainServiceOutletId;
    }

    public LongFilter getAcquiringTransactionId() {
        return acquiringTransactionId;
    }

    public LongFilter acquiringTransactionId() {
        if (acquiringTransactionId == null) {
            acquiringTransactionId = new LongFilter();
        }
        return acquiringTransactionId;
    }

    public void setAcquiringTransactionId(LongFilter acquiringTransactionId) {
        this.acquiringTransactionId = acquiringTransactionId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AssetRegistrationCriteria that = (AssetRegistrationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetNumber, that.assetNumber) &&
            Objects.equals(assetTag, that.assetTag) &&
            Objects.equals(assetDetails, that.assetDetails) &&
            Objects.equals(assetCost, that.assetCost) &&
            Objects.equals(modelNumber, that.modelNumber) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(capitalizationDate, that.capitalizationDate) &&
            Objects.equals(historicalCost, that.historicalCost) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(placeholderId, that.placeholderId) &&
            Objects.equals(assetCategoryId, that.assetCategoryId) &&
            Objects.equals(dealerId, that.dealerId) &&
            Objects.equals(settlementCurrencyId, that.settlementCurrencyId) &&
            Objects.equals(mainServiceOutletId, that.mainServiceOutletId) &&
            Objects.equals(acquiringTransactionId, that.acquiringTransactionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetNumber,
            assetTag,
            assetDetails,
            assetCost,
            modelNumber,
            serialNumber,
            capitalizationDate,
            historicalCost,
            registrationDate,
            placeholderId,
            assetCategoryId,
            dealerId,
            settlementCurrencyId,
            mainServiceOutletId,
            acquiringTransactionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetRegistrationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (assetNumber != null ? "assetNumber=" + assetNumber + ", " : "") +
            (assetTag != null ? "assetTag=" + assetTag + ", " : "") +
            (assetDetails != null ? "assetDetails=" + assetDetails + ", " : "") +
            (assetCost != null ? "assetCost=" + assetCost + ", " : "") +
            (modelNumber != null ? "modelNumber=" + modelNumber + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (capitalizationDate != null ? "capitalizationDate=" + capitalizationDate + ", " : "") +
            (historicalCost != null ? "historicalCost=" + historicalCost + ", " : "") +
            (registrationDate != null ? "registrationDate=" + registrationDate + ", " : "") +
            (placeholderId != null ? "placeholderId=" + placeholderId + ", " : "") +
            (assetCategoryId != null ? "assetCategoryId=" + assetCategoryId + ", " : "") +
            (dealerId != null ? "dealerId=" + dealerId + ", " : "") +
            (settlementCurrencyId != null ? "settlementCurrencyId=" + settlementCurrencyId + ", " : "") +
            (mainServiceOutletId != null ? "mainServiceOutletId=" + mainServiceOutletId + ", " : "") +
            (acquiringTransactionId != null ? "acquiringTransactionId=" + acquiringTransactionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
