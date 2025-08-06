package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import io.github.erp.domain.DealerGroup;
import io.github.erp.service.dto.DealerGroupDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DealerGroup} and its DTO {@link DealerGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface DealerGroupMapper extends EntityMapper<DealerGroupDTO, DealerGroup> {
    @Mapping(target = "parentGroup", source = "parentGroup", qualifiedByName = "groupName")
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "idSet")
    DealerGroupDTO toDto(DealerGroup s);

    @Mapping(target = "removePlaceholder", ignore = true)
    DealerGroup toEntity(DealerGroupDTO dealerGroupDTO);

    @Named("groupName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "groupName", source = "groupName")
    DealerGroupDTO toDtoGroupName(DealerGroup dealerGroup);

    @Named("groupNameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "groupName", source = "groupName")
    Set<DealerGroupDTO> toDtoGroupNameSet(Set<DealerGroup> dealerGroup);
}
