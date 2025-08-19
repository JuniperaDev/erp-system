package io.github.erp.internal.acl.financial;

import io.github.erp.service.dto.SettlementDTO;

import java.util.Optional;

public interface PaymentProcessingACL {
    
    Optional<SettlementDTO> harmonizePaymentData(SettlementDTO payment);
    
    boolean validatePaymentIntegrity(SettlementDTO payment);
    
    Optional<String> standardizePaymentReference(SettlementDTO payment);
    
    Optional<String> extractPaymentCategory(SettlementDTO payment);
}
