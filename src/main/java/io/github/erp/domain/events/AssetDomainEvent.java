package io.github.erp.domain.events;

import org.springframework.context.ApplicationEvent;
import java.time.LocalDateTime;

public abstract class AssetDomainEvent extends ApplicationEvent {
    
    private final Long assetId;
    private final LocalDateTime occurredOn;
    
    protected AssetDomainEvent(Object source, Long assetId) {
        super(source);
        this.assetId = assetId;
        this.occurredOn = LocalDateTime.now();
    }
    
    public Long getAssetId() {
        return assetId;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}
