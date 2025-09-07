package com.ecom.automation.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            // Initialize browser and page objects
            testContext.initializeBrowser();
            
            logger.info("Test environment ready:");
            logger.info("- Environment: {}", testContext.getConfig().getBaseUrl());
            logger.info("- Browser: {}", testContext.getConfig().getBrowser());
            logger.info("- Headless: {}", testContext.getConfig().isHeadless());
            logger.info("- Timeout: {}ms", testContext.getConfig().getTimeout());
            
        } catch (Exception e) {
            logger.error("Failed to set up test environment: {}", e.getMessage(), e);
            throw new RuntimeException("Test setup failed", e);
        }
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
    public void tearDown() {
        logger.info("=== Cleaning up test environment ===");
        
        try {
            // Clean up browser resources
            testContext.cleanup();
            logger.info("Test environment cleaned up successfully");
            
        } catch (Exception e) {
            logger.error("Error during cleanup: {}", e.getMessage(), e);
        }
    }
}