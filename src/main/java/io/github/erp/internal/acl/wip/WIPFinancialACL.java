package io.github.erp.internal.acl.wip;

import io.github.erp.internal.acl.AbstractTranslationACL;
import io.github.erp.internal.acl.dto.ProjectCostData;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Optional;

@Component
public class WIPFinancialACL extends AbstractTranslationACL<WorkInProgressRegistrationDTO, SettlementDTO> 
    implements WIPFinancialIntegrationACL {

    public WIPFinancialACL(Validator validator) {
        super(validator);
    }

    @Override
    public Optional<SettlementDTO> translateWIPToSettlement(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration == null) {
            return Optional.empty();
        }
        
        try {
            SettlementDTO settlement = translate(wipRegistration);
            return Optional.ofNullable(settlement);
        } catch (Exception e) {
            log.error("Failed to translate WIP to settlement: {}", wipRegistration.getId(), e);
            return Optional.empty();
        }
    }

    @Override
    protected SettlementDTO performTranslation(WorkInProgressRegistrationDTO source) {
        SettlementDTO settlement = new SettlementDTO();
        
        settlement.setPaymentNumber(generateSettlementReference(source).orElse("WIP-" + source.getId()));
        settlement.setPaymentDate(source.getInstalmentDate());
        settlement.setPaymentAmount(source.getInstalmentAmount());
        settlement.setDescription("WIP project cost: " + source.getParticulars());
        settlement.setNotes("Automated settlement from WIP registration");
        
        return settlement;
    }

    @Override
    public Optional<ProjectCostData> extractProjectCostData(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration == null) {
            return Optional.empty();
        }
        
        try {
            ProjectCostData data = ProjectCostData.builder()
                .projectCode(wipRegistration.getSequenceNumber())
                .projectName(wipRegistration.getParticulars())
                .totalCost(wipRegistration.getInstalmentAmount())
                .completionPercentage(BigDecimal.valueOf(wipRegistration.getLevelOfCompletion() != null ? 
                    wipRegistration.getLevelOfCompletion() : 0.0))
                .transferDate(wipRegistration.getInstalmentDate())
                .costCenter(extractCostCenter(wipRegistration))
                .description(wipRegistration.getParticulars())
                .build();
                
            return Optional.of(data);
        } catch (Exception e) {
            log.error("Failed to extract project cost data from WIP: {}", wipRegistration.getId(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean validateWIPForFinancialIntegration(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration == null) {
            return false;
        }
        
        return wipRegistration.getInstalmentAmount() != null &&
               wipRegistration.getInstalmentDate() != null &&
               wipRegistration.getSequenceNumber() != null;
    }

    @Override
    public Optional<String> generateSettlementReference(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration == null || wipRegistration.getSequenceNumber() == null) {
            return Optional.empty();
        }
        
        String reference = "WIP-" + wipRegistration.getSequenceNumber();
        return Optional.of(reference);
    }

    private String extractCostCenter(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration.getOutletCode() != null) {
            return wipRegistration.getOutletCode().getOutletCode();
        }
        return "DEFAULT-CC";
    }
}
