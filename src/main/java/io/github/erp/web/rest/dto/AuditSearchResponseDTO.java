package io.github.erp.web.rest.dto;

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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "Audit event search response")
public class AuditSearchResponseDTO {

    @Schema(description = "Search result hits")
    @JsonProperty("hits")
    private List<AuditEventDTO> hits;

    @Schema(description = "Total number of matching events")
    @JsonProperty("totalHits")
    private Long totalHits;

    @Schema(description = "Search aggregations")
    @JsonProperty("aggregations")
    private Map<String, Object> aggregations;

    @Schema(description = "Search execution time in milliseconds")
    @JsonProperty("took")
    private Integer took;

    public List<AuditEventDTO> getHits() {
        return hits;
    }

    public void setHits(List<AuditEventDTO> hits) {
        this.hits = hits;
    }

    public Long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Long totalHits) {
        this.totalHits = totalHits;
    }

    public Map<String, Object> getAggregations() {
        return aggregations;
    }

    public void setAggregations(Map<String, Object> aggregations) {
        this.aggregations = aggregations;
    }

    public Integer getTook() {
        return took;
    }

    public void setTook(Integer took) {
        this.took = took;
    }
}
