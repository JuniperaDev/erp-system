package io.github.erp.service.events;

import io.github.erp.domain.enumeration.ProcurementEntityType;
import io.github.erp.domain.events.AssetAcquiredEvent;
import io.github.erp.domain.events.AssetProcurementLinkedEvent;
import io.github.erp.service.AssetProcurementLinkService;
import io.github.erp.service.dto.AssetProcurementLinkDTO;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component("assetProcurementEventHandler")
public class AssetEventHandler {

    private final Logger log = LoggerFactory.getLogger(AssetEventHandler.class);

    private final AssetProcurementLinkService assetProcurementLinkService;

    public AssetEventHandler(AssetProcurementLinkService assetProcurementLinkService) {
        this.assetProcurementLinkService = assetProcurementLinkService;
    }

    @EventListener
    @Async
    public void handleAssetAcquiredEvent(AssetAcquiredEvent event) {
        log.debug("Handling AssetAcquiredEvent for asset: {}", event.getAssetId());
        
        AssetProcurementLinkDTO linkDTO = new AssetProcurementLinkDTO();
        linkDTO.setAssetId(event.getAssetId());
        linkDTO.setProcurementEntityId(event.getSettlementId());
        linkDTO.setProcurementEntityType(ProcurementEntityType.SETTLEMENT);
        linkDTO.setLinkDate(event.getAcquisitionDate());
        
        assetProcurementLinkService.save(linkDTO);
    }

    @EventListener
    @Async
    public void handleAssetProcurementLinkedEvent(AssetProcurementLinkedEvent event) {
        log.debug("Handling AssetProcurementLinkedEvent for asset: {} with procurement entity: {}", 
                  event.getAssetId(), event.getProcurementEntityId());
        
        AssetProcurementLinkDTO linkDTO = new AssetProcurementLinkDTO();
        linkDTO.setAssetId(event.getAssetId());
        linkDTO.setProcurementEntityId(event.getProcurementEntityId());
        try {
            linkDTO.setProcurementEntityType(ProcurementEntityType.valueOf(event.getProcurementEntityType()));
        } catch (IllegalArgumentException e) {
            log.warn("Invalid procurement entity type: {}", event.getProcurementEntityType());
            return;
        }
        linkDTO.setLinkDate(LocalDate.now());
        
        assetProcurementLinkService.save(linkDTO);
    }
}
