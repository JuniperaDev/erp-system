variable "environment" {
  description = "Environment name (dev, staging, prod)"
  type        = string
  validation {
    condition     = contains(["dev", "staging", "prod"], var.environment)
    error_message = "Environment must be one of: dev, staging, prod."
  }
}

variable "location" {
  description = "Azure region for resources"
  type        = string
  default     = "East US 2"
}

variable "resource_group_name" {
  description = "Name of the resource group"
  type        = string
}

variable "project_name" {
  description = "Name of the project"
  type        = string
  default     = "erp-system"
}

variable "tags" {
  description = "Common tags for all resources"
  type        = map(string)
  default = {
    Project     = "ERP System"
    ManagedBy   = "Terraform"
    Environment = ""
  }
}

variable "kubernetes_version" {
  description = "Kubernetes version for AKS cluster"
  type        = string
  default     = "1.28"
}

variable "node_count" {
  description = "Initial number of nodes in the AKS cluster"
  type        = number
  default     = 3
}

variable "min_node_count" {
  description = "Minimum number of nodes for auto-scaling"
  type        = number
  default     = 2
}

variable "max_node_count" {
  description = "Maximum number of nodes for auto-scaling"
  type        = number
  default     = 10
}

variable "vm_size" {
  description = "Size of the Virtual Machine for AKS nodes"
  type        = string
  default     = "Standard_D4s_v3"
}

variable "database_sku_name" {
  description = "SKU name for PostgreSQL database"
  type        = string
  default     = "GP_Standard_D4s_v3"
}

variable "database_storage_mb" {
  description = "Storage size in MB for PostgreSQL database"
  type        = number
  default     = 32768
}

variable "enable_high_availability" {
  description = "Enable high availability for production environments"
  type        = bool
  default     = false
}

variable "backup_retention_days" {
  description = "Backup retention period in days"
  type        = number
  default     = 7
}
