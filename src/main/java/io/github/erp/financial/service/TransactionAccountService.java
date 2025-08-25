package io.github.erp.financial.service;

import io.github.erp.financial.service.dto.TransactionAccountDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.financial.domain.TransactionAccount}.
 */
public interface TransactionAccountService {
    /**
     * Save a transactionAccount.
     *
     * @param transactionAccountDTO the entity to save.
     * @return the persisted entity.
     */
    TransactionAccountDTO save(TransactionAccountDTO transactionAccountDTO);

    /**
     * Updates a transactionAccount.
     *
     * @param transactionAccountDTO the entity to update.
     * @return the persisted entity.
     */
    TransactionAccountDTO update(TransactionAccountDTO transactionAccountDTO);

    /**
     * Partially updates a transactionAccount.
     *
     * @param transactionAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionAccountDTO> partialUpdate(TransactionAccountDTO transactionAccountDTO);

    /**
     * Get all the transactionAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionAccountDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transactionAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionAccountDTO> findOne(Long id);

    /**
     * Delete the "id" transactionAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the transactionAccount corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionAccountDTO> search(String query, Pageable pageable);
}
