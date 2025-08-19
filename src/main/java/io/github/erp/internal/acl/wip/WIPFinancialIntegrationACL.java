package io.github.erp.internal.acl.wip;

import io.github.erp.internal.acl.dto.ProjectCostData;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;

import java.util.Optional;

public interface WIPFinancialIntegrationACL {
    
    Optional<SettlementDTO> translateWIPToSettlement(WorkInProgressRegistrationDTO wipRegistration);
    
    Optional<ProjectCostData> extractProjectCostData(WorkInProgressRegistrationDTO wipRegistration);
    
    boolean validateWIPForFinancialIntegration(WorkInProgressRegistrationDTO wipRegistration);
    
    Optional<String> generateSettlementReference(WorkInProgressRegistrationDTO wipRegistration);
}
