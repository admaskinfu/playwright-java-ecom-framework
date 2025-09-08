package com.ecom.automation.testrunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * ApiTestRunner - TestNG runner for API tests
 * 
 * This class runs Cucumber API tests using TestNG.
 * It's similar to the UI test runner but focused on API testing.
 * 
 * Similar to Python's test runners but using TestNG and Cucumber
 */
@CucumberOptions(
    features = "src/test/resources/features/backend/customer-api.feature",
    glue = "com.ecom.automation.stepdefinitions",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/api-html-report",
        "json:target/cucumber-reports/api-cucumber.json",
        "junit:target/cucumber-reports/api-cucumber.xml"
    },
    tags = "@api",  // Only run API tests
    monochrome = true,
    publish = false
)
public class ApiTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
