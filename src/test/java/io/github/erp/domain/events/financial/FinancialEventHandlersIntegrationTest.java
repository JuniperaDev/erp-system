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

import io.github.erp.ErpSystemApp;
import io.github.erp.domain.events.DomainEventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ErpSystemApp.class)
@ActiveProfiles("test")
@Transactional
public class FinancialEventHandlersIntegrationTest {

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private FinancialAssetEventHandler financialAssetEventHandler;

    @Autowired
    private FinancialReportingEventHandler financialReportingEventHandler;

    @Test
    public void shouldHandleSettlementCreatedEventForAssetTracking() {
        SettlementCreatedEvent event = new SettlementCreatedEvent(
            "settlement-123",
            "SETTLE-TEST-001",
            BigDecimal.valueOf(5000.00),
            LocalDate.now(),
            "USD",
            "Test settlement for asset tracking",
            "Test Biller",
            UUID.randomUUID()
        );

        assertThat(financialAssetEventHandler).isNotNull();
        
        financialAssetEventHandler.handleSettlementCreated(event);
    }

    @Test
    public void shouldHandlePaymentProcessedEventForAssetAcquisition() {
        PaymentProcessedEvent event = new PaymentProcessedEvent(
            "payment-456",
            "PAY-TEST-001",
            BigDecimal.valueOf(3000.00),
            BigDecimal.valueOf(3000.00),
            LocalDate.now(),
            "USD",
            "Test payment for asset acquisition",
            "Test Dealer",
            "PO-001",
            UUID.randomUUID()
        );

        assertThat(financialAssetEventHandler).isNotNull();
        
        financialAssetEventHandler.handlePaymentProcessed(event);
    }

    @Test
    public void shouldHandleInvoiceSettledEventForAssetTracking() {
        InvoiceSettledEvent event = new InvoiceSettledEvent(
            "invoice-789",
            "INV-TEST-001",
            BigDecimal.valueOf(2500.00),
            LocalDate.now(),
            "USD",
            "PAY-REF-001",
            "Test Dealer",
            BigDecimal.valueOf(2500.00),
            LocalDate.now(),
            UUID.randomUUID()
        );

        assertThat(financialAssetEventHandler).isNotNull();
        
        financialAssetEventHandler.handleInvoiceSettled(event);
    }

    @Test
    public void shouldHandleSettlementCreatedEventForReporting() {
        SettlementCreatedEvent event = new SettlementCreatedEvent(
            "settlement-report-123",
            "SETTLE-REPORT-001",
            BigDecimal.valueOf(7500.00),
            LocalDate.now(),
            "EUR",
            "Test settlement for reporting",
            "Report Biller",
            UUID.randomUUID()
        );

        assertThat(financialReportingEventHandler).isNotNull();
        
        financialReportingEventHandler.handleSettlementCreated(event);
    }

    @Test
    public void shouldHandlePaymentProcessedEventForReporting() {
        PaymentProcessedEvent event = new PaymentProcessedEvent(
            "payment-report-456",
            "PAY-REPORT-001",
            BigDecimal.valueOf(4500.00),
            BigDecimal.valueOf(4500.00),
            LocalDate.now(),
            "EUR",
            "Test payment for reporting",
            "Report Dealer",
            "PO-REPORT-001",
            UUID.randomUUID()
        );

        assertThat(financialReportingEventHandler).isNotNull();
        
        financialReportingEventHandler.handlePaymentProcessed(event);
    }

    @Test
    public void shouldHandleInvoiceSettledEventForReporting() {
        InvoiceSettledEvent event = new InvoiceSettledEvent(
            "invoice-report-789",
            "INV-REPORT-001",
            BigDecimal.valueOf(3500.00),
            LocalDate.now(),
            "EUR",
            "PAY-REF-REPORT-001",
            "Report Dealer",
            BigDecimal.valueOf(3500.00),
            LocalDate.now(),
            UUID.randomUUID()
        );

        assertThat(financialReportingEventHandler).isNotNull();
        
        financialReportingEventHandler.handleInvoiceSettled(event);
    }

    @Test
    public void shouldPublishAndHandleFinancialEventsEndToEnd() {
        SettlementCreatedEvent settlementEvent = new SettlementCreatedEvent(
            "e2e-settlement-123",
            "E2E-SETTLE-001",
            BigDecimal.valueOf(10000.00),
            LocalDate.now(),
            "GBP",
            "End-to-end test settlement",
            "E2E Biller",
            UUID.randomUUID()
        );

        PaymentProcessedEvent paymentEvent = new PaymentProcessedEvent(
            "e2e-payment-456",
            "E2E-PAY-001",
            BigDecimal.valueOf(8000.00),
            BigDecimal.valueOf(8000.00),
            LocalDate.now(),
            "GBP",
            "End-to-end test payment",
            "E2E Dealer",
            "E2E-PO-001",
            UUID.randomUUID()
        );

        InvoiceSettledEvent invoiceEvent = new InvoiceSettledEvent(
            "e2e-invoice-789",
            "E2E-INV-001",
            BigDecimal.valueOf(6000.00),
            LocalDate.now(),
            "GBP",
            "E2E-PAY-REF-001",
            "E2E Dealer",
            BigDecimal.valueOf(6000.00),
            LocalDate.now(),
            UUID.randomUUID()
        );

        domainEventPublisher.publish(settlementEvent);
        domainEventPublisher.publish(paymentEvent);
        domainEventPublisher.publish(invoiceEvent);

        assertThat(settlementEvent.getAggregateId()).isEqualTo("e2e-settlement-123");
        assertThat(paymentEvent.getAggregateId()).isEqualTo("e2e-payment-456");
        assertThat(invoiceEvent.getAggregateId()).isEqualTo("e2e-invoice-789");
    }
}
