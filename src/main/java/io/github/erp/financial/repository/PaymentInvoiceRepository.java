package io.github.erp.financial.repository;

import io.github.erp.financial.domain.PaymentInvoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PaymentInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentInvoiceRepository extends JpaRepository<PaymentInvoice, Long>, JpaSpecificationExecutor<PaymentInvoice> {}
