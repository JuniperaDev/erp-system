package io.github.erp.internal.acl.depreciation;

import io.github.erp.internal.acl.AbstractTranslationACL;
import io.github.erp.internal.acl.dto.DepreciationData;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationEntryDTO;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class AssetToDepreciationACL extends AbstractTranslationACL<AssetRegistrationDTO, DepreciationData> 
    implements AssetDepreciationIntegrationACL {

    public AssetToDepreciationACL(Validator validator) {
        super(validator);
    }

    @Override
    public Optional<DepreciationData> translateAssetToDepreciationData(AssetRegistrationDTO asset) {
        if (asset == null) {
            return Optional.empty();
        }
        
        try {
            DepreciationData data = translate(asset);
            return Optional.ofNullable(data);
        } catch (Exception e) {
            log.error("Failed to translate asset to depreciation data: {}", asset.getId(), e);
            return Optional.empty();
        }
    }

    @Override
    protected DepreciationData performTranslation(AssetRegistrationDTO source) {
        return DepreciationData.builder()
            .assetId(source.getId().toString())
            .depreciationAmount(calculateDepreciationAmount(source))
            .depreciationDate(LocalDate.now())
            .depreciationMethod(determineDepreciationMethod(source).orElse("STRAIGHT_LINE"))
            .netBookValue(calculateNetBookValue(source))
            .fiscalPeriod(getCurrentFiscalPeriod())
            .build();
    }

    @Override
    public Optional<DepreciationEntryDTO> createDepreciationEntry(AssetRegistrationDTO asset, DepreciationData depreciationData) {
        if (asset == null || depreciationData == null) {
            return Optional.empty();
        }
        
        try {
            DepreciationEntryDTO entry = new DepreciationEntryDTO();
            entry.setDepreciationAmount(depreciationData.getDepreciationAmount());
            entry.setDepreciationPeriodStartDate(depreciationData.getDepreciationDate());
            entry.setDepreciationPeriodEndDate(depreciationData.getDepreciationDate().plusMonths(1));
            entry.setAssetNumber(asset.getId());
            entry.setNetBookValue(depreciationData.getNetBookValue());
            
            return Optional.of(entry);
        } catch (Exception e) {
            log.error("Failed to create depreciation entry for asset: {}", asset.getId(), e);
            return Optional.empty();
        }
    }

    @Override
    public boolean validateAssetForDepreciation(AssetRegistrationDTO asset) {
        if (asset == null) {
            return false;
        }
        
        return asset.getAssetCost() != null &&
               asset.getCapitalizationDate() != null &&
               asset.getAssetCategory() != null &&
               asset.getAssetCost().compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public Optional<String> determineDepreciationMethod(AssetRegistrationDTO asset) {
        if (asset == null || asset.getAssetCategory() == null) {
            return Optional.of("STRAIGHT_LINE");
        }
        
        if (asset.getAssetCategory().getDepreciationMethod() != null) {
            return Optional.of(asset.getAssetCategory().getDepreciationMethod().getDepreciationMethodName());
        }
        
        return Optional.of("STRAIGHT_LINE");
    }

    private BigDecimal calculateDepreciationAmount(AssetRegistrationDTO asset) {
        if (asset.getAssetCost() == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal usefulLife = BigDecimal.valueOf(5);
        return asset.getAssetCost().divide(usefulLife, 2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal calculateNetBookValue(AssetRegistrationDTO asset) {
        if (asset.getAssetCost() == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal depreciationAmount = calculateDepreciationAmount(asset);
        return asset.getAssetCost().subtract(depreciationAmount);
    }

    private String getCurrentFiscalPeriod() {
        LocalDate now = LocalDate.now();
        return now.getYear() + "-" + String.format("%02d", now.getMonthValue());
    }
}
