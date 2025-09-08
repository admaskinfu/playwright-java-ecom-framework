# #!/bin/bash

# # =================================================================
# # Frontend Tests Runner Script
# # =================================================================
# # Professional test automation framework - Frontend test execution
# # 
# # This script executes all frontend (UI) tests using Playwright + Cucumber
# # Designed to scale with thousands of test cases
# # 
# # Usage:
# #   ./run-frontend-tests.sh                    # Default browser (webkit)
# #   ./run-frontend-tests.sh chrome             # Chrome browser
# #   ./run-frontend-tests.sh webkit             # WebKit browser
# #   ./run-frontend-tests.sh firefox            # Firefox browser
# # 
# # Environment Variables:
# #   BROWSER - Override default browser
# #   HEADLESS - Run in headless mode (true/false)
# #   PARALLEL - Number of parallel threads
# # =================================================================

# # Colors for output
# RED='\033[0;31m'
# GREEN='\033[0;32m'
# YELLOW='\033[1;33m'
# BLUE='\033[0;34m'
# NC='\033[0m' # No Color

# # Configuration
# BROWSER=${1:-${BROWSER:-webkit}}
# HEADLESS=${HEADLESS:-false}
# PARALLEL=${PARALLEL:-1}

# echo -e "${BLUE}🚀 Running Frontend Tests${NC}"
# echo -e "${BLUE}========================${NC}"
# echo ""
# echo -e "Browser: ${YELLOW}${BROWSER}${NC}"
# echo -e "Headless: ${YELLOW}${HEADLESS}${NC}"
# echo -e "Parallel: ${YELLOW}${PARALLEL}${NC}"
# echo -e "Test Type: ${YELLOW}UI Automation (Playwright + Cucumber)${NC}"
# echo -e "Test Runner: ${YELLOW}HomepageTestRunner${NC}"
# echo ""

# # Check if Maven is available
# if ! command -v mvn &> /dev/null; then
#     echo -e "${RED}❌ Maven is not installed or not in PATH${NC}"
#     echo "Please install Maven and try again"
#     exit 1
# fi

# # Check if Java is available
# if ! command -v java &> /dev/null; then
#     echo -e "${RED}❌ Java is not installed or not in PATH${NC}"
#     echo "Please install Java 17+ and try again"
#     exit 1
# fi

# echo -e "${BLUE}📋 Test Scope:${NC}"
# echo "  • All frontend (UI) test scenarios"
# echo "  • Homepage functionality validation"
# echo "  • Cross-browser compatibility testing"
# echo ""

# echo -e "${BLUE}🔧 Running tests...${NC}"
# echo ""

# # Run the frontend tests
# echo "Running tests... (this may take a moment)"
# TEST_OUTPUT=$(mvn test -Dtest=HomepageTestRunner -Dbrowser=${BROWSER} -Dheadless=${HEADLESS} -Dparallel=${PARALLEL} -q 2>/dev/null)
# TEST_EXIT_CODE=$?

# # Show only the summary lines
# echo "$TEST_OUTPUT" | grep -E "(Tests run|BUILD|ERROR|FAILURE)" || true

# if [ $TEST_EXIT_CODE -eq 0 ]; then
#     echo ""
#     echo -e "${GREEN}✅ Frontend tests completed successfully!${NC}"
#     echo -e "${GREEN}📊 Check target/surefire-reports/ for detailed results${NC}"
#     exit 0
# else
#     echo ""
#     echo -e "${RED}❌ Frontend tests failed!${NC}"
#     echo -e "${RED}📊 Check target/surefire-reports/ for error details${NC}"
#     exit 1
# fi
set -x
set -e
mvn test -Dtest=HomepageTestRunner