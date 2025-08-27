# IFRS16 Leasing Bounded Context

## Context Overview

The IFRS16 Leasing bounded context is responsible for lease accounting compliance according to International Financial Reporting Standard 16. This context manages lease contracts, calculates lease liabilities, processes lease payments, and handles Right-of-Use (ROU) asset depreciation.

## Business Responsibilities

- Lease contract management and lifecycle
- IFRS16 compliance calculations and reporting
- Lease liability computation and tracking
- Lease payment processing and scheduling
- Right-of-Use asset depreciation
- Lease amortization schedule generation
- Lease accounting policy enforcement

## Core Domain Entities

### Aggregate Roots

#### DetailedLeaseContract
- **Purpose**: Central aggregate for lease contract management
- **Key Attributes**:
  - `bookingId` (unique lease identifier)
  - `leaseTitle` (contract title)
  - `shortTitle` (abbreviated reference)
  - `inceptionDate` (contract inception)
  - `commencementDate` (lease commencement)
  - `serialNumber` (UUID for tracking)
- **Business Rules**:
  - Booking ID must be unique across all contracts
  - Commencement date cannot precede inception date
  - Contract must have valid reporting periods

#### LeaseLiability
- **Purpose**: IFRS16 lease liability calculations
- **Key Attributes**: `liabilityAmount`, `interestRate`, `calculationDate`
- **Business Rules**:
  - Liability amount must reflect present value of lease payments
  - Interest rate must be positive
  - Calculations must follow IFRS16 standards

### Supporting Entities

#### LeasePayment
- **Purpose**: Individual lease payment tracking
- **Key Attributes**: `paymentAmount`, `paymentDate`, `paymentPeriod`
- **Relationship**: Many-to-one with DetailedLeaseContract

#### LeaseAmortizationSchedule
- **Purpose**: Amortization schedule management
- **Key Attributes**: `scheduleDate`, `principalAmount`, `interestAmount`
- **Relationship**: One-to-many with LeaseLiabilityScheduleItem

#### LeaseLiabilityScheduleItem
- **Purpose**: Individual schedule line items
- **Key Attributes**: `periodStartDate`, `periodEndDate`, `paymentAmount`
- **Relationship**: Many-to-one with LeaseAmortizationSchedule

#### RouDepreciationEntry
- **Purpose**: Right-of-Use asset depreciation
- **Key Attributes**: `depreciationAmount`, `depreciationDate`, `netBookValue`
- **Relationship**: Related to lease contract through business logic

## External Dependencies

### Financial Core Context
- **Dealer**: Lessor information and management
- **Settlement**: Lease payment processing
- **SettlementCurrency**: Multi-currency lease support

### Shared Kernel
- **ServiceOutlet**: Lease location and branch assignment
- **FiscalMonth**: Reporting period management
- **FiscalQuarter**: Quarterly reporting alignment
- **BusinessDocument**: Lease contract documentation

### Depreciation Context
- **DepreciationMethod**: ROU asset depreciation methods
- **DepreciationPeriod**: Alignment with depreciation cycles

## Context Boundaries

### What's Inside the Context
- Lease contract lifecycle management
- IFRS16 compliance calculations
- Lease liability computations
- Lease payment scheduling and tracking
- ROU asset depreciation specific to leases
- Lease amortization schedules
- Lease accounting policy enforcement

### What's Outside the Context
- General asset depreciation (Depreciation Context)
- Payment processing infrastructure (Financial Core)
- General asset management (Asset Management)
- Financial reporting aggregation (Reporting)

## Integration Points

### Inbound Dependencies
- **Lease Payments**: Receives payment instructions from Financial Core
- **Fiscal Periods**: Uses fiscal calendar from Shared Kernel
- **Lessor Information**: Receives vendor data from Financial Core

### Outbound Events
- **LeaseContractCreated**: Published when new lease is established
- **LeasePaymentMade**: Published when lease payment is processed
- **LeaseLiabilityCalculated**: Published when liability is recalculated
- **RouDepreciationCalculated**: Published when ROU depreciation is computed

### Anti-Corruption Layers Required
- **Financial Integration**: Translate payment data to lease-specific context
- **Fiscal Calendar Integration**: Adapt fiscal periods to lease reporting needs
- **Vendor Integration**: Transform lessor data for lease management

## Data Consistency Requirements

### Strong Consistency
- Lease contract terms and conditions
- Lease liability calculations
- Lease payment schedules
- ROU asset depreciation

### Eventual Consistency
- Cross-context payment confirmations
- Reporting data synchronization
- Audit trail propagation

## API Boundaries

### Public Interface
```yaml
POST   /api/lease-contracts          # Create lease contract
GET    /api/lease-contracts/{id}     # Get contract details
PUT    /api/lease-contracts/{id}     # Update contract
POST   /api/lease-contracts/{id}/payments # Record payment
GET    /api/lease-liabilities        # List liabilities
POST   /api/lease-liabilities/calculate # Recalculate liabilities
GET    /api/amortization-schedules   # Get schedules
POST   /api/amortization-schedules/generate # Generate schedule
GET    /api/rou-depreciation         # ROU depreciation entries
```

### Internal Services
- DetailedLeaseContractService
- LeaseLiabilityService
- LeasePaymentService
- LeaseAmortizationService
- RouDepreciationService

## Business Rules and Invariants

1. **IFRS16 Compliance**: All calculations must follow IFRS16 standards
2. **Contract Integrity**: Lease terms must be consistent and valid
3. **Payment Scheduling**: Payments must align with contract terms
4. **Liability Accuracy**: Lease liabilities must reflect present value calculations
5. **Depreciation Consistency**: ROU depreciation must align with lease terms
6. **Reporting Alignment**: All calculations must align with fiscal periods

## IFRS16 Specific Requirements

### Initial Recognition
- Recognition of lease liability at present value of lease payments
- Recognition of ROU asset at cost (lease liability + prepayments + initial direct costs)
- Proper classification of lease vs. service components

### Subsequent Measurement
- Lease liability measured using effective interest method
- ROU asset depreciated over shorter of lease term or useful life
- Reassessment of lease liability when contract terms change

### Disclosure Requirements
- Maturity analysis of lease liabilities
- Reconciliation of opening and closing balances
- Lease expense breakdown and analysis

## Migration Considerations

### Current State
- Lease entities scattered across domain package
- Mixed lease and general asset depreciation logic
- Manual IFRS16 compliance processes

### Target State
- Dedicated lease accounting context with IFRS16 automation
- Integrated lease liability and ROU depreciation calculations
- Automated compliance reporting and disclosures

### Migration Risks
- Complex IFRS16 calculation logic requires careful validation
- Integration with existing financial and asset systems
- Historical lease data migration and reconciliation

## Compliance and Audit

- **IFRS16 Standards**: Full compliance with international standards
- **Audit Trail**: Complete transaction and calculation audit trails
- **Documentation**: Comprehensive lease contract and calculation documentation
- **Validation**: Automated validation of IFRS16 calculations

## Team Ownership

- **Primary Team**: Lease Accounting Team
- **Domain Expert**: IFRS16 Specialist/Lease Accountant
- **Technical Lead**: Senior Developer with lease accounting expertise
- **Stakeholders**: External Auditors, Financial Reporting Team, Treasury Team
