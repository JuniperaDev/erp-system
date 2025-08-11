package io.github.erp.domain.events.financial;

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
public class FinancialAssetEventHandler {

    private static final Logger log = LoggerFactory.getLogger(FinancialAssetEventHandler.class);

    @DomainEventHandlerMethodAnnotation(eventType = "SettlementCreatedEvent", order = 1)
    public void handleSettlementCreated(DomainEvent event) {
        if (event instanceof SettlementCreatedEvent) {
            SettlementCreatedEvent settlementEvent = (SettlementCreatedEvent) event;
            log.info("Processing settlement for asset tracking: Settlement {} with amount {}", 
                    settlementEvent.getPaymentNumber(), settlementEvent.getPaymentAmount());
            
            try {
                processSettlementForAssetTracking(settlementEvent);
                log.info("Successfully processed settlement {} for asset tracking", 
                        settlementEvent.getPaymentNumber());
            } catch (Exception e) {
                log.error("Failed to process settlement {} for asset tracking", 
                        settlementEvent.getPaymentNumber(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "PaymentProcessedEvent", order = 1)
    public void handlePaymentProcessed(DomainEvent event) {
        if (event instanceof PaymentProcessedEvent) {
            PaymentProcessedEvent paymentEvent = (PaymentProcessedEvent) event;
            log.info("Processing payment for asset acquisition tracking: Payment {} with amount {}", 
                    paymentEvent.getPaymentNumber(), paymentEvent.getPaymentAmount());
            
            try {
                processPaymentForAssetAcquisition(paymentEvent);
                log.info("Successfully processed payment {} for asset acquisition tracking", 
                        paymentEvent.getPaymentNumber());
            } catch (Exception e) {
                log.error("Failed to process payment {} for asset acquisition tracking", 
                        paymentEvent.getPaymentNumber(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "InvoiceSettledEvent", order = 1)
    public void handleInvoiceSettled(DomainEvent event) {
        if (event instanceof InvoiceSettledEvent) {
            InvoiceSettledEvent invoiceEvent = (InvoiceSettledEvent) event;
            log.info("Processing invoice settlement for asset tracking: Invoice {} with amount {}", 
                    invoiceEvent.getInvoiceNumber(), invoiceEvent.getInvoiceAmount());
            
            try {
                processInvoiceSettlementForAssetTracking(invoiceEvent);
                log.info("Successfully processed invoice settlement {} for asset tracking", 
                        invoiceEvent.getInvoiceNumber());
            } catch (Exception e) {
                log.error("Failed to process invoice settlement {} for asset tracking", 
                        invoiceEvent.getInvoiceNumber(), e);
            }
        }
    }

    private void processSettlementForAssetTracking(SettlementCreatedEvent event) {
        log.debug("Updating asset acquisition records for settlement: {}", event.getPaymentNumber());
        
        if (event.getPaymentAmount() != null && event.getPaymentAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            log.info("Settlement {} processed for asset tracking - Amount: {} {}", 
                    event.getPaymentNumber(), 
                    event.getPaymentAmount(), 
                    event.getSettlementCurrencyCode());
        }
    }

    private void processPaymentForAssetAcquisition(PaymentProcessedEvent event) {
        log.debug("Tracking asset acquisition for payment: {}", event.getPaymentNumber());
        
        if (event.getPaymentAmount() != null && event.getInvoicedAmount() != null) {
            log.info("Payment {} processed for asset acquisition - Payment: {}, Invoiced: {} {}", 
                    event.getPaymentNumber(), 
                    event.getPaymentAmount(), 
                    event.getInvoicedAmount(),
                    event.getSettlementCurrency());
        }
    }

    private void processInvoiceSettlementForAssetTracking(InvoiceSettledEvent event) {
        log.debug("Processing invoice settlement for asset tracking: {}", event.getInvoiceNumber());
        
        if (event.getInvoiceAmount() != null && event.getSettlementAmount() != null) {
            log.info("Invoice settlement {} processed for asset tracking - Invoice: {}, Settlement: {} {}", 
                    event.getInvoiceNumber(), 
                    event.getInvoiceAmount(), 
                    event.getSettlementAmount(),
                    event.getCurrency());
        }
    }
}
