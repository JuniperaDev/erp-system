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

variable "database_subnet_id" {
  description = "ID of the database subnet"
  type        = string
}

variable "private_dns_zone_id" {
  description = "ID of the private DNS zone"
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

variable "postgres_version" {
  description = "PostgreSQL version"
  type        = string
  default     = "14"
}

variable "admin_username" {
  description = "Administrator username for PostgreSQL"
  type        = string
  default     = "erpadmin"
}

variable "admin_password" {
  description = "Administrator password for PostgreSQL"
  type        = string
  sensitive   = true
}

variable "sku_name" {
  description = "SKU name for PostgreSQL server"
  type        = string
  default     = "GP_Standard_D4s_v3"
}

variable "storage_mb" {
  description = "Storage size in MB"
  type        = number
  default     = 32768
}

variable "backup_retention_days" {
  description = "Backup retention period in days"
  type        = number
  default     = 7
}

variable "enable_geo_redundant_backup" {
  description = "Enable geo-redundant backup"
  type        = bool
  default     = false
}

variable "enable_high_availability" {
  description = "Enable high availability"
  type        = bool
  default     = false
}

variable "availability_zone" {
  description = "Availability zone for the primary server"
  type        = string
  default     = "1"
}

variable "standby_availability_zone" {
  description = "Availability zone for the standby server"
  type        = string
  default     = "2"
}

variable "maintenance_window" {
  description = "Maintenance window configuration"
  type = object({
    day_of_week  = number
    start_hour   = number
    start_minute = number
  })
  default = {
    day_of_week  = 0 # Sunday
    start_hour   = 2 # 2 AM
    start_minute = 0
  }
}

variable "enable_firewall_rules" {
  description = "Enable firewall rules for AKS subnet"
  type        = bool
  default     = false
}

variable "aks_subnet_cidr" {
  description = "CIDR block of the AKS subnet"
  type        = string
  default     = "10.0.1.0/24"
}
