package io.github.erp.financial.service.dto;

import io.github.erp.service.dto.BusinessDocumentDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.Settlement} entity.
 */
public class SettlementDTO implements Serializable {

    private Long id;

    @NotNull
    private String paymentNumber;

    private LocalDate paymentDate;

    private BigDecimal paymentAmount;

    private String description;

    private String notes;

    private byte[] calculationFile;

    private String calculationFileContentType;

    private String calculationFileToken;

    private String paymentFile;

    private String paymentFileContentType;

    private String paymentFileToken;

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private PaymentCategoryDTO paymentCategory;

    private SettlementGroupDTO settlementGroup;

    private DealerDTO biller;

    private Set<PaymentInvoiceDTO> paymentInvoices = new HashSet<>();

    private Set<DealerDTO> signatories = new HashSet<>();

    private SettlementCurrencyDTO settlementCurrency;

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
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

    public byte[] getCalculationFile() {
        return calculationFile;
    }

    public void setCalculationFile(byte[] calculationFile) {
        this.calculationFile = calculationFile;
    }

    public String getCalculationFileContentType() {
        return calculationFileContentType;
    }

    public void setCalculationFileContentType(String calculationFileContentType) {
        this.calculationFileContentType = calculationFileContentType;
    }

    public String getCalculationFileToken() {
        return calculationFileToken;
    }

    public void setCalculationFileToken(String calculationFileToken) {
        this.calculationFileToken = calculationFileToken;
    }

    public String getPaymentFile() {
        return paymentFile;
    }

    public void setPaymentFile(String paymentFile) {
        this.paymentFile = paymentFile;
    }

    public String getPaymentFileContentType() {
        return paymentFileContentType;
    }

    public void setPaymentFileContentType(String paymentFileContentType) {
        this.paymentFileContentType = paymentFileContentType;
    }

    public String getPaymentFileToken() {
        return paymentFileToken;
    }

    public void setPaymentFileToken(String paymentFileToken) {
        this.paymentFileToken = paymentFileToken;
    }

    public Set<PaymentLabelDTO> getPaymentLabels() {
        return paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabelDTO> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public PaymentCategoryDTO getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(PaymentCategoryDTO paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public SettlementGroupDTO getSettlementGroup() {
        return settlementGroup;
    }

    public void setSettlementGroup(SettlementGroupDTO settlementGroup) {
        this.settlementGroup = settlementGroup;
    }

    public DealerDTO getBiller() {
        return biller;
    }

    public void setBiller(DealerDTO biller) {
        this.biller = biller;
    }

    public Set<PaymentInvoiceDTO> getPaymentInvoices() {
        return paymentInvoices;
    }

    public void setPaymentInvoices(Set<PaymentInvoiceDTO> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }

    public Set<DealerDTO> getSignatories() {
        return signatories;
    }

    public void setSignatories(Set<DealerDTO> signatories) {
        this.signatories = signatories;
    }

    public SettlementCurrencyDTO getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrencyDTO settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public Set<BusinessDocumentDTO> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocumentDTO> businessDocuments) {
        this.businessDocuments = businessDocuments;
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
        if (!(o instanceof SettlementDTO)) {
            return false;
        }

        SettlementDTO settlementDTO = (SettlementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, settlementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "SettlementDTO{" +
            "id=" + getId() +
            ", paymentNumber='" + getPaymentNumber() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", calculationFile='" + getCalculationFile() + "'" +
            ", calculationFileToken='" + getCalculationFileToken() + "'" +
            ", paymentFile='" + getPaymentFile() + "'" +
            ", paymentFileToken='" + getPaymentFileToken() + "'" +
            ", paymentLabels=" + getPaymentLabels() +
            ", paymentCategory=" + getPaymentCategory() +
            ", settlementGroup=" + getSettlementGroup() +
            ", biller=" + getBiller() +
            ", paymentInvoices=" + getPaymentInvoices() +
            ", signatories=" + getSignatories() +
            ", settlementCurrency=" + getSettlementCurrency() +
            ", businessDocuments=" + getBusinessDocuments() +
            ", placeholders=" + getPlaceholders() +
            "}";
    }
}
