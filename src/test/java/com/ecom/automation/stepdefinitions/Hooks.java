package com.ecom.automation.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * Hooks - Centralized test lifecycle management
 * 
 * This class manages the setup and teardown of test resources using Cucumber's
 * dependency injection system (PicoContainer). It's similar to Python's conftest.py
 * fixtures but implemented as a dedicated hooks class.
 * 
 * Key benefits:
 * - Centralized resource management
 * - Automatic dependency injection via constructor
 * - Clean separation of concerns
 * - Reusable across all step definition classes
 * 
 * How it works:
 * 1. PicoContainer automatically injects TestContext into constructor
 * 2. @Before hook initializes browser before each scenario
 * 3. @After hook cleans up resources after each scenario
 * 4. All step definitions can access the same TestContext instance
 */
public class Hooks {
    
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    
    private final TestContext testContext;
    
    /**
     * Constructor with dependency injection
     * 
     * PicoContainer automatically injects the TestContext instance here.
     * This is similar to Python's fixture injection in conftest.py:
     * 
     * Python equivalent:
     * def test_something(browser_fixture):  # browser_fixture is injected
     *     pass
     * 
     * Java equivalent:
     * public Hooks(TestContext testContext) {  # testContext is injected
     *     this.testContext = testContext;
     * }
     */
    public Hooks(TestContext testContext) {
        this.testContext = testContext;
        logger.debug("Hooks initialized with TestContext");
    }
    
    /**
     * Setup hook - runs before each scenario
     * 
     * This is similar to Python's @pytest.fixture(autouse=True) in conftest.py
     * that automatically runs before each test.
     * 
     * Python equivalent:
     * @pytest.fixture(autouse=True)
     * def setup_browser():
     *     # setup code
     *     yield
     *     # teardown code
     * 
     * Java equivalent:
     * @Before
     * public void setUp() {
     *     // setup code
     * }
     */
    @Before
    public void setUp() {
        logger.info("=== Setting up test environment ===");
        
        try {
            // Check if this is an API test by looking at the stack trace
            boolean isApiTest = isApiTest();
            
            if (isApiTest) {
                logger.info("API test detected - skipping browser initialization");
                logger.info("API test environment ready");
                logger.info("- Ready for API testing");
                logger.info("- No browser initialization needed");
            } else {
                // Initialize browser and page objects for UI tests
                testContext.initializeBrowser();
                
                logger.info("UI test environment ready:");
                logger.info("- Environment: {}", testContext.getConfig().getBaseUrl());
                logger.info("- Browser: {}", testContext.getConfig().getBrowser());
                logger.info("- Headless: {}", testContext.getConfig().isHeadless());
                logger.info("- Timeout: {}ms", testContext.getConfig().getTimeout());
            }
            
        } catch (Exception e) {
            logger.error("Failed to set up test environment: {}", e.getMessage(), e);
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    /**
     * Check if this is an API test by examining the current test context
     * 
     * This uses a more reliable approach by checking the test context
     * and system properties to determine if we're running API tests.
     */
    private boolean isApiTest() {
        // Check if we're running the ApiTestRunner specifically
        String testClass = System.getProperty("test");
        if (testClass != null && testClass.contains("ApiTestRunner")) {
            return true;
        }
        
        // Check if we're in an API test context by looking at the current thread
        String threadName = Thread.currentThread().getName();
        if (threadName.contains("ApiTestRunner") || threadName.contains("Api")) {
            return true;
        }
        
        // Check system properties for API test indicators
        String cucumberTags = System.getProperty("cucumber.filter.tags");
        if (cucumberTags != null && cucumberTags.contains("@api")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Teardown hook - runs after each scenario
     * 
     * This is similar to Python's fixture teardown in conftest.py
     * that automatically runs after each test.
     * 
     * Python equivalent:
     * @pytest.fixture(autouse=True)
     * def setup_browser():
     *     # setup code
     *     yield
     *     # teardown code (this part)
     * 
     * Java equivalent:
     * @After
     * public void tearDown() {
     *     // teardown code
     * }
     */
    @After
    public void tearDown(Scenario scenario) {
        logger.info("=== Cleaning up test environment ===");
        
        try {
            // Check if this is an API test
            boolean isApiTest = isApiTest();
            
            if (isApiTest) {
                logger.info("API test detected - skipping browser cleanup and screenshot capture");
                logger.info("API test environment cleaned up successfully");
            } else {
                // Capture screenshot on test failure for UI tests
                if (scenario.isFailed()) {
                    captureScreenshotOnFailure(scenario);
                }
                
                // Clean up browser resources for UI tests
                testContext.cleanup();
                logger.info("UI test environment cleaned up successfully");
            }
            
        } catch (Exception e) {
            logger.error("Error during cleanup: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Capture screenshot when test fails using Playwright's built-in methods
     * 
     * This method uses Playwright's native screenshot capabilities for simplicity.
     * 
     * @param scenario Cucumber scenario object containing test information
     */
    private void captureScreenshotOnFailure(Scenario scenario) {
        try {
            // Get the current page from TestContext
            Page page = testContext.getPage();
            
            if (page == null) {
                logger.warn("Cannot capture screenshot: Page object is null");
                return;
            }
            
            // Create screenshots directory
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("target/screenshots/failed"));
            
            // Generate descriptive filename with test context and timestamp
            String timestamp = java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String testName = getTestName(scenario);
            String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9._-]", "_");
            String filename = String.format("%s_%s_FAILED_%s.png", 
                testName, scenarioName, timestamp);
            Path screenshotPath = java.nio.file.Paths.get("target/screenshots/failed", filename);
            
            // Use Playwright's built-in screenshot method with best practices
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(screenshotPath)
                    .setFullPage(true)
                    .setTimeout(5000));  // 5 second timeout for screenshot
            
            logger.info("Screenshot captured for failed test: {}", screenshotPath);
            
            // Attach screenshot to Cucumber report
            try {
                byte[] screenshotBytes = java.nio.file.Files.readAllBytes(screenshotPath);
                scenario.attach(screenshotBytes, "image/png", "Screenshot on Failure");
                logger.info("Screenshot attached to Cucumber report");
            } catch (Exception e) {
                logger.warn("Failed to attach screenshot to Cucumber report: {}", e.getMessage());
            }
            
        } catch (Exception e) {
            logger.error("Failed to capture screenshot on test failure: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Get test name from scenario for screenshot naming
     * 
     * @param scenario Cucumber scenario object
     * @return Test name for screenshot filename
     */
    private String getTestName(Scenario scenario) {
        // Try to get the test class name from the scenario
        String scenarioId = scenario.getId();
        
        if (scenarioId != null && scenarioId.contains(";")) {
            // Extract class name from scenario ID (format: package.Class;scenario)
            String[] parts = scenarioId.split(";");
            if (parts.length > 0) {
                String className = parts[0];
                // Extract just the class name without package
                String[] classParts = className.split("\\.");
                return classParts[classParts.length - 1];
            }
        }
        
        // Fallback to scenario name or default
        return scenario.getName() != null ? scenario.getName() : "UnknownTest";
    }
}