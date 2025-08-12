# ERP System Exploratory Testing Guide

## Table of Contents
1. [Overview](#overview)
2. [Authentication Setup](#authentication-setup)
3. [Environment Configuration](#environment-configuration)
4. [Core Domain Areas](#core-domain-areas)
5. [API Endpoint Testing](#api-endpoint-testing)
6. [CRUD Operations Guide](#crud-operations-guide)
7. [Security and Authorization](#security-and-authorization)
8. [Testing Workflows](#testing-workflows)
9. [Common Issues and Troubleshooting](#common-issues-and-troubleshooting)

## Overview

This guide provides comprehensive instructions for exploratory testing of the ERP System using Postman. The system is built with Spring Boot and uses JWT-based authentication with role-based access control.

**Base URL**: `http://localhost:8080` (for local testing)
**API Base Path**: `/api`

## Authentication Setup

### 1. JWT Authentication

The system uses JWT (JSON Web Token) for authentication. All API requests (except public endpoints) require a valid JWT token.

#### Login Endpoint
```bash
POST /api/authenticate
Content-Type: application/json

{
  "username": "your_username",
  "password": "your_password",
  "rememberMe": false
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin",
    "rememberMe": false
  }'
```

**Response:**
```json
{
  "id_token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

#### Using the Token
Add the JWT token to all subsequent requests:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### 2. User Registration (if enabled)
```bash
POST /api/register
Content-Type: application/json

{
  "login": "newuser",
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "langKey": "en"
}
```

### 3. Account Management Endpoints

#### Get Current User Account
```bash
GET /api/account
Authorization: Bearer {token}
```

#### Update Account Information
```bash
POST /api/account
Authorization: Bearer {token}
Content-Type: application/json

{
  "firstName": "Updated",
  "lastName": "Name",
  "email": "updated@example.com",
  "langKey": "en"
}
```

#### Change Password
```bash
POST /api/account/change-password
Authorization: Bearer {token}
Content-Type: application/json

{
  "currentPassword": "oldpassword",
  "newPassword": "newpassword"
}
```

## Environment Configuration

### Required Environment Variables
- `SECURITY_AUTHENTICATION_JWT_BASE64_SECRET`: JWT signing secret
- Database configuration (PostgreSQL)
- Elasticsearch configuration (optional for search)

### Docker Services
Start required services:
```bash
docker-compose -f src/main/docker/postgresql.yml up -d
docker-compose -f src/main/docker/elasticsearch.yml up -d
```

## Core Domain Areas

The ERP system covers the following major functional areas:

### 1. Asset Management
- **Asset Registration**: Fixed asset tracking and registration
- **Asset Disposal**: Asset disposal and write-off management
- **Asset Depreciation**: Depreciation calculation and tracking
- **Asset Categories**: Asset classification and categorization

### 2. Lease Management (IFRS 16)
- **Lease Contracts**: IFRS 16 compliant lease contract management
- **Lease Liabilities**: Lease liability calculation and tracking
- **ROU Assets**: Right of Use asset management
- **Lease Amortization**: Amortization schedule management

### 3. Payment and Settlement
- **Settlements**: Payment processing and settlement management
- **Payment Invoices**: Invoice management and processing
- **Payment Categories**: Payment classification
- **Settlement Groups**: Payment grouping and organization

### 4. Work in Progress (WIP)
- **WIP Registration**: Project and work-in-progress tracking
- **WIP Outstanding Reports**: Outstanding work reporting
- **Project Management**: Work project register management

### 5. Prepayment Management
- **Prepayment Accounts**: Prepaid expense tracking
- **Prepayment Amortization**: Amortization of prepaid expenses
- **Prepayment Reports**: Prepayment reporting and analysis

### 6. Financial Reporting
- **Report Generation**: PDF, XLSX, CSV report generation
- **Report Requisitions**: Report request management
- **Financial Reports**: Various financial reporting modules

## API Endpoint Testing

### Standard CRUD Pattern

All domain entities follow a consistent REST API pattern:

#### Create (POST)
```bash
POST /api/{entity-name}
Authorization: Bearer {token}
Content-Type: application/json

{
  // Entity data
}
```

#### Read All (GET)
```bash
GET /api/{entity-name}
Authorization: Bearer {token}
```

#### Read All with Pagination
```bash
GET /api/{entity-name}?page=0&size=20&sort=id,desc
Authorization: Bearer {token}
```

#### Read One (GET)
```bash
GET /api/{entity-name}/{id}
Authorization: Bearer {token}
```

#### Update (PUT)
```bash
PUT /api/{entity-name}/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "id": {id},
  // Updated entity data
}
```

#### Partial Update (PATCH)
```bash
PATCH /api/{entity-name}/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "id": {id},
  // Partial entity data
}
```

#### Delete (DELETE)
```bash
DELETE /api/{entity-name}/{id}
Authorization: Bearer {token}
```

#### Search
```bash
GET /api/_search/{entity-name}?query={search-term}
Authorization: Bearer {token}
```

#### Count
```bash
GET /api/{entity-name}/count
Authorization: Bearer {token}
```

## CRUD Operations Guide

### 1. Asset Registration

#### Create Asset Registration
```bash
POST /api/asset-registrations
Authorization: Bearer {token}
Content-Type: application/json

{
  "assetNumber": "AST-001",
  "assetTag": "TAG-001",
  "assetDetails": "Computer Equipment",
  "assetCost": 1500.00,
  "capitalizationDate": "2024-01-15",
  "registrationDate": "2024-01-15",
  "assetCategory": {
    "id": 1
  },
  "dealer": {
    "id": 1
  },
  "acquiringTransaction": {
    "id": 1
  }
}
```

#### Get All Asset Registrations
```bash
GET /api/asset-registrations
Authorization: Bearer {token}
```

#### Get Asset Registration by ID
```bash
GET /api/asset-registrations/1
Authorization: Bearer {token}
```

#### Update Asset Registration
```bash
PUT /api/asset-registrations/1
Authorization: Bearer {token}
Content-Type: application/json

{
  "id": 1,
  "assetNumber": "AST-001",
  "assetTag": "TAG-001-UPDATED",
  "assetDetails": "Updated Computer Equipment",
  "assetCost": 1600.00,
  "capitalizationDate": "2024-01-15",
  "registrationDate": "2024-01-15"
}
```

#### Search Asset Registrations
```bash
GET /api/_search/asset-registrations?query=computer
Authorization: Bearer {token}
```

### 2. Settlement Management

#### Create Settlement
```bash
POST /api/settlements
Authorization: Bearer {token}
Content-Type: application/json

{
  "paymentNumber": "PAY-001",
  "paymentDate": "2024-01-15",
  "paymentAmount": 1500.00,
  "description": "Equipment Purchase Payment",
  "settlementCurrency": {
    "id": 1
  },
  "paymentCategory": {
    "id": 1
  },
  "biller": {
    "id": 1
  }
}
```

#### Get All Settlements
```bash
GET /api/settlements
Authorization: Bearer {token}
```

### 3. Lease Liability Management

#### Create Lease Liability
```bash
POST /api/lease-liabilities
Authorization: Bearer {token}
Content-Type: application/json

{
  "leaseId": "LEASE-001",
  "liabilityAmount": 50000.00,
  "startDate": "2024-01-01",
  "endDate": "2026-12-31",
  "interestRate": 0.05,
  "leaseContract": {
    "id": 1
  }
}
```

### 4. Work in Progress Registration

#### Create WIP Registration
```bash
POST /api/work-in-progress-registrations
Authorization: Bearer {token}
Content-Type: application/json

{
  "sequenceNumber": "WIP-001",
  "particulars": "Office Building Construction",
  "instalmentDate": "2024-01-15",
  "instalmentAmount": 25000.00,
  "levelOfCompletion": 0.25,
  "completed": false,
  "settlementCurrency": {
    "id": 1
  },
  "dealer": {
    "id": 1
  }
}
```

### 5. Prepayment Account Management

#### Create Prepayment Account
```bash
POST /api/prepayment-accounts
Authorization: Bearer {token}
Content-Type: application/json

{
  "catalogueNumber": "PREP-001",
  "particulars": "Annual Software License",
  "prepaymentAmount": 12000.00,
  "recognitionDate": "2024-01-01",
  "settlementCurrency": {
    "id": 1
  },
  "dealer": {
    "id": 1
  }
}
```

## Security and Authorization

### User Roles and Authorities

The system supports the following roles:

#### Administrative Roles
- `ROLE_ADMIN`: Full system access
- `ROLE_DEV`: Development access
- `ROLE_DBA`: Database administration

#### Functional Roles
- `ROLE_FIXED_ASSETS_USER`: Asset management access
- `ROLE_LEASE_MANAGER`: Lease management access
- `ROLE_PAYMENTS_USER`: Payment processing access
- `ROLE_BOOK_KEEPING`: Bookkeeping operations
- `ROLE_PREPAYMENTS_MODULE_USER`: Prepayment management
- `ROLE_TAX_MODULE_USER`: Tax module access
- `ROLE_GRANULAR_REPORTS_USER`: Detailed reporting access
- `ROLE_REPORT_ACCESSOR`: Report viewing access
- `ROLE_REPORT_DESIGNER`: Report design access
- `ROLE_REQUISITION_MANAGER`: Requisition management
- `ROLE_DOCUMENT_MODULE_USER`: Document management

#### GDI (Granular Data Interface) Roles
- `ROLE_GDI_FINANCE_USER`: Finance data access
- `ROLE_GDI_OPERATIONS_USER`: Operations data access
- `ROLE_GDI_CARD_CENTRE_USER`: Card center access
- `ROLE_GDI_CREDIT_MANAGEMENT_USER`: Credit management
- `ROLE_GDI_PAYMENTS_USER`: Payment data access
- `ROLE_GDI_PRODUCTS_USER`: Product data access
- `ROLE_SECURITY_USER`: Security management
- `ROLE_RISK_DEPARTMENT_USER`: Risk management
- `ROLE_TREASURY_USER`: Treasury operations
- `ROLE_HR_USER`: Human resources access

### Testing Authorization

Test different endpoints with different user roles to verify access control:

1. Create users with different roles
2. Authenticate with each user
3. Test access to restricted endpoints
4. Verify proper 403 Forbidden responses for unauthorized access

## Testing Workflows

### 1. Asset Lifecycle Testing

1. **Create Asset Category**
   ```bash
   POST /api/asset-categories
   ```

2. **Create Dealer**
   ```bash
   POST /api/dealers
   ```

3. **Create Settlement for Asset Purchase**
   ```bash
   POST /api/settlements
   ```

4. **Register Asset**
   ```bash
   POST /api/asset-registrations
   ```

5. **Create Depreciation Period**
   ```bash
   POST /api/depreciation-periods
   ```

6. **Process Asset Depreciation**
   ```bash
   POST /api/depreciation-jobs
   ```

### 2. Lease Management Testing

1. **Create Service Outlet**
   ```bash
   POST /api/service-outlets
   ```

2. **Create Lease Contract**
   ```bash
   POST /api/ifrs-16-lease-contracts
   ```

3. **Create Lease Liability**
   ```bash
   POST /api/lease-liabilities
   ```

4. **Generate Amortization Schedule**
   ```bash
   POST /api/lease-amortization-calculations
   ```

### 3. Payment Processing Testing

1. **Create Payment Category**
   ```bash
   POST /api/payment-categories
   ```

2. **Create Payment Invoice**
   ```bash
   POST /api/payment-invoices
   ```

3. **Process Settlement**
   ```bash
   POST /api/settlements
   ```

4. **Generate Payment Reports**
   ```bash
   POST /api/report-requisitions
   ```

## Common Issues and Troubleshooting

### Authentication Issues

1. **Invalid Token Error**
   - Ensure token is properly formatted with "Bearer " prefix
   - Check token expiration
   - Verify JWT secret configuration

2. **403 Forbidden**
   - Check user roles and authorities
   - Verify endpoint access requirements
   - Ensure user has necessary permissions

### Data Validation Errors

1. **Required Field Validation**
   - Check entity constraints in domain classes
   - Ensure all @NotNull fields are provided
   - Verify data types and formats

2. **Unique Constraint Violations**
   - Check for duplicate values in unique fields
   - Verify business rules for entity creation

### Environment Issues

1. **Database Connection**
   - Verify PostgreSQL is running
   - Check database configuration
   - Ensure proper schema initialization

2. **Elasticsearch Issues**
   - Verify Elasticsearch is running (for search functionality)
   - Check index configuration
   - Test search endpoints separately

### Testing Best Practices

1. **Use Postman Collections**
   - Organize endpoints by functional area
   - Set up environment variables for base URL and tokens
   - Create test scripts for automated validation

2. **Data Setup**
   - Create test data in proper sequence (dependencies first)
   - Use consistent test data across scenarios
   - Clean up test data after testing

3. **Error Handling**
   - Test both success and error scenarios
   - Verify proper HTTP status codes
   - Check error message formats

4. **Performance Testing**
   - Test pagination with large datasets
   - Verify search performance
   - Test concurrent user scenarios

## Additional Resources

- **API Documentation**: Available at `/swagger-ui.html` when running locally
- **Database Schema**: Check Liquibase migration files in `src/main/resources/config/liquibase/`
- **Security Configuration**: Review `SecurityConfiguration.java` for endpoint access rules
- **Integration Tests**: Examine test files in `src/test/java/` for example API usage

---

**Note**: This guide covers the core functionality of the ERP system. For specific business requirements or custom endpoints, refer to the actual implementation and consult with the development team.
