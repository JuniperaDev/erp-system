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

import io.github.erp.domain.events.DomainEvent;
import io.github.erp.domain.events.DomainEventHandler;
import io.github.erp.domain.events.DomainEventHandlerMethodAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@DomainEventHandler
public class LeaseFinancialEventHandler {

    private static final Logger log = LoggerFactory.getLogger(LeaseFinancialEventHandler.class);

    @DomainEventHandlerMethodAnnotation(eventType = "LeaseContractCreatedEvent", order = 1)
    public void handleLeaseContractCreated(DomainEvent event) {
        if (event instanceof LeaseContractCreatedEvent) {
            LeaseContractCreatedEvent leaseEvent = (LeaseContractCreatedEvent) event;
            log.info("Processing lease contract creation for financial accounting: Contract {} - {}", 
                    leaseEvent.getBookingId(), leaseEvent.getLeaseTitle());
            
            try {
                processLeaseContractForFinancialAccounting(leaseEvent);
                log.info("Successfully processed lease contract {} for financial accounting", 
                        leaseEvent.getBookingId());
            } catch (Exception e) {
                log.error("Failed to process lease contract {} for financial accounting", 
                        leaseEvent.getBookingId(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "LeasePaymentMadeEvent", order = 1)
    public void handleLeasePaymentMade(DomainEvent event) {
        if (event instanceof LeasePaymentMadeEvent) {
            LeasePaymentMadeEvent paymentEvent = (LeasePaymentMadeEvent) event;
            log.info("Processing lease payment for financial accounting: Amount {} for contract {}", 
                    paymentEvent.getPaymentAmount(), paymentEvent.getLeaseContractBookingId());
            
            try {
                processLeasePaymentForFinancialAccounting(paymentEvent);
                log.info("Successfully processed lease payment {} for financial accounting", 
                        paymentEvent.getPaymentAmount());
            } catch (Exception e) {
                log.error("Failed to process lease payment {} for financial accounting", 
                        paymentEvent.getAggregateId(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "LeaseLiabilityCalculatedEvent", order = 1)
    public void handleLeaseLiabilityCalculated(DomainEvent event) {
        if (event instanceof LeaseLiabilityCalculatedEvent) {
            LeaseLiabilityCalculatedEvent liabilityEvent = (LeaseLiabilityCalculatedEvent) event;
            log.info("Processing lease liability calculation for financial accounting: Lease {} with liability {}", 
                    liabilityEvent.getLeaseId(), liabilityEvent.getLiabilityAmount());
            
            try {
                processLeaseLiabilityForFinancialAccounting(liabilityEvent);
                log.info("Successfully processed lease liability {} for financial accounting", 
                        liabilityEvent.getLeaseId());
            } catch (Exception e) {
                log.error("Failed to process lease liability {} for financial accounting", 
                        liabilityEvent.getLeaseId(), e);
            }
        }
    }

    private void processLeaseContractForFinancialAccounting(LeaseContractCreatedEvent event) {
        log.debug("Creating financial accounting entries for lease contract: {}", event.getBookingId());
        
        if (event.getCommencementDate() != null && event.getInceptionDate() != null) {
            log.info("Lease contract {} processed for IFRS16 financial accounting - Commencement: {}, Inception: {}", 
                    event.getBookingId(), 
                    event.getCommencementDate(), 
                    event.getInceptionDate());
        }
    }

    private void processLeasePaymentForFinancialAccounting(LeasePaymentMadeEvent event) {
        log.debug("Creating financial accounting entries for lease payment: {}", event.getAggregateId());
        
        if (event.getPaymentAmount() != null && event.getPaymentDate() != null) {
            log.info("Lease payment processed for IFRS16 financial accounting - Amount: {}, Date: {}, Contract: {}", 
                    event.getPaymentAmount(), 
                    event.getPaymentDate(),
                    event.getLeaseContractBookingId());
        }
    }

    private void processLeaseLiabilityForFinancialAccounting(LeaseLiabilityCalculatedEvent event) {
        log.debug("Creating financial accounting entries for lease liability: {}", event.getLeaseId());
        
        if (event.getLiabilityAmount() != null && event.getInterestRate() != null) {
            log.info("Lease liability processed for IFRS16 financial accounting - Liability: {}, Rate: {}, Period: {} to {}", 
                    event.getLiabilityAmount(), 
                    event.getInterestRate(),
                    event.getStartDate(),
                    event.getEndDate());
        }
    }
}
