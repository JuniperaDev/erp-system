package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.Placeholder;
import io.github.erp.repository.PlaceholderRepository;
import io.github.erp.repository.search.PlaceholderSearchRepository;
import io.github.erp.service.PlaceholderService;
import io.github.erp.service.AuditTrailService;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.mapper.PlaceholderMapper;
import io.github.erp.repository.AssetWarrantyRepository;
import io.github.erp.repository.LeaseModelMetadataRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Placeholder}.
 */
@Service
@Transactional
public class PlaceholderServiceImpl implements PlaceholderService {

    private final Logger log = LoggerFactory.getLogger(PlaceholderServiceImpl.class);

    private final PlaceholderRepository placeholderRepository;

    private final PlaceholderMapper placeholderMapper;

    private final PlaceholderSearchRepository placeholderSearchRepository;
    private final AuditTrailService auditTrailService;
    private final AssetWarrantyRepository assetWarrantyRepository;
    private final LeaseModelMetadataRepository leaseModelMetadataRepository;

    public PlaceholderServiceImpl(
        PlaceholderRepository placeholderRepository,
        PlaceholderMapper placeholderMapper,
        PlaceholderSearchRepository placeholderSearchRepository,
        AuditTrailService auditTrailService,
        AssetWarrantyRepository assetWarrantyRepository,
        LeaseModelMetadataRepository leaseModelMetadataRepository
    ) {
        this.placeholderRepository = placeholderRepository;
        this.placeholderMapper = placeholderMapper;
        this.placeholderSearchRepository = placeholderSearchRepository;
        this.auditTrailService = auditTrailService;
        this.assetWarrantyRepository = assetWarrantyRepository;
        this.leaseModelMetadataRepository = leaseModelMetadataRepository;
    }

    @Override
    public PlaceholderDTO save(PlaceholderDTO placeholderDTO) {
        log.debug("Request to save Placeholder : {}", placeholderDTO);
        Placeholder placeholder = placeholderMapper.toEntity(placeholderDTO);
        placeholder = placeholderRepository.save(placeholder);
        PlaceholderDTO result = placeholderMapper.toDto(placeholder);
        placeholderSearchRepository.save(placeholder);
        return result;
    }

    @Override
    public Optional<PlaceholderDTO> partialUpdate(PlaceholderDTO placeholderDTO) {
        log.debug("Request to partially update Placeholder : {}", placeholderDTO);

        return placeholderRepository
            .findById(placeholderDTO.getId())
            .map(existingPlaceholder -> {
                placeholderMapper.partialUpdate(existingPlaceholder, placeholderDTO);

                return existingPlaceholder;
            })
            .map(placeholderRepository::save)
            .map(savedPlaceholder -> {
                placeholderSearchRepository.save(savedPlaceholder);

                return savedPlaceholder;
            })
            .map(placeholderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaceholderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Placeholders");
        return placeholderRepository.findAll(pageable).map(placeholderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlaceholderDTO> findOne(Long id) {
        log.debug("Request to get Placeholder : {}", id);
        return placeholderRepository.findById(id).map(placeholderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Placeholder : {}", id);
        placeholderRepository.deleteById(id);
        placeholderSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaceholderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Placeholders for query {}", query);
        return placeholderSearchRepository.search(query, pageable).map(placeholderMapper::toDto);
    }

    @Override
    public Set<PlaceholderDTO> attachPlaceholders(Long entityId, String entityType, Set<PlaceholderDTO> placeholders) {
        log.debug("Request to attach {} placeholders to {} with id {}", placeholders.size(), entityType, entityId);
        
        Set<PlaceholderDTO> attachedPlaceholders = new HashSet<>();
        
        for (PlaceholderDTO placeholder : placeholders) {
            if (addPlaceholderToEntity(entityId, entityType, placeholder)) {
                attachedPlaceholders.add(placeholder);
                auditTrailService.logPlaceholderEvent(entityType, entityId, placeholder.getId(), "ATTACH", getCurrentUser());
            }
        }
        
        return attachedPlaceholders;
    }

    @Override
    public Set<PlaceholderDTO> detachPlaceholders(Long entityId, String entityType, Set<Long> placeholderIds) {
        log.debug("Request to detach {} placeholders from {} with id {}", placeholderIds.size(), entityType, entityId);
        
        Set<PlaceholderDTO> remainingPlaceholders = getAttachedPlaceholders(entityId, entityType);
        
        for (Long placeholderId : placeholderIds) {
            if (removePlaceholderFromEntity(entityId, entityType, placeholderId)) {
                remainingPlaceholders.removeIf(placeholder -> placeholder.getId().equals(placeholderId));
                auditTrailService.logPlaceholderEvent(entityType, entityId, placeholderId, "DETACH", getCurrentUser());
            }
        }
        
        return remainingPlaceholders;
    }

    @Override
    public Set<PlaceholderDTO> getAttachedPlaceholders(Long entityId, String entityType) {
        log.debug("Request to get attached placeholders for {} with id {}", entityType, entityId);
        
        switch (entityType.toLowerCase()) {
            case "assetwarranty":
                return assetWarrantyRepository.findById(entityId)
                    .map(entity -> entity.getPlaceholders().stream()
                        .map(placeholderMapper::toDto)
                        .collect(Collectors.toSet()))
                    .orElse(new HashSet<>());
                    
            case "leasemodelmetadata":
                return leaseModelMetadataRepository.findById(entityId)
                    .map(entity -> entity.getPlaceholders().stream()
                        .map(placeholderMapper::toDto)
                        .collect(Collectors.toSet()))
                    .orElse(new HashSet<>());
                    
            default:
                log.warn("Unsupported entity type for placeholder attachments: {}", entityType);
                return new HashSet<>();
        }
    }

    @Override
    public Page<PlaceholderDTO> findByTokenPattern(String tokenPattern, Pageable pageable) {
        log.debug("Request to find placeholders by token pattern: {}", tokenPattern);
        return placeholderSearchRepository.search("token:" + tokenPattern, pageable).map(placeholderMapper::toDto);
    }

    @Override
    public Set<PlaceholderDTO> createFromTemplates(Set<PlaceholderDTO> templates) {
        log.debug("Request to create {} placeholders from templates", templates.size());
        
        Set<PlaceholderDTO> createdPlaceholders = new HashSet<>();
        
        for (PlaceholderDTO template : templates) {
            PlaceholderDTO newPlaceholder = new PlaceholderDTO();
            newPlaceholder.setDescription(template.getDescription());
            newPlaceholder.setToken(template.getToken());
            newPlaceholder.setFileUploadToken(template.getFileUploadToken());
            newPlaceholder.setCompilationToken(template.getCompilationToken());
            
            PlaceholderDTO saved = save(newPlaceholder);
            createdPlaceholders.add(saved);
        }
        
        return createdPlaceholders;
    }

    private boolean addPlaceholderToEntity(Long entityId, String entityType, PlaceholderDTO placeholder) {
        switch (entityType.toLowerCase()) {
            case "assetwarranty":
                return assetWarrantyRepository.findById(entityId)
                    .map(entity -> {
                        entity.getPlaceholders().add(placeholderMapper.toEntity(placeholder));
                        assetWarrantyRepository.save(entity);
                        return true;
                    })
                    .orElse(false);
                    
            case "leasemodelmetadata":
                return leaseModelMetadataRepository.findById(entityId)
                    .map(entity -> {
                        entity.getPlaceholders().add(placeholderMapper.toEntity(placeholder));
                        leaseModelMetadataRepository.save(entity);
                        return true;
                    })
                    .orElse(false);
                    
            default:
                log.warn("Unsupported entity type for placeholder attachments: {}", entityType);
                return false;
        }
    }

    private boolean removePlaceholderFromEntity(Long entityId, String entityType, Long placeholderId) {
        switch (entityType.toLowerCase()) {
            case "assetwarranty":
                return assetWarrantyRepository.findById(entityId)
                    .map(entity -> {
                        boolean removed = entity.getPlaceholders().removeIf(placeholder -> placeholder.getId().equals(placeholderId));
                        if (removed) {
                            assetWarrantyRepository.save(entity);
                        }
                        return removed;
                    })
                    .orElse(false);
                    
            case "leasemodelmetadata":
                return leaseModelMetadataRepository.findById(entityId)
                    .map(entity -> {
                        boolean removed = entity.getPlaceholders().removeIf(placeholder -> placeholder.getId().equals(placeholderId));
                        if (removed) {
                            leaseModelMetadataRepository.save(entity);
                        }
                        return removed;
                    })
                    .orElse(false);
                    
            default:
                log.warn("Unsupported entity type for placeholder attachments: {}", entityType);
                return false;
        }
    }

    private String getCurrentUser() {
        return "system";
    }
}
