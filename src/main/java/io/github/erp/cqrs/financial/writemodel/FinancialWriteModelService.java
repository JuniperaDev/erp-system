package io.github.erp.cqrs.financial.writemodel;

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

import io.github.erp.domain.Settlement;
import io.github.erp.domain.Payment;
import io.github.erp.domain.Invoice;
import io.github.erp.domain.events.financial.SettlementCreatedEvent;
import io.github.erp.domain.events.financial.PaymentProcessedEvent;
import io.github.erp.domain.events.financial.InvoiceSettledEvent;
import io.github.erp.domain.events.DomainEventStore;
import io.github.erp.repository.SettlementRepository;
import io.github.erp.repository.PaymentRepository;
import io.github.erp.repository.InvoiceRepository;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.dto.PaymentDTO;
import io.github.erp.service.dto.InvoiceDTO;
import io.github.erp.service.mapper.SettlementMapper;
import io.github.erp.service.mapper.PaymentMapper;
import io.github.erp.service.mapper.InvoiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class FinancialWriteModelService {

    private final Logger log = LoggerFactory.getLogger(FinancialWriteModelService.class);

    private final SettlementRepository settlementRepository;
    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final SettlementMapper settlementMapper;
    private final PaymentMapper paymentMapper;
    private final InvoiceMapper invoiceMapper;
    private final DomainEventStore domainEventStore;

    public FinancialWriteModelService(
        SettlementRepository settlementRepository,
        PaymentRepository paymentRepository,
        InvoiceRepository invoiceRepository,
        SettlementMapper settlementMapper,
        PaymentMapper paymentMapper,
        InvoiceMapper invoiceMapper,
        DomainEventStore domainEventStore
    ) {
        this.settlementRepository = settlementRepository;
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.settlementMapper = settlementMapper;
        this.paymentMapper = paymentMapper;
        this.invoiceMapper = invoiceMapper;
        this.domainEventStore = domainEventStore;
    }

    public SettlementDTO createSettlement(SettlementDTO settlementDTO) {
        log.debug("Request to create Settlement : {}", settlementDTO);
        
        Settlement settlement = settlementMapper.toEntity(settlementDTO);
        settlement = settlementRepository.save(settlement);
        SettlementDTO result = settlementMapper.toDto(settlement);

        SettlementCreatedEvent event = new SettlementCreatedEvent(
            settlement.getId().toString(),
            settlement.getPaymentNumber(),
            settlement.getPaymentAmount(),
            settlement.getPaymentDate(),
            settlement.getSettlementCurrency() != null ? 
                settlement.getSettlementCurrency().getIso4217CurrencyCode() : null,
            settlement.getDescription(),
            settlement.getBiller() != null ? settlement.getBiller().getDealerName() : null,
            java.util.UUID.randomUUID()
        );
        
        domainEventStore.store(event);
        log.info("Settlement created and event published: {}", settlement.getId());
        
        return result;
    }

    public Optional<SettlementDTO> updateSettlement(SettlementDTO settlementDTO) {
        log.debug("Request to update Settlement : {}", settlementDTO);

        return settlementRepository
            .findById(settlementDTO.getId())
            .map(existingSettlement -> {
                settlementMapper.partialUpdate(existingSettlement, settlementDTO);
                return existingSettlement;
            })
            .map(settlementRepository::save)
            .map(updatedSettlement -> {
                SettlementCreatedEvent event = new SettlementCreatedEvent(
                    updatedSettlement.getId().toString(),
                    updatedSettlement.getPaymentNumber(),
                    updatedSettlement.getPaymentAmount(),
                    updatedSettlement.getPaymentDate(),
                    updatedSettlement.getSettlementCurrency() != null ? 
                        updatedSettlement.getSettlementCurrency().getIso4217CurrencyCode() : null,
                    updatedSettlement.getDescription(),
                    updatedSettlement.getBiller() != null ? updatedSettlement.getBiller().getDealerName() : null,
                    java.util.UUID.randomUUID()
                );
                
                domainEventStore.store(event);
                log.info("Settlement updated and event published: {}", updatedSettlement.getId());
                
                return updatedSettlement;
            })
            .map(settlementMapper::toDto);
    }

    public void deleteSettlement(Long id) {
        log.debug("Request to delete Settlement : {}", id);
        settlementRepository.deleteById(id);
    }

    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        log.debug("Request to create Payment : {}", paymentDTO);
        
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        PaymentDTO result = paymentMapper.toDto(payment);

        PaymentProcessedEvent event = new PaymentProcessedEvent(
            payment.getId().toString(),
            payment.getPaymentNumber(),
            payment.getPaymentAmount(),
            payment.getInvoicedAmount(),
            payment.getPaymentDate(),
            payment.getSettlementCurrency() != null ? payment.getSettlementCurrency().name() : null,
            payment.getDescription(),
            payment.getDealerName(),
            payment.getPurchaseOrderNumber(),
            java.util.UUID.randomUUID()
        );
        
        domainEventStore.store(event);
        log.info("Payment created and event published: {}", payment.getId());
        
        return result;
    }

    public Optional<PaymentDTO> updatePayment(PaymentDTO paymentDTO) {
        log.debug("Request to update Payment : {}", paymentDTO);

        return paymentRepository
            .findById(paymentDTO.getId())
            .map(existingPayment -> {
                paymentMapper.partialUpdate(existingPayment, paymentDTO);
                return existingPayment;
            })
            .map(paymentRepository::save)
            .map(updatedPayment -> {
                PaymentProcessedEvent event = new PaymentProcessedEvent(
                    updatedPayment.getId().toString(),
                    updatedPayment.getPaymentNumber(),
                    updatedPayment.getPaymentAmount(),
                    updatedPayment.getInvoicedAmount(),
                    updatedPayment.getPaymentDate(),
                    updatedPayment.getSettlementCurrency() != null ? updatedPayment.getSettlementCurrency().name() : null,
                    updatedPayment.getDescription(),
                    updatedPayment.getDealerName(),
                    updatedPayment.getPurchaseOrderNumber(),
                    java.util.UUID.randomUUID()
                );
                
                domainEventStore.store(event);
                log.info("Payment updated and event published: {}", updatedPayment.getId());
                
                return updatedPayment;
            })
            .map(paymentMapper::toDto);
    }

    public void deletePayment(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        log.debug("Request to create Invoice : {}", invoiceDTO);
        
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        invoice = invoiceRepository.save(invoice);
        InvoiceDTO result = invoiceMapper.toDto(invoice);

        InvoiceSettledEvent event = new InvoiceSettledEvent(
            invoice.getId().toString(),
            invoice.getInvoiceNumber(),
            invoice.getInvoiceAmount(),
            invoice.getInvoiceDate(),
            invoice.getCurrency() != null ? invoice.getCurrency().name() : null,
            invoice.getPaymentReference(),
            invoice.getDealerName(),
            BigDecimal.ZERO,
            LocalDate.now(),
            java.util.UUID.randomUUID()
        );
        
        domainEventStore.store(event);
        log.info("Invoice created and event published: {}", invoice.getId());
        
        return result;
    }

    public Optional<InvoiceDTO> updateInvoice(InvoiceDTO invoiceDTO) {
        log.debug("Request to update Invoice : {}", invoiceDTO);

        return invoiceRepository
            .findById(invoiceDTO.getId())
            .map(existingInvoice -> {
                invoiceMapper.partialUpdate(existingInvoice, invoiceDTO);
                return existingInvoice;
            })
            .map(invoiceRepository::save)
            .map(updatedInvoice -> {
                InvoiceSettledEvent event = new InvoiceSettledEvent(
                    updatedInvoice.getId().toString(),
                    updatedInvoice.getInvoiceNumber(),
                    updatedInvoice.getInvoiceAmount(),
                    updatedInvoice.getInvoiceDate(),
                    updatedInvoice.getCurrency() != null ? updatedInvoice.getCurrency().name() : null,
                    updatedInvoice.getPaymentReference(),
                    updatedInvoice.getDealerName(),
                    BigDecimal.ZERO,
                    LocalDate.now(),
                    java.util.UUID.randomUUID()
                );
                
                domainEventStore.store(event);
                log.info("Invoice updated and event published: {}", updatedInvoice.getId());
                
                return updatedInvoice;
            })
            .map(invoiceMapper::toDto);
    }

    public void deleteInvoice(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }

    public void settleInvoice(Long invoiceId, BigDecimal settlementAmount, LocalDate settlementDate) {
        log.debug("Request to settle Invoice : {} with amount : {}", invoiceId, settlementAmount);
        
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();
            
            InvoiceSettledEvent event = new InvoiceSettledEvent(
                invoice.getId().toString(),
                invoice.getInvoiceNumber(),
                invoice.getInvoiceAmount(),
                invoice.getInvoiceDate(),
                invoice.getCurrency() != null ? invoice.getCurrency().name() : null,
                invoice.getPaymentReference(),
                invoice.getDealerName(),
                settlementAmount,
                settlementDate,
                java.util.UUID.randomUUID()
            );
            
            domainEventStore.store(event);
            log.info("Invoice settlement event published: {}", invoice.getId());
        }
    }
}
