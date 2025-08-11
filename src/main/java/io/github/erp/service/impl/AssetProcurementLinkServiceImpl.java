package io.github.erp.service.impl;

import io.github.erp.domain.AssetProcurementLink;
import io.github.erp.repository.AssetProcurementLinkRepository;
import io.github.erp.service.AssetProcurementLinkService;
import io.github.erp.service.dto.AssetProcurementLinkDTO;
import io.github.erp.service.mapper.AssetProcurementLinkMapper;
import io.github.erp.service.validation.AssetProcurementLinkValidationService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AssetProcurementLinkServiceImpl implements AssetProcurementLinkService {

    private final Logger log = LoggerFactory.getLogger(AssetProcurementLinkServiceImpl.class);

    private final AssetProcurementLinkRepository assetProcurementLinkRepository;

    private final AssetProcurementLinkMapper assetProcurementLinkMapper;

    private final AssetProcurementLinkValidationService validationService;

    public AssetProcurementLinkServiceImpl(
        AssetProcurementLinkRepository assetProcurementLinkRepository,
        AssetProcurementLinkMapper assetProcurementLinkMapper,
        AssetProcurementLinkValidationService validationService
    ) {
        this.assetProcurementLinkRepository = assetProcurementLinkRepository;
        this.assetProcurementLinkMapper = assetProcurementLinkMapper;
        this.validationService = validationService;
    }

    @Override
    public AssetProcurementLinkDTO save(AssetProcurementLinkDTO assetProcurementLinkDTO) {
        log.debug("Request to save AssetProcurementLink : {}", assetProcurementLinkDTO);
        
        if (!validationService.isValidAssetId(assetProcurementLinkDTO.getAssetId())) {
            throw new IllegalArgumentException("Invalid asset ID");
        }
        
        if (!validationService.isValidProcurementEntityId(assetProcurementLinkDTO.getProcurementEntityId())) {
            throw new IllegalArgumentException("Invalid procurement entity ID");
        }
        
        if (!validationService.isValidProcurementEntityType(assetProcurementLinkDTO.getProcurementEntityType())) {
            throw new IllegalArgumentException("Invalid procurement entity type: " + assetProcurementLinkDTO.getProcurementEntityType());
        }
        
        AssetProcurementLink assetProcurementLink = assetProcurementLinkMapper.toEntity(assetProcurementLinkDTO);
        assetProcurementLink = assetProcurementLinkRepository.save(assetProcurementLink);
        return assetProcurementLinkMapper.toDto(assetProcurementLink);
    }

    @Override
    public Optional<AssetProcurementLinkDTO> partialUpdate(AssetProcurementLinkDTO assetProcurementLinkDTO) {
        log.debug("Request to partially update AssetProcurementLink : {}", assetProcurementLinkDTO);

        if (assetProcurementLinkDTO.getAssetId() != null && !validationService.isValidAssetId(assetProcurementLinkDTO.getAssetId())) {
            throw new IllegalArgumentException("Invalid asset ID");
        }
        
        if (assetProcurementLinkDTO.getProcurementEntityId() != null && !validationService.isValidProcurementEntityId(assetProcurementLinkDTO.getProcurementEntityId())) {
            throw new IllegalArgumentException("Invalid procurement entity ID");
        }
        
        if (assetProcurementLinkDTO.getProcurementEntityType() != null && !validationService.isValidProcurementEntityType(assetProcurementLinkDTO.getProcurementEntityType())) {
            throw new IllegalArgumentException("Invalid procurement entity type: " + assetProcurementLinkDTO.getProcurementEntityType());
        }

        return assetProcurementLinkRepository
            .findById(assetProcurementLinkDTO.getId())
            .map(existingAssetProcurementLink -> {
                assetProcurementLinkMapper.partialUpdate(existingAssetProcurementLink, assetProcurementLinkDTO);
                return existingAssetProcurementLink;
            })
            .map(assetProcurementLinkRepository::save)
            .map(assetProcurementLinkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetProcurementLinkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetProcurementLinks");
        return assetProcurementLinkRepository.findAll(pageable).map(assetProcurementLinkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetProcurementLinkDTO> findOne(Long id) {
        log.debug("Request to get AssetProcurementLink : {}", id);
        return assetProcurementLinkRepository.findById(id).map(assetProcurementLinkMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetProcurementLink : {}", id);
        assetProcurementLinkRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetProcurementLinkDTO> findByAssetId(Long assetId) {
        log.debug("Request to get AssetProcurementLinks by assetId : {}", assetId);
        return assetProcurementLinkRepository.findByAssetId(assetId)
            .stream()
            .map(assetProcurementLinkMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetProcurementLinkDTO> findByProcurementEntity(Long procurementEntityId, String procurementEntityType) {
        log.debug("Request to get AssetProcurementLinks by procurement entity : {} {}", procurementEntityId, procurementEntityType);
        return assetProcurementLinkRepository.findByProcurementEntityIdAndProcurementEntityType(procurementEntityId, procurementEntityType)
            .stream()
            .map(assetProcurementLinkMapper::toDto)
            .collect(Collectors.toList());
    }
}
