package io.github.erp.financial.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PaymentCategoryDTO implements Serializable {
    private Long id;
    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();
    private Set<Object> paymentCalculations = new HashSet<>();
    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<PaymentLabelDTO> getPaymentLabels() {
        return paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabelDTO> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public Set<Object> getPaymentCalculations() {
        return paymentCalculations;
    }

    public void setPaymentCalculations(Set<Object> paymentCalculations) {
        this.paymentCalculations = paymentCalculations;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }
}
