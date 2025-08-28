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
import org.springframework.data.domain.Pageable;

import java.util.List;

@Schema(description = "Paginated audit trail response")
public class AuditTrailResponseDTO {

    @Schema(description = "Audit events in current page")
    @JsonProperty("content")
    private List<AuditEventDTO> content;

    @Schema(description = "Pagination information")
    @JsonProperty("pageable")
    private PageableDTO pageable;

    @Schema(description = "Total number of audit events")
    @JsonProperty("totalElements")
    private Long totalElements;

    @Schema(description = "Total number of pages")
    @JsonProperty("totalPages")
    private Integer totalPages;

    @Schema(description = "Whether this is the first page")
    @JsonProperty("first")
    private Boolean first;

    @Schema(description = "Whether this is the last page")
    @JsonProperty("last")
    private Boolean last;

    @Schema(description = "Number of elements in current page")
    @JsonProperty("numberOfElements")
    private Integer numberOfElements;

    public List<AuditEventDTO> getContent() {
        return content;
    }

    public void setContent(List<AuditEventDTO> content) {
        this.content = content;
    }

    public PageableDTO getPageable() {
        return pageable;
    }

    public void setPageable(PageableDTO pageable) {
        this.pageable = pageable;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public static class PageableDTO {
        @Schema(description = "Page number")
        @JsonProperty("page")
        private Integer page;

        @Schema(description = "Page size")
        @JsonProperty("size")
        private Integer size;

        @Schema(description = "Sort criteria")
        @JsonProperty("sort")
        private List<String> sort;

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public List<String> getSort() {
            return sort;
        }

        public void setSort(List<String> sort) {
            this.sort = sort;
        }
    }
}
