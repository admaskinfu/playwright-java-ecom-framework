package com.ecom.automation.testrunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Test Runner for Homepage UAT scenarios
 * 
 * This class tells TestNG how to run our Cucumber scenarios.
 * It's similar to Python's test runner configuration in behave.
 * 
 * The @CucumberOptions annotation configures:
 * - features: where to find .feature files
 * - glue: where to find step definitions
 * - plugin: what reports to generate
 */
@CucumberOptions(
    features = "src/test/resources/features/frontend/homepage.feature",  // Only homepage features
    glue = "com.ecom.automation.stepdefinitions",  // Package with step definitions
    plugin = {
        "pretty",  // Pretty console output
        "html:target/cucumber-reports/html.html",  // HTML report with proper extension
        "json:target/cucumber-reports/cucumber.json",  // JSON report for Maven plugin
        "junit:target/cucumber-reports/cucumber.xml"  // JUnit XML report
    },
    monochrome = true,  // Clean console output
    dryRun = false  // Set to true to validate step definitions without running
)
public class HomepageTestRunner extends AbstractTestNGCucumberTests {
    
    // This class extends AbstractTestNGCucumberTests which provides
    // the necessary methods to run Cucumber scenarios with TestNG
    // No additional code needed - TestNG will handle the execution
}
