package io.github.erp.internal.acl.assets;

import io.github.erp.internal.acl.AbstractTranslationACL;
import io.github.erp.internal.acl.dto.AssetAcquisitionData;
import io.github.erp.service.dto.SettlementDTO;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.Optional;

@Component
public class SettlementToAssetAcquisitionACL extends AbstractTranslationACL<SettlementDTO, AssetAcquisitionData> 
    implements SettlementIntegrationACL {

    public SettlementToAssetAcquisitionACL(Validator validator) {
        super(validator);
    }

    @Override
    public Optional<AssetAcquisitionData> translateSettlementToAssetAcquisition(SettlementDTO settlement) {
        if (settlement == null) {
            return Optional.empty();
        }
        
        try {
            AssetAcquisitionData result = translate(settlement);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("Failed to translate settlement to asset acquisition: {}", settlement.getId(), e);
            return Optional.empty();
        }
    }

    @Override
    protected AssetAcquisitionData performTranslation(SettlementDTO source) {
        return AssetAcquisitionData.builder()
            .assetNumber(generateAssetNumberFromSettlement(source))
            .assetTag(generateAssetTagFromSettlement(source))
            .acquisitionCost(source.getPaymentAmount())
            .acquisitionDate(source.getPaymentDate())
            .vendorName(extractVendorName(source))
            .vendorCode(extractVendorCode(source))
            .description(source.getDescription())
            .currencyCode(extractCurrencyCode(source))
            .build();
    }

    @Override
    public boolean validateSettlementForAssetAcquisition(SettlementDTO settlement) {
        if (settlement == null) {
            return false;
        }
        
        return settlement.getPaymentAmount() != null &&
               settlement.getPaymentDate() != null &&
               settlement.getBiller() != null &&
               settlement.getSettlementCurrency() != null;
    }

    @Override
    public Optional<String> generateAssetNumber(SettlementDTO settlement) {
        if (settlement == null || settlement.getPaymentNumber() == null) {
            return Optional.empty();
        }
        
        String assetNumber = "AST-" + settlement.getPaymentNumber();
        return Optional.of(assetNumber);
    }

    @Override
    public Optional<String> extractVendorInformation(SettlementDTO settlement) {
        if (settlement == null || settlement.getBiller() == null) {
            return Optional.empty();
        }
        
        return Optional.of(settlement.getBiller().getDealerName());
    }

    private String generateAssetNumberFromSettlement(SettlementDTO settlement) {
        return generateAssetNumber(settlement).orElse("AST-" + System.currentTimeMillis());
    }

    private String generateAssetTagFromSettlement(SettlementDTO settlement) {
        if (settlement.getPaymentNumber() != null) {
            return "TAG-" + settlement.getPaymentNumber();
        }
        return "TAG-" + System.currentTimeMillis();
    }

    private String extractVendorName(SettlementDTO settlement) {
        if (settlement.getBiller() != null) {
            return settlement.getBiller().getDealerName();
        }
        return "Unknown Vendor";
    }

    private String extractVendorCode(SettlementDTO settlement) {
        if (settlement.getBiller() != null && settlement.getBiller().getId() != null) {
            return "VND-" + settlement.getBiller().getId();
        }
        return "VND-UNKNOWN";
    }

    private String extractCurrencyCode(SettlementDTO settlement) {
        if (settlement.getSettlementCurrency() != null) {
            return settlement.getSettlementCurrency().getIso4217CurrencyCode();
        }
        return "USD";
    }
}
