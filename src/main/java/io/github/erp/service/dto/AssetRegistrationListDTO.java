package io.github.erp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AssetRegistrationListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String assetNumber;
    private String assetTag;
    private String assetDetails;
    private BigDecimal assetCost;
    private LocalDate capitalizationDate;
    private String assetCategoryName;
    private String dealerName;
    private String settlementCurrencyCode;

    public AssetRegistrationListDTO() {}

    public AssetRegistrationListDTO(Long id, String assetNumber, String assetTag, String assetDetails, 
                                   BigDecimal assetCost, LocalDate capitalizationDate, 
                                   String assetCategoryName, String dealerName, String settlementCurrencyCode) {
        this.id = id;
        this.assetNumber = assetNumber;
        this.assetTag = assetTag;
        this.assetDetails = assetDetails;
        this.assetCost = assetCost;
        this.capitalizationDate = capitalizationDate;
        this.assetCategoryName = assetCategoryName;
        this.dealerName = dealerName;
        this.settlementCurrencyCode = settlementCurrencyCode;
    }

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

    public LocalDate getCapitalizationDate() {
        return capitalizationDate;
    }

    public void setCapitalizationDate(LocalDate capitalizationDate) {
        this.capitalizationDate = capitalizationDate;
    }

    public String getAssetCategoryName() {
        return assetCategoryName;
    }

    public void setAssetCategoryName(String assetCategoryName) {
        this.assetCategoryName = assetCategoryName;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getSettlementCurrencyCode() {
        return settlementCurrencyCode;
    }

    public void setSettlementCurrencyCode(String settlementCurrencyCode) {
        this.settlementCurrencyCode = settlementCurrencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetRegistrationListDTO)) return false;
        AssetRegistrationListDTO that = (AssetRegistrationListDTO) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AssetRegistrationListDTO{" +
            "id=" + id +
            ", assetNumber='" + assetNumber + '\'' +
            ", assetTag='" + assetTag + '\'' +
            ", assetDetails='" + assetDetails + '\'' +
            ", assetCost=" + assetCost +
            ", capitalizationDate=" + capitalizationDate +
            ", assetCategoryName='" + assetCategoryName + '\'' +
            ", dealerName='" + dealerName + '\'' +
            ", settlementCurrencyCode='" + settlementCurrencyCode + '\'' +
            '}';
    }
}
