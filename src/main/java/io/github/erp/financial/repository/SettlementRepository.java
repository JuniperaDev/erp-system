package io.github.erp.financial.repository;

import io.github.erp.domain.Settlement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Settlement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SettlementRepository extends JpaRepository<Settlement, Long>, JpaSpecificationExecutor<Settlement> {}
