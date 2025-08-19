package io.github.erp.internal.acl.depreciation;

import io.github.erp.internal.acl.dto.DepreciationData;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationEntryDTO;

import java.util.Optional;

public interface AssetDepreciationIntegrationACL {
    
    Optional<DepreciationData> translateAssetToDepreciationData(AssetRegistrationDTO asset);
    
    Optional<DepreciationEntryDTO> createDepreciationEntry(AssetRegistrationDTO asset, DepreciationData depreciationData);
    
    boolean validateAssetForDepreciation(AssetRegistrationDTO asset);
    
    Optional<String> determineDepreciationMethod(AssetRegistrationDTO asset);
}
