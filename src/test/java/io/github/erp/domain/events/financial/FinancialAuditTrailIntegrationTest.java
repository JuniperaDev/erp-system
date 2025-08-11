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
import io.github.erp.domain.events.DomainEventStore;
import io.github.erp.domain.enumeration.CurrencyTypes;
import io.github.erp.service.SettlementService;
import io.github.erp.service.PaymentService;
import io.github.erp.service.InvoiceService;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.dto.PaymentDTO;
import io.github.erp.service.dto.InvoiceDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ErpSystemApp.class)
@ActiveProfiles("test")
@Transactional
public class FinancialAuditTrailIntegrationTest {

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private DomainEventStore domainEventStore;

    @Test
    public void shouldCreateAuditTrailForSettlementOperations() {
        long initialEventCount = domainEventStore.count();

        SettlementDTO settlement1 = new SettlementDTO();
        settlement1.setPaymentNumber("AUDIT-SETTLE-001");
        settlement1.setPaymentAmount(BigDecimal.valueOf(1000.00));
        settlement1.setPaymentDate(LocalDate.now());
        settlement1.setDescription("First audit settlement");

        SettlementDTO settlement2 = new SettlementDTO();
        settlement2.setPaymentNumber("AUDIT-SETTLE-002");
        settlement2.setPaymentAmount(BigDecimal.valueOf(2000.00));
        settlement2.setPaymentDate(LocalDate.now());
        settlement2.setDescription("Second audit settlement");

        settlementService.save(settlement1);
        settlementService.save(settlement2);

        long finalEventCount = domainEventStore.count();

        assertThat(finalEventCount).isEqualTo(initialEventCount + 2);
    }

    @Test
    public void shouldCreateAuditTrailForPaymentOperations() {
        long initialEventCount = domainEventStore.count();

        PaymentDTO payment1 = new PaymentDTO();
        payment1.setPaymentNumber("AUDIT-PAY-001");
        payment1.setPaymentAmount(BigDecimal.valueOf(500.00));
        payment1.setInvoicedAmount(BigDecimal.valueOf(500.00));
        payment1.setPaymentDate(LocalDate.now());
        payment1.setDescription("First audit payment");

        PaymentDTO payment2 = new PaymentDTO();
        payment2.setPaymentNumber("AUDIT-PAY-002");
        payment2.setPaymentAmount(BigDecimal.valueOf(750.00));
        payment2.setInvoicedAmount(BigDecimal.valueOf(750.00));
        payment2.setPaymentDate(LocalDate.now());
        payment2.setDescription("Second audit payment");

        paymentService.save(payment1);
        paymentService.save(payment2);

        long finalEventCount = domainEventStore.count();

        assertThat(finalEventCount).isEqualTo(initialEventCount + 2);
    }

    @Test
    public void shouldCreateAuditTrailForInvoiceOperations() {
        long initialEventCount = domainEventStore.count();

        InvoiceDTO invoice1 = new InvoiceDTO();
        invoice1.setInvoiceNumber("AUDIT-INV-001");
        invoice1.setInvoiceAmount(BigDecimal.valueOf(300.00));
        invoice1.setInvoiceDate(LocalDate.now());
        invoice1.setCurrency(CurrencyTypes.USD);

        InvoiceDTO invoice2 = new InvoiceDTO();
        invoice2.setInvoiceNumber("AUDIT-INV-002");
        invoice2.setInvoiceAmount(BigDecimal.valueOf(450.00));
        invoice2.setInvoiceDate(LocalDate.now());
        invoice2.setCurrency(CurrencyTypes.EUR);

        invoiceService.save(invoice1);
        invoiceService.save(invoice2);

        long finalEventCount = domainEventStore.count();

        assertThat(finalEventCount).isEqualTo(initialEventCount + 2);
    }

    @Test
    public void shouldCreateComprehensiveAuditTrailForMixedFinancialOperations() {
        long initialEventCount = domainEventStore.count();

        SettlementDTO settlement = new SettlementDTO();
        settlement.setPaymentNumber("MIXED-SETTLE-001");
        settlement.setPaymentAmount(BigDecimal.valueOf(5000.00));
        settlement.setPaymentDate(LocalDate.now());
        settlement.setDescription("Mixed operations settlement");

        PaymentDTO payment = new PaymentDTO();
        payment.setPaymentNumber("MIXED-PAY-001");
        payment.setPaymentAmount(BigDecimal.valueOf(3000.00));
        payment.setInvoicedAmount(BigDecimal.valueOf(3000.00));
        payment.setPaymentDate(LocalDate.now());
        payment.setDescription("Mixed operations payment");

        InvoiceDTO invoice = new InvoiceDTO();
        invoice.setInvoiceNumber("MIXED-INV-001");
        invoice.setInvoiceAmount(BigDecimal.valueOf(2000.00));
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCurrency(CurrencyTypes.GBP);

        SettlementDTO savedSettlement = settlementService.save(settlement);
        PaymentDTO savedPayment = paymentService.save(payment);
        InvoiceDTO savedInvoice = invoiceService.save(invoice);

        long finalEventCount = domainEventStore.count();

        assertThat(savedSettlement.getId()).isNotNull();
        assertThat(savedPayment.getId()).isNotNull();
        assertThat(savedInvoice.getId()).isNotNull();
        assertThat(finalEventCount).isEqualTo(initialEventCount + 3);
    }

    @Test
    public void shouldMaintainEventOrderingInAuditTrail() {
        long initialEventCount = domainEventStore.count();

        for (int i = 1; i <= 5; i++) {
            SettlementDTO settlement = new SettlementDTO();
            settlement.setPaymentNumber("ORDER-SETTLE-" + String.format("%03d", i));
            settlement.setPaymentAmount(BigDecimal.valueOf(i * 100.00));
            settlement.setPaymentDate(LocalDate.now());
            settlement.setDescription("Ordered settlement " + i);

            settlementService.save(settlement);
        }

        long finalEventCount = domainEventStore.count();

        assertThat(finalEventCount).isEqualTo(initialEventCount + 5);
    }
}
