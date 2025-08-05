package io.github.erp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

public class AssetProcurementLinkDTO implements Serializable {

    private Long id;

    @NotNull
    private Long assetId;

    @NotNull
    private Long procurementEntityId;

    @NotNull
    private String procurementEntityType;

    private LocalDate linkDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getProcurementEntityId() {
        return procurementEntityId;
    }

    public void setProcurementEntityId(Long procurementEntityId) {
        this.procurementEntityId = procurementEntityId;
    }

    public String getProcurementEntityType() {
        return procurementEntityType;
    }

    public void setProcurementEntityType(String procurementEntityType) {
        this.procurementEntityType = procurementEntityType;
    }

    public LocalDate getLinkDate() {
        return linkDate;
    }

    public void setLinkDate(LocalDate linkDate) {
        this.linkDate = linkDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetProcurementLinkDTO)) {
            return false;
        }

        AssetProcurementLinkDTO assetProcurementLinkDTO = (AssetProcurementLinkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetProcurementLinkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "AssetProcurementLinkDTO{" +
            "id=" + getId() +
            ", assetId=" + getAssetId() +
            ", procurementEntityId=" + getProcurementEntityId() +
            ", procurementEntityType='" + getProcurementEntityType() + "'" +
            ", linkDate='" + getLinkDate() + "'" +
            "}";
    }
}
