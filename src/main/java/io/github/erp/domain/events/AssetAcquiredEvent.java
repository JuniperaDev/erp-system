package io.github.erp.domain.events;

import java.time.LocalDate;

public class AssetAcquiredEvent extends AssetDomainEvent {
    
    private final Long settlementId;
    private final LocalDate acquisitionDate;
    
    public AssetAcquiredEvent(Object source, Long assetId, Long settlementId, LocalDate acquisitionDate) {
        super(source, assetId);
        this.settlementId = settlementId;
        this.acquisitionDate = acquisitionDate;
    }
    
    public Long getSettlementId() {
        return settlementId;
    }
    
    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }
}
