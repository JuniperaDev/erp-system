package io.github.erp.domain.events.financial;

import io.github.erp.domain.events.DomainEventPublisher;
import io.github.erp.domain.events.DomainEventStore;
import io.github.erp.service.impl.SettlementServiceImpl;
import io.github.erp.service.impl.PaymentServiceImpl;
import io.github.erp.service.impl.InvoiceServiceImpl;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.dto.PaymentDTO;
import io.github.erp.service.dto.InvoiceDTO;
import io.github.erp.domain.Settlement;
import io.github.erp.domain.Payment;
import io.github.erp.domain.Invoice;
import io.github.erp.domain.enumeration.CurrencyTypes;
import io.github.erp.repository.SettlementRepository;
import io.github.erp.repository.PaymentRepository;
import io.github.erp.repository.InvoiceRepository;
import io.github.erp.repository.search.SettlementSearchRepository;
import io.github.erp.repository.search.PaymentSearchRepository;
import io.github.erp.repository.search.InvoiceSearchRepository;
import io.github.erp.service.mapper.SettlementMapper;
import io.github.erp.service.mapper.PaymentMapper;
import io.github.erp.service.mapper.InvoiceMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FinancialDomainEventsUnitTest {

    @Mock
    private SettlementRepository settlementRepository;
    @Mock
    private SettlementMapper settlementMapper;
    @Mock
    private SettlementSearchRepository settlementSearchRepository;
    @Mock
    private DomainEventPublisher domainEventPublisher;
    
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private PaymentSearchRepository paymentSearchRepository;
    
    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private InvoiceMapper invoiceMapper;
    @Mock
    private InvoiceSearchRepository invoiceSearchRepository;

    private SettlementServiceImpl settlementService;
    private PaymentServiceImpl paymentService;
    private InvoiceServiceImpl invoiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        settlementService = new SettlementServiceImpl(
            settlementRepository, settlementMapper, settlementSearchRepository, domainEventPublisher);
        
        paymentService = new PaymentServiceImpl(
            paymentRepository, paymentMapper, paymentSearchRepository, domainEventPublisher);
            
        invoiceService = new InvoiceServiceImpl(
            invoiceRepository, invoiceMapper, invoiceSearchRepository, domainEventPublisher);
    }

    @Test
    void testSettlementCreatedEventIsPublished() {
        SettlementDTO settlementDTO = new SettlementDTO();
        settlementDTO.setPaymentNumber("TEST-SETTLEMENT-001");
        settlementDTO.setPaymentAmount(BigDecimal.valueOf(1000.00));
        settlementDTO.setPaymentDate(LocalDate.now());
        settlementDTO.setDescription("Test settlement for domain events");

        Settlement settlement = new Settlement();
        settlement.setId(1L);
        settlement.setPaymentNumber("TEST-SETTLEMENT-001");
        settlement.setPaymentAmount(BigDecimal.valueOf(1000.00));
        settlement.setPaymentDate(LocalDate.now());
        settlement.setDescription("Test settlement for domain events");

        when(settlementMapper.toEntity(settlementDTO)).thenReturn(settlement);
        when(settlementRepository.save(settlement)).thenReturn(settlement);
        when(settlementMapper.toDto(settlement)).thenReturn(settlementDTO);

        settlementService.save(settlementDTO);

        ArgumentCaptor<SettlementCreatedEvent> eventCaptor = ArgumentCaptor.forClass(SettlementCreatedEvent.class);
        verify(domainEventPublisher).publish(eventCaptor.capture());

        SettlementCreatedEvent capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertEquals("1", capturedEvent.getAggregateId());
        assertEquals("Settlement", capturedEvent.getAggregateType());
        assertEquals("TEST-SETTLEMENT-001", capturedEvent.getPaymentNumber());
        assertEquals(BigDecimal.valueOf(1000.00), capturedEvent.getPaymentAmount());
        assertEquals("Test settlement for domain events", capturedEvent.getDescription());
    }

    @Test
    void testPaymentProcessedEventIsPublished() {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentNumber("TEST-PAYMENT-001");
        paymentDTO.setPaymentAmount(BigDecimal.valueOf(750.00));
        paymentDTO.setPaymentDate(LocalDate.now());
        paymentDTO.setDescription("Test payment for domain events");

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setPaymentNumber("TEST-PAYMENT-001");
        payment.setPaymentAmount(BigDecimal.valueOf(750.00));
        payment.setPaymentDate(LocalDate.now());
        payment.setDescription("Test payment for domain events");

        when(paymentMapper.toEntity(paymentDTO)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toDto(payment)).thenReturn(paymentDTO);

        paymentService.save(paymentDTO);

        ArgumentCaptor<PaymentProcessedEvent> eventCaptor = ArgumentCaptor.forClass(PaymentProcessedEvent.class);
        verify(domainEventPublisher).publish(eventCaptor.capture());

        PaymentProcessedEvent capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertEquals("1", capturedEvent.getAggregateId());
        assertEquals("Payment", capturedEvent.getAggregateType());
        assertEquals("TEST-PAYMENT-001", capturedEvent.getPaymentNumber());
        assertEquals(BigDecimal.valueOf(750.00), capturedEvent.getPaymentAmount());
        assertEquals("Test payment for domain events", capturedEvent.getDescription());
    }

    @Test
    void testInvoiceSettledEventIsPublished() {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceNumber("TEST-INVOICE-001");
        invoiceDTO.setInvoiceAmount(BigDecimal.valueOf(500.00));
        invoiceDTO.setInvoiceDate(LocalDate.now());
        invoiceDTO.setCurrency(CurrencyTypes.USD);

        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setInvoiceNumber("TEST-INVOICE-001");
        invoice.setInvoiceAmount(BigDecimal.valueOf(500.00));
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setCurrency(CurrencyTypes.USD);

        when(invoiceMapper.toEntity(invoiceDTO)).thenReturn(invoice);
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(invoiceMapper.toDto(invoice)).thenReturn(invoiceDTO);

        invoiceService.save(invoiceDTO);

        ArgumentCaptor<InvoiceSettledEvent> eventCaptor = ArgumentCaptor.forClass(InvoiceSettledEvent.class);
        verify(domainEventPublisher).publish(eventCaptor.capture());

        InvoiceSettledEvent capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertEquals("1", capturedEvent.getAggregateId());
        assertEquals("Invoice", capturedEvent.getAggregateType());
        assertEquals("TEST-INVOICE-001", capturedEvent.getInvoiceNumber());
        assertEquals(BigDecimal.valueOf(500.00), capturedEvent.getInvoiceAmount());
        assertEquals(CurrencyTypes.USD.toString(), capturedEvent.getCurrency());
    }

    @Test
    void testFinancialAssetEventHandlerProcessesEvents() {
        FinancialAssetEventHandler handler = new FinancialAssetEventHandler();

        SettlementCreatedEvent settlementEvent = new SettlementCreatedEvent(
            "1", "TEST-001", BigDecimal.valueOf(1000.00), LocalDate.now(), 
            "USD", "Test settlement", "Test Biller", java.util.UUID.randomUUID());

        PaymentProcessedEvent paymentEvent = new PaymentProcessedEvent(
            "1", "PAY-001", BigDecimal.valueOf(750.00), BigDecimal.valueOf(800.00), 
            LocalDate.now(), "USD", "Test payment", "Test Dealer", "PO-001", java.util.UUID.randomUUID());

        InvoiceSettledEvent invoiceEvent = new InvoiceSettledEvent(
            "1", "INV-001", BigDecimal.valueOf(500.00), LocalDate.now(),
            "USD", "PAY-REF-001", "Test Dealer", BigDecimal.valueOf(500.00), 
            LocalDate.now(), java.util.UUID.randomUUID());

        assertDoesNotThrow(() -> {
            handler.handleSettlementCreated(settlementEvent);
            handler.handlePaymentProcessed(paymentEvent);
            handler.handleInvoiceSettled(invoiceEvent);
        });
    }
}
