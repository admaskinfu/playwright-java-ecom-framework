# Frontend Features

This folder contains Gherkin feature files for **UI automation tests** using Playwright.

## Current Features:
- `homepage.feature` - Homepage UI test scenarios (ECOM-1 through ECOM-8)

## Test Types:
- **Smoke Tests** - Critical functionality verification
- **Regression Tests** - Comprehensive UI validation
- **Cross-browser Tests** - WebKit, Chrome, Firefox support

## Running Frontend Tests:
```bash
# Run all frontend tests (by tag)
mvn test -Dtag=frontend

# Run all frontend tests (by test runner)
mvn test -Dtest=HomepageTestRunner

# Run specific test by tag
mvn test -Dtag=ECOM-1

# Run with specific browser
mvn test -Dtag=ECOM-1 -Dbrowser=chrome

# Run by feature folder
mvn test -Dcucumber.features="src/test/resources/features/frontend"
```
