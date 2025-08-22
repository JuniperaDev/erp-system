package io.github.erp.financial.service.mapper;

import io.github.erp.financial.domain.Payment;
import io.github.erp.financial.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {}
