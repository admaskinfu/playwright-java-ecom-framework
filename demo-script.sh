#!/bin/bash

# E-commerce Test Automation Framework Demo Script
# This script demonstrates the key features of our Java-based test automation framework

echo "🚀 E-commerce Test Automation Framework Demo"
echo "=============================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_step() {
    echo -e "${BLUE}📋 Step $1: $2${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Check if we're in the right directory
if [ ! -f "pom.xml" ]; then
    print_error "Please run this script from the project root directory"
    exit 1
fi

print_step "1" "Framework Overview"
echo "This framework demonstrates:"
echo "  • Java 17 with Maven"
echo "  • Playwright for UI automation"
echo "  • REST Assured for API testing"
echo "  • TestNG as test runner"
echo "  • Cucumber for BDD"
echo "  • Professional architecture with dependency injection"
echo ""

print_step "2" "Project Structure"
echo "Key components:"
echo "  • src/main/java/ - Core framework classes"
echo "  • src/test/java/ - Test implementations"
echo "  • src/test/resources/ - Configuration and feature files"
echo "  • pom.xml - Maven dependencies and build configuration"
echo ""

print_step "3" "Configuration Management"
echo "Environment-specific configurations:"
echo "  • dev.properties - Development environment"
echo "  • staging.properties - Staging environment"
echo "  • prod.properties - Production environment"
echo "  • Secure API credential management with environment variables"
echo ""

print_step "4" "Running UI Tests (Homepage Tests)"
print_warning "Starting UI tests - this will open a browser window"
echo ""

# Run homepage tests
echo "Running: mvn test -Dtag=ECOM-1"
mvn test -Dtag=ECOM-1
if [ $? -eq 0 ]; then
    print_success "UI Test completed successfully"
else
    print_error "UI Test failed"
fi
echo ""

print_step "5" "Running API Tests"
print_warning "API tests require valid credentials - showing framework structure"
echo ""

# Show API test structure without running (since credentials might not be set)
echo "API Test Structure:"
echo "  • BaseApiTest - Common API functionality"
echo "  • CustomerApiTest - Customer management tests"
echo "  • Secure credential validation"
echo "  • Professional error handling"
echo ""

print_step "6" "Framework Features Demonstrated"
echo "✅ Professional Architecture:"
echo "  • Page Object Model (POM)"
echo "  • Dependency Injection with PicoContainer"
echo "  • Factory Pattern for page creation"
echo "  • Centralized configuration management"
echo ""

echo "✅ Test Management:"
echo "  • Cucumber BDD with Gherkin syntax"
echo "  • TestNG integration for parallel execution"
echo "  • Tag-based test execution"
echo "  • Comprehensive logging with SLF4J"
echo ""

echo "✅ Security & Best Practices:"
echo "  • Environment variable-based credential management"
echo "  • Clear error messages and setup instructions"
echo "  • Professional Git workflow"
echo "  • Comprehensive .gitignore"
echo ""

print_step "7" "Key Commands for Interview"
echo "Essential Maven commands:"
echo "  • mvn clean compile          - Compile the project"
echo "  • mvn test                   - Run all tests"
echo "  • mvn test -Dtag=ECOM-1      - Run specific test by tag"
echo "  • mvn test -Dtags='@ui'      - Run tests by tag category"
echo "  • mvn clean install          - Full build and test"
echo ""

print_step "8" "Framework Highlights for Interview"
echo "🎯 What makes this framework professional:"
echo "  • Scalable architecture with proper separation of concerns"
echo "  • Industry-standard tools and practices"
echo "  • Comprehensive error handling and logging"
echo "  • Secure credential management"
echo "  • Easy to maintain and extend"
echo "  • Ready for CI/CD integration"
echo ""

print_success "Demo completed! This framework demonstrates professional Java test automation skills."
echo ""
echo "📚 Next steps for interview:"
echo "  1. Explain the architecture and design patterns used"
echo "  2. Show how to add new tests and maintain the framework"
echo "  3. Discuss CI/CD integration possibilities"
echo "  4. Highlight security and best practices implemented"
echo ""

print_warning "Note: API tests require valid WooCommerce credentials to run successfully"
echo "Set environment variables:"
echo "  export API_CONSUMER_KEY='your_key_here'"
echo "  export API_CONSUMER_SECRET='your_secret_here'"
