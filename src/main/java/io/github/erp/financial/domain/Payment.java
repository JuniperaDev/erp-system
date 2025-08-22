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
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "payment_number", nullable = false)
    @Field(type = FieldType.Text)
    private String paymentNumber;

    @Column(name = "payment_date")
    @Field(type = FieldType.Date)
    private LocalDate paymentDate;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    @Field(type = FieldType.Double)
    private BigDecimal paymentAmount;

    @Column(name = "invoiced_amount", precision = 21, scale = 2)
    @Field(type = FieldType.Double)
    private BigDecimal invoicedAmount;

    @Column(name = "description")
    @Field(type = FieldType.Text)
    private String description;

    @Column(name = "settlement_currency")
    @Field(type = FieldType.Text)
    private String settlementCurrency;

    @Column(name = "calculation_file")
    @Lob
    private byte[] calculationFile;

    @Column(name = "calculation_file_content_type")
    private String calculationFileContentType;

    @Column(name = "dealer_name")
    @Field(type = FieldType.Text)
    private String dealerName;

    @Column(name = "purchase_order_number")
    @Field(type = FieldType.Text)
    private String purchaseOrderNumber;

    @Column(name = "file_upload_token")
    @Field(type = FieldType.Text)
    private String fileUploadToken;

    @Column(name = "compilation_token")
    @Field(type = FieldType.Text)
    private String compilationToken;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_payment__payment_label",
        joinColumns = @JoinColumn(name = "payment_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_label_id")
    )
    @JsonIgnoreProperties(value = { "containingPaymentLabel", "placeholders" }, allowSetters = true)
    private Set<PaymentLabel> paymentLabels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "paymentLabels", "dealerGroup", "placeholders" },
        allowSetters = true
    )
    private PaymentCategory paymentCategory;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_payment__placeholder",
        joinColumns = @JoinColumn(name = "payment_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "dealerGroup", "placeholders", "paymentInvoices", "otherDealerNames", "paymentCalculations", "serviceOutlet", "settlementCurrency", "businessDocument",
        },
        allowSetters = true
    )
    private Dealer payee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "placeholders" }, allowSetters = true)
    private SettlementCurrency settlementCurrencyEntity;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_payment__payment_invoice",
        joinColumns = @JoinColumn(name = "payment_id"),
        inverseJoinColumns = @JoinColumn(name = "payment_invoice_id")
    )
    @JsonIgnoreProperties(
        value = {
            "purchaseOrders", "placeholders", "paymentLabels", "settlementCurrency", "biller", "deliveryNotes", "jobSheets", "businessDocuments",
        },
        allowSetters = true
    )
    private Set<PaymentInvoice> paymentInvoices = new HashSet<>();

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

    public BigDecimal getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
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

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
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

    public Set<PaymentLabel> getPaymentLabels() {
        return paymentLabels;
    }

    public void setPaymentLabels(Set<PaymentLabel> paymentLabels) {
        this.paymentLabels = paymentLabels;
    }

    public PaymentCategory getPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(PaymentCategory paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public Set<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public Dealer getPayee() {
        return payee;
    }

    public void setPayee(Dealer payee) {
        this.payee = payee;
    }

    public SettlementCurrency getSettlementCurrencyEntity() {
        return settlementCurrencyEntity;
    }

    public void setSettlementCurrencyEntity(SettlementCurrency settlementCurrencyEntity) {
        this.settlementCurrencyEntity = settlementCurrencyEntity;
    }

    public Set<PaymentInvoice> getPaymentInvoices() {
        return paymentInvoices;
    }

    public void setPaymentInvoices(Set<PaymentInvoice> paymentInvoices) {
        this.paymentInvoices = paymentInvoices;
    }
}
