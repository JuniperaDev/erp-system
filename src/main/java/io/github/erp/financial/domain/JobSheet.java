package io.github.erp.financial.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.BusinessDocument;
import io.github.erp.domain.BusinessStamp;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A JobSheet.
 */
@Entity
@Table(name = "job_sheet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "jobsheet")
public class JobSheet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "serial_number", nullable = false, unique = true)
    @Field(type = FieldType.Text)
    private String serialNumber;

    @Column(name = "job_sheet_date")
    @Field(type = FieldType.Date)
    private LocalDate jobSheetDate;

    @Column(name = "details")
    @Field(type = FieldType.Text)
    private String details;

    @Lob
    @Column(name = "remarks")
    private String remarks;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "dealerGroup", "placeholders", "paymentInvoices", "otherDealerNames", "paymentCalculations", "serviceOutlet", "settlementCurrency", "businessDocument",
        },
        allowSetters = true
    )
    private Dealer biller;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_job_sheet__signatories",
        joinColumns = @JoinColumn(name = "job_sheet_id"),
        inverseJoinColumns = @JoinColumn(name = "signatories_id")
    )
    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "dealerGroup", "placeholders", "paymentInvoices", "otherDealerNames", "paymentCalculations", "serviceOutlet", "settlementCurrency", "businessDocument",
        },
        allowSetters = true
    )
    private Set<Dealer> signatories = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "dealerGroup", "placeholders", "paymentInvoices", "otherDealerNames", "paymentCalculations", "serviceOutlet", "settlementCurrency", "businessDocument",
        },
        allowSetters = true
    )
    private Dealer contactPerson;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_job_sheet__business_stamps",
        joinColumns = @JoinColumn(name = "job_sheet_id"),
        inverseJoinColumns = @JoinColumn(name = "business_stamps_id")
    )
    @JsonIgnoreProperties(value = { "stampHolder", "placeholders" }, allowSetters = true)
    private Set<BusinessStamp> businessStamps = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_job_sheet__placeholder",
        joinColumns = @JoinColumn(name = "job_sheet_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_job_sheet__payment_label",
        joinColumns = @JoinColumn(name = "job_sheet_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_label_id")
    )
    @JsonIgnoreProperties(value = { "containingPaymentLabel", "placeholders" }, allowSetters = true)
    private Set<PaymentLabel> paymentLabels = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_job_sheet__business_document",
        joinColumns = @JoinColumn(name = "job_sheet_id"),
        inverseJoinColumns = @JoinColumn(name = "business_document_id")
    )
    @JsonIgnoreProperties(
        value = {
            "createdBy", "lastModifiedBy", "originatingDepartment", "applicationMappings", "placeholders", "fileChecksumAlgorithm", "securityClearance",
        },
        allowSetters = true
    )
    private Set<BusinessDocument> businessDocuments = new HashSet<>();

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

    public Dealer getBiller() {
        return biller;
    }

    public void setBiller(Dealer biller) {
        this.biller = biller;
    }

    public Set<Dealer> getSignatories() {
        return signatories;
    }

    public void setSignatories(Set<Dealer> signatories) {
        this.signatories = signatories;
    }

    public Dealer getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(Dealer contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Set<BusinessStamp> getBusinessStamps() {
        return businessStamps;
    }

    public void setBusinessStamps(Set<BusinessStamp> businessStamps) {
        this.businessStamps = businessStamps;
    }

    public Set<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public Set<PaymentLabel> getPaymentLabels() {
        return paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public Set<BusinessDocument> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocument> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }
}
