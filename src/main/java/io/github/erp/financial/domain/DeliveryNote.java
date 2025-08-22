package io.github.erp.financial.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A DeliveryNote.
 */
@Entity
@Table(name = "delivery_note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "deliverynote")
public class DeliveryNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "delivery_note_number", nullable = false, unique = true)
    @Field(type = FieldType.Text)
    private String deliveryNoteNumber;

    @NotNull
    @Column(name = "document_date", nullable = false)
    @Field(type = FieldType.Date)
    private LocalDate documentDate;

    @Column(name = "description")
    @Field(type = FieldType.Text)
    private String description;

    @Column(name = "serial_number")
    @Field(type = FieldType.Text)
    private String serialNumber;

    @Column(name = "quantity")
    @Field(type = FieldType.Integer)
    private Integer quantity;

    @Lob
    @Column(name = "remarks")
    private String remarks;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_delivery_note__placeholder",
        joinColumns = @JoinColumn(name = "delivery_note_id"),
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
    private Dealer receivedBy;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_delivery_note__delivery_stamps",
        joinColumns = @JoinColumn(name = "delivery_note_id"),
        inverseJoinColumns = @JoinColumn(name = "delivery_stamps_id")
    )
    @JsonIgnoreProperties(value = { "stampHolder", "placeholders" }, allowSetters = true)
    private Set<BusinessStamp> deliveryStamps = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "settlementCurrency", "dealer", "signatories", "vendor", "businessDocuments" },
        allowSetters = true
    )
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "dealerGroup", "placeholders", "paymentInvoices", "otherDealerNames", "paymentCalculations", "serviceOutlet", "settlementCurrency", "businessDocument",
        },
        allowSetters = true
    )
    private Dealer supplier;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_delivery_note__signatories",
        joinColumns = @JoinColumn(name = "delivery_note_id"),
        inverseJoinColumns = @JoinColumn(name = "signatories_id")
    )
    @JsonIgnoreProperties(
        value = {
            "paymentLabels", "dealerGroup", "placeholders", "paymentInvoices", "otherDealerNames", "paymentCalculations", "serviceOutlet", "settlementCurrency", "businessDocument",
        },
        allowSetters = true
    )
    private Set<Dealer> signatories = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_delivery_note__other_purchase_orders",
        joinColumns = @JoinColumn(name = "delivery_note_id"),
        inverseJoinColumns = @JoinColumn(name = "other_purchase_orders_id")
    )
    @JsonIgnoreProperties(
        value = { "settlementCurrency", "dealer", "signatories", "vendor", "businessDocuments" },
        allowSetters = true
    )
    private Set<PurchaseOrder> otherPurchaseOrders = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_delivery_note__business_document",
        joinColumns = @JoinColumn(name = "delivery_note_id"),
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

    public String getDeliveryNoteNumber() {
        return deliveryNoteNumber;
    }

    public void setDeliveryNoteNumber(String deliveryNoteNumber) {
        this.deliveryNoteNumber = deliveryNoteNumber;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Set<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public Dealer getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(Dealer receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Set<BusinessStamp> getDeliveryStamps() {
        return deliveryStamps;
    }

    public void setDeliveryStamps(Set<BusinessStamp> deliveryStamps) {
        this.deliveryStamps = deliveryStamps;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Dealer getSupplier() {
        return supplier;
    }

    public void setSupplier(Dealer supplier) {
        this.supplier = supplier;
    }

    public Set<Dealer> getSignatories() {
        return signatories;
    }

    public void setSignatories(Set<Dealer> signatories) {
        this.signatories = signatories;
    }

    public Set<PurchaseOrder> getOtherPurchaseOrders() {
        return otherPurchaseOrders;
    }

    public void setOtherPurchaseOrders(Set<PurchaseOrder> otherPurchaseOrders) {
        this.otherPurchaseOrders = otherPurchaseOrders;
    }

    public Set<BusinessDocument> getBusinessDocuments() {
        return businessDocuments;
    }

    public void setBusinessDocuments(Set<BusinessDocument> businessDocuments) {
        this.businessDocuments = businessDocuments;
    }
}
