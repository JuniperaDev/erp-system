package io.github.erp.domain.events;

public class AssetProcurementLinkedEvent extends AssetDomainEvent {
    
    private final Long procurementEntityId;
    private final String procurementEntityType;
    
    public AssetProcurementLinkedEvent(Object source, Long assetId, Long procurementEntityId, String procurementEntityType) {
        super(source, assetId);
        this.procurementEntityId = procurementEntityId;
        this.procurementEntityType = procurementEntityType;
    }
    
    public Long getProcurementEntityId() {
        return procurementEntityId;
    }
    
    public String getProcurementEntityType() {
        return procurementEntityType;
    }
}
