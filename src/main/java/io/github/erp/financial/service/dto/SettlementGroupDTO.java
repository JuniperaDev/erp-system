package io.github.erp.financial.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SettlementGroupDTO implements Serializable {
    private Long id;
    private SettlementGroupDTO parentGroup;
    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SettlementGroupDTO getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(SettlementGroupDTO parentGroup) {
        this.parentGroup = parentGroup;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }
}
