package io.github.erp.financial.service.mapper;

import io.github.erp.financial.domain.BusinessStamp;
import io.github.erp.financial.service.dto.BusinessStampDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessStamp} and its DTO {@link BusinessStampDTO}.
 */
@Mapper(componentModel = "spring")
public interface BusinessStampMapper extends EntityMapper<BusinessStampDTO, BusinessStamp> {}
