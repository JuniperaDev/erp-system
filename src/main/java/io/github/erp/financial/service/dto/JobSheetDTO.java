package io.github.erp.financial.service.dto;

import io.github.erp.service.dto.BusinessDocumentDTO;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.financial.domain.JobSheet} entity.
 */
public class JobSheetDTO implements Serializable {

    private Long id;

    @NotNull
    private String serialNumber;

    private LocalDate jobSheetDate;

    private String details;

    private String remarks;

    private DealerDTO biller;

    private Set<DealerDTO> signatories = new HashSet<>();

    private DealerDTO contactPerson;

    private Set<BusinessStampDTO> businessStamps = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private Set<PaymentLabelDTO> paymentLabels = new HashSet<>();

    private Set<BusinessDocumentDTO> businessDocuments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getJobSheetDate() {
        return jobSheetDate;
    }

    public void setJobSheetDate(LocalDate jobSheetDate) {
        this.jobSheetDate = jobSheetDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public DealerDTO getBiller() {
        return biller;
    }

    public void setBiller(DealerDTO biller) {
        this.biller = biller;
    }

    public Set<DealerDTO> getSignatories() {
        return signatories;
    }

    public void setSignatories(Set<DealerDTO> signatories) {
        this.signatories = signatories;
    }

    public DealerDTO getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(DealerDTO contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Set<BusinessStampDTO> getBusinessStamps() {
        return businessStamps;
    }

    public void setBusinessStamps(Set<BusinessStampDTO> businessStamps) {
        this.businessStamps = businessStamps;
    }

    public Set<PlaceholderDTO> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<PlaceholderDTO> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<PaymentLabelDTO> getPaymentLabels() {
        return paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabelDTO> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public Set<BusinessDocumentDTO> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocumentDTO> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }
}
