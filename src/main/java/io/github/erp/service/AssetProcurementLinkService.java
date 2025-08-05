package io.github.erp.service;

import io.github.erp.service.dto.AssetProcurementLinkDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssetProcurementLinkService {
    
    AssetProcurementLinkDTO save(AssetProcurementLinkDTO assetProcurementLinkDTO);
    
    Optional<AssetProcurementLinkDTO> partialUpdate(AssetProcurementLinkDTO assetProcurementLinkDTO);
    
    Page<AssetProcurementLinkDTO> findAll(Pageable pageable);
    
    Optional<AssetProcurementLinkDTO> findOne(Long id);
    
    void delete(Long id);
    
    List<AssetProcurementLinkDTO> findByAssetId(Long assetId);
    
    List<AssetProcurementLinkDTO> findByProcurementEntity(Long procurementEntityId, String procurementEntityType);
}
