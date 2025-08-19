package io.github.erp.internal.acl.assets;

import io.github.erp.internal.acl.dto.AssetAcquisitionData;
import io.github.erp.service.dto.SettlementDTO;

import java.util.Optional;

public interface SettlementIntegrationACL {
    
    Optional<AssetAcquisitionData> translateSettlementToAssetAcquisition(SettlementDTO settlement);
    
    boolean validateSettlementForAssetAcquisition(SettlementDTO settlement);
    
    Optional<String> generateAssetNumber(SettlementDTO settlement);
    
    Optional<String> extractVendorInformation(SettlementDTO settlement);
}
