package io.github.erp.financial.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class PlaceholderDTO implements Serializable {
    private Long id;
    private PlaceholderDTO containingPlaceholder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlaceholderDTO getContainingPlaceholder() {
        return containingPlaceholder;
    }

    public void setContainingPlaceholder(PlaceholderDTO containingPlaceholder) {
        this.containingPlaceholder = containingPlaceholder;
    }
}
