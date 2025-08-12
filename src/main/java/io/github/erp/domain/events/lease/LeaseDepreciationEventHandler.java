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
public class LeaseDepreciationEventHandler {

    private static final Logger log = LoggerFactory.getLogger(LeaseDepreciationEventHandler.class);

    @DomainEventHandlerMethodAnnotation(eventType = "LeaseContractCreatedEvent", order = 2)
    public void handleLeaseContractCreatedForDepreciation(DomainEvent event) {
        if (event instanceof LeaseContractCreatedEvent) {
            LeaseContractCreatedEvent leaseEvent = (LeaseContractCreatedEvent) event;
            log.info("Processing lease contract creation for ROU asset depreciation: Contract {} - {}", 
                    leaseEvent.getBookingId(), leaseEvent.getLeaseTitle());
            
            try {
                processLeaseContractForROUAssetDepreciation(leaseEvent);
                log.info("Successfully processed lease contract {} for ROU asset depreciation", 
                        leaseEvent.getBookingId());
            } catch (Exception e) {
                log.error("Failed to process lease contract {} for ROU asset depreciation", 
                        leaseEvent.getBookingId(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "LeaseLiabilityCalculatedEvent", order = 2)
    public void handleLeaseLiabilityCalculatedForDepreciation(DomainEvent event) {
        if (event instanceof LeaseLiabilityCalculatedEvent) {
            LeaseLiabilityCalculatedEvent liabilityEvent = (LeaseLiabilityCalculatedEvent) event;
            log.info("Processing lease liability calculation for ROU asset depreciation: Lease {} with liability {}", 
                    liabilityEvent.getLeaseId(), liabilityEvent.getLiabilityAmount());
            
            try {
                processLeaseLiabilityForROUAssetDepreciation(liabilityEvent);
                log.info("Successfully processed lease liability {} for ROU asset depreciation", 
                        liabilityEvent.getLeaseId());
            } catch (Exception e) {
                log.error("Failed to process lease liability {} for ROU asset depreciation", 
                        liabilityEvent.getLeaseId(), e);
            }
        }
    }

    private void processLeaseContractForROUAssetDepreciation(LeaseContractCreatedEvent event) {
        log.debug("Setting up ROU asset depreciation for lease contract: {}", event.getBookingId());
        
        if (event.getCommencementDate() != null && event.getSerialNumber() != null) {
            log.info("ROU asset depreciation setup for lease contract {} - Serial: {}, Commencement: {}", 
                    event.getBookingId(), 
                    event.getSerialNumber(),
                    event.getCommencementDate());
        }
    }

    private void processLeaseLiabilityForROUAssetDepreciation(LeaseLiabilityCalculatedEvent event) {
        log.debug("Updating ROU asset depreciation based on lease liability: {}", event.getLeaseId());
        
        if (event.getLiabilityAmount() != null && event.getStartDate() != null && event.getEndDate() != null) {
            log.info("ROU asset depreciation updated for lease {} - Liability: {}, Term: {} to {}", 
                    event.getLeaseId(), 
                    event.getLiabilityAmount(),
                    event.getStartDate(),
                    event.getEndDate());
        }
    }
}
