package io.github.erp.context.assets.acl;

import io.github.erp.internal.acl.AbstractTranslationACL;
import io.github.erp.internal.acl.dto.AssetAcquisitionData;
import io.github.erp.service.dto.SettlementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.Optional;

@Component("contextSettlementToAssetAcquisitionACL")
public class SettlementToAssetAcquisitionACL extends AbstractTranslationACL<SettlementDTO, AssetAcquisitionData> {

    private static final Logger log = LoggerFactory.getLogger(SettlementToAssetAcquisitionACL.class);

    public SettlementToAssetAcquisitionACL(Validator validator) {
        super(validator);
    }

    @Override
    protected AssetAcquisitionData performTranslation(SettlementDTO source) {
        return AssetAcquisitionData.builder()
            .acquisitionCost(source.getPaymentAmount())
            .acquisitionDate(source.getPaymentDate())
            .vendorName(source.getBiller() != null ? source.getBiller().getDealerName() : "Unknown Vendor")
            .vendorCode(source.getBiller() != null ? "VND-" + source.getBiller().getId() : "VND-UNKNOWN")
            .currencyCode(source.getSettlementCurrency() != null ? 
                source.getSettlementCurrency().getIso4217CurrencyCode() : "USD")
            .description(source.getDescription())
            .build();
    }

    public Optional<AssetAcquisitionData> adaptSettlementToAssetAcquisition(SettlementDTO settlement) {
        if (settlement == null) {
            return Optional.empty();
        }
        
        try {
            AssetAcquisitionData result = translate(settlement);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("Failed to adapt settlement to asset acquisition: {}", settlement.getId(), e);
            return Optional.empty();
        }
    }

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

    public Optional<String> generateAssetNumber(SettlementDTO settlement) {
        if (settlement == null || settlement.getPaymentNumber() == null) {
            return Optional.empty();
        }
        
        String assetNumber = "AST-PAY-" + settlement.getPaymentNumber();
        return Optional.of(assetNumber);
    }

    public boolean validateSettlementForAssetAcquisition(SettlementDTO settlement) {
        if (settlement == null) {
            return false;
        }
        
        return settlement.getPaymentAmount() != null &&
               settlement.getPaymentDate() != null &&
               settlement.getBiller() != null &&
               settlement.getSettlementCurrency() != null;
    }
}
