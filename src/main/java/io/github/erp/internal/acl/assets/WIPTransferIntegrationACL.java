package io.github.erp.internal.acl.assets;

import io.github.erp.internal.acl.dto.AssetAcquisitionData;
import io.github.erp.internal.acl.dto.ProjectCostData;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;

import java.util.Optional;

public interface WIPTransferIntegrationACL {
    
    Optional<AssetAcquisitionData> translateWIPToAssetRegistration(WorkInProgressRegistrationDTO wipRegistration);
    
    Optional<ProjectCostData> extractProjectCostData(WorkInProgressRegistrationDTO wipRegistration);
    
    boolean validateWIPForAssetTransfer(WorkInProgressRegistrationDTO wipRegistration);
    
    Optional<String> generateAssetNumberFromWIP(WorkInProgressRegistrationDTO wipRegistration);
}
