# Playwright Java E-commerce Test Framework

A professional-grade test automation framework built with Java, Playwright, TestNG, and Cucumber for e-commerce testing.

## 🚀 Features

- **Modern Tech Stack**: Java 17, Playwright, TestNG, Cucumber, REST Assured
- **Page Object Model**: Clean, maintainable page objects with centralized locator management
- **Dependency Injection**: Cucumber PicoContainer for shared object management
- **Environment Management**: Support for dev, staging, and production environments
- **Professional Logging**: SLF4J with Logback for comprehensive logging
- **Tag-based Execution**: Run specific test suites using Maven profiles
- **Cross-browser Support**: WebKit, Firefox, and Chrome support

## 📁 Project Structure

```
src/
├── main/java/com/ecom/automation/
│   ├── config/          # Environment configuration management
│   ├── factory/         # Object creation patterns
│   ├── locators/        # Centralized locator management
│   ├── pages/           # Page Object Model classes
│   └── utils/           # Utility classes
└── test/
    ├── java/com/ecom/automation/
    │   ├── stepdefinitions/  # Cucumber step definitions
    │   └── testrunners/      # TestNG test runners
    └── resources/
        ├── config/           # Environment-specific configs
        └── features/         # Gherkin feature files
```

## 🛠️ Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Git

## 🏃‍♂️ Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd playwright-java-ecom-framework
   ```

2. **Run tests**
   ```bash
   # Run all tests
   mvn test
   
   # Run specific test by tag
   mvn test -Dtag=ECOM-1
   
   # Run with specific environment
   mvn test -Denv=staging
   ```

## 📋 Test Cases

- **ECOM-1**: Verify 16 products are displayed on homepage
- **ECOM-2**: Verify 4 columns of products are displayed
- **ECOM-3**: Verify 4 rows of products are displayed
- **ECOM-5**: Verify sorting dropdown is displayed at top
- **ECOM-6**: Verify sorting dropdown is displayed at bottom
- **ECOM-7**: Verify "shop" heading is displayed
- **ECOM-8**: Verify header menu is displayed

## 🔧 Configuration

Environment-specific configurations are managed in `src/test/resources/config/`:
- `dev.properties` - Development environment
- `staging.properties` - Staging environment  
- `prod.properties` - Production environment

## 📝 Contributing

1. Create a feature branch from `main`
2. Make your changes
3. Run tests to ensure everything works
4. Create a pull request

## 📄 License

This project is licensed under the MIT License.
