package io.github.erp.financial.service.impl;

import io.github.erp.financial.service.InvoiceService;
import io.github.erp.service.dto.InvoiceDTO;
import io.github.erp.repository.InvoiceRepository;
import io.github.erp.repository.search.InvoiceSearchRepository;
import io.github.erp.service.mapper.InvoiceMapper;
import io.github.erp.domain.events.DomainEventPublisher;
import io.github.erp.domain.events.financial.InvoiceProcessedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service("financialInvoiceServiceImpl")
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final DomainEventPublisher domainEventPublisher;
    private final InvoiceSearchRepository invoiceSearchRepository;

    public InvoiceServiceImpl(
        InvoiceRepository invoiceRepository,
        InvoiceMapper invoiceMapper,
        DomainEventPublisher domainEventPublisher,
        @Autowired(required = false) InvoiceSearchRepository invoiceSearchRepository
    ) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.domainEventPublisher = domainEventPublisher;
        this.invoiceSearchRepository = invoiceSearchRepository;
    }

    @Override
    public InvoiceDTO save(InvoiceDTO invoiceDTO) {
        log.debug("Request to save Invoice : {}", invoiceDTO);
        var invoice = invoiceMapper.toEntity(invoiceDTO);
        invoice = invoiceRepository.save(invoice);
        InvoiceDTO result = invoiceMapper.toDto(invoice);
        if (invoiceSearchRepository != null) {
            invoiceSearchRepository.save(invoice);
        }
        
        InvoiceProcessedEvent event = new InvoiceProcessedEvent(
            invoice.getId().toString(),
            invoice.getInvoiceNumber(),
            invoice.getInvoiceAmount(),
            invoice.getInvoiceDate()
        );
        domainEventPublisher.publish(event);
        
        return result;
    }

    @Override
    public Optional<InvoiceDTO> partialUpdate(InvoiceDTO invoiceDTO) {
        log.debug("Request to partially update Invoice : {}", invoiceDTO);
        return invoiceRepository
            .findById(invoiceDTO.getId())
            .map(existingInvoice -> {
                invoiceMapper.partialUpdate(existingInvoice, invoiceDTO);
                return existingInvoice;
            })
            .map(invoiceRepository::save)
            .map(savedInvoice -> {
                if (invoiceSearchRepository != null) {
                    invoiceSearchRepository.save(savedInvoice);
                }
                return savedInvoice;
            })
            .map(invoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll(pageable).map(invoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return invoiceRepository.findAllWithEagerRelationships(pageable).map(invoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceDTO> findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findOneWithEagerRelationships(id).map(invoiceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
        if (invoiceSearchRepository != null) {
            invoiceSearchRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Invoices for query {}", query);
        if (invoiceSearchRepository != null) {
            return invoiceSearchRepository.search(query, pageable).map(invoiceMapper::toDto);
        }
        return invoiceRepository.findAll(pageable).map(invoiceMapper::toDto);
    }
}
