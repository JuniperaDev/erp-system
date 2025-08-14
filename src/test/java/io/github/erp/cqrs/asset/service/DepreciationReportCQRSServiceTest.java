package io.github.erp.cqrs.asset.service;

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

import io.github.erp.cqrs.asset.readmodel.DepreciationReportReadModel;
import io.github.erp.cqrs.asset.repositories.DepreciationReportReadModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepreciationReportCQRSServiceTest {

    @Mock
    private DepreciationReportReadModelRepository depreciationReportReadModelRepository;

    private DepreciationReportCQRSService depreciationReportCQRSService;

    private DepreciationReportReadModel depreciationReportReadModel;

    @BeforeEach
    void setUp() {
        depreciationReportCQRSService = new DepreciationReportCQRSService(depreciationReportReadModelRepository);
        
        depreciationReportReadModel = new DepreciationReportReadModel()
            .id(1L)
            .assetId(100L)
            .assetNumber("AST-001")
            .depreciationAmount(BigDecimal.valueOf(1000))
            .accumulatedDepreciation(BigDecimal.valueOf(5000))
            .netBookValue(BigDecimal.valueOf(5000))
            .depreciationPeriod("2024-01")
            .categoryName("Equipment")
            .fiscalYear(2024)
            .depreciationDate(LocalDate.now());
    }

    @Test
    void shouldFindAllDepreciationReports() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DepreciationReportReadModel> expectedPage = new PageImpl<>(Arrays.asList(depreciationReportReadModel));
        
        when(depreciationReportReadModelRepository.findAll(pageable)).thenReturn(expectedPage);
        
        Page<DepreciationReportReadModel> result = depreciationReportCQRSService.findAllDepreciationReports(pageable);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAssetNumber()).isEqualTo("AST-001");
    }

    @Test
    void shouldFindDepreciationReportById() {
        when(depreciationReportReadModelRepository.findById(1L)).thenReturn(Optional.of(depreciationReportReadModel));
        
        Optional<DepreciationReportReadModel> result = depreciationReportCQRSService.findDepreciationReport(1L);
        
        assertThat(result).isPresent();
        assertThat(result.get().getAssetNumber()).isEqualTo("AST-001");
    }

    @Test
    void shouldGetTotalDepreciationByPeriod() {
        String depreciationPeriod = "2024-01";
        BigDecimal expectedTotal = BigDecimal.valueOf(25000);
        
        when(depreciationReportReadModelRepository.getTotalDepreciationByPeriod(depreciationPeriod)).thenReturn(expectedTotal);
        
        BigDecimal result = depreciationReportCQRSService.getTotalDepreciationByPeriod(depreciationPeriod);
        
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void shouldGetDistinctDepreciationPeriods() {
        when(depreciationReportReadModelRepository.findDistinctDepreciationPeriods())
            .thenReturn(Arrays.asList("2024-01", "2024-02", "2024-03"));
        
        var result = depreciationReportCQRSService.getDistinctDepreciationPeriods();
        
        assertThat(result).hasSize(3);
        assertThat(result).contains("2024-01", "2024-02", "2024-03");
    }
}
