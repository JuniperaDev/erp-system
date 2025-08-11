package io.github.erp.docmgmt.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class DocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String documentTitle;

    private String description;

    @NotNull
    private UUID documentSerial;

    @NotNull
    private String filePath;

    @NotNull
    private String contentType;

    @NotNull
    private Long fileSize;

    @NotNull
    private String checksum;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastModifiedDate;

    private Integer version;

    private Boolean isDeleted;

    private Long createdById;

    private String createdByLogin;

    private Long lastModifiedById;

    private String lastModifiedByLogin;

    private Long originatingDepartmentId;

    private String originatingDepartmentDealerName;

    private Long checksumAlgorithmId;

    private String checksumAlgorithmName;

    private Long securityClearanceId;

    private String securityClearanceClearanceLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getDocumentSerial() {
        return documentSerial;
    }

    public void setDocumentSerial(UUID documentSerial) {
        this.documentSerial = documentSerial;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String createdByLogin) {
        this.createdByLogin = createdByLogin;
    }

    public Long getLastModifiedById() {
        return lastModifiedById;
    }

    public void setLastModifiedById(Long lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public String getLastModifiedByLogin() {
        return lastModifiedByLogin;
    }

    public void setLastModifiedByLogin(String lastModifiedByLogin) {
        this.lastModifiedByLogin = lastModifiedByLogin;
    }

    public Long getOriginatingDepartmentId() {
        return originatingDepartmentId;
    }

    public void setOriginatingDepartmentId(Long originatingDepartmentId) {
        this.originatingDepartmentId = originatingDepartmentId;
    }

    public String getOriginatingDepartmentDealerName() {
        return originatingDepartmentDealerName;
    }

    public void setOriginatingDepartmentDealerName(String originatingDepartmentDealerName) {
        this.originatingDepartmentDealerName = originatingDepartmentDealerName;
    }

    public Long getChecksumAlgorithmId() {
        return checksumAlgorithmId;
    }

    public void setChecksumAlgorithmId(Long checksumAlgorithmId) {
        this.checksumAlgorithmId = checksumAlgorithmId;
    }

    public String getChecksumAlgorithmName() {
        return checksumAlgorithmName;
    }

    public void setChecksumAlgorithmName(String checksumAlgorithmName) {
        this.checksumAlgorithmName = checksumAlgorithmName;
    }

    public Long getSecurityClearanceId() {
        return securityClearanceId;
    }

    public void setSecurityClearanceId(Long securityClearanceId) {
        this.securityClearanceId = securityClearanceId;
    }

    public String getSecurityClearanceClearanceLevel() {
        return securityClearanceClearanceLevel;
    }

    public void setSecurityClearanceClearanceLevel(String securityClearanceClearanceLevel) {
        this.securityClearanceClearanceLevel = securityClearanceClearanceLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDTO)) {
            return false;
        }

        DocumentDTO documentDTO = (DocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "DocumentDTO{" +
            "id=" + getId() +
            ", documentTitle='" + getDocumentTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", documentSerial='" + getDocumentSerial() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", fileSize=" + getFileSize() +
            ", checksum='" + getChecksum() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", version=" + getVersion() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdById=" + getCreatedById() +
            ", lastModifiedById=" + getLastModifiedById() +
            ", originatingDepartmentId=" + getOriginatingDepartmentId() +
            ", checksumAlgorithmId=" + getChecksumAlgorithmId() +
            ", securityClearanceId=" + getSecurityClearanceId() +
            "}";
    }
}
