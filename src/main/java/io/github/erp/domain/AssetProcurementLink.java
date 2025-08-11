package io.github.erp.domain;

import java.io.Serializable;
import java.time.LocalDate;
import io.github.erp.domain.enumeration.ProcurementEntityType;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "asset_procurement_link")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetprocurementlink")
public class AssetProcurementLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    @NotNull
    @Column(name = "procurement_entity_id", nullable = false)
    private Long procurementEntityId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "procurement_entity_type", nullable = false)
    private ProcurementEntityType procurementEntityType;

    @Column(name = "link_date")
    private LocalDate linkDate;

    public Long getId() {
        return this.id;
    }

    public AssetProcurementLink id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetId() {
        return this.assetId;
    }

    public AssetProcurementLink assetId(Long assetId) {
        this.setAssetId(assetId);
        return this;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getProcurementEntityId() {
        return this.procurementEntityId;
    }

    public AssetProcurementLink procurementEntityId(Long procurementEntityId) {
        this.setProcurementEntityId(procurementEntityId);
        return this;
    }

    public void setProcurementEntityId(Long procurementEntityId) {
        this.procurementEntityId = procurementEntityId;
    }

    public ProcurementEntityType getProcurementEntityType() {
        return this.procurementEntityType;
    }

    public AssetProcurementLink procurementEntityType(ProcurementEntityType procurementEntityType) {
        this.setProcurementEntityType(procurementEntityType);
        return this;
    }

    public void setProcurementEntityType(ProcurementEntityType procurementEntityType) {
        this.procurementEntityType = procurementEntityType;
    }

    public LocalDate getLinkDate() {
        return this.linkDate;
    }

    public AssetProcurementLink linkDate(LocalDate linkDate) {
        this.setLinkDate(linkDate);
        return this;
    }

    public void setLinkDate(LocalDate linkDate) {
        this.linkDate = linkDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetProcurementLink)) {
            return false;
        }
        return id != null && id.equals(((AssetProcurementLink) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AssetProcurementLink{" +
            "id=" + getId() +
            ", assetId=" + getAssetId() +
            ", procurementEntityId=" + getProcurementEntityId() +
            ", procurementEntityType='" + getProcurementEntityType() + "'" +
            ", linkDate='" + getLinkDate() + "'" +
            "}";
    }
}
