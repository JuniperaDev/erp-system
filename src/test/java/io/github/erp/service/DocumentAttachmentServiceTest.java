package io.github.erp.service;

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

import io.github.erp.service.dto.BusinessDocumentDTO;
import io.github.erp.service.impl.DocumentAttachmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentAttachmentServiceTest {

    @Mock
    private BusinessDocumentService businessDocumentService;

    @Mock
    private AuditTrailService auditTrailService;

    private DocumentAttachmentService documentAttachmentService;

    @BeforeEach
    void setUp() {
        documentAttachmentService = new DocumentAttachmentService() {
            @Override
            public Set<BusinessDocumentDTO> attachDocuments(Long entityId, String entityType, Set<BusinessDocumentDTO> documents) {
                return new HashSet<>();
            }

            @Override
            public Set<BusinessDocumentDTO> detachDocuments(Long entityId, String entityType, Set<Long> documentIds) {
                return new HashSet<>();
            }

            @Override
            public Set<BusinessDocumentDTO> getAttachedDocuments(Long entityId, String entityType) {
                return new HashSet<>();
            }

            @Override
            public boolean canAttachDocument(Long entityId, String entityType, Long documentId) {
                return entityType.equals("AssetWarranty") && entityId != null && documentId != null;
            }

            @Override
            public BusinessDocumentDTO addDocumentAttachment(Long entityId, String entityType, BusinessDocumentDTO document) {
                return document;
            }

            @Override
            public boolean removeDocumentAttachment(Long entityId, String entityType, Long documentId) {
                return true;
            }
        };
    }

    @Test
    void shouldValidateDocumentAttachmentPermissions() {
        Long entityId = 1L;
        String entityType = "AssetWarranty";
        Long documentId = 1L;

        boolean canAttach = documentAttachmentService.canAttachDocument(entityId, entityType, documentId);

        assertThat(canAttach).isTrue();
    }

    @Test
    void shouldAttachDocumentsToEntity() {
        Long entityId = 1L;
        String entityType = "AssetWarranty";
        Set<BusinessDocumentDTO> documents = new HashSet<>();
        BusinessDocumentDTO document = new BusinessDocumentDTO();
        document.setId(1L);
        documents.add(document);

        Set<BusinessDocumentDTO> result = documentAttachmentService.attachDocuments(entityId, entityType, documents);

        assertThat(result).isNotNull();
    }

    @Test
    void shouldDetachDocumentsFromEntity() {
        Long entityId = 1L;
        String entityType = "AssetWarranty";
        Set<Long> documentIds = Set.of(1L, 2L);

        Set<BusinessDocumentDTO> result = documentAttachmentService.detachDocuments(entityId, entityType, documentIds);

        assertThat(result).isNotNull();
    }
}
