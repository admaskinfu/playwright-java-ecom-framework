# API Testing Setup Guide

This guide explains how to securely configure API testing for the e-commerce framework using OAuth 1.0a authentication.

## 🔐 Security Best Practices

### Environment Variables
The framework uses environment variables for API credentials to ensure security:

```bash
# Set these environment variables before running tests
export API_CONSUMER_KEY="your_actual_consumer_key"
export API_CONSUMER_SECRET="your_actual_consumer_secret"
```

### Configuration Files
API credentials are configured in environment-specific property files:
- `src/test/resources/config/dev.properties`
- `src/test/resources/config/staging.properties`
- `src/test/resources/config/prod.properties`

## 🚀 Quick Start

### 1. Set Environment Variables
```bash
# For development
export API_CONSUMER_KEY="ck_your_development_key_here"
export API_CONSUMER_SECRET="cs_your_development_secret_here"

# For staging
export API_CONSUMER_KEY="ck_your_staging_key_here"
export API_CONSUMER_SECRET="cs_your_staging_secret_here"

# For production
export API_CONSUMER_KEY="ck_your_production_key_here"
export API_CONSUMER_SECRET="cs_your_production_secret_here"
```

### 2. Run API Tests
```bash
# Run all tests (includes API tests)
mvn test

# Run only API tests
mvn test -Dtest=SimpleCustomerApiTest

# Run with specific environment
mvn test -Denv=staging

# Run by tag
mvn test -Dtag=ECOM-165

# Quick API test script
./test-api.sh
```

## 📋 Test Cases

### ECOM-165: Customer Creation
- **Test**: Verify POST /customers creates user with email and password only
- **Endpoint**: POST /customers
- **Required Fields**: email, password
- **Expected Response**: 201 Created
- **Authentication**: OAuth 1.0a with HMAC-SHA1 signature

## 🔧 Configuration Details

### API Base URLs
- **Development**: `http://demostore.supersqa.com/wp-json/wc/v3`
- **Staging**: `http://staging.demostore.supersqa.com/wp-json/wc/v3`
- **Production**: `http://prod.demostore.supersqa.com/wp-json/wc/v3`

### Authentication
The framework uses **OAuth 1.0a with HMAC-SHA1 signature** for WooCommerce REST API:
- **Consumer Key**: `oauth_consumer_key` parameter
- **Consumer Secret**: Used to generate `oauth_signature`
- **Signature Method**: HMAC-SHA1
- **Version**: OAuth 1.0

## 🛡️ Security Features

1. **Environment Variable Substitution**: Credentials are loaded from environment variables
2. **Masked Logging**: API keys are masked in logs for security
3. **No Hardcoded Secrets**: All sensitive data is externalized
4. **Environment-Specific Configs**: Different credentials for different environments
5. **OAuth 1.0a Implementation**: Secure authentication with HMAC-SHA1 signatures

## 🔧 OAuth 1.0a Implementation

The framework includes a custom `SimpleOAuthClient` that handles:
- **Signature Generation**: HMAC-SHA1 signatures for secure authentication
- **Parameter Management**: Automatic generation of OAuth parameters
- **Request Signing**: Proper OAuth 1.0a request signing
- **Response Handling**: Clean response processing

### OAuth Parameters Generated:
```
oauth_consumer_key=ck_your_key_here
oauth_signature_method=HMAC-SHA1
oauth_timestamp=1757269889
oauth_nonce=random_nonce_string
oauth_version=1.0
oauth_signature=generated_hmac_signature
```

## 📝 Example Usage

### Running Tests with Environment Variables
```bash
# Set credentials
export API_CONSUMER_KEY="ck_1234567890abcdef"
export API_CONSUMER_SECRET="cs_abcdef1234567890"

# Run API tests
mvn test -Dtest=SimpleCustomerApiTest

# Or use the quick script
./test-api.sh
```

### Running Tests with Maven Properties
```bash
# Override credentials via Maven properties
API_CONSUMER_KEY="ck_1234567890abcdef" API_CONSUMER_SECRET="cs_abcdef1234567890" mvn test
```

## 🚨 Important Security Notes

1. **Never commit real API credentials** to version control
2. **Use environment variables** for all sensitive data
3. **Rotate credentials regularly** for security
4. **Use different credentials** for different environments
5. **Monitor API usage** to detect unauthorized access
6. **OAuth signatures are time-sensitive** - ensure system clock is accurate

## 🔍 Troubleshooting

### Common Issues

1. **"API consumer key should be configured"**
   - Solution: Set the `API_CONSUMER_KEY` environment variable

2. **"API consumer secret should be configured"**
   - Solution: Set the `API_CONSUMER_SECRET` environment variable

3. **401 Unauthorized**
   - Solution: Verify API credentials are correct and have proper permissions
   - Check if OAuth signature is being generated correctly

4. **404 Not Found**
   - Solution: Check if the API endpoint URL is correct

5. **OAuth Signature Issues**
   - Solution: Ensure system clock is accurate (OAuth timestamps are time-sensitive)

## 📚 Additional Resources

- [WooCommerce REST API Documentation](https://woocommerce.github.io/woocommerce-rest-api-docs/)
- [OAuth 1.0a Specification](https://tools.ietf.org/html/rfc5849)
- [TestNG Documentation](https://testng.org/doc/)
