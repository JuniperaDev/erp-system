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
import io.github.erp.financial.service.SettlementService;
import io.github.erp.financial.service.PaymentService;
import io.github.erp.financial.service.InvoiceService;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.dto.PaymentDTO;
import io.github.erp.service.dto.InvoiceDTO;
import io.github.erp.service.dto.SettlementCurrencyDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ErpSystemApp.class, properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration,org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration,org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration,org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration,org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration",
    "spring.cache.type=none",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "domain.events.kafka.enabled=false"
})
@ActiveProfiles("test")
@Transactional
public class FinancialDomainEventsIntegrationTest {

    @Autowired
    @Qualifier("financialSettlementServiceImpl")
    private SettlementService settlementService;

    @Autowired
    @Qualifier("financialPaymentServiceImpl")
    private PaymentService paymentService;

    @Autowired
    @Qualifier("financialInvoiceServiceImpl")
    private InvoiceService invoiceService;

    @Autowired
    private DomainEventStore domainEventStore;

    @Test
    public void shouldPublishSettlementCreatedEventWhenSettlementIsSaved() {
        SettlementDTO settlementDTO = new SettlementDTO();
        settlementDTO.setPaymentNumber("SETTLE-001");
        settlementDTO.setPaymentAmount(BigDecimal.valueOf(1000.00));
        settlementDTO.setPaymentDate(LocalDate.now());
        settlementDTO.setDescription("Test settlement");
        settlementDTO.setSettlementCurrency(null);

        long eventCountBefore = domainEventStore.count();

        SettlementDTO savedSettlement = settlementService.save(settlementDTO);

        long eventCountAfter = domainEventStore.count();

        assertThat(savedSettlement).isNotNull();
        assertThat(savedSettlement.getId()).isNotNull();
        assertThat(eventCountAfter).isGreaterThan(eventCountBefore);
    }

    @Test
    public void shouldPublishPaymentProcessedEventWhenPaymentIsSaved() {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentNumber("PAY-001");
        paymentDTO.setPaymentAmount(BigDecimal.valueOf(500.00));
        paymentDTO.setInvoicedAmount(BigDecimal.valueOf(500.00));
        paymentDTO.setPaymentDate(LocalDate.now());
        paymentDTO.setDescription("Test payment");
        paymentDTO.setSettlementCurrency(CurrencyTypes.USD);

        long eventCountBefore = domainEventStore.count();

        PaymentDTO savedPayment = paymentService.save(paymentDTO);

        long eventCountAfter = domainEventStore.count();

        assertThat(savedPayment).isNotNull();
        assertThat(savedPayment.getId()).isNotNull();
        assertThat(eventCountAfter).isGreaterThan(eventCountBefore);
    }

    @Test
    public void shouldPublishInvoiceSettledEventWhenInvoiceIsSaved() {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceNumber("INV-001-" + System.currentTimeMillis());
        invoiceDTO.setInvoiceAmount(BigDecimal.valueOf(750.00));
        invoiceDTO.setInvoiceDate(LocalDate.now());
        invoiceDTO.setCurrency(CurrencyTypes.USD);

        long eventCountBefore = domainEventStore.count();

        InvoiceDTO savedInvoice = invoiceService.save(invoiceDTO);

        long eventCountAfter = domainEventStore.count();

        assertThat(savedInvoice).isNotNull();
        assertThat(savedInvoice.getId()).isNotNull();
        assertThat(eventCountAfter).isGreaterThan(eventCountBefore);
    }

    @Test
    public void shouldCreateFinancialAuditTrailThroughEvents() {
        SettlementDTO settlementDTO = new SettlementDTO();
        settlementDTO.setPaymentNumber("AUDIT-SETTLE-001");
        settlementDTO.setPaymentAmount(BigDecimal.valueOf(2000.00));
        settlementDTO.setPaymentDate(LocalDate.now());
        settlementDTO.setDescription("Audit trail test settlement");
        settlementDTO.setSettlementCurrency(null);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentNumber("AUDIT-PAY-001");
        paymentDTO.setPaymentAmount(BigDecimal.valueOf(1500.00));
        paymentDTO.setInvoicedAmount(BigDecimal.valueOf(1500.00));
        paymentDTO.setPaymentDate(LocalDate.now());
        paymentDTO.setDescription("Audit trail test payment");
        paymentDTO.setSettlementCurrency(CurrencyTypes.USD);

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceNumber("AUDIT-INV-001-" + System.currentTimeMillis());
        invoiceDTO.setInvoiceAmount(BigDecimal.valueOf(1200.00));
        invoiceDTO.setInvoiceDate(LocalDate.now());
        invoiceDTO.setCurrency(CurrencyTypes.USD);

        long eventCountBefore = domainEventStore.count();

        settlementService.save(settlementDTO);
        paymentService.save(paymentDTO);
        invoiceService.save(invoiceDTO);

        long eventCountAfter = domainEventStore.count();

        assertThat(eventCountAfter).isEqualTo(eventCountBefore + 3);
    }
}
