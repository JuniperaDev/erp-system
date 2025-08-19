package io.github.erp.internal.acl.financial;

import io.github.erp.internal.acl.AbstractTranslationACL;
import io.github.erp.service.dto.SettlementDTO;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.Optional;

@Component
public class MultiContextPaymentACL extends AbstractTranslationACL<SettlementDTO, SettlementDTO> 
    implements PaymentProcessingACL {

    public MultiContextPaymentACL(Validator validator) {
        super(validator);
    }

    @Override
    public Optional<SettlementDTO> harmonizePaymentData(SettlementDTO payment) {
        if (payment == null) {
            return Optional.empty();
        }
        
        try {
            SettlementDTO harmonized = translate(payment);
            return Optional.ofNullable(harmonized);
        } catch (Exception e) {
            log.error("Failed to harmonize payment data: {}", payment.getId(), e);
            return Optional.empty();
        }
    }

    @Override
    protected SettlementDTO performTranslation(SettlementDTO source) {
        SettlementDTO harmonized = new SettlementDTO();
        
        harmonized.setId(source.getId());
        harmonized.setPaymentNumber(standardizePaymentReference(source).orElse(source.getPaymentNumber()));
        harmonized.setPaymentDate(source.getPaymentDate());
        harmonized.setPaymentAmount(source.getPaymentAmount());
        harmonized.setDescription(normalizeDescription(source.getDescription()));
        harmonized.setNotes(source.getNotes());
        harmonized.setRemarks(source.getRemarks());
        
        harmonized.setSettlementCurrency(source.getSettlementCurrency());
        harmonized.setPaymentLabels(source.getPaymentLabels());
        harmonized.setPaymentCategory(source.getPaymentCategory());
        harmonized.setBiller(source.getBiller());
        harmonized.setPaymentInvoices(source.getPaymentInvoices());
        harmonized.setSignatories(source.getSignatories());
        harmonized.setBusinessDocuments(source.getBusinessDocuments());
        
        return harmonized;
    }

    @Override
    public boolean validatePaymentIntegrity(SettlementDTO payment) {
        if (payment == null) {
            return false;
        }
        
        return payment.getPaymentAmount() != null &&
               payment.getPaymentDate() != null &&
               payment.getSettlementCurrency() != null &&
               payment.getBiller() != null &&
               payment.getPaymentCategory() != null;
    }

    @Override
    public Optional<String> standardizePaymentReference(SettlementDTO payment) {
        if (payment == null || payment.getPaymentNumber() == null) {
            return Optional.empty();
        }
        
        String standardized = payment.getPaymentNumber().toUpperCase().trim();
        if (!standardized.startsWith("PAY-")) {
            standardized = "PAY-" + standardized;
        }
        
        return Optional.of(standardized);
    }

    @Override
    public Optional<String> extractPaymentCategory(SettlementDTO payment) {
        if (payment == null || payment.getPaymentCategory() == null) {
            return Optional.empty();
        }
        
        return Optional.of(payment.getPaymentCategory().getCategoryName());
    }

    private String normalizeDescription(String description) {
        if (description == null) {
            return null;
        }
        
        return description.trim().replaceAll("\\s+", " ");
    }
}
