package io.github.erp.repository;

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
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DealerGroup entity.
 */
@Repository
public interface DealerGroupRepository extends JpaRepository<DealerGroup, Long>, JpaSpecificationExecutor<DealerGroup> {
    default Optional<DealerGroup> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DealerGroup> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DealerGroup> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct dealerGroup from DealerGroup dealerGroup left join fetch dealerGroup.parentGroup left join fetch dealerGroup.placeholders",
        countQuery = "select count(distinct dealerGroup) from DealerGroup dealerGroup"
    )
    Page<DealerGroup> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct dealerGroup from DealerGroup dealerGroup left join fetch dealerGroup.parentGroup left join fetch dealerGroup.placeholders")
    List<DealerGroup> findAllWithToOneRelationships();

    @Query("select dealerGroup from DealerGroup dealerGroup left join fetch dealerGroup.parentGroup left join fetch dealerGroup.placeholders where dealerGroup.id =:id")
    Optional<DealerGroup> findOneWithToOneRelationships(@Param("id") Long id);
}
