package io.github.erp.repository;

import io.github.erp.domain.AssetProcurementLink;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface AssetProcurementLinkRepository extends JpaRepository<AssetProcurementLink, Long> {
    
    List<AssetProcurementLink> findByAssetId(Long assetId);
    
    List<AssetProcurementLink> findByProcurementEntityIdAndProcurementEntityType(Long procurementEntityId, String procurementEntityType);
    
    void deleteByAssetId(Long assetId);
    
    void deleteByProcurementEntityIdAndProcurementEntityType(Long procurementEntityId, String procurementEntityType);
}
