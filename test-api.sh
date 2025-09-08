#!/bin/bash

# Set API credentials and run test
export API_CONSUMER_KEY="ck_22ad57a684d4591f114772227d1f2489b5e55750"
export API_CONSUMER_SECRET="cs_c40dcfbfb01d6cefdc7f62c97cc8acc7446f5382"

echo "🚀 Running OAuth 1.0a API test..."
echo "Using credentials: ${API_CONSUMER_KEY:0:10}... / ${API_CONSUMER_SECRET:0:10}..."
echo ""

# Run the simple OAuth test
mvn test -Dtest=SimpleCustomerApiTest

