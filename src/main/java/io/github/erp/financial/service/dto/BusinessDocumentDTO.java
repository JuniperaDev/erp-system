package io.github.erp.financial.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BusinessDocumentDTO implements Serializable {
    private Long id;
    private Object createdBy;
    private Object lastModifiedBy;
    private Object originatingDepartment;
    private Set<Object> applicationMappings = new HashSet<>();
    private Set<PlaceholderDTO> placeholders = new HashSet<>();
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

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }
}
