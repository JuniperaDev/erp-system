package io.github.erp.financial.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "invoice")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false)
    @Field(type = FieldType.Text)
    private String invoiceNumber;

    @Column(name = "invoice_date")
    @Field(type = FieldType.Date)
    private LocalDate invoiceDate;

    @Column(name = "invoice_amount", precision = 21, scale = 2)
    @Field(type = FieldType.Double)
    private BigDecimal invoiceAmount;

    @NotNull
    @Column(name = "currency", nullable = false)
    @Field(type = FieldType.Text)
    private String currency;

    @Column(name = "payment_reference")
    @Field(type = FieldType.Text)
    private String paymentReference;

    @Column(name = "dealer_name")
    @Field(type = FieldType.Text)
    private String dealerName;

    @Column(name = "file_upload_token")
    @Field(type = FieldType.Text)
    private String fileUploadToken;

    @Column(name = "compilation_token")
    @Field(type = FieldType.Text)
    private String compilationToken;

    @Lob
    @Column(name = "remarks")
    private String remarks;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_invoice__purchase_order",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "purchase_order_id")
    )
    @JsonIgnoreProperties(
        value = { "settlementCurrency", "dealer", "signatories", "vendor", "businessDocuments" },
        allowSetters = true
    )
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_invoice__placeholder",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_invoice__payment_label",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_label_id")
    )
    @JsonIgnoreProperties(value = { "containingPaymentLabel", "placeholders" }, allowSetters = true)
    private Set<PaymentLabel> paymentLabels = new HashSet<>();

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
    private Dealer biller;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_invoice__delivery_note",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "delivery_note_id")
    )
    @JsonIgnoreProperties(
        value = { "placeholders", "receivedBy", "deliveryStamps", "purchaseOrder", "supplier", "signatories", "otherPurchaseOrders", "businessDocuments" },
        allowSetters = true
    )
    private Set<DeliveryNote> deliveryNotes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_invoice__job_sheet",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "job_sheet_id")
    )
    @JsonIgnoreProperties(value = { "biller", "signatories", "contactPerson", "businessStamps", "placeholders", "paymentLabels", "businessDocuments" }, allowSetters = true)
    private Set<JobSheet> jobSheets = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_invoice__business_document",
        joinColumns = @JoinColumn(name = "invoice_id"),
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
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

    public Set<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.purchaseOrders = purchaseOrders;
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

    public SettlementCurrency getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(SettlementCurrency settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public Dealer getBiller() {
        return biller;
    }

    public void setBiller(Dealer biller) {
        this.biller = biller;
    }

    public Set<DeliveryNote> getDeliveryNotes() {
        return deliveryNotes;
    }

    public void setDeliveryNotes(Set<DeliveryNote> deliveryNotes) {
        this.deliveryNotes = deliveryNotes;
    }

    public Set<JobSheet> getJobSheets() {
        return jobSheets;
    }

    public void setJobSheets(Set<JobSheet> jobSheets) {
        this.jobSheets = jobSheets;
    }

    public Set<BusinessDocument> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocument> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }
}
