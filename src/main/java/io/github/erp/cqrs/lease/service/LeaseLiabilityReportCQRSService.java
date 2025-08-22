package io.github.erp.cqrs.lease.service;

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

import io.github.erp.cqrs.lease.readmodel.LeaseLiabilityReportReadModel;
import io.github.erp.cqrs.lease.repositories.LeaseLiabilityReportReadModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LeaseLiabilityReportCQRSService {

    private final LeaseLiabilityReportReadModelRepository leaseLiabilityReportReadModelRepository;

    public LeaseLiabilityReportCQRSService(LeaseLiabilityReportReadModelRepository leaseLiabilityReportReadModelRepository) {
        this.leaseLiabilityReportReadModelRepository = leaseLiabilityReportReadModelRepository;
    }

    public Page<LeaseLiabilityReportReadModel> findAllLeaseLiabilityReports(Pageable pageable) {
        return leaseLiabilityReportReadModelRepository.findAll(pageable);
    }

    public Optional<LeaseLiabilityReportReadModel> findLeaseLiabilityReport(Long id) {
        return leaseLiabilityReportReadModelRepository.findById(id);
    }

    public Page<LeaseLiabilityReportReadModel> findLeaseLiabilityReportsByDate(LocalDate reportingDate, Pageable pageable) {
        return leaseLiabilityReportReadModelRepository.findByReportingDate(reportingDate, pageable);
    }

    public Page<LeaseLiabilityReportReadModel> findLeaseLiabilityReportsByFiscalYear(Integer fiscalYear, Pageable pageable) {
        return leaseLiabilityReportReadModelRepository.findByFiscalYear(fiscalYear, pageable);
    }

    public Page<LeaseLiabilityReportReadModel> findLeaseLiabilityReportsByFiscalQuarter(String fiscalQuarter, Pageable pageable) {
        return leaseLiabilityReportReadModelRepository.findByFiscalQuarter(fiscalQuarter, pageable);
    }

    public Page<LeaseLiabilityReportReadModel> findLeaseLiabilityReportsByDateRange(
        LocalDate startDate, 
        LocalDate endDate, 
        Pageable pageable
    ) {
        return leaseLiabilityReportReadModelRepository.findByReportingDateBetween(startDate, endDate, pageable);
    }

    public Page<LeaseLiabilityReportReadModel> findLeaseLiabilityReportsByFiscalYearAndDate(
        Integer fiscalYear,
        LocalDate reportingDate, 
        Pageable pageable
    ) {
        return leaseLiabilityReportReadModelRepository.findByFiscalYearAndReportingDate(fiscalYear, reportingDate, pageable);
    }

    public BigDecimal getTotalOpeningBalanceByDate(LocalDate reportingDate) {
        return leaseLiabilityReportReadModelRepository.getTotalOpeningBalanceByReportingDate(reportingDate);
    }

    public BigDecimal getTotalCashPaymentByDate(LocalDate reportingDate) {
        return leaseLiabilityReportReadModelRepository.getTotalCashPaymentByReportingDate(reportingDate);
    }

    public BigDecimal getTotalPrincipalPaymentByDate(LocalDate reportingDate) {
        return leaseLiabilityReportReadModelRepository.getTotalPrincipalPaymentByReportingDate(reportingDate);
    }

    public BigDecimal getTotalInterestPaymentByDate(LocalDate reportingDate) {
        return leaseLiabilityReportReadModelRepository.getTotalInterestPaymentByReportingDate(reportingDate);
    }

    public BigDecimal getTotalOutstandingBalanceByDate(LocalDate reportingDate) {
        return leaseLiabilityReportReadModelRepository.getTotalOutstandingBalanceByReportingDate(reportingDate);
    }

    public BigDecimal getTotalOpeningBalanceByFiscalYear(Integer fiscalYear) {
        return leaseLiabilityReportReadModelRepository.getTotalOpeningBalanceByFiscalYear(fiscalYear);
    }

    public BigDecimal getTotalCashPaymentByFiscalYear(Integer fiscalYear) {
        return leaseLiabilityReportReadModelRepository.getTotalCashPaymentByFiscalYear(fiscalYear);
    }

    public BigDecimal getTotalPrincipalPaymentByFiscalYear(Integer fiscalYear) {
        return leaseLiabilityReportReadModelRepository.getTotalPrincipalPaymentByFiscalYear(fiscalYear);
    }

    public BigDecimal getTotalInterestPaymentByFiscalYear(Integer fiscalYear) {
        return leaseLiabilityReportReadModelRepository.getTotalInterestPaymentByFiscalYear(fiscalYear);
    }

    public BigDecimal getTotalOutstandingBalanceByFiscalYear(Integer fiscalYear) {
        return leaseLiabilityReportReadModelRepository.getTotalOutstandingBalanceByFiscalYear(fiscalYear);
    }

    public Long getLeaseLiabilityCountByDate(LocalDate reportingDate) {
        return leaseLiabilityReportReadModelRepository.countByReportingDate(reportingDate);
    }

    public List<LocalDate> getDistinctReportingDates() {
        return leaseLiabilityReportReadModelRepository.findDistinctReportingDates();
    }

    public List<LeaseLiabilityReportReadModel> findLeaseLiabilityReportsByLeaseId(Long leaseId) {
        return leaseLiabilityReportReadModelRepository.findByLeaseId(leaseId);
    }
}
