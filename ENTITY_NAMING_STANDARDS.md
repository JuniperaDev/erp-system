# Entity Naming Standards

## Overview
This document establishes standardized naming conventions for all entities in the ERP System to ensure consistency, maintainability, and clarity across the codebase.

## Core Principles

### 1. Business Domain Language
- Use clear, business-focused terminology that domain experts understand
- Avoid technical standard prefixes (IFRS16, ISO, GAAP, etc.)
- Prefer descriptive names over abbreviated forms

### 2. Naming Patterns

#### Entity Classes
- **Format**: PascalCase (e.g., `AssetRegistration`, `LeaseContract`)
- **No technical prefixes**: Avoid `IFRS16LeaseContract`, use `DetailedLeaseContract` instead
- **Consistent suffixes**: Use standard patterns for similar concepts

#### Common Suffixes
- `Registration`: For entities that register/record items (e.g., `AssetRegistration`, `WorkInProgressRegistration`)
- `Report`: For report entities (e.g., `DepreciationReport`, `AssetReport`)
- `ReportItem`: For individual report line items (e.g., `AssetReportItem`, `DepreciationReportItem`)
- `Metadata`: For configuration/metadata entities (e.g., `ContractMetadata`, `LeaseMetadata`)
- `Type`: For classification entities (e.g., `AssetType`, `ContractType`)
- `Status`: For status tracking entities (e.g., `ContractStatus`, `ProcessStatus`)

#### Abbreviations
- **ROU**: Right-of-Use (always uppercase, e.g., `ROUAssetReport`)
- **NBV**: Net Book Value (always uppercase, e.g., `NBVReport`)
- **WIP**: Work In Progress (always uppercase, e.g., `WIPReport`)
- **CRB**: Credit Reference Bureau (always uppercase)
- **TA**: Transaction Account (always uppercase)

### 3. Database Naming
- Table names: snake_case (e.g., `asset_registration`, `lease_contract`)
- Column names: snake_case (e.g., `asset_number`, `lease_title`)
- Index names: Follow pattern `idx_{table}_{column(s)}`

### 4. API Naming
- REST endpoints: kebab-case (e.g., `/api/asset-registrations`, `/api/lease-contracts`)
- JSON properties: camelCase (e.g., `assetNumber`, `leaseTitle`)

## Specific Naming Decisions

### Lease Entities
- `LeaseContract`: Basic lease contract information
- `DetailedLeaseContract`: Comprehensive lease contract with IFRS16 compliance details
- `LeasePayment`: Individual lease payment records
- `LeaseMetadata`: Lease configuration and metadata

### Asset Entities
- `AssetRegistration`: Asset registration and basic information
- `WorkInProgressRegistration`: Work-in-progress asset registration
- `ROUAssetReport`: Right-of-Use asset reports (not `RouAssetReport`)

### Report Entities
- Pattern: `{Domain}Report` for main reports, `{Domain}ReportItem` for line items
- Examples: `DepreciationReport`/`DepreciationReportItem`, `AssetReport`/`AssetReportItem`

## Migration Guidelines

### When Renaming Entities
1. Update JHipster entity configuration (`.jhipster/{EntityName}.json`)
2. Create Liquibase migration for database changes
3. Update all generated code (services, repositories, DTOs, controllers)
4. Update test files and mock configurations
5. Update API documentation
6. Maintain backward compatibility where possible

### Backward Compatibility
- Provide API aliases for renamed endpoints during transition period
- Document breaking changes in migration notes
- Use database views for legacy table name support if needed

## Validation Checklist

Before finalizing entity names:
- [ ] Name uses business domain language
- [ ] No technical standard prefixes
- [ ] Follows established suffix patterns
- [ ] Abbreviations use correct capitalization
- [ ] Database naming follows snake_case convention
- [ ] API endpoints follow kebab-case convention
- [ ] All related files updated consistently

## Examples

### Good Examples
```java
// Clear business domain names
public class AssetRegistration { }
public class DetailedLeaseContract { }
public class ROUAssetNBVReportItem { }
public class WorkInProgressRegistration { }
```

### Avoid
```java
// Technical prefixes
public class IFRS16LeaseContract { }
public class ISOCurrencyCode { }

// Inconsistent capitalization
public class RouAssetReport { }  // Should be ROUAssetReport
public class rouDepreciationEntry { }  // Should be ROUDepreciationEntry
```

## Enforcement

These standards are enforced through:
- Code review process
- Automated naming convention checks
- JHipster entity generation templates
- Database migration review process

## References

- [DEVELOPMENT_METHODOLOGY.md](./DEVELOPMENT_METHODOLOGY.md)
- [Domain Model Improvements](./modernization/architecture/DOMAIN_MODEL_IMPROVEMENTS.md)
- [Domain Improvements Backlog](./modernization/backlog/DOMAIN_IMPROVEMENTS_BACKLOG.md)
