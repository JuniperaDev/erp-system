package io.github.erp.financial.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.domain.BusinessDocument;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * A PurchaseOrder.
 */
@Entity
@Table(name = "purchase_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "purchaseorder")
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "purchase_order_number", nullable = false, unique = true)
    @Field(type = FieldType.Text)
    private String purchaseOrderNumber;

    @Column(name = "purchase_order_date")
    @Field(type = FieldType.Date)
    private LocalDate purchaseOrderDate;

    @Column(name = "purchase_order_amount", precision = 21, scale = 2)
    @Field(type = FieldType.Double)
    private BigDecimal purchaseOrderAmount;

    @Column(name = "description")
    @Field(type = FieldType.Text)
    private String description;

    @Column(name = "notes")
    @Field(type = FieldType.Text)
    private String notes;

    @Column(name = "file_upload_token")
    @Field(type = FieldType.Text)
    private String fileUploadToken;

    @Column(name = "compilation_token")
    @Field(type = FieldType.Text)
    private String compilationToken;

    @Lob
    @Column(name = "remarks")
    private String remarks;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrency;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "dealerGroup", "placeholders", "paymentInvoices", "otherDealerNames", "paymentCalculations", "serviceOutlet", "settlementCurrency", "businessDocument",
        },
        allowSetters = true
    )
    private Dealer dealer;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_purchase_order__signatory",
        joinColumns = @JoinColumn(name = "purchase_order_id"),
        inverseJoinColumns = @JoinColumn(name = "signatory_id")
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
    private Dealer vendor;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_purchase_order__business_document",
        joinColumns = @JoinColumn(name = "purchase_order_id"),
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

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public LocalDate getPurchaseOrderDate() {
        return purchaseOrderDate;
    }

    public void setPurchaseOrderDate(LocalDate purchaseOrderDate) {
        this.purchaseOrderDate = purchaseOrderDate;
    }

    public BigDecimal getPurchaseOrderAmount() {
        return purchaseOrderAmount;
    }

    public void setPurchaseOrderAmount(BigDecimal purchaseOrderAmount) {
        this.purchaseOrderAmount = purchaseOrderAmount;
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

    public String getFileUploadToken() {
        return fileUploadToken;
    }

    public void setFileUploadToken(String fileUploadToken) {
        this.fileUploadToken = fileUploadToken;
    }

    public String getCompilationToken() {
        return compilationToken;
    }

    public void setCompilationToken(String compilationToken) {
        this.compilationToken = compilationToken;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public SettlementCurrency getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public Set<Dealer> getSignatories() {
        return signatories;
    }

    public void setSignatories(Set<Dealer> signatories) {
        this.signatories = signatories;
    }

    public Dealer getVendor() {
        return vendor;
    }

    public void setVendor(Dealer vendor) {
        this.vendor = vendor;
    }

    public Set<BusinessDocument> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocument> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }
}
