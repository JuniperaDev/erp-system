variable "project_name" {
  description = "Name of the project"
  type        = string
}

variable "environment" {
  description = "Environment name"
  type        = string
}

variable "location" {
  description = "Azure region"
  type        = string
}

variable "resource_group_name" {
  description = "Name of the resource group"
  type        = string
}

variable "aks_subnet_id" {
  description = "ID of the AKS subnet for network rules"
  type        = string
}

variable "private_endpoint_subnet_id" {
  description = "ID of the subnet for private endpoints"
  type        = string
}

variable "key_vault_id" {
  description = "ID of the Key Vault for storing secrets"
  type        = string
  default     = null
}

variable "tags" {
  description = "Tags to apply to resources"
  type        = map(string)
  default     = {}
}

variable "account_tier" {
  description = "Storage account tier"
  type        = string
  default     = "Standard"
  validation {
    condition     = contains(["Standard", "Premium"], var.account_tier)
    error_message = "Account tier must be Standard or Premium."
  }
}

variable "replication_type" {
  description = "Storage account replication type"
  type        = string
  default     = "LRS"
  validation {
    condition     = contains(["LRS", "GRS", "RAGRS", "ZRS", "GZRS", "RAGZRS"], var.replication_type)
    error_message = "Replication type must be one of: LRS, GRS, RAGRS, ZRS, GZRS, RAGZRS."
  }
}

variable "enable_versioning" {
  description = "Enable blob versioning"
  type        = bool
  default     = true
}

variable "enable_change_feed" {
  description = "Enable blob change feed"
  type        = bool
  default     = true
}

variable "blob_retention_days" {
  description = "Blob soft delete retention period in days"
  type        = number
  default     = 30
}

variable "container_retention_days" {
  description = "Container soft delete retention period in days"
  type        = number
  default     = 30
}

variable "lifecycle_cool_after_days" {
  description = "Days after which to move blobs to cool tier"
  type        = number
  default     = 30
}

variable "lifecycle_archive_after_days" {
  description = "Days after which to move blobs to archive tier"
  type        = number
  default     = 90
}

variable "lifecycle_delete_after_days" {
  description = "Days after which to delete blobs"
  type        = number
  default     = 2555
}

variable "lifecycle_snapshot_delete_days" {
  description = "Days after which to delete blob snapshots"
  type        = number
  default     = 90
}

variable "lifecycle_version_delete_days" {
  description = "Days after which to delete blob versions"
  type        = number
  default     = 90
}

variable "lifecycle_reports_cool_days" {
  description = "Days after which to move reports to cool tier"
  type        = number
  default     = 7
}

variable "lifecycle_reports_archive_days" {
  description = "Days after which to move reports to archive tier"
  type        = number
  default     = 30
}

variable "lifecycle_reports_delete_days" {
  description = "Days after which to delete reports"
  type        = number
  default     = 365
}

variable "lifecycle_audit_archive_days" {
  description = "Days after which to move audit logs to archive tier"
  type        = number
  default     = 90
}

variable "lifecycle_backup_cool_days" {
  description = "Days after which to move backups to cool tier"
  type        = number
  default     = 7
}

variable "lifecycle_backup_archive_days" {
  description = "Days after which to move backups to archive tier"
  type        = number
  default     = 30
}

variable "lifecycle_backup_delete_days" {
  description = "Days after which to delete backups"
  type        = number
  default     = 1095
}
