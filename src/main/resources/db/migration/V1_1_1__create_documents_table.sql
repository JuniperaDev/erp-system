
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    document_title VARCHAR(255) NOT NULL,
    description TEXT,
    document_serial UUID NOT NULL UNIQUE,
    file_path VARCHAR(500) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    checksum VARCHAR(128) NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP WITH TIME ZONE,
    version INTEGER DEFAULT 1,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_by_id BIGINT NOT NULL,
    last_modified_by_id BIGINT,
    originating_department_id BIGINT NOT NULL,
    checksum_algorithm_id BIGINT NOT NULL,
    security_clearance_id BIGINT NOT NULL
);

CREATE TABLE file_metadata (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    storage_path VARCHAR(500) NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    encoding VARCHAR(50),
    upload_token VARCHAR(255),
    processing_status VARCHAR(50) DEFAULT 'PENDING',
    error_message TEXT,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

CREATE INDEX idx_documents_document_serial ON documents(document_serial);
CREATE INDEX idx_documents_created_by ON documents(created_by_id);
CREATE INDEX idx_documents_originating_department ON documents(originating_department_id);
CREATE INDEX idx_documents_security_clearance ON documents(security_clearance_id);
CREATE INDEX idx_documents_created_date ON documents(created_date);
CREATE INDEX idx_documents_is_deleted ON documents(is_deleted);
CREATE INDEX idx_file_metadata_document_id ON file_metadata(document_id);
CREATE INDEX idx_file_metadata_upload_token ON file_metadata(upload_token);


CREATE SEQUENCE document_serial_seq START 1;

COMMENT ON TABLE documents IS 'Core document management table for the extracted document service';
COMMENT ON TABLE file_metadata IS 'Additional metadata for document files';
COMMENT ON COLUMN documents.document_serial IS 'Unique identifier for document tracking';
COMMENT ON COLUMN documents.checksum IS 'File integrity checksum (MD5/SHA-512)';
COMMENT ON COLUMN documents.version IS 'Document version for optimistic locking';
COMMENT ON COLUMN documents.is_deleted IS 'Soft delete flag for audit trail';
