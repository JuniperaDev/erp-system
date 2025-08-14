package io.github.erp.cqrs.financial.eventhandlers;

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
import io.github.erp.domain.events.DomainEvent;
import io.github.erp.domain.events.DomainEventHandler;
import io.github.erp.domain.events.DomainEventHandlerMethodAnnotation;
import io.github.erp.domain.events.financial.SettlementCreatedEvent;
import io.github.erp.domain.events.financial.PaymentProcessedEvent;
import io.github.erp.domain.events.financial.InvoiceSettledEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Component
@DomainEventHandler
public class FinancialReportEventHandler {

    private static final Logger log = LoggerFactory.getLogger(FinancialReportEventHandler.class);

    private final FinancialReportReadModelRepository financialReportReadModelRepository;

    public FinancialReportEventHandler(FinancialReportReadModelRepository financialReportReadModelRepository) {
        this.financialReportReadModelRepository = financialReportReadModelRepository;
    }

    @DomainEventHandlerMethodAnnotation(eventType = "SettlementCreatedEvent", order = 3)
    @Transactional
    public void handleSettlementCreated(DomainEvent event) {
        if (event instanceof SettlementCreatedEvent) {
            SettlementCreatedEvent settlementEvent = (SettlementCreatedEvent) event;
            log.info("Processing settlement created event for financial reporting: {}", settlementEvent.getPaymentNumber());
            
            try {
                createOrUpdateFinancialReportFromSettlement(settlementEvent);
                log.info("Successfully processed financial report for settlement: {}", settlementEvent.getPaymentNumber());
            } catch (Exception e) {
                log.error("Failed to process financial report for settlement: {}", settlementEvent.getPaymentNumber(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "PaymentProcessedEvent", order = 3)
    @Transactional
    public void handlePaymentProcessed(DomainEvent event) {
        if (event instanceof PaymentProcessedEvent) {
            PaymentProcessedEvent paymentEvent = (PaymentProcessedEvent) event;
            log.info("Processing payment processed event for financial reporting: {}", paymentEvent.getPaymentNumber());
            
            try {
                createOrUpdateFinancialReportFromPayment(paymentEvent);
                log.info("Successfully processed financial report for payment: {}", paymentEvent.getPaymentNumber());
            } catch (Exception e) {
                log.error("Failed to process financial report for payment: {}", paymentEvent.getPaymentNumber(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "InvoiceSettledEvent", order = 3)
    @Transactional
    public void handleInvoiceSettled(DomainEvent event) {
        if (event instanceof InvoiceSettledEvent) {
            InvoiceSettledEvent invoiceEvent = (InvoiceSettledEvent) event;
            log.info("Processing invoice settled event for financial reporting: {}", invoiceEvent.getInvoiceNumber());
            
            try {
                createOrUpdateFinancialReportFromInvoice(invoiceEvent);
                log.info("Successfully processed financial report for invoice: {}", invoiceEvent.getInvoiceNumber());
            } catch (Exception e) {
                log.error("Failed to process financial report for invoice: {}", invoiceEvent.getInvoiceNumber(), e);
            }
        }
    }

    private void createOrUpdateFinancialReportFromSettlement(SettlementCreatedEvent event) {
        Long settlementId = Long.parseLong(event.getAggregateId());
        
        Optional<FinancialReportReadModel> existingReadModel = 
            financialReportReadModelRepository.findByTransactionId(settlementId)
                .stream()
                .filter(model -> "Settlement".equals(model.getTransactionType()))
                .findFirst();

        FinancialReportReadModel readModel;
        if (existingReadModel.isPresent()) {
            readModel = existingReadModel.get();
            updateFinancialReportFromSettlement(readModel, event);
        } else {
            readModel = createNewFinancialReportFromSettlement(event);
        }

        financialReportReadModelRepository.save(readModel);
    }

    private void createOrUpdateFinancialReportFromPayment(PaymentProcessedEvent event) {
        Long paymentId = Long.parseLong(event.getAggregateId());
        
        Optional<FinancialReportReadModel> existingReadModel = 
            financialReportReadModelRepository.findByTransactionId(paymentId)
                .stream()
                .filter(model -> "Payment".equals(model.getTransactionType()))
                .findFirst();

        FinancialReportReadModel readModel;
        if (existingReadModel.isPresent()) {
            readModel = existingReadModel.get();
            updateFinancialReportFromPayment(readModel, event);
        } else {
            readModel = createNewFinancialReportFromPayment(event);
        }

        financialReportReadModelRepository.save(readModel);
    }

    private void createOrUpdateFinancialReportFromInvoice(InvoiceSettledEvent event) {
        Long invoiceId = Long.parseLong(event.getAggregateId());
        
        Optional<FinancialReportReadModel> existingReadModel = 
            financialReportReadModelRepository.findByTransactionId(invoiceId)
                .stream()
                .filter(model -> "Invoice".equals(model.getTransactionType()))
                .findFirst();

        FinancialReportReadModel readModel;
        if (existingReadModel.isPresent()) {
            readModel = existingReadModel.get();
            updateFinancialReportFromInvoice(readModel, event);
        } else {
            readModel = createNewFinancialReportFromInvoice(event);
        }

        financialReportReadModelRepository.save(readModel);
    }

    private FinancialReportReadModel createNewFinancialReportFromSettlement(SettlementCreatedEvent event) {
        Long settlementId = Long.parseLong(event.getAggregateId());
        
        return new FinancialReportReadModel()
            .transactionId(settlementId)
            .transactionType("Settlement")
            .transactionNumber(event.getPaymentNumber())
            .transactionDate(event.getPaymentDate())
            .transactionAmount(event.getPaymentAmount())
            .settlementAmount(event.getPaymentAmount())
            .outstandingAmount(BigDecimal.ZERO)
            .currencyCode(event.getSettlementCurrencyCode())
            .dealerName(event.getBillerName())
            .description(event.getDescription())
            .reportingDate(LocalDate.now())
            .fiscalYear(event.getPaymentDate() != null ? event.getPaymentDate().getYear() : LocalDate.now().getYear())
            .fiscalMonth(event.getPaymentDate() != null ? event.getPaymentDate().getMonthValue() : LocalDate.now().getMonthValue())
            .lastUpdated(LocalDate.now());
    }

    private FinancialReportReadModel createNewFinancialReportFromPayment(PaymentProcessedEvent event) {
        Long paymentId = Long.parseLong(event.getAggregateId());
        BigDecimal outstandingAmount = event.getInvoicedAmount() != null && event.getPaymentAmount() != null ?
            event.getInvoicedAmount().subtract(event.getPaymentAmount()) : BigDecimal.ZERO;
        
        return new FinancialReportReadModel()
            .transactionId(paymentId)
            .transactionType("Payment")
            .transactionNumber(event.getPaymentNumber())
            .transactionDate(event.getPaymentDate())
            .transactionAmount(event.getInvoicedAmount())
            .settlementAmount(event.getPaymentAmount())
            .outstandingAmount(outstandingAmount)
            .currencyCode(event.getSettlementCurrency())
            .dealerName(event.getDealerName())
            .paymentReference(event.getPurchaseOrderNumber())
            .description(event.getDescription())
            .reportingDate(LocalDate.now())
            .fiscalYear(event.getPaymentDate() != null ? event.getPaymentDate().getYear() : LocalDate.now().getYear())
            .fiscalMonth(event.getPaymentDate() != null ? event.getPaymentDate().getMonthValue() : LocalDate.now().getMonthValue())
            .lastUpdated(LocalDate.now());
    }

    private FinancialReportReadModel createNewFinancialReportFromInvoice(InvoiceSettledEvent event) {
        Long invoiceId = Long.parseLong(event.getAggregateId());
        BigDecimal outstandingAmount = event.getInvoiceAmount() != null && event.getSettlementAmount() != null ?
            event.getInvoiceAmount().subtract(event.getSettlementAmount()) : event.getInvoiceAmount();
        
        return new FinancialReportReadModel()
            .transactionId(invoiceId)
            .transactionType("Invoice")
            .transactionNumber(event.getInvoiceNumber())
            .transactionDate(event.getInvoiceDate())
            .transactionAmount(event.getInvoiceAmount())
            .settlementAmount(event.getSettlementAmount())
            .outstandingAmount(outstandingAmount)
            .currencyCode(event.getCurrency())
            .dealerName(event.getDealerName())
            .paymentReference(event.getPaymentReference())
            .reportingDate(LocalDate.now())
            .fiscalYear(event.getInvoiceDate() != null ? event.getInvoiceDate().getYear() : LocalDate.now().getYear())
            .fiscalMonth(event.getInvoiceDate() != null ? event.getInvoiceDate().getMonthValue() : LocalDate.now().getMonthValue())
            .lastUpdated(LocalDate.now());
    }

    private void updateFinancialReportFromSettlement(FinancialReportReadModel readModel, SettlementCreatedEvent event) {
        readModel.transactionAmount(event.getPaymentAmount())
                 .settlementAmount(event.getPaymentAmount())
                 .outstandingAmount(BigDecimal.ZERO)
                 .currencyCode(event.getSettlementCurrencyCode())
                 .dealerName(event.getBillerName())
                 .description(event.getDescription())
                 .lastUpdated(LocalDate.now());
    }

    private void updateFinancialReportFromPayment(FinancialReportReadModel readModel, PaymentProcessedEvent event) {
        BigDecimal outstandingAmount = event.getInvoicedAmount() != null && event.getPaymentAmount() != null ?
            event.getInvoicedAmount().subtract(event.getPaymentAmount()) : BigDecimal.ZERO;
            
        readModel.transactionAmount(event.getInvoicedAmount())
                 .settlementAmount(event.getPaymentAmount())
                 .outstandingAmount(outstandingAmount)
                 .currencyCode(event.getSettlementCurrency())
                 .dealerName(event.getDealerName())
                 .paymentReference(event.getPurchaseOrderNumber())
                 .description(event.getDescription())
                 .lastUpdated(LocalDate.now());
    }

    private void updateFinancialReportFromInvoice(FinancialReportReadModel readModel, InvoiceSettledEvent event) {
        BigDecimal outstandingAmount = event.getInvoiceAmount() != null && event.getSettlementAmount() != null ?
            event.getInvoiceAmount().subtract(event.getSettlementAmount()) : event.getInvoiceAmount();
            
        readModel.transactionAmount(event.getInvoiceAmount())
                 .settlementAmount(event.getSettlementAmount())
                 .outstandingAmount(outstandingAmount)
                 .currencyCode(event.getCurrency())
                 .dealerName(event.getDealerName())
                 .paymentReference(event.getPaymentReference())
                 .lastUpdated(LocalDate.now());
    }
}
