package io.github.erp.cqrs.financial.service;

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

import io.github.erp.cqrs.financial.readmodel.FinancialReportReadModel;
import io.github.erp.cqrs.financial.repositories.FinancialReportReadModelRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialReportCQRSServiceTest {

    @Mock
    private FinancialReportReadModelRepository financialReportReadModelRepository;

    private FinancialReportCQRSService financialReportCQRSService;

    private FinancialReportReadModel financialReportReadModel;

    @BeforeEach
    void setUp() {
        financialReportCQRSService = new FinancialReportCQRSService(financialReportReadModelRepository);
        
        financialReportReadModel = new FinancialReportReadModel()
            .id(1L)
            .transactionId(100L)
            .transactionType("Settlement")
            .transactionNumber("SET-001")
            .transactionDate(LocalDate.of(2024, 1, 15))
            .transactionAmount(new BigDecimal("10000.00"))
            .settlementAmount(new BigDecimal("10000.00"))
            .outstandingAmount(BigDecimal.ZERO)
            .currencyCode("USD")
            .dealerName("Test Dealer")
            .description("Test settlement")
            .reportingDate(LocalDate.of(2024, 1, 31))
            .fiscalYear(2024)
            .fiscalMonth(1)
            .lastUpdated(LocalDate.now());
    }

    @Test
    void findAllFinancialReports() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<FinancialReportReadModel> page = new PageImpl<>(Arrays.asList(financialReportReadModel));
        
        when(financialReportReadModelRepository.findAll(pageable)).thenReturn(page);
        
        Page<FinancialReportReadModel> result = financialReportCQRSService.findAllFinancialReports(pageable);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTransactionNumber()).isEqualTo("SET-001");
    }

    @Test
    void findFinancialReport() {
        when(financialReportReadModelRepository.findById(1L)).thenReturn(Optional.of(financialReportReadModel));
        
        Optional<FinancialReportReadModel> result = financialReportCQRSService.findFinancialReport(1L);
        
        assertThat(result).isPresent();
        assertThat(result.get().getTransactionNumber()).isEqualTo("SET-001");
    }

    @Test
    void findFinancialReportsByDate() {
        LocalDate reportingDate = LocalDate.of(2024, 1, 31);
        Pageable pageable = PageRequest.of(0, 10);
        Page<FinancialReportReadModel> page = new PageImpl<>(Arrays.asList(financialReportReadModel));
        
        when(financialReportReadModelRepository.findByReportingDate(reportingDate, pageable)).thenReturn(page);
        
        Page<FinancialReportReadModel> result = financialReportCQRSService.findFinancialReportsByDate(reportingDate, pageable);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getReportingDate()).isEqualTo(reportingDate);
    }

    @Test
    void findFinancialReportsByTransactionType() {
        String transactionType = "Settlement";
        Pageable pageable = PageRequest.of(0, 10);
        Page<FinancialReportReadModel> page = new PageImpl<>(Arrays.asList(financialReportReadModel));
        
        when(financialReportReadModelRepository.findByTransactionType(transactionType, pageable)).thenReturn(page);
        
        Page<FinancialReportReadModel> result = financialReportCQRSService.findFinancialReportsByTransactionType(transactionType, pageable);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTransactionType()).isEqualTo(transactionType);
    }

    @Test
    void getTotalTransactionAmountByDate() {
        LocalDate reportingDate = LocalDate.of(2024, 1, 31);
        BigDecimal expectedTotal = new BigDecimal("50000.00");
        
        when(financialReportReadModelRepository.getTotalTransactionAmountByDate(reportingDate)).thenReturn(expectedTotal);
        
        BigDecimal result = financialReportCQRSService.getTotalTransactionAmountByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void getTotalTransactionAmountByDateWhenNull() {
        LocalDate reportingDate = LocalDate.of(2024, 1, 31);
        
        when(financialReportReadModelRepository.getTotalTransactionAmountByDate(reportingDate)).thenReturn(null);
        
        BigDecimal result = financialReportCQRSService.getTotalTransactionAmountByDate(reportingDate);
        
        assertThat(result).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void getTransactionCountByDate() {
        LocalDate reportingDate = LocalDate.of(2024, 1, 31);
        Long expectedCount = 5L;
        
        when(financialReportReadModelRepository.getTransactionCountByDate(reportingDate)).thenReturn(expectedCount);
        
        Long result = financialReportCQRSService.getTransactionCountByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedCount);
    }

    @Test
    void getDistinctTransactionTypes() {
        List<String> expectedTypes = Arrays.asList("Settlement", "Payment", "Invoice");
        
        when(financialReportReadModelRepository.getDistinctTransactionTypes()).thenReturn(expectedTypes);
        
        List<String> result = financialReportCQRSService.getDistinctTransactionTypes();
        
        assertThat(result).isEqualTo(expectedTypes);
        assertThat(result).hasSize(3);
    }

    @Test
    void findFinancialReportsByTransactionId() {
        Long transactionId = 100L;
        List<FinancialReportReadModel> expectedReports = Arrays.asList(financialReportReadModel);
        
        when(financialReportReadModelRepository.findByTransactionId(transactionId)).thenReturn(expectedReports);
        
        List<FinancialReportReadModel> result = financialReportCQRSService.findFinancialReportsByTransactionId(transactionId);
        
        assertThat(result).isEqualTo(expectedReports);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTransactionId()).isEqualTo(transactionId);
    }
}
