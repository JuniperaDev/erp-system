# Work-in-Progress Bounded Context

## Context Overview

The Work-in-Progress (WIP) bounded context is responsible for tracking project work and capital expenditure from initiation through completion and transfer to fixed assets. This context manages the accumulation of costs, project progress tracking, and the eventual capitalization of completed projects as fixed assets.

## Business Responsibilities

- Work-in-progress registration and tracking
- Project cost accumulation and management
- Progress monitoring and completion tracking
- Transfer to fixed assets upon completion
- Project-based financial reporting
- WIP valuation and inventory management
- Project milestone and deliverable tracking

## Core Domain Entities

### Aggregate Roots

#### WorkInProgressRegistration
- **Purpose**: Central aggregate for WIP item management
- **Key Attributes**:
  - `sequenceNumber` (unique WIP identifier)
  - `particulars` (project description)
  - `instalmentDate` (cost incurrence date)
  - `instalmentAmount` (cost amount)
  - `levelOfCompletion` (progress percentage)
  - `completed` (completion status)
- **Business Rules**:
  - Sequence number must be unique
  - Instalment amount must be positive
  - Level of completion must be between 0 and 100
  - Completed projects cannot accept new costs

#### WorkProjectRegister
- **Purpose**: Project master data and management
- **Key Attributes**: `projectTitle`, `projectDescription`, `expectedCompletion`
- **Business Rules**:
  - Project titles must be unique
  - Expected completion dates must be realistic

### Supporting Entities

#### WorkInProgressTransfer
- **Purpose**: Transfer of completed WIP to fixed assets
- **Key Attributes**: `transferDate`, `transferAmount`, `targetAssetCategory`
- **Relationship**: Related to WorkInProgressRegistration

#### WorkInProgressOverview
- **Purpose**: Aggregated WIP reporting and analytics
- **Key Attributes**: `totalWIPValue`, `numberOfProjects`, `averageCompletion`
- **Relationship**: Computed from WorkInProgressRegistration data

## External Dependencies

### Asset Management Context
- **AssetCategory**: Target category for WIP transfers
- **AssetAccessory**: Related accessories for WIP projects
- **AssetWarranty**: Warranty information for completed projects

### Financial Core Context
- **Settlement**: Payment processing for WIP costs
- **PaymentInvoice**: Invoice management for project expenses
- **PurchaseOrder**: Purchase orders for project materials
- **Dealer**: Vendor management for project suppliers

### Shared Kernel
- **ServiceOutlet**: Project location and branch assignment
- **SettlementCurrency**: Multi-currency project support
- **BusinessDocument**: Project documentation management

## Context Boundaries

### What's Inside the Context
- WIP registration and lifecycle management
- Project cost accumulation and tracking
- Progress monitoring and completion status
- WIP-to-asset transfer processes
- Project-based cost allocation
- WIP inventory valuation

### What's Outside the Context
- Final asset registration (Asset Management)
- Payment processing infrastructure (Financial Core)
- Asset depreciation calculations (Depreciation)
- Project financial reporting (Reporting)

## Integration Points

### Inbound Dependencies
- **Project Payments**: Receives settlement data from Financial Core
- **Material Purchases**: Receives purchase order and invoice data
- **Project Documentation**: Uses document management from Shared Kernel

### Outbound Events
- **WIPRegistered**: Published when new WIP item is created
- **WIPProgressUpdated**: Published when completion level changes
- **WIPCompleted**: Published when project is marked complete
- **WIPTransferred**: Published when WIP is transferred to assets

### Anti-Corruption Layers Required
- **Financial Integration**: Translate payment data to project cost context
- **Asset Integration**: Transform WIP data for asset creation
- **Vendor Integration**: Adapt supplier data for project management

## Data Consistency Requirements

### Strong Consistency
- WIP cost accumulation and amounts
- Project completion status
- WIP transfer transactions
- Project-vendor relationships

### Eventual Consistency
- Cross-context payment confirmations
- Asset creation from WIP transfers
- Reporting data synchronization

## API Boundaries

### Public Interface
```yaml
POST   /api/wip/registrations        # Create WIP item
GET    /api/wip/registrations/{id}   # Get WIP details
PUT    /api/wip/registrations/{id}   # Update WIP
POST   /api/wip/transfers            # Transfer to asset
GET    /api/wip/projects             # List projects
POST   /api/wip/projects             # Create project
GET    /api/wip/overview             # WIP overview data
PUT    /api/wip/registrations/{id}/progress # Update progress
```

### Internal Services
- WorkInProgressRegistrationService
- WorkProjectRegisterService
- WorkInProgressTransferService
- WorkInProgressOverviewService

## Business Rules and Invariants

1. **Cost Accumulation**: WIP costs must be properly allocated to projects
2. **Progress Tracking**: Completion percentages must be realistic and validated
3. **Transfer Rules**: Only completed projects can be transferred to assets
4. **Currency Consistency**: All project costs must use consistent currency
5. **Vendor Validation**: All project suppliers must be validated vendors
6. **Documentation**: Projects must have proper supporting documentation

## Project Lifecycle Management

### WIP Stages
1. **Initiation**: Project setup and initial registration
2. **In Progress**: Cost accumulation and progress tracking
3. **Near Completion**: Final cost allocation and validation
4. **Completed**: Project closure and transfer preparation
5. **Transferred**: Conversion to fixed asset

### Cost Accumulation Rules
- **Direct Costs**: Materials, labor, and direct expenses
- **Indirect Costs**: Allocated overhead and administrative costs
- **Currency Handling**: Multi-currency project support
- **Cost Validation**: Proper authorization and documentation

## Transfer to Assets Process

### Transfer Criteria
- Project must be marked as completed
- All costs must be properly documented
- Transfer authorization must be obtained
- Target asset category must be specified

### Transfer Process
1. **Validation**: Verify completion and documentation
2. **Cost Calculation**: Compute total project cost
3. **Asset Creation**: Create asset registration in Asset Management
4. **WIP Closure**: Mark WIP as transferred and close
5. **Audit Trail**: Generate complete transfer documentation

## Migration Considerations

### Current State
- WIP entities mixed with asset and financial logic
- Manual project tracking and cost accumulation
- Inconsistent transfer processes to fixed assets

### Target State
- Dedicated project management with automated cost tracking
- Streamlined WIP-to-asset transfer processes
- Integrated project progress monitoring

### Migration Risks
- Complex multi-context cost allocation requires careful mapping
- Historical WIP data validation and migration
- Integration with existing asset and financial systems

## Project Reporting and Analytics

- **Cost Analysis**: Project cost breakdown and variance analysis
- **Progress Tracking**: Completion status and milestone reporting
- **Transfer Reports**: WIP-to-asset transfer summaries
- **Inventory Valuation**: WIP inventory value and aging analysis

## Team Ownership

- **Primary Team**: Project Management Team
- **Domain Expert**: Project Manager/Capital Projects Specialist
- **Technical Lead**: Senior Developer with project management expertise
- **Stakeholders**: Finance Team, Asset Managers, Operations Team
