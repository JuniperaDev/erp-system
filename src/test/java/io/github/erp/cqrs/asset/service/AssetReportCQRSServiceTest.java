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

import io.github.erp.cqrs.asset.readmodel.AssetReportReadModel;
import io.github.erp.cqrs.asset.repositories.AssetReportReadModelRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetReportCQRSServiceTest {

    @Mock
    private AssetReportReadModelRepository assetReportReadModelRepository;

    private AssetReportCQRSService assetReportCQRSService;

    private AssetReportReadModel assetReportReadModel;

    @BeforeEach
    void setUp() {
        assetReportCQRSService = new AssetReportCQRSService(assetReportReadModelRepository);
        
        assetReportReadModel = new AssetReportReadModel()
            .id(1L)
            .assetId(100L)
            .assetNumber("AST-001")
            .assetTag("TAG-001")
            .assetDescription("Test Asset")
            .assetCost(BigDecimal.valueOf(10000))
            .currentNBV(BigDecimal.valueOf(8000))
            .categoryName("Equipment")
            .dealerName("Test Dealer")
            .reportingDate(LocalDate.now())
            .lastUpdated(LocalDate.now());
    }

    @Test
    void shouldFindAllAssetReports() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AssetReportReadModel> expectedPage = new PageImpl<>(Arrays.asList(assetReportReadModel));
        
        when(assetReportReadModelRepository.findAll(pageable)).thenReturn(expectedPage);
        
        Page<AssetReportReadModel> result = assetReportCQRSService.findAllAssetReports(pageable);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getAssetNumber()).isEqualTo("AST-001");
    }

    @Test
    void shouldFindAssetReportById() {
        when(assetReportReadModelRepository.findById(1L)).thenReturn(Optional.of(assetReportReadModel));
        
        Optional<AssetReportReadModel> result = assetReportCQRSService.findAssetReport(1L);
        
        assertThat(result).isPresent();
        assertThat(result.get().getAssetNumber()).isEqualTo("AST-001");
    }

    @Test
    void shouldFindAssetReportsByDate() {
        LocalDate reportingDate = LocalDate.now();
        Pageable pageable = PageRequest.of(0, 10);
        Page<AssetReportReadModel> expectedPage = new PageImpl<>(Arrays.asList(assetReportReadModel));
        
        when(assetReportReadModelRepository.findByReportingDate(reportingDate, pageable)).thenReturn(expectedPage);
        
        Page<AssetReportReadModel> result = assetReportCQRSService.findAssetReportsByDate(reportingDate, pageable);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldGetTotalNBVByDate() {
        LocalDate reportingDate = LocalDate.now();
        BigDecimal expectedTotal = BigDecimal.valueOf(50000);
        
        when(assetReportReadModelRepository.getTotalNBVByReportingDate(reportingDate)).thenReturn(expectedTotal);
        
        BigDecimal result = assetReportCQRSService.getTotalNBVByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedTotal);
    }
}
