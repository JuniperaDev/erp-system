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

import io.github.erp.service.DocumentAttachmentService;
import io.github.erp.service.BusinessDocumentService;
import io.github.erp.service.AuditTrailService;
import io.github.erp.service.dto.BusinessDocumentDTO;
import io.github.erp.repository.RouModelMetadataRepository;
import io.github.erp.repository.AssetWarrantyRepository;
import io.github.erp.repository.LeaseModelMetadataRepository;
import io.github.erp.service.mapper.BusinessDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing document attachments across entities.
 */
@Service
@Transactional
public class DocumentAttachmentServiceImpl implements DocumentAttachmentService {

    private final Logger log = LoggerFactory.getLogger(DocumentAttachmentServiceImpl.class);

    private final BusinessDocumentService businessDocumentService;
    private final AuditTrailService auditTrailService;
    private final RouModelMetadataRepository rouModelMetadataRepository;
    private final AssetWarrantyRepository assetWarrantyRepository;
    private final LeaseModelMetadataRepository leaseModelMetadataRepository;
    private final BusinessDocumentMapper businessDocumentMapper;

    public DocumentAttachmentServiceImpl(
        BusinessDocumentService businessDocumentService,
        AuditTrailService auditTrailService,
        RouModelMetadataRepository rouModelMetadataRepository,
        AssetWarrantyRepository assetWarrantyRepository,
        LeaseModelMetadataRepository leaseModelMetadataRepository,
        BusinessDocumentMapper businessDocumentMapper
    ) {
        this.businessDocumentService = businessDocumentService;
        this.auditTrailService = auditTrailService;
        this.rouModelMetadataRepository = rouModelMetadataRepository;
        this.assetWarrantyRepository = assetWarrantyRepository;
        this.leaseModelMetadataRepository = leaseModelMetadataRepository;
        this.businessDocumentMapper = businessDocumentMapper;
    }

    @Override
    public Set<BusinessDocumentDTO> attachDocuments(Long entityId, String entityType, Set<BusinessDocumentDTO> documents) {
        log.debug("Request to attach {} documents to {} with id {}", documents.size(), entityType, entityId);
        
        Set<BusinessDocumentDTO> attachedDocuments = new HashSet<>();
        
        for (BusinessDocumentDTO document : documents) {
            if (canAttachDocument(entityId, entityType, document.getId())) {
                BusinessDocumentDTO attached = addDocumentAttachment(entityId, entityType, document);
                if (attached != null) {
                    attachedDocuments.add(attached);
                    auditTrailService.logDocumentEvent(entityType, entityId, document.getId(), "ATTACH", getCurrentUser());
                }
            }
        }
        
        return attachedDocuments;
    }

    @Override
    public Set<BusinessDocumentDTO> detachDocuments(Long entityId, String entityType, Set<Long> documentIds) {
        log.debug("Request to detach {} documents from {} with id {}", documentIds.size(), entityType, entityId);
        
        Set<BusinessDocumentDTO> remainingDocuments = getAttachedDocuments(entityId, entityType);
        
        for (Long documentId : documentIds) {
            if (removeDocumentAttachment(entityId, entityType, documentId)) {
                remainingDocuments.removeIf(doc -> doc.getId().equals(documentId));
                auditTrailService.logDocumentEvent(entityType, entityId, documentId, "DETACH", getCurrentUser());
            }
        }
        
        return remainingDocuments;
    }

    @Override
    public Set<BusinessDocumentDTO> getAttachedDocuments(Long entityId, String entityType) {
        log.debug("Request to get attached documents for {} with id {}", entityType, entityId);
        
        switch (entityType.toLowerCase()) {
            case "roumodelmetadata":
                return rouModelMetadataRepository.findById(entityId)
                    .map(entity -> entity.getDocumentAttachments().stream()
                        .map(businessDocumentMapper::toDto)
                        .collect(Collectors.toSet()))
                    .orElse(new HashSet<>());
                    
            case "assetwarranty":
                return assetWarrantyRepository.findById(entityId)
                    .map(entity -> entity.getWarrantyAttachments().stream()
                        .map(businessDocumentMapper::toDto)
                        .collect(Collectors.toSet()))
                    .orElse(new HashSet<>());
                    
            case "leasemodelmetadata":
                return leaseModelMetadataRepository.findById(entityId)
                    .map(entity -> {
                        Set<BusinessDocumentDTO> docs = new HashSet<>();
                        if (entity.getModelAttachments() != null) {
                            docs.add(businessDocumentMapper.toDto(entity.getModelAttachments()));
                        }
                        return docs;
                    })
                    .orElse(new HashSet<>());
                    
            default:
                log.warn("Unsupported entity type for document attachments: {}", entityType);
                return new HashSet<>();
        }
    }

    @Override
    public boolean canAttachDocument(Long entityId, String entityType, Long documentId) {
        if (entityId == null || entityType == null || documentId == null) {
            return false;
        }
        
        return businessDocumentService.findOne(documentId).isPresent() && 
               entityExists(entityId, entityType);
    }

    @Override
    public BusinessDocumentDTO addDocumentAttachment(Long entityId, String entityType, BusinessDocumentDTO document) {
        log.debug("Request to add document attachment {} to {} with id {}", document.getId(), entityType, entityId);
        
        switch (entityType.toLowerCase()) {
            case "roumodelmetadata":
                return rouModelMetadataRepository.findById(entityId)
                    .map(entity -> {
                        entity.getDocumentAttachments().add(businessDocumentMapper.toEntity(document));
                        rouModelMetadataRepository.save(entity);
                        return document;
                    })
                    .orElse(null);
                    
            case "assetwarranty":
                return assetWarrantyRepository.findById(entityId)
                    .map(entity -> {
                        entity.getWarrantyAttachments().add(businessDocumentMapper.toEntity(document));
                        assetWarrantyRepository.save(entity);
                        return document;
                    })
                    .orElse(null);
                    
            case "leasemodelmetadata":
                return leaseModelMetadataRepository.findById(entityId)
                    .map(entity -> {
                        entity.setModelAttachments(businessDocumentMapper.toEntity(document));
                        leaseModelMetadataRepository.save(entity);
                        return document;
                    })
                    .orElse(null);
                    
            default:
                log.warn("Unsupported entity type for document attachments: {}", entityType);
                return null;
        }
    }

    @Override
    public boolean removeDocumentAttachment(Long entityId, String entityType, Long documentId) {
        log.debug("Request to remove document attachment {} from {} with id {}", documentId, entityType, entityId);
        
        switch (entityType.toLowerCase()) {
            case "roumodelmetadata":
                return rouModelMetadataRepository.findById(entityId)
                    .map(entity -> {
                        boolean removed = entity.getDocumentAttachments().removeIf(doc -> doc.getId().equals(documentId));
                        if (removed) {
                            rouModelMetadataRepository.save(entity);
                        }
                        return removed;
                    })
                    .orElse(false);
                    
            case "assetwarranty":
                return assetWarrantyRepository.findById(entityId)
                    .map(entity -> {
                        boolean removed = entity.getWarrantyAttachments().removeIf(doc -> doc.getId().equals(documentId));
                        if (removed) {
                            assetWarrantyRepository.save(entity);
                        }
                        return removed;
                    })
                    .orElse(false);
                    
            case "leasemodelmetadata":
                return leaseModelMetadataRepository.findById(entityId)
                    .map(entity -> {
                        if (entity.getModelAttachments() != null && entity.getModelAttachments().getId().equals(documentId)) {
                            entity.setModelAttachments(null);
                            leaseModelMetadataRepository.save(entity);
                            return true;
                        }
                        return false;
                    })
                    .orElse(false);
                    
            default:
                log.warn("Unsupported entity type for document attachments: {}", entityType);
                return false;
        }
    }

    private boolean entityExists(Long entityId, String entityType) {
        switch (entityType.toLowerCase()) {
            case "roumodelmetadata":
                return rouModelMetadataRepository.existsById(entityId);
            case "assetwarranty":
                return assetWarrantyRepository.existsById(entityId);
            case "leasemodelmetadata":
                return leaseModelMetadataRepository.existsById(entityId);
            default:
                return false;
        }
    }

    private String getCurrentUser() {
        return "system";
    }
}
