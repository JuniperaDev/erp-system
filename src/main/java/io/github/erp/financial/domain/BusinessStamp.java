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
 * A BusinessStamp.
 */
@Entity
@Table(name = "business_stamp")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "businessstamp")
public class BusinessStamp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @Column(name = "stamp_date")
    @Field(type = FieldType.Date)
    private LocalDate stampDate;

    @Column(name = "purpose")
    @Field(type = FieldType.Text)
    private String purpose;

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
    private Dealer stampHolder;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_business_stamp__placeholder",
        joinColumns = @JoinColumn(name = "business_stamp_id"),
        inverseJoinColumns = @JoinColumn(name = "placeholder_id")
    )
    @JsonIgnoreProperties(value = { "containingPlaceholder" }, allowSetters = true)
    private Set<Placeholder> placeholders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStampDate() {
        return stampDate;
    }

    public void setStampDate(LocalDate stampDate) {
        this.stampDate = stampDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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

    public Dealer getStampHolder() {
        return stampHolder;
    }

    public void setStampHolder(Dealer stampHolder) {
        this.stampHolder = stampHolder;
    }

    public Set<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }
}
