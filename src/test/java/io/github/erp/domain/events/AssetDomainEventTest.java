package io.github.erp.domain.events;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class AssetDomainEventTest {

    @Test
    void assetAcquiredEventTest() {
        Long assetId = 1L;
        Long settlementId = 2L;
        LocalDate acquisitionDate = LocalDate.now();
        
        AssetAcquiredEvent event = new AssetAcquiredEvent(this, assetId, settlementId, acquisitionDate);
        
        assertThat(event.getAssetId()).isEqualTo(assetId);
        assertThat(event.getSettlementId()).isEqualTo(settlementId);
        assertThat(event.getAcquisitionDate()).isEqualTo(acquisitionDate);
        assertThat(event.getOccurredOn()).isNotNull();
    }

    @Test
    void assetProcurementLinkedEventTest() {
        Long assetId = 1L;
        Long procurementEntityId = 2L;
        String procurementEntityType = "PurchaseOrder";
        
        AssetProcurementLinkedEvent event = new AssetProcurementLinkedEvent(this, assetId, procurementEntityId, procurementEntityType);
        
        assertThat(event.getAssetId()).isEqualTo(assetId);
        assertThat(event.getProcurementEntityId()).isEqualTo(procurementEntityId);
        assertThat(event.getProcurementEntityType()).isEqualTo(procurementEntityType);
        assertThat(event.getOccurredOn()).isNotNull();
    }
}
