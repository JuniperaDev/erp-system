# Asset Management Bounded Context

## Context Overview

The Asset Management bounded context is responsible for the complete lifecycle management of fixed assets from acquisition through disposal. This context encapsulates all asset-related business logic and maintains the integrity of asset data throughout its lifecycle.

## Business Responsibilities

- Asset registration and categorization
- Asset accessories and warranties management
- Asset disposal and revaluation processes
- Asset master data maintenance
- Asset lifecycle tracking and reporting

## Core Domain Entities

### Aggregate Roots

#### AssetRegistration
- **Purpose**: Central aggregate root for all asset-related operations
- **Key Attributes**: 
  - `assetNumber` (unique identifier)
  - `assetTag` (physical identifier)
  - `assetCost` (acquisition cost)
  - `capitalizationDate` (when asset becomes operational)
  - `historicalCost` (original cost basis)
- **Business Rules**:
  - Asset number must be unique across the system
  - Capitalization date cannot be in the future
  - Asset cost must be positive

### Supporting Entities

#### AssetCategory
- **Purpose**: Categorization and depreciation method assignment
- **Key Attributes**: `categoryName`, `depreciationMethod`
- **Relationship**: One-to-many with AssetRegistration

#### AssetAccessory
- **Purpose**: Track additional components and accessories
- **Key Attributes**: `accessoryDetails`, `modelNumber`, `serialNumber`
- **Relationship**: Many-to-many with AssetRegistration

#### AssetWarranty
- **Purpose**: Warranty information and coverage tracking
- **Key Attributes**: `warrantyDetails`, `warrantyPeriod`, `warrantyExpiry`
- **Relationship**: Many-to-many with AssetRegistration

#### AssetDisposal
- **Purpose**: Asset disposal transactions and methods
- **Key Attributes**: `disposalMethod`, `disposalDate`, `disposalProceeds`
- **Relationship**: One-to-many with AssetRegistration

#### AssetRevaluation
- **Purpose**: Asset revaluation adjustments
- **Key Attributes**: `revaluationDate`, `revaluationAmount`, `revaluationReference`
- **Relationship**: One-to-many with AssetRegistration

## External Dependencies

### Financial Core Context
- **Settlement**: Required for asset acquisition transactions
- **Dealer**: Vendor/supplier information for asset purchases
- **SettlementCurrency**: Currency information for asset costs

### Shared Kernel
- **ServiceOutlet**: Location/branch information for asset assignment
- **Placeholder**: Generic reference data and metadata
- **ApplicationUser**: User information for asset assignments

## Context Boundaries

### What's Inside the Context
- All asset-related entities and their lifecycle management
- Asset categorization and classification logic
- Asset valuation and revaluation processes
- Asset disposal and write-off procedures
- Asset accessory and warranty management

### What's Outside the Context
- Financial transaction processing (Financial Core)
- Depreciation calculations (Depreciation Context)
- Lease-specific asset handling (IFRS16 Leasing Context)
- Work-in-progress asset creation (Work-in-Progress Context)

## Integration Points

### Inbound Dependencies
- **Asset Acquisition**: Receives settlement information from Financial Core
- **Asset Creation**: Receives completed WIP transfers from Work-in-Progress Context
- **Asset Assignment**: Uses ServiceOutlet information from Shared Kernel

### Outbound Events
- **AssetRegistered**: Published when new asset is registered
- **AssetCategoryChanged**: Published when asset category is modified
- **AssetDisposed**: Published when asset is disposed
- **AssetRevalued**: Published when asset is revalued

### Anti-Corruption Layers Required
- **Settlement Integration**: Translate financial settlement data to asset acquisition context
- **ServiceOutlet Integration**: Adapt shared location data to asset-specific needs
- **Dealer Integration**: Transform vendor data for asset management purposes

## Data Consistency Requirements

### Strong Consistency
- Asset registration and core asset data
- Asset-category relationships
- Asset disposal transactions

### Eventual Consistency
- Asset-settlement relationships (via events)
- Asset reporting data synchronization
- Cross-context asset references

## API Boundaries

### Public Interface
```yaml
POST   /api/assets                    # Create asset
GET    /api/assets/{id}              # Get asset details
PUT    /api/assets/{id}              # Update asset
DELETE /api/assets/{id}              # Dispose asset
GET    /api/assets                   # List assets with filters
POST   /api/assets/{id}/accessories  # Add accessory
POST   /api/assets/{id}/warranties   # Add warranty
POST   /api/assets/{id}/revaluations # Create revaluation
```

### Internal Services
- AssetRegistrationService
- AssetCategoryService
- AssetAccessoryService
- AssetWarrantyService
- AssetDisposalService
- AssetRevaluationService

## Business Rules and Invariants

1. **Asset Uniqueness**: Each asset must have a unique asset number
2. **Cost Validation**: Asset cost must be positive and in valid currency
3. **Date Consistency**: Capitalization date must not precede registration date
4. **Category Assignment**: Every asset must belong to a valid category
5. **Disposal Rules**: Disposed assets cannot be modified except for disposal reversal
6. **Revaluation Rules**: Revaluations must have proper authorization and documentation

## Migration Considerations

### Current State
- All asset entities currently reside in the monolithic domain package
- Strong coupling with Settlement entities through direct JPA relationships
- Mixed responsibilities with depreciation and reporting logic

### Target State
- Isolated asset management module with clear boundaries
- Event-driven integration with other contexts
- Independent deployment and scaling capability

### Migration Risks
- Complex Settlement-Asset relationships require careful decoupling
- Existing reports span multiple contexts and need refactoring
- Asset disposal processes involve financial transactions

## Team Ownership

- **Primary Team**: Asset Management Team
- **Domain Expert**: Asset Manager
- **Technical Lead**: Senior Developer with asset domain knowledge
- **Stakeholders**: Finance Team (for asset valuation), Operations Team (for asset tracking)
