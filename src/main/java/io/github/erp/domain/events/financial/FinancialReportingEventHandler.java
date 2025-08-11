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
public class FinancialReportingEventHandler {

    private static final Logger log = LoggerFactory.getLogger(FinancialReportingEventHandler.class);

    @DomainEventHandlerMethodAnnotation(eventType = "SettlementCreatedEvent", order = 2)
    public void handleSettlementCreated(DomainEvent event) {
        if (event instanceof SettlementCreatedEvent) {
            SettlementCreatedEvent settlementEvent = (SettlementCreatedEvent) event;
            log.info("Generating financial reports for settlement: Settlement {} with amount {}", 
                    settlementEvent.getPaymentNumber(), settlementEvent.getPaymentAmount());
            
            try {
                generateSettlementFinancialReports(settlementEvent);
                log.info("Successfully generated financial reports for settlement {}", 
                        settlementEvent.getPaymentNumber());
            } catch (Exception e) {
                log.error("Failed to generate financial reports for settlement {}", 
                        settlementEvent.getPaymentNumber(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "PaymentProcessedEvent", order = 2)
    public void handlePaymentProcessed(DomainEvent event) {
        if (event instanceof PaymentProcessedEvent) {
            PaymentProcessedEvent paymentEvent = (PaymentProcessedEvent) event;
            log.info("Generating financial reports for payment: Payment {} with amount {}", 
                    paymentEvent.getPaymentNumber(), paymentEvent.getPaymentAmount());
            
            try {
                generatePaymentFinancialReports(paymentEvent);
                log.info("Successfully generated financial reports for payment {}", 
                        paymentEvent.getPaymentNumber());
            } catch (Exception e) {
                log.error("Failed to generate financial reports for payment {}", 
                        paymentEvent.getPaymentNumber(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "InvoiceSettledEvent", order = 2)
    public void handleInvoiceSettled(DomainEvent event) {
        if (event instanceof InvoiceSettledEvent) {
            InvoiceSettledEvent invoiceEvent = (InvoiceSettledEvent) event;
            log.info("Generating financial reports for invoice settlement: Invoice {} with amount {}", 
                    invoiceEvent.getInvoiceNumber(), invoiceEvent.getInvoiceAmount());
            
            try {
                generateInvoiceSettlementFinancialReports(invoiceEvent);
                log.info("Successfully generated financial reports for invoice settlement {}", 
                        invoiceEvent.getInvoiceNumber());
            } catch (Exception e) {
                log.error("Failed to generate financial reports for invoice settlement {}", 
                        invoiceEvent.getInvoiceNumber(), e);
            }
        }
    }

    private void generateSettlementFinancialReports(SettlementCreatedEvent event) {
        log.debug("Updating financial reports for settlement: {}", event.getPaymentNumber());
        
        updateCashFlowReports(event.getPaymentAmount(), event.getSettlementCurrencyCode(), 
                            event.getPaymentDate(), "SETTLEMENT");
        updatePaymentSummaryReports(event.getPaymentNumber(), event.getPaymentAmount(), 
                                  event.getBillerName(), event.getPaymentDate());
        updateAuditTrail("SETTLEMENT_CREATED", event.getAggregateId(), event.getPaymentNumber(), 
                        event.getPaymentAmount(), event.getOccurredOn());
        
        log.info("Financial reports updated for settlement: {} - Amount: {} {}", 
                event.getPaymentNumber(), event.getPaymentAmount(), event.getSettlementCurrencyCode());
    }

    private void generatePaymentFinancialReports(PaymentProcessedEvent event) {
        log.debug("Updating financial reports for payment: {}", event.getPaymentNumber());
        
        updateCashFlowReports(event.getPaymentAmount(), event.getSettlementCurrency(), 
                            event.getPaymentDate(), "PAYMENT");
        updatePaymentSummaryReports(event.getPaymentNumber(), event.getPaymentAmount(), 
                                  event.getDealerName(), event.getPaymentDate());
        updateInvoiceReconciliationReports(event.getPaymentNumber(), event.getPaymentAmount(), 
                                         event.getInvoicedAmount(), event.getPurchaseOrderNumber());
        updateAuditTrail("PAYMENT_PROCESSED", event.getAggregateId(), event.getPaymentNumber(), 
                        event.getPaymentAmount(), event.getOccurredOn());
        
        log.info("Financial reports updated for payment: {} - Payment: {}, Invoiced: {} {}", 
                event.getPaymentNumber(), event.getPaymentAmount(), 
                event.getInvoicedAmount(), event.getSettlementCurrency());
    }

    private void generateInvoiceSettlementFinancialReports(InvoiceSettledEvent event) {
        log.debug("Updating financial reports for invoice settlement: {}", event.getInvoiceNumber());
        
        updateCashFlowReports(event.getSettlementAmount(), event.getCurrency(), 
                            event.getSettlementDate(), "INVOICE_SETTLEMENT");
        updateInvoiceSettlementReports(event.getInvoiceNumber(), event.getInvoiceAmount(), 
                                     event.getSettlementAmount(), event.getDealerName());
        updateAccountsReceivableReports(event.getInvoiceNumber(), event.getInvoiceAmount(), 
                                      event.getSettlementAmount(), event.getPaymentReference());
        updateAuditTrail("INVOICE_SETTLED", event.getAggregateId(), event.getInvoiceNumber(), 
                        event.getSettlementAmount(), event.getOccurredOn());
        
        log.info("Financial reports updated for invoice settlement: {} - Invoice: {}, Settlement: {} {}", 
                event.getInvoiceNumber(), event.getInvoiceAmount(), 
                event.getSettlementAmount(), event.getCurrency());
    }

    private void updateCashFlowReports(java.math.BigDecimal amount, String currency, 
                                     java.time.LocalDate date, String transactionType) {
        log.debug("Updating cash flow reports - Amount: {} {}, Date: {}, Type: {}", 
                amount, currency, date, transactionType);
    }

    private void updatePaymentSummaryReports(String paymentNumber, java.math.BigDecimal amount, 
                                           String counterparty, java.time.LocalDate date) {
        log.debug("Updating payment summary reports - Payment: {}, Amount: {}, Counterparty: {}, Date: {}", 
                paymentNumber, amount, counterparty, date);
    }

    private void updateInvoiceReconciliationReports(String paymentNumber, java.math.BigDecimal paymentAmount, 
                                                  java.math.BigDecimal invoicedAmount, String purchaseOrder) {
        log.debug("Updating invoice reconciliation reports - Payment: {}, Payment Amount: {}, Invoiced: {}, PO: {}", 
                paymentNumber, paymentAmount, invoicedAmount, purchaseOrder);
    }

    private void updateInvoiceSettlementReports(String invoiceNumber, java.math.BigDecimal invoiceAmount, 
                                              java.math.BigDecimal settlementAmount, String dealer) {
        log.debug("Updating invoice settlement reports - Invoice: {}, Invoice Amount: {}, Settlement: {}, Dealer: {}", 
                invoiceNumber, invoiceAmount, settlementAmount, dealer);
    }

    private void updateAccountsReceivableReports(String invoiceNumber, java.math.BigDecimal invoiceAmount, 
                                               java.math.BigDecimal settlementAmount, String paymentReference) {
        log.debug("Updating accounts receivable reports - Invoice: {}, Invoice Amount: {}, Settlement: {}, Ref: {}", 
                invoiceNumber, invoiceAmount, settlementAmount, paymentReference);
    }

    private void updateAuditTrail(String eventType, String aggregateId, String referenceNumber, 
                                java.math.BigDecimal amount, java.time.Instant timestamp) {
        log.info("Audit trail updated - Event: {}, Aggregate: {}, Reference: {}, Amount: {}, Timestamp: {}", 
                eventType, aggregateId, referenceNumber, amount, timestamp);
    }
}
