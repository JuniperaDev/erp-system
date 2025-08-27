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

variable "resource_group_id" {
  description = "ID of the resource group"
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

variable "aks_identity_principal_id" {
  description = "Principal ID of the AKS managed identity"
  type        = string
  default     = null
}

variable "network_security_group_name" {
  description = "Name of the network security group for additional rules"
  type        = string
  default     = null
}

variable "tags" {
  description = "Tags to apply to resources"
  type        = map(string)
  default     = {}
}

variable "key_vault_sku" {
  description = "SKU for Key Vault"
  type        = string
  default     = "standard"
  validation {
    condition     = contains(["standard", "premium"], var.key_vault_sku)
    error_message = "Key Vault SKU must be standard or premium."
  }
}

variable "soft_delete_retention_days" {
  description = "Soft delete retention period in days"
  type        = number
  default     = 90
  validation {
    condition     = var.soft_delete_retention_days >= 7 && var.soft_delete_retention_days <= 90
    error_message = "Soft delete retention must be between 7 and 90 days."
  }
}

variable "enable_purge_protection" {
  description = "Enable purge protection for Key Vault"
  type        = bool
  default     = true
}

variable "admin_group_object_ids" {
  description = "Object IDs of Azure AD groups that should have admin access"
  type        = list(string)
  default     = []
}

variable "create_security_rules" {
  description = "Create additional network security rules"
  type        = bool
  default     = false
}

variable "enable_policy_assignments" {
  description = "Enable Azure Policy assignments"
  type        = bool
  default     = true
}

variable "allowed_locations" {
  description = "List of allowed Azure regions"
  type        = list(string)
  default     = ["East US 2", "West US 2", "Central US"]
}
