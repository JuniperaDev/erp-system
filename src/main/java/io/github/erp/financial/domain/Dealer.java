package io.github.erp.financial.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Dealer.
 */
@Entity
@Table(name = "dealer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dealer")
public class Dealer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "dealer_name", nullable = false)
    @Field(type = FieldType.Text)
    private String dealerName;

    @Column(name = "tax_number")
    @Field(type = FieldType.Text)
    private String taxNumber;

    @Column(name = "identification_document_number")
    @Field(type = FieldType.Text)
    private String identificationDocumentNumber;

    @Column(name = "organization_name")
    @Field(type = FieldType.Text)
    private String organizationName;

    @Column(name = "department")
    @Field(type = FieldType.Text)
    private String department;

    @Column(name = "position")
    @Field(type = FieldType.Text)
    private String position;

    @Column(name = "postal_address")
    @Field(type = FieldType.Text)
    private String postalAddress;

    @Column(name = "physical_address")
    @Field(type = FieldType.Text)
    private String physicalAddress;

    @Column(name = "account_name")
    @Field(type = FieldType.Text)
    private String accountName;

    @Column(name = "account_number")
    @Field(type = FieldType.Text)
    private String accountNumber;

    @Column(name = "bankers_name")
    @Field(type = FieldType.Text)
    private String bankersName;

    @Column(name = "bankers_branch")
    @Field(type = FieldType.Text)
    private String bankersBranch;

    @Column(name = "bankers_swift_code")
    @Field(type = FieldType.Text)
    private String bankersSwiftCode;

    @Column(name = "file_upload_token")
    @Field(type = FieldType.Text)
    private String fileUploadToken;

    @Column(name = "compilation_token")
    @Field(type = FieldType.Text)
    private String compilationToken;

    @Lob
    @Column(name = "remarks")
    private byte[] remarks;

    @Column(name = "remarks_content_type")
    private String remarksContentType;

    @Column(name = "other_names")
    @Field(type = FieldType.Text)
    private String otherNames;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_dealer__placeholder",
        joinColumns = @JoinColumn(name = "dealer_id"),
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

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getIdentificationDocumentNumber() {
        return identificationDocumentNumber;
    }

    public void setIdentificationDocumentNumber(String identificationDocumentNumber) {
        this.identificationDocumentNumber = identificationDocumentNumber;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankersName() {
        return bankersName;
    }

    public void setBankersName(String bankersName) {
        this.bankersName = bankersName;
    }

    public String getBankersBranch() {
        return bankersBranch;
    }

    public void setBankersBranch(String bankersBranch) {
        this.bankersBranch = bankersBranch;
    }

    public String getBankersSwiftCode() {
        return bankersSwiftCode;
    }

    public void setBankersSwiftCode(String bankersSwiftCode) {
        this.bankersSwiftCode = bankersSwiftCode;
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

    public byte[] getRemarks() {
        return remarks;
    }

    public void setRemarks(byte[] remarks) {
        this.remarks = remarks;
    }

    public String getRemarksContentType() {
        return remarksContentType;
    }

    public void setRemarksContentType(String remarksContentType) {
        this.remarksContentType = remarksContentType;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public Set<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Set<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }
}
