# Quick Setup Guide

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Git**

## Installation

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd playwright-java-ecom-framework
   ```

2. **Verify Java installation:**
   ```bash
   java -version
   # Should show Java 17 or higher
   ```

3. **Verify Maven installation:**
   ```bash
   mvn -version
   # Should show Maven 3.6 or higher
   ```

## Running the Tests

### Option 1: Quick API Test
```bash
# Set API credentials and run API test
./test-api.sh
```

### Option 2: Manual Test Execution

1. **Compile the project:**
   ```bash
   mvn clean compile
   ```

2. **Run UI tests:**
   ```bash
   mvn test -Dtag=ECOM-1
   ```

3. **Run all tests:**
   ```bash
   mvn test
   ```

4. **Run API tests only:**
   ```bash
   mvn test -Dtest=SimpleCustomerApiTest
   ```

## API Testing Setup

For API tests, set environment variables:

```bash
# Set your WooCommerce API credentials
export API_CONSUMER_KEY="your_consumer_key_here"
export API_CONSUMER_SECRET="your_consumer_secret_here"

# Run API tests
mvn test -Dtest=SimpleCustomerApiTest

# Or use the quick script
./test-api.sh
```

## Troubleshooting

### Java Issues
- Ensure Java 17+ is installed and `JAVA_HOME` is set
- Check `java -version` output

### Maven Issues
- Ensure Maven is in your PATH
- Check `mvn -version` output

### Browser Issues
- Framework uses WebKit by default (works on macOS)
- Chrome may require additional setup

### API Issues
- Verify API credentials are correct
- Check if WooCommerce API is accessible
- Ensure credentials have write permissions
- OAuth 1.0a signatures are time-sensitive - ensure system clock is accurate

## What You'll See

- **Browser automation** - Playwright opens browser windows
- **Test execution** - Detailed logging and progress
- **Test reports** - Generated in `target/surefire-reports/`
- **Professional output** - Clean, informative test results

## Next Steps

1. Explore the code structure
2. Run different test scenarios
3. Modify tests to see changes
4. Add your own test cases

---

**Ready to demonstrate professional Java test automation! 🚀**
