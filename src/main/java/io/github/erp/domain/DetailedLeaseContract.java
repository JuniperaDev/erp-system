package io.github.erp.domain;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A DetailedLeaseContract.
 */
@Entity
@Table(name = "detailed_lease_contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "detailedleasecontract-" + "#{ T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM')) }")
public class DetailedLeaseContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "booking_id", nullable = false, unique = true)
    @Field(type = FieldType.Keyword)
    private String bookingId;

    @NotNull
    @Column(name = "lease_title", nullable = false)
    @Field(type = FieldType.Keyword)
    private String leaseTitle;

    @Column(name = "short_title", unique = true)
    @Field(type = FieldType.Keyword)
    private String shortTitle;

    @Column(name = "description")
    @Field(type = FieldType.Text)
    private String description;

    @NotNull
    @Column(name = "inception_date", nullable = false)
    @Field(type = FieldType.Date)
    private LocalDate inceptionDate;

    @NotNull
    @Column(name = "commencement_date", nullable = false)
    @Field(type = FieldType.Date)
    private LocalDate commencementDate;

    @Column(name = "serial_number")
    @Field(type = FieldType.Binary)
    private UUID serialNumber;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "placeholders", "bankCode", "outletType", "outletStatus", "countyName", "subCountyName" },
        allowSetters = true
    )
    private ServiceOutlet superintendentServiceOutlet;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "paymentLabels", "dealerGroup", "placeholders" }, allowSetters = true)
    private Dealer mainDealer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings", "fiscalQuarter" }, allowSetters = true)
    private FiscalMonth firstReportingPeriod;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "fiscalYear", "placeholders", "universallyUniqueMappings", "fiscalQuarter" }, allowSetters = true)
    private FiscalMonth lastReportingPeriod;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private BusinessDocument leaseContractDocument;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "createdBy",
            "lastModifiedBy",
            "originatingDepartment",
            "applicationMappings",
            "placeholders",
            "fileChecksumAlgorithm",
            "securityClearance",
        },
        allowSetters = true
    )
    private BusinessDocument leaseContractCalculations;

    @OneToMany(mappedBy = "leaseContract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "leaseContract" }, allowSetters = true)
    private Set<LeasePayment> leasePayments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetailedLeaseContract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return this.bookingId;
    }

    public DetailedLeaseContract bookingId(String bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getLeaseTitle() {
        return this.leaseTitle;
    }

    public DetailedLeaseContract leaseTitle(String leaseTitle) {
        this.setLeaseTitle(leaseTitle);
        return this;
    }

    public void setLeaseTitle(String leaseTitle) {
        this.leaseTitle = leaseTitle;
    }

    public String getShortTitle() {
        return this.shortTitle;
    }

    public DetailedLeaseContract shortTitle(String shortTitle) {
        this.setShortTitle(shortTitle);
        return this;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getDescription() {
        return this.description;
    }

    public DetailedLeaseContract description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getInceptionDate() {
        return this.inceptionDate;
    }

    public DetailedLeaseContract inceptionDate(LocalDate inceptionDate) {
        this.setInceptionDate(inceptionDate);
        return this;
    }

    public void setInceptionDate(LocalDate inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public LocalDate getCommencementDate() {
        return this.commencementDate;
    }

    public DetailedLeaseContract commencementDate(LocalDate commencementDate) {
        this.setCommencementDate(commencementDate);
        return this;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public UUID getSerialNumber() {
        return this.serialNumber;
    }

    public DetailedLeaseContract serialNumber(UUID serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(UUID serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ServiceOutlet getSuperintendentServiceOutlet() {
        return this.superintendentServiceOutlet;
    }

    public void setSuperintendentServiceOutlet(ServiceOutlet serviceOutlet) {
        this.superintendentServiceOutlet = serviceOutlet;
    }

    public DetailedLeaseContract superintendentServiceOutlet(ServiceOutlet serviceOutlet) {
        this.setSuperintendentServiceOutlet(serviceOutlet);
        return this;
    }

    public Dealer getMainDealer() {
        return this.mainDealer;
    }

    public void setMainDealer(Dealer dealer) {
        this.mainDealer = dealer;
    }

    public DetailedLeaseContract mainDealer(Dealer dealer) {
        this.setMainDealer(dealer);
        return this;
    }

    public FiscalMonth getFirstReportingPeriod() {
        return this.firstReportingPeriod;
    }

    public void setFirstReportingPeriod(FiscalMonth fiscalMonth) {
        this.firstReportingPeriod = fiscalMonth;
    }

    public DetailedLeaseContract firstReportingPeriod(FiscalMonth fiscalMonth) {
        this.setFirstReportingPeriod(fiscalMonth);
        return this;
    }

    public FiscalMonth getLastReportingPeriod() {
        return this.lastReportingPeriod;
    }

    public void setLastReportingPeriod(FiscalMonth fiscalMonth) {
        this.lastReportingPeriod = fiscalMonth;
    }

    public DetailedLeaseContract lastReportingPeriod(FiscalMonth fiscalMonth) {
        this.setLastReportingPeriod(fiscalMonth);
        return this;
    }

    public BusinessDocument getLeaseContractDocument() {
        return this.leaseContractDocument;
    }

    public void setLeaseContractDocument(BusinessDocument businessDocument) {
        this.leaseContractDocument = businessDocument;
    }

    public DetailedLeaseContract leaseContractDocument(BusinessDocument businessDocument) {
        this.setLeaseContractDocument(businessDocument);
        return this;
    }

    public BusinessDocument getLeaseContractCalculations() {
        return this.leaseContractCalculations;
    }

    public void setLeaseContractCalculations(BusinessDocument businessDocument) {
        this.leaseContractCalculations = businessDocument;
    }

    public DetailedLeaseContract leaseContractCalculations(BusinessDocument businessDocument) {
        this.setLeaseContractCalculations(businessDocument);
        return this;
    }

    public Set<LeasePayment> getLeasePayments() {
        return this.leasePayments;
    }

    public void setLeasePayments(Set<LeasePayment> leasePayments) {
        if (this.leasePayments != null) {
            this.leasePayments.forEach(i -> i.setLeaseContract(null));
        }
        if (leasePayments != null) {
            leasePayments.forEach(i -> i.setLeaseContract(this));
        }
        this.leasePayments = leasePayments;
    }

    public DetailedLeaseContract leasePayments(Set<LeasePayment> leasePayments) {
        this.setLeasePayments(leasePayments);
        return this;
    }

    public DetailedLeaseContract addLeasePayment(LeasePayment leasePayment) {
        this.leasePayments.add(leasePayment);
        leasePayment.setLeaseContract(this);
        return this;
    }

    public DetailedLeaseContract removeLeasePayment(LeasePayment leasePayment) {
        this.leasePayments.remove(leasePayment);
        leasePayment.setLeaseContract(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailedLeaseContract)) {
            return false;
        }
        return id != null && id.equals(((DetailedLeaseContract) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailedLeaseContract{" +
            "id=" + getId() +
            ", bookingId='" + getBookingId() + "'" +
            ", leaseTitle='" + getLeaseTitle() + "'" +
            ", shortTitle='" + getShortTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", inceptionDate='" + getInceptionDate() + "'" +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            "}";
    }
}
