package io.github.erp.financial.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PaymentLabelDTO implements Serializable {
    private Long id;
    private PaymentLabelDTO containingPaymentLabel;
    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentLabelDTO getContainingPaymentLabel() {
        return containingPaymentLabel;
    }

    public void setContainingPaymentLabel(PaymentLabelDTO containingPaymentLabel) {
        this.containingPaymentLabel = containingPaymentLabel;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }
}
