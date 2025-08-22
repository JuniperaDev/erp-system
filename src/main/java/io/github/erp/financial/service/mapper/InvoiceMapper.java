package io.github.erp.financial.service.mapper;

import io.github.erp.financial.domain.Invoice;
import io.github.erp.financial.service.dto.InvoiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {}
