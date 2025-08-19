package io.github.erp.financial.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A BusinessDocument entity for the financial context.
 */
@Entity
@Table(name = "business_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BusinessDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private Object createdBy;

    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private Object lastModifiedBy;

    private Object originatingDepartment;
    private Set<Object> applicationMappings = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    private Object fileChecksumAlgorithm;
    private Object securityClearance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Object lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Set<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }
}
