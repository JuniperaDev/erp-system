package io.github.erp.context.assets.acl;

import io.github.erp.internal.acl.AbstractTranslationACL;
import io.github.erp.internal.acl.dto.AssetAcquisitionData;
import io.github.erp.internal.acl.dto.ProjectCostData;
import io.github.erp.internal.acl.assets.WIPTransferIntegrationACL;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Optional;

@Component
public class WIPToAssetRegistrationACL extends AbstractTranslationACL<WorkInProgressRegistrationDTO, AssetAcquisitionData> 
    implements WIPTransferIntegrationACL {

    public WIPToAssetRegistrationACL(Validator validator) {
        super(validator);
    }

    @Override
    public Optional<AssetAcquisitionData> translateWIPToAssetRegistration(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration == null) {
            return Optional.empty();
        }
        
        try {
            AssetAcquisitionData result = translate(wipRegistration);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("Failed to translate WIP to asset registration: {}", wipRegistration.getId(), e);
            return Optional.empty();
        }
    }

    @Override
    protected AssetAcquisitionData performTranslation(WorkInProgressRegistrationDTO source) {
        return AssetAcquisitionData.builder()
            .assetNumber(generateAssetNumberFromWIP(source).orElse("AST-WIP-" + source.getId()))
            .assetTag(generateAssetTagFromWIP(source))
            .acquisitionCost(source.getInstalmentAmount())
            .acquisitionDate(source.getInstalmentDate())
            .vendorName(extractVendorNameFromWIP(source))
            .vendorCode(extractVendorCodeFromWIP(source))
            .description(source.getParticulars())
            .currencyCode(extractCurrencyCodeFromWIP(source))
            .build();
    }

    @Override
    public Optional<ProjectCostData> extractProjectCostData(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration == null) {
            return Optional.empty();
        }
        
        try {
            ProjectCostData projectData = ProjectCostData.builder()
                .projectCode(wipRegistration.getSequenceNumber())
                .projectName(wipRegistration.getParticulars())
                .totalCost(wipRegistration.getInstalmentAmount())
                .completionPercentage(BigDecimal.valueOf(wipRegistration.getLevelOfCompletion() != null ? 
                    wipRegistration.getLevelOfCompletion() : 0.0))
                .transferDate(wipRegistration.getInstalmentDate())
                .costCenter(extractCostCenterFromWIP(wipRegistration))
                .description(wipRegistration.getParticulars())
                .build();
                
            return Optional.of(projectData);
        } catch (Exception e) {
            log.error("Failed to extract project cost data from WIP: {}", wipRegistration.getId(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean validateWIPForAssetTransfer(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration == null) {
            return false;
        }
        
        return wipRegistration.getInstalmentAmount() != null &&
               wipRegistration.getInstalmentDate() != null &&
               wipRegistration.getCompleted() != null &&
               wipRegistration.getCompleted() &&
               wipRegistration.getLevelOfCompletion() != null &&
               wipRegistration.getLevelOfCompletion() >= 100.0;
    }

    @Override
    public Optional<String> generateAssetNumberFromWIP(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration == null || wipRegistration.getSequenceNumber() == null) {
            return Optional.empty();
        }
        
        String assetNumber = "AST-WIP-" + wipRegistration.getSequenceNumber();
        return Optional.of(assetNumber);
    }

    private String generateAssetTagFromWIP(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration.getSequenceNumber() != null) {
            return "TAG-WIP-" + wipRegistration.getSequenceNumber();
        }
        return "TAG-WIP-" + wipRegistration.getId();
    }

    private String extractVendorNameFromWIP(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration.getDealer() != null) {
            return wipRegistration.getDealer().getDealerName();
        }
        return "Internal Project";
    }

    private String extractVendorCodeFromWIP(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration.getDealer() != null && wipRegistration.getDealer().getId() != null) {
            return "VND-" + wipRegistration.getDealer().getId();
        }
        return "VND-INTERNAL";
    }

    private String extractCurrencyCodeFromWIP(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration.getSettlementCurrency() != null) {
            return wipRegistration.getSettlementCurrency().getIso4217CurrencyCode();
        }
        return "USD";
    }

    private String extractCostCenterFromWIP(WorkInProgressRegistrationDTO wipRegistration) {
        if (wipRegistration.getOutletCode() != null) {
            return wipRegistration.getOutletCode().getOutletCode();
        }
        return "DEFAULT-CC";
    }
}
