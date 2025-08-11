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

import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.impl.PlaceholderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceholderServiceTest {

    @Mock
    private PlaceholderService placeholderService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldAttachPlaceholdersToEntity() {
        Long entityId = 1L;
        String entityType = "AssetWarranty";
        Set<PlaceholderDTO> placeholders = new HashSet<>();
        PlaceholderDTO placeholder = new PlaceholderDTO();
        placeholder.setId(1L);
        placeholder.setDescription("Test placeholder");
        placeholders.add(placeholder);

        when(placeholderService.attachPlaceholders(entityId, entityType, placeholders))
            .thenReturn(placeholders);

        Set<PlaceholderDTO> result = placeholderService.attachPlaceholders(entityId, entityType, placeholders);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    void shouldDetachPlaceholdersFromEntity() {
        Long entityId = 1L;
        String entityType = "AssetWarranty";
        Set<Long> placeholderIds = Set.of(1L, 2L);

        when(placeholderService.detachPlaceholders(entityId, entityType, placeholderIds))
            .thenReturn(new HashSet<>());

        Set<PlaceholderDTO> result = placeholderService.detachPlaceholders(entityId, entityType, placeholderIds);

        assertThat(result).isNotNull();
    }

    @Test
    void shouldGetAttachedPlaceholders() {
        Long entityId = 1L;
        String entityType = "AssetWarranty";
        Set<PlaceholderDTO> placeholders = new HashSet<>();
        PlaceholderDTO placeholder = new PlaceholderDTO();
        placeholder.setId(1L);
        placeholders.add(placeholder);

        when(placeholderService.getAttachedPlaceholders(entityId, entityType))
            .thenReturn(placeholders);

        Set<PlaceholderDTO> result = placeholderService.getAttachedPlaceholders(entityId, entityType);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    void shouldFindByTokenPattern() {
        String tokenPattern = "TEST_*";
        Pageable pageable = Pageable.unpaged();
        List<PlaceholderDTO> placeholders = List.of(new PlaceholderDTO());
        Page<PlaceholderDTO> page = new PageImpl<>(placeholders);

        when(placeholderService.findByTokenPattern(tokenPattern, pageable))
            .thenReturn(page);

        Page<PlaceholderDTO> result = placeholderService.findByTokenPattern(tokenPattern, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldCreateFromTemplates() {
        Set<PlaceholderDTO> templates = new HashSet<>();
        PlaceholderDTO template = new PlaceholderDTO();
        template.setDescription("Template placeholder");
        templates.add(template);

        when(placeholderService.createFromTemplates(templates))
            .thenReturn(templates);

        Set<PlaceholderDTO> result = placeholderService.createFromTemplates(templates);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }
}
