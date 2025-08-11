package io.github.erp.domain.events.lease;

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

import io.github.erp.domain.events.AbstractDomainEvent;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "lease_contract_created_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LeaseContractCreatedEvent extends AbstractDomainEvent {

    @Column(name = "booking_id")
    private String bookingId;

    @Column(name = "lease_title")
    private String leaseTitle;

    @Column(name = "short_title")
    private String shortTitle;

    @Column(name = "inception_date")
    private LocalDate inceptionDate;

    @Column(name = "commencement_date")
    private LocalDate commencementDate;

    @Column(name = "serial_number")
    private UUID serialNumber;

    @Column(name = "superintendent_service_outlet_id")
    private Long superintendentServiceOutletId;

    @Column(name = "main_dealer_id")
    private Long mainDealerId;

    @Column(name = "first_reporting_period_id")
    private Long firstReportingPeriodId;

    @Column(name = "last_reporting_period_id")
    private Long lastReportingPeriodId;

    protected LeaseContractCreatedEvent() {
        super();
    }

    public LeaseContractCreatedEvent(String leaseContractId, String bookingId, String leaseTitle,
                                   String shortTitle, LocalDate inceptionDate, LocalDate commencementDate, 
                                   UUID serialNumber, Long superintendentServiceOutletId, Long mainDealerId,
                                   Long firstReportingPeriodId, Long lastReportingPeriodId, UUID correlationId) {
        super(leaseContractId, "DetailedLeaseContract", correlationId);
        this.bookingId = bookingId;
        this.leaseTitle = leaseTitle;
        this.shortTitle = shortTitle;
        this.inceptionDate = inceptionDate;
        this.commencementDate = commencementDate;
        this.serialNumber = serialNumber;
        this.superintendentServiceOutletId = superintendentServiceOutletId;
        this.mainDealerId = mainDealerId;
        this.firstReportingPeriodId = firstReportingPeriodId;
        this.lastReportingPeriodId = lastReportingPeriodId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getLeaseTitle() {
        return leaseTitle;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public LocalDate getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(LocalDate inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public UUID getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(UUID serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getSuperintendentServiceOutletId() {
        return superintendentServiceOutletId;
    }

    public void setSuperintendentServiceOutletId(Long superintendentServiceOutletId) {
        this.superintendentServiceOutletId = superintendentServiceOutletId;
    }

    public Long getMainDealerId() {
        return mainDealerId;
    }

    public void setMainDealerId(Long mainDealerId) {
        this.mainDealerId = mainDealerId;
    }

    public Long getFirstReportingPeriodId() {
        return firstReportingPeriodId;
    }

    public void setFirstReportingPeriodId(Long firstReportingPeriodId) {
        this.firstReportingPeriodId = firstReportingPeriodId;
    }

    public Long getLastReportingPeriodId() {
        return lastReportingPeriodId;
    }

    public void setLastReportingPeriodId(Long lastReportingPeriodId) {
        this.lastReportingPeriodId = lastReportingPeriodId;
    }
}
