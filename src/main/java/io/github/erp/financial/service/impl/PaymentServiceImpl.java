package io.github.erp.financial.service.impl;

import io.github.erp.financial.domain.Payment;
import io.github.erp.financial.repository.PaymentRepository;
import io.github.erp.financial.service.PaymentService;
import io.github.erp.financial.service.dto.PaymentDTO;
import io.github.erp.financial.service.mapper.PaymentMapper;
import io.github.erp.domain.events.DomainEventPublisher;
import io.github.erp.domain.events.financial.PaymentProcessedEvent;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final DomainEventPublisher domainEventPublisher;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper, DomainEventPublisher domainEventPublisher) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public PaymentDTO save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        PaymentDTO result = paymentMapper.toDto(payment);
        
        PaymentProcessedEvent event = new PaymentProcessedEvent(
            payment.getId().toString(),
            payment.getPaymentNumber(),
            payment.getPaymentAmount(),
            payment.getInvoicedAmount(),
            payment.getPaymentDate(),
            "USD", // Default settlement currency
            payment.getDescription(),
            "", // Default dealer name
            "", // Default purchase order number
            java.util.UUID.randomUUID()
        );
        domainEventPublisher.publish(event);
        
        return result;
    }

    @Override
    public PaymentDTO update(PaymentDTO paymentDTO) {
        log.debug("Request to update Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    @Override
    public Optional<PaymentDTO> partialUpdate(PaymentDTO paymentDTO) {
        log.debug("Request to partially update Payment : {}", paymentDTO);

        return paymentRepository
            .findById(paymentDTO.getId())
            .map(existingPayment -> {
                paymentMapper.partialUpdate(existingPayment, paymentDTO);

                return existingPayment;
            })
            .map(paymentRepository::save)
            .map(paymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable).map(paymentMapper::toDto);
    }

    public Page<PaymentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paymentRepository.findAllWithEagerRelationships(pageable).map(paymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findOneWithEagerRelationships(id).map(paymentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Payments for query {}", query);
        return paymentRepository.findAll(pageable).map(paymentMapper::toDto);
    }
}
