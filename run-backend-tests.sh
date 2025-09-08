# #!/bin/bash

# # =================================================================
# # Backend Tests Runner Script
# # =================================================================
# # Professional test automation framework - Backend test execution
# # 
# # This script executes all backend (API) tests using OAuth 1.0a + TestNG
# # Designed to scale with thousands of test cases
# # 
# # Usage:
# #   ./run-backend-tests.sh
# #   API_CONSUMER_KEY="your_key" API_CONSUMER_SECRET="your_secret" ./run-backend-tests.sh
# # 
# # Environment Variables:
# #   API_CONSUMER_KEY - WooCommerce API consumer key
# #   API_CONSUMER_SECRET - WooCommerce API consumer secret
# #   API_BASE_URL - Override default API base URL
# #   PARALLEL - Number of parallel threads
# # =================================================================

# # Colors for output
# RED='\033[0;31m'
# GREEN='\033[0;32m'
# YELLOW='\033[1;33m'
# BLUE='\033[0;34m'
# NC='\033[0m' # No Color

# # Configuration
# PARALLEL=${PARALLEL:-1}
# API_BASE_URL=${API_BASE_URL:-""}

# echo -e "${BLUE}🚀 Running Backend Tests${NC}"
# echo -e "${BLUE}=======================${NC}"
# echo ""
# echo -e "Parallel: ${YELLOW}${PARALLEL}${NC}"
# echo -e "Test Type: ${YELLOW}API Testing (OAuth 1.0a + TestNG)${NC}"
# echo -e "Test Runner: ${YELLOW}ApiTestRunner${NC}"
# if [ -n "$API_BASE_URL" ]; then
#     echo -e "API Base URL: ${YELLOW}${API_BASE_URL}${NC}"
# fi
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

# # Check API credentials
# if [ -z "$API_CONSUMER_KEY" ] || [ -z "$API_CONSUMER_SECRET" ]; then
#     echo -e "${YELLOW}⚠️  API credentials not set!${NC}"
#     echo ""
#     echo -e "${BLUE}🔧 Setup Instructions:${NC}"
#     echo "1. Set environment variables:"
#     echo "   export API_CONSUMER_KEY=\"your_consumer_key_here\""
#     echo "   export API_CONSUMER_SECRET=\"your_consumer_secret_here\""
#     echo ""
#     echo "2. Or run with credentials:"
#     echo "   API_CONSUMER_KEY=\"your_key\" API_CONSUMER_SECRET=\"your_secret\" ./run-backend-tests.sh"
#     echo ""
#     echo "3. Or use the quick API test script:"
#     echo "   ./test-api.sh"
#     echo ""
#     echo -e "${RED}❌ Cannot run backend tests without API credentials${NC}"
#     exit 1
# fi

# echo -e "${GREEN}✅ API credentials found${NC}"
# echo -e "Consumer Key: ${YELLOW}${API_CONSUMER_KEY:0:10}...${NC}"
# echo -e "Consumer Secret: ${YELLOW}${API_CONSUMER_SECRET:0:10}...${NC}"
# echo ""

# echo -e "${BLUE}📋 Test Scope:${NC}"
# echo "  • All backend (API) test scenarios"
# echo "  • Customer API functionality validation"
# echo "  • OAuth 1.0a authentication testing"
# echo "  • Error handling and edge cases"
# echo ""

# echo -e "${BLUE}🔧 Running tests...${NC}"
# echo ""

# # Run the backend tests
# MAVEN_ARGS="-Dtest=ApiTestRunner -Dparallel=${PARALLEL}"
# if [ -n "$API_BASE_URL" ]; then
#     MAVEN_ARGS="${MAVEN_ARGS} -Dapi.base.url=${API_BASE_URL}"
# fi

# echo "Running tests... (this may take a moment)"
# TEST_OUTPUT=$(mvn test ${MAVEN_ARGS} -q 2>/dev/null)
# TEST_EXIT_CODE=$?

# # Show only the summary lines
# echo "$TEST_OUTPUT" | grep -E "(Tests run|BUILD|ERROR|FAILURE)" || true

# if [ $TEST_EXIT_CODE -eq 0 ]; then
#     echo ""
#     echo -e "${GREEN}✅ Backend tests completed successfully!${NC}"
#     echo -e "${GREEN}📊 Check target/surefire-reports/ for detailed results${NC}"
#     exit 0
# else
#     echo ""
#     echo -e "${RED}❌ Backend tests failed!${NC}"
#     echo -e "${RED}📊 Check target/surefire-reports/ for error details${NC}"
#     exit 1
# fi

set -x
set -e

mvn test -Dtest=ApiTestRunner