package io.github.erp.internal.acl.leases;

import io.github.erp.internal.acl.dto.LeasePaymentData;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.dto.SettlementDTO;

import java.util.Optional;

public interface LeaseFinancialIntegrationACL {
    
    Optional<SettlementDTO> translateLeasePaymentToSettlement(LeasePaymentDTO leasePayment);
    
    Optional<LeasePaymentData> extractLeasePaymentData(LeasePaymentDTO leasePayment);
    
    boolean validateLeasePaymentForFinancialIntegration(LeasePaymentDTO leasePayment);
    
    Optional<String> generateSettlementReference(LeasePaymentDTO leasePayment);
}
