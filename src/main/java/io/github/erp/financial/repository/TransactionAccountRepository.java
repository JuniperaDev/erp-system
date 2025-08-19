package io.github.erp.financial.repository;

import io.github.erp.financial.domain.TransactionAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TransactionAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionAccountRepository extends JpaRepository<TransactionAccount, Long>, JpaSpecificationExecutor<TransactionAccount> {}
