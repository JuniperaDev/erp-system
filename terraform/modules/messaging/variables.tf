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

variable "sku" {
  description = "SKU for Event Hub namespace"
  type        = string
  default     = "Standard"
  validation {
    condition     = contains(["Basic", "Standard", "Premium"], var.sku)
    error_message = "SKU must be Basic, Standard, or Premium."
  }
}

variable "capacity" {
  description = "Throughput units for Event Hub namespace"
  type        = number
  default     = 2
  validation {
    condition     = var.capacity >= 1 && var.capacity <= 20
    error_message = "Capacity must be between 1 and 20."
  }
}

variable "auto_inflate_enabled" {
  description = "Enable auto-inflate for Event Hub namespace"
  type        = bool
  default     = true
}

variable "maximum_throughput_units" {
  description = "Maximum throughput units when auto-inflate is enabled"
  type        = number
  default     = 10
}

variable "partition_count" {
  description = "Number of partitions for Event Hubs"
  type        = number
  default     = 4
  validation {
    condition     = var.partition_count >= 1 && var.partition_count <= 32
    error_message = "Partition count must be between 1 and 32."
  }
}

variable "message_retention" {
  description = "Message retention period in days"
  type        = number
  default     = 7
  validation {
    condition     = var.message_retention >= 1 && var.message_retention <= 7
    error_message = "Message retention must be between 1 and 7 days for Standard SKU."
  }
}
