# Context Map and Inter-Context Relationships

## Overview

This document defines the relationships and communication patterns between the six bounded contexts in the ERP System. It serves as the integration blueprint for the microservices architecture, specifying how contexts interact while maintaining their autonomy.

## Context Relationship Types

### Shared Kernel
Common entities and concepts shared across multiple contexts:
- **ServiceOutlet**: Location and branch information
- **SettlementCurrency**: Currency and exchange rate data
- **FiscalMonth/Quarter/Year**: Fiscal calendar and reporting periods
- **Placeholder**: Generic reference data and metadata
- **ApplicationUser**: User information and authorization
- **BusinessDocument**: Document management and attachments

### Customer-Supplier Relationships
Contexts with clear upstream/downstream dependencies:

#### Financial Core → Asset Management
- **Relationship**: Customer-Supplier
- **Direction**: Financial Core supplies settlement data to Asset Management
- **Integration**: Settlement events trigger asset acquisition processes
- **Anti-Corruption Layer**: Asset Management translates settlement data to asset context

#### Work-in-Progress → Asset Management
- **Relationship**: Customer-Supplier
- **Direction**: WIP supplies completed project data to Asset Management
- **Integration**: WIP completion events trigger asset creation
- **Anti-Corruption Layer**: Asset Management transforms WIP data to asset registration format

#### Asset Management → Depreciation
- **Relationship**: Customer-Supplier
- **Direction**: Asset Management supplies asset data to Depreciation
- **Integration**: Asset events trigger depreciation calculations
- **Anti-Corruption Layer**: Depreciation context adapts asset data for calculation purposes

### Conformist Relationships
Contexts that conform to shared standards:

#### All Contexts → Reporting
- **Relationship**: Conformist
- **Direction**: All contexts supply data to Reporting
- **Integration**: Domain events feed reporting data aggregation
- **Anti-Corruption Layer**: Reporting harmonizes data formats across contexts

## Context Integration Patterns

### Synchronous Integration (REST APIs)

#### Asset Management ↔ Financial Core
```yaml
# Asset acquisition validation
GET /api/settlements/{id}/validation
POST /api/assets/{id}/settlement-link

# Vendor information retrieval
GET /api/dealers/{id}
GET /api/dealers/search?criteria=...
```

#### IFRS16 Leasing ↔ Financial Core
```yaml
# Lease payment processing
POST /api/settlements/lease-payments
GET /api/dealers/{id}/lessor-info

# Currency conversion
GET /api/currencies/{code}/rate
```

### Asynchronous Integration (Domain Events)

#### Asset Management Events
```yaml
AssetRegistered:
  - triggers: Depreciation calculation setup
  - consumers: [Depreciation Context, Reporting Context]
  
AssetDisposed:
  - triggers: Depreciation calculation termination
  - consumers: [Depreciation Context, Financial Core, Reporting Context]
  
AssetRevalued:
  - triggers: Depreciation recalculation
  - consumers: [Depreciation Context, Reporting Context]
```

#### Financial Core Events
```yaml
SettlementCreated:
  - triggers: Asset acquisition processing
  - consumers: [Asset Management Context, WIP Context, Reporting Context]
  
PaymentProcessed:
  - triggers: Lease payment recording
  - consumers: [IFRS16 Leasing Context, Reporting Context]
  
VendorCreated:
  - triggers: Vendor data synchronization
  - consumers: [Asset Management Context, WIP Context, IFRS16 Leasing Context]
```

#### IFRS16 Leasing Events
```yaml
LeaseContractCreated:
  - triggers: ROU asset creation, liability calculation
  - consumers: [Asset Management Context, Depreciation Context, Reporting Context]
  
LeasePaymentMade:
  - triggers: Liability adjustment, payment recording
  - consumers: [Financial Core Context, Reporting Context]
```

#### Depreciation Events
```yaml
DepreciationCalculated:
  - triggers: NBV updates, financial reporting
  - consumers: [Asset Management Context, Reporting Context]
  
DepreciationJobCompleted:
  - triggers: Period closure, reporting updates
  - consumers: [Reporting Context, Financial Core Context]
```

#### Work-in-Progress Events
```yaml
WIPCompleted:
  - triggers: Asset creation preparation
  - consumers: [Asset Management Context, Reporting Context]
  
WIPTransferred:
  - triggers: Asset registration, cost capitalization
  - consumers: [Asset Management Context, Financial Core Context, Reporting Context]
```

## Anti-Corruption Layer Specifications

### Asset Management ACLs

#### Settlement Integration ACL
```java
public class SettlementToAssetAcquisitionAdapter {
    public AssetAcquisitionData adapt(SettlementData settlement) {
        return AssetAcquisitionData.builder()
            .acquisitionCost(settlement.getPaymentAmount())
            .acquisitionDate(settlement.getPaymentDate())
            .vendor(adaptDealer(settlement.getBiller()))
            .currency(settlement.getSettlementCurrency())
            .build();
    }
}
```

#### WIP Transfer Integration ACL
```java
public class WIPToAssetTransferAdapter {
    public AssetRegistrationData adapt(WIPTransferData wipTransfer) {
        return AssetRegistrationData.builder()
            .assetCost(wipTransfer.getTotalProjectCost())
            .capitalizationDate(wipTransfer.getCompletionDate())
            .assetDetails(wipTransfer.getProjectDescription())
            .category(determineAssetCategory(wipTransfer))
            .build();
    }
}
```

### Financial Core ACLs

#### Multi-Context Payment Processing ACL
```java
public class PaymentContextAdapter {
    public SettlementData adaptAssetPayment(AssetPurchaseRequest request) {
        return SettlementData.builder()
            .paymentAmount(request.getAssetCost())
            .description("Asset Acquisition: " + request.getAssetTag())
            .biller(request.getVendor())
            .paymentCategory(ASSET_PURCHASE_CATEGORY)
            .build();
    }
    
    public SettlementData adaptLeasePayment(LeasePaymentRequest request) {
        return SettlementData.builder()
            .paymentAmount(request.getPaymentAmount())
            .description("Lease Payment: " + request.getLeaseReference())
            .biller(request.getLessor())
            .paymentCategory(LEASE_PAYMENT_CATEGORY)
            .build();
    }
}
```

### Reporting Context ACLs

#### Cross-Context Data Harmonization ACL
```java
public class ReportingDataHarmonizer {
    public UnifiedAssetData harmonize(
        AssetData assetData,
        DepreciationData depreciationData,
        FinancialData financialData) {
        
        return UnifiedAssetData.builder()
            .assetIdentifier(assetData.getAssetNumber())
            .currentValue(calculateCurrentValue(assetData, depreciationData))
            .acquisitionDetails(harmonizeAcquisition(assetData, financialData))
            .depreciationSummary(harmonizeDepreciation(depreciationData))
            .build();
    }
}
```

## Data Consistency Patterns

### Saga Pattern Implementation

#### Asset Acquisition Saga
```yaml
Steps:
  1. Validate Settlement (Financial Core)
  2. Create Asset Registration (Asset Management)
  3. Setup Depreciation Schedule (Depreciation Context)
  4. Update Reporting Data (Reporting Context)

Compensation:
  - Reverse asset registration if depreciation setup fails
  - Reverse settlement if asset creation fails
```

#### WIP Transfer Saga
```yaml
Steps:
  1. Validate WIP Completion (WIP Context)
  2. Calculate Total Project Cost (WIP Context)
  3. Create Asset Registration (Asset Management)
  4. Mark WIP as Transferred (WIP Context)
  5. Setup Depreciation (Depreciation Context)

Compensation:
  - Reverse WIP transfer status
  - Remove asset registration
  - Restore WIP active status
```

### Event Sourcing for Audit Trail

#### Event Store Structure
```yaml
Events:
  - AssetLifecycleEvents: Registration, modification, disposal
  - FinancialTransactionEvents: Settlements, payments, adjustments
  - DepreciationEvents: Calculations, adjustments, corrections
  - LeaseEvents: Contract creation, payments, modifications
  - WIPEvents: Registration, progress, completion, transfer
```

## Communication Protocols

### Synchronous Communication
- **Protocol**: HTTPS/REST
- **Timeout**: 30 seconds for standard operations, 5 minutes for complex calculations
- **Retry Policy**: Exponential backoff with maximum 3 retries
- **Circuit Breaker**: Fail-fast after 5 consecutive failures

### Asynchronous Communication
- **Message Broker**: Apache Kafka
- **Topic Strategy**: One topic per bounded context (asset.events, financial.events, etc.)
- **Message Format**: JSON with schema registry validation
- **Delivery Guarantee**: At-least-once delivery with idempotent consumers

### Event Schema Examples

#### Asset Registration Event
```json
{
  "eventType": "AssetRegistered",
  "eventId": "uuid",
  "timestamp": "2024-01-15T10:30:00Z",
  "aggregateId": "asset-12345",
  "version": 1,
  "data": {
    "assetNumber": "AST-2024-001",
    "assetTag": "TAG-001",
    "assetCost": 50000.00,
    "currency": "USD",
    "capitalizationDate": "2024-01-15",
    "category": "EQUIPMENT",
    "vendor": "VENDOR-123"
  }
}
```

#### Settlement Created Event
```json
{
  "eventType": "SettlementCreated",
  "eventId": "uuid",
  "timestamp": "2024-01-15T10:25:00Z",
  "aggregateId": "settlement-67890",
  "version": 1,
  "data": {
    "settlementId": "STL-2024-001",
    "paymentAmount": 50000.00,
    "currency": "USD",
    "paymentDate": "2024-01-15",
    "vendor": "VENDOR-123",
    "category": "ASSET_PURCHASE"
  }
}
```

## Context Deployment Dependencies

### Deployment Order
1. **Shared Kernel Services**: ServiceOutlet, Currency, FiscalCalendar
2. **Financial Core**: Foundation for all financial operations
3. **Asset Management**: Depends on Financial Core for settlements
4. **Depreciation**: Depends on Asset Management for asset data
5. **IFRS16 Leasing**: Depends on Financial Core and Asset Management
6. **Work-in-Progress**: Depends on Financial Core and Asset Management
7. **Reporting**: Depends on all other contexts for data aggregation

### Runtime Dependencies
- **Database per Service**: Each context has its own database
- **Shared Reference Data**: Common lookup tables replicated across contexts
- **Event Store**: Centralized event storage for audit and replay
- **Message Broker**: Kafka cluster for asynchronous communication

## Monitoring and Observability

### Context Health Checks
- Individual health endpoints for each context
- Cross-context integration health monitoring
- Event processing lag monitoring
- Data consistency validation checks

### Distributed Tracing
- Trace requests across context boundaries
- Monitor saga execution and compensation
- Track event processing chains
- Identify performance bottlenecks

### Business Metrics
- Asset registration success rates
- Settlement processing times
- Depreciation calculation accuracy
- Report generation performance
- Cross-context data consistency metrics
