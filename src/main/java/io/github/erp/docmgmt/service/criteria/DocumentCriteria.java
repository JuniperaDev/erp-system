package io.github.erp.docmgmt.service.criteria;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

public class DocumentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentTitle;

    private StringFilter description;

    private StringFilter contentType;

    private LongFilter fileSize;

    private StringFilter checksum;

    private ZonedDateTimeFilter createdDate;

    private ZonedDateTimeFilter lastModifiedDate;

    private IntegerFilter version;

    private BooleanFilter isDeleted;

    private LongFilter createdById;

    private LongFilter lastModifiedById;

    private LongFilter originatingDepartmentId;

    private LongFilter checksumAlgorithmId;

    private LongFilter securityClearanceId;

    private Boolean distinct;

    public DocumentCriteria() {}

    public DocumentCriteria(DocumentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.documentTitle = other.documentTitle == null ? null : other.documentTitle.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.contentType = other.contentType == null ? null : other.contentType.copy();
        this.fileSize = other.fileSize == null ? null : other.fileSize.copy();
        this.checksum = other.checksum == null ? null : other.checksum.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.version = other.version == null ? null : other.version.copy();
        this.isDeleted = other.isDeleted == null ? null : other.isDeleted.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.lastModifiedById = other.lastModifiedById == null ? null : other.lastModifiedById.copy();
        this.originatingDepartmentId = other.originatingDepartmentId == null ? null : other.originatingDepartmentId.copy();
        this.checksumAlgorithmId = other.checksumAlgorithmId == null ? null : other.checksumAlgorithmId.copy();
        this.securityClearanceId = other.securityClearanceId == null ? null : other.securityClearanceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocumentCriteria copy() {
        return new DocumentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDocumentTitle() {
        return documentTitle;
    }

    public StringFilter documentTitle() {
        if (documentTitle == null) {
            documentTitle = new StringFilter();
        }
        return documentTitle;
    }

    public void setDocumentTitle(StringFilter documentTitle) {
        this.documentTitle = documentTitle;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getContentType() {
        return contentType;
    }

    public StringFilter contentType() {
        if (contentType == null) {
            contentType = new StringFilter();
        }
        return contentType;
    }

    public void setContentType(StringFilter contentType) {
        this.contentType = contentType;
    }

    public LongFilter getFileSize() {
        return fileSize;
    }

    public LongFilter fileSize() {
        if (fileSize == null) {
            fileSize = new LongFilter();
        }
        return fileSize;
    }

    public void setFileSize(LongFilter fileSize) {
        this.fileSize = fileSize;
    }

    public StringFilter getChecksum() {
        return checksum;
    }

    public StringFilter checksum() {
        if (checksum == null) {
            checksum = new StringFilter();
        }
        return checksum;
    }

    public void setChecksum(StringFilter checksum) {
        this.checksum = checksum;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            createdDate = new ZonedDateTimeFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTimeFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public ZonedDateTimeFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new ZonedDateTimeFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTimeFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public IntegerFilter getVersion() {
        return version;
    }

    public IntegerFilter version() {
        if (version == null) {
            version = new IntegerFilter();
        }
        return version;
    }

    public void setVersion(IntegerFilter version) {
        this.version = version;
    }

    public BooleanFilter getIsDeleted() {
        return isDeleted;
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            isDeleted = new BooleanFilter();
        }
        return isDeleted;
    }

    public void setIsDeleted(BooleanFilter isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public LongFilter getLastModifiedById() {
        return lastModifiedById;
    }

    public LongFilter lastModifiedById() {
        if (lastModifiedById == null) {
            lastModifiedById = new LongFilter();
        }
        return lastModifiedById;
    }

    public void setLastModifiedById(LongFilter lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public LongFilter getOriginatingDepartmentId() {
        return originatingDepartmentId;
    }

    public LongFilter originatingDepartmentId() {
        if (originatingDepartmentId == null) {
            originatingDepartmentId = new LongFilter();
        }
        return originatingDepartmentId;
    }

    public void setOriginatingDepartmentId(LongFilter originatingDepartmentId) {
        this.originatingDepartmentId = originatingDepartmentId;
    }

    public LongFilter getChecksumAlgorithmId() {
        return checksumAlgorithmId;
    }

    public LongFilter checksumAlgorithmId() {
        if (checksumAlgorithmId == null) {
            checksumAlgorithmId = new LongFilter();
        }
        return checksumAlgorithmId;
    }

    public void setChecksumAlgorithmId(LongFilter checksumAlgorithmId) {
        this.checksumAlgorithmId = checksumAlgorithmId;
    }

    public LongFilter getSecurityClearanceId() {
        return securityClearanceId;
    }

    public LongFilter securityClearanceId() {
        if (securityClearanceId == null) {
            securityClearanceId = new LongFilter();
        }
        return securityClearanceId;
    }

    public void setSecurityClearanceId(LongFilter securityClearanceId) {
        this.securityClearanceId = securityClearanceId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentCriteria that = (DocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentTitle, that.documentTitle) &&
            Objects.equals(description, that.description) &&
            Objects.equals(contentType, that.contentType) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(checksum, that.checksum) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(version, that.version) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(lastModifiedById, that.lastModifiedById) &&
            Objects.equals(originatingDepartmentId, that.originatingDepartmentId) &&
            Objects.equals(checksumAlgorithmId, that.checksumAlgorithmId) &&
            Objects.equals(securityClearanceId, that.securityClearanceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentTitle,
            description,
            contentType,
            fileSize,
            checksum,
            createdDate,
            lastModifiedDate,
            version,
            isDeleted,
            createdById,
            lastModifiedById,
            originatingDepartmentId,
            checksumAlgorithmId,
            securityClearanceId,
            distinct
        );
    }

    @Override
    public String toString() {
        return "DocumentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (documentTitle != null ? "documentTitle=" + documentTitle + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (contentType != null ? "contentType=" + contentType + ", " : "") +
            (fileSize != null ? "fileSize=" + fileSize + ", " : "") +
            (checksum != null ? "checksum=" + checksum + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (version != null ? "version=" + version + ", " : "") +
            (isDeleted != null ? "isDeleted=" + isDeleted + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (lastModifiedById != null ? "lastModifiedById=" + lastModifiedById + ", " : "") +
            (originatingDepartmentId != null ? "originatingDepartmentId=" + originatingDepartmentId + ", " : "") +
            (checksumAlgorithmId != null ? "checksumAlgorithmId=" + checksumAlgorithmId + ", " : "") +
            (securityClearanceId != null ? "securityClearanceId=" + securityClearanceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
