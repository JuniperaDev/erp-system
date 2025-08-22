package io.github.erp.context.assets.service;

import io.github.erp.context.assets.acl.SettlementToAssetAcquisitionACL;
import io.github.erp.context.assets.domain.AssetRegistration;
import io.github.erp.internal.acl.dto.AssetAcquisitionData;
import io.github.erp.service.dto.SettlementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AssetManagementContextService {

    private final Logger log = LoggerFactory.getLogger(AssetManagementContextService.class);
    
    private final SettlementToAssetAcquisitionACL settlementToAssetAcquisitionACL;

    public AssetManagementContextService(SettlementToAssetAcquisitionACL settlementToAssetAcquisitionACL) {
        this.settlementToAssetAcquisitionACL = settlementToAssetAcquisitionACL;
    }

    public Optional<AssetAcquisitionData> processSettlementForAssetAcquisition(SettlementDTO settlement) {
        log.debug("Processing settlement for asset acquisition: {}", settlement.getId());
        
        if (!settlementToAssetAcquisitionACL.validateSettlementForAssetAcquisition(settlement)) {
            log.warn("Settlement {} is not valid for asset acquisition", settlement.getId());
            return Optional.empty();
        }
        
        return settlementToAssetAcquisitionACL.adaptSettlementToAssetAcquisition(settlement);
    }

    public void linkAssetToSettlement(AssetRegistration asset, Long settlementId) {
        log.debug("Linking asset {} to settlement {}", asset.getId(), settlementId);
        asset.setAcquiringTransactionId(settlementId);
    }
}
