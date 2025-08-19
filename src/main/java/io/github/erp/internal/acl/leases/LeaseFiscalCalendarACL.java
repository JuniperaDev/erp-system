package io.github.erp.internal.acl.leases;

import io.github.erp.service.dto.FiscalMonthDTO;
import io.github.erp.service.dto.LeasePaymentDTO;

import java.time.LocalDate;
import java.util.Optional;

public interface LeaseFiscalCalendarACL {
    
    Optional<FiscalMonthDTO> mapPaymentDateToFiscalPeriod(LeasePaymentDTO leasePayment);
    
    Optional<LocalDate> getFiscalYearStart(LocalDate paymentDate);
    
    Optional<LocalDate> getFiscalYearEnd(LocalDate paymentDate);
    
    boolean isValidFiscalPeriod(LocalDate date);
}
