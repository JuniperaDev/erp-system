package io.github.erp.financial.service;

import io.github.erp.financial.service.dto.PaymentInvoiceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.financial.domain.PaymentInvoice}.
 */
public interface PaymentInvoiceService {
    /**
     * Save a paymentInvoice.
     *
     * @param paymentInvoiceDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentInvoiceDTO save(PaymentInvoiceDTO paymentInvoiceDTO);

    /**
     * Updates a paymentInvoice.
     *
     * @param paymentInvoiceDTO the entity to update.
     * @return the persisted entity.
     */
    PaymentInvoiceDTO update(PaymentInvoiceDTO paymentInvoiceDTO);

    /**
     * Partially updates a paymentInvoice.
     *
     * @param paymentInvoiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentInvoiceDTO> partialUpdate(PaymentInvoiceDTO paymentInvoiceDTO);

    /**
     * Get all the paymentInvoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentInvoiceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" paymentInvoice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentInvoiceDTO> findOne(Long id);

    /**
     * Delete the "id" paymentInvoice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the paymentInvoice corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentInvoiceDTO> search(String query, Pageable pageable);
}
