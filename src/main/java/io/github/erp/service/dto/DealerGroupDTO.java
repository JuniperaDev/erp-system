package io.github.erp.service.dto;

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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.DealerGroup} entity.
 */
public class DealerGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String groupName;

    private String description;

    @Lob
    private String notes;

    private DealerGroupDTO parentGroup;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public DealerGroupDTO getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(DealerGroupDTO parentGroup) {
        this.parentGroup = parentGroup;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DealerGroupDTO)) {
            return false;
        }

        DealerGroupDTO dealerGroupDTO = (DealerGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dealerGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "DealerGroupDTO{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", parentGroup=" + getParentGroup() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
