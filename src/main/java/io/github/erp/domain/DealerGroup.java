package io.github.erp.domain;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A DealerGroup.
 */
@Entity
@Table(name = "dealer_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dealergroup")
public class DealerGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "group_name", nullable = false, unique = true)
    @Field(type = FieldType.Text)
    private String groupName;

    @Column(name = "description")
    @Field(type = FieldType.Text)
    private String description;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "notes")
    @Field(type = FieldType.Text)
    private String notes;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parentGroup", "placeholders" }, allowSetters = true)
    private DealerGroup parentGroup;

    @ManyToMany
    @JoinTable(
        name = "rel_dealer_group__placeholder",
        joinColumns = @JoinColumn(name = "dealer_group_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public DealerGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public DealerGroup groupName(String groupName) {
        this.setGroupName(groupName);
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return this.description;
    }

    public DealerGroup description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return this.notes;
    }

    public DealerGroup notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public DealerGroup getParentGroup() {
        return this.parentGroup;
    }

    public void setParentGroup(DealerGroup dealerGroup) {
        this.parentGroup = dealerGroup;
    }

    public DealerGroup parentGroup(DealerGroup dealerGroup) {
        this.setParentGroup(dealerGroup);
        return this;
    }

    public Set<Placeholder> getPlaceholders() {
        return this.placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public DealerGroup placeholders(Set<Placeholder> placeholders) {
        this.setPlaceholders(placeholders);
        return this;
    }

    public DealerGroup addPlaceholder(Placeholder placeholder) {
        this.placeholders.add(placeholder);
        return this;
    }

    public DealerGroup removePlaceholder(Placeholder placeholder) {
        this.placeholders.remove(placeholder);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DealerGroup)) {
            return false;
        }
        return id != null && id.equals(((DealerGroup) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "DealerGroup{" +
            "id=" + getId() +
            ", groupName='" + getGroupName() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
