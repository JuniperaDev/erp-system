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

import io.github.erp.cqrs.lease.readmodel.LeaseReportReadModel;
import io.github.erp.cqrs.lease.repositories.LeaseReportReadModelRepository;
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
public class LeaseReportCQRSService {

    private final LeaseReportReadModelRepository leaseReportReadModelRepository;

    public LeaseReportCQRSService(LeaseReportReadModelRepository leaseReportReadModelRepository) {
        this.leaseReportReadModelRepository = leaseReportReadModelRepository;
    }

    public Page<LeaseReportReadModel> findAllLeaseReports(Pageable pageable) {
        return leaseReportReadModelRepository.findAll(pageable);
    }

    public Optional<LeaseReportReadModel> findLeaseReport(Long id) {
        return leaseReportReadModelRepository.findById(id);
    }

    public Page<LeaseReportReadModel> findLeaseReportsByDate(LocalDate reportingDate, Pageable pageable) {
        return leaseReportReadModelRepository.findByReportingDate(reportingDate, pageable);
    }

    public Page<LeaseReportReadModel> findLeaseReportsByStatus(String leaseStatus, Pageable pageable) {
        return leaseReportReadModelRepository.findByLeaseStatus(leaseStatus, pageable);
    }

    public Page<LeaseReportReadModel> findLeaseReportsByFiscalYear(Integer fiscalYear, Pageable pageable) {
        return leaseReportReadModelRepository.findByFiscalYear(fiscalYear, pageable);
    }

    public Page<LeaseReportReadModel> findLeaseReportsByFiscalQuarter(String fiscalQuarter, Pageable pageable) {
        return leaseReportReadModelRepository.findByFiscalQuarter(fiscalQuarter, pageable);
    }

    public Page<LeaseReportReadModel> findLeaseReportsByDateRange(
        LocalDate startDate, 
        LocalDate endDate, 
        Pageable pageable
    ) {
        return leaseReportReadModelRepository.findByReportingDateBetween(startDate, endDate, pageable);
    }

    public Page<LeaseReportReadModel> findLeaseReportsByStatusAndDate(
        String leaseStatus, 
        LocalDate reportingDate, 
        Pageable pageable
    ) {
        return leaseReportReadModelRepository.findByLeaseStatusAndReportingDate(leaseStatus, reportingDate, pageable);
    }

    public Page<LeaseReportReadModel> findLeaseReportsByFiscalYearAndStatus(
        Integer fiscalYear,
        String leaseStatus, 
        Pageable pageable
    ) {
        return leaseReportReadModelRepository.findByFiscalYearAndLeaseStatus(fiscalYear, leaseStatus, pageable);
    }

    public BigDecimal getTotalLeaseAmountByDate(LocalDate reportingDate) {
        return leaseReportReadModelRepository.getTotalLeaseAmountByReportingDate(reportingDate);
    }

    public BigDecimal getTotalLeaseAmountByStatusAndDate(String leaseStatus, LocalDate reportingDate) {
        return leaseReportReadModelRepository.getTotalLeaseAmountByStatusAndReportingDate(leaseStatus, reportingDate);
    }

    public BigDecimal getTotalLeaseAmountByFiscalYear(Integer fiscalYear) {
        return leaseReportReadModelRepository.getTotalLeaseAmountByFiscalYear(fiscalYear);
    }

    public BigDecimal getTotalLeaseAmountByFiscalYearAndStatus(Integer fiscalYear, String leaseStatus) {
        return leaseReportReadModelRepository.getTotalLeaseAmountByFiscalYearAndStatus(fiscalYear, leaseStatus);
    }

    public BigDecimal getTotalLeaseAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        return leaseReportReadModelRepository.getTotalLeaseAmountByDateRange(startDate, endDate);
    }

    public BigDecimal getTotalLeaseAmountByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, String leaseStatus) {
        return leaseReportReadModelRepository.getTotalLeaseAmountByDateRangeAndStatus(startDate, endDate, leaseStatus);
    }

    public BigDecimal getTotalLeaseAmountByDateRangeAndFiscalYear(LocalDate startDate, LocalDate endDate, Integer fiscalYear) {
        return leaseReportReadModelRepository.getTotalLeaseAmountByDateRangeAndFiscalYear(startDate, endDate, fiscalYear);
    }

    public Long getLeaseCountByDate(LocalDate reportingDate) {
        return leaseReportReadModelRepository.countByReportingDate(reportingDate);
    }

    public Long getLeaseCountByStatusAndDate(String leaseStatus, LocalDate reportingDate) {
        return leaseReportReadModelRepository.countByLeaseStatusAndReportingDate(leaseStatus, reportingDate);
    }

    public List<LocalDate> getDistinctReportingDates() {
        return leaseReportReadModelRepository.findDistinctReportingDates();
    }

    public List<String> getDistinctLeaseStatuses() {
        return leaseReportReadModelRepository.findDistinctLeaseStatuses();
    }

    public List<LeaseReportReadModel> findLeaseReportsByLeaseId(Long leaseId) {
        return leaseReportReadModelRepository.findByLeaseId(leaseId);
    }
}
