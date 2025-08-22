package io.github.erp.financial.service.impl;

import io.github.erp.financial.service.PaymentService;
import io.github.erp.service.dto.PaymentDTO;
import io.github.erp.repository.PaymentRepository;
import io.github.erp.repository.search.PaymentSearchRepository;
import io.github.erp.service.mapper.PaymentMapper;
import io.github.erp.domain.events.DomainEventPublisher;
import io.github.erp.domain.events.financial.PaymentProcessedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service("financialPaymentServiceImpl")
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final DomainEventPublisher domainEventPublisher;
    private final PaymentSearchRepository paymentSearchRepository;

    public PaymentServiceImpl(
        PaymentRepository paymentRepository,
        PaymentMapper paymentMapper,
        DomainEventPublisher domainEventPublisher,
        @Autowired(required = false) PaymentSearchRepository paymentSearchRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.domainEventPublisher = domainEventPublisher;
        this.paymentSearchRepository = paymentSearchRepository;
    }

    @Override
    public PaymentDTO save(PaymentDTO paymentDTO) {
        log.debug("Request to save Payment : {}", paymentDTO);
        var payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        PaymentDTO result = paymentMapper.toDto(payment);
        if (paymentSearchRepository != null) {
            paymentSearchRepository.save(payment);
        }
        
        PaymentProcessedEvent event = new PaymentProcessedEvent(
            payment.getId().toString(),
            payment.getPaymentNumber(),
            payment.getPaymentAmount(),
            payment.getInvoicedAmount(),
            payment.getPaymentDate(),
            payment.getSettlementCurrency() != null ? payment.getSettlementCurrency().toString() : "USD",
            payment.getDescription(),
            payment.getDealerName(),
            payment.getPurchaseOrderNumber(),
            java.util.UUID.randomUUID()
        );
        domainEventPublisher.publish(event);
        
        return result;
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
            .map(savedPayment -> {
                if (paymentSearchRepository != null) {
                    paymentSearchRepository.save(savedPayment);
                }
                return savedPayment;
            })
            .map(paymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll(pageable).map(paymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
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
        if (paymentSearchRepository != null) {
            paymentSearchRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Payments for query {}", query);
        if (paymentSearchRepository != null) {
            return paymentSearchRepository.search(query, pageable).map(paymentMapper::toDto);
        }
        return paymentRepository.findAll(pageable).map(paymentMapper::toDto);
    }
}
