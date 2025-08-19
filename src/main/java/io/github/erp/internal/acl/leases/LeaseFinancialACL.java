package io.github.erp.internal.acl.leases;

import io.github.erp.internal.acl.AbstractTranslationACL;
import io.github.erp.internal.acl.dto.LeasePaymentData;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.dto.SettlementDTO;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.Optional;

@Component
public class LeaseFinancialACL extends AbstractTranslationACL<LeasePaymentDTO, SettlementDTO> 
    implements LeaseFinancialIntegrationACL {

    public LeaseFinancialACL(Validator validator) {
        super(validator);
    }

    @Override
    public Optional<SettlementDTO> translateLeasePaymentToSettlement(LeasePaymentDTO leasePayment) {
        if (leasePayment == null) {
            return Optional.empty();
        }
        
        try {
            SettlementDTO settlement = translate(leasePayment);
            return Optional.ofNullable(settlement);
        } catch (Exception e) {
            log.error("Failed to translate lease payment to settlement: {}", leasePayment.getId(), e);
            return Optional.empty();
        }
    }

    @Override
    protected SettlementDTO performTranslation(LeasePaymentDTO source) {
        SettlementDTO settlement = new SettlementDTO();
        
        settlement.setPaymentNumber(generateSettlementReference(source).orElse("LEASE-" + source.getId()));
        settlement.setPaymentDate(source.getPaymentDate());
        settlement.setPaymentAmount(source.getPaymentAmount());
        settlement.setDescription("Lease payment for contract: " + extractContractReference(source));
        settlement.setNotes("Automated settlement from lease payment");
        
        return settlement;
    }

    @Override
    public Optional<LeasePaymentData> extractLeasePaymentData(LeasePaymentDTO leasePayment) {
        if (leasePayment == null) {
            return Optional.empty();
        }
        
        try {
            LeasePaymentData data = LeasePaymentData.builder()
                .leaseContractId(extractContractId(leasePayment))
                .paymentAmount(leasePayment.getPaymentAmount())
                .paymentDate(leasePayment.getPaymentDate())
                .paymentType("LEASE_PAYMENT")
                .currencyCode(extractCurrencyCode(leasePayment))
                .description("Lease payment for contract: " + extractContractReference(leasePayment))
                .build();
                
            return Optional.of(data);
        } catch (Exception e) {
            log.error("Failed to extract lease payment data: {}", leasePayment.getId(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean validateLeasePaymentForFinancialIntegration(LeasePaymentDTO leasePayment) {
        if (leasePayment == null) {
            return false;
        }
        
        return leasePayment.getPaymentAmount() != null &&
               leasePayment.getPaymentDate() != null &&
               leasePayment.getLeaseContract() != null;
    }

    @Override
    public Optional<String> generateSettlementReference(LeasePaymentDTO leasePayment) {
        if (leasePayment == null || leasePayment.getId() == null) {
            return Optional.empty();
        }
        
        String contractRef = extractContractReference(leasePayment);
        String reference = "LEASE-" + contractRef + "-" + leasePayment.getId();
        return Optional.of(reference);
    }

    private String extractContractId(LeasePaymentDTO leasePayment) {
        if (leasePayment.getLeaseContract() != null && leasePayment.getLeaseContract().getId() != null) {
            return leasePayment.getLeaseContract().getId().toString();
        }
        return "UNKNOWN";
    }

    private String extractContractReference(LeasePaymentDTO leasePayment) {
        if (leasePayment.getLeaseContract() != null) {
            if (leasePayment.getLeaseContract().getBookingId() != null) {
                return leasePayment.getLeaseContract().getBookingId();
            }
            if (leasePayment.getLeaseContract().getId() != null) {
                return leasePayment.getLeaseContract().getId().toString();
            }
        }
        return "UNKNOWN";
    }

    private String extractCurrencyCode(LeasePaymentDTO leasePayment) {
        return "USD";
    }
}
