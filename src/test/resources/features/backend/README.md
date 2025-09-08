# Backend Features

This folder contains Gherkin feature files for **API testing** using OAuth 1.0a authentication.

## Current Features:
- `customer-api.feature` - Customer API test scenarios (ECOM-165)

## Test Types:
- **API Integration Tests** - Backend service validation
- **Authentication Tests** - OAuth 1.0a verification
- **Data Validation Tests** - Request/response validation

## Running Backend Tests:
```bash
# Run all backend tests (by tag)
mvn test -Dtag=backend

# Run all backend tests (by test runner)
mvn test -Dtest=ApiTestRunner

# Run specific test by tag
mvn test -Dtag=ECOM-165

# Run with API credentials
export API_CONSUMER_KEY="your_key"
export API_CONSUMER_SECRET="your_secret"
mvn test -Dtest=SimpleCustomerApiTest

# Run by feature folder
mvn test -Dcucumber.features="src/test/resources/features/backend"

# Quick API test script
./test-api.sh
```

## Authentication:
- **Method**: OAuth 1.0a with HMAC-SHA1 signature
- **Credentials**: Environment variables (API_CONSUMER_KEY, API_CONSUMER_SECRET)
- **Base URL**: Configured per environment (dev/staging/prod)
