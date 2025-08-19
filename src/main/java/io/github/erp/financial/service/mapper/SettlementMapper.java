package io.github.erp.financial.service.mapper;

import io.github.erp.financial.domain.Settlement;
import io.github.erp.financial.service.dto.SettlementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Settlement} and its DTO {@link SettlementDTO}.
 */
@Mapper(componentModel = "spring")
public interface SettlementMapper extends EntityMapper<SettlementDTO, Settlement> {}
