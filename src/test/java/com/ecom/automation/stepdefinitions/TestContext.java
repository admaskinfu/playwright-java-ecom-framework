package com.ecom.automation.stepdefinitions;

import com.ecom.automation.config.ConfigManager;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TestContext - Centralized management of Playwright objects
 * 
 * This class follows the Singleton pattern to ensure we have one instance
 * of Playwright objects throughout the test execution. It's similar to
 * Python's conftest.py fixtures but implemented as a shared context object.
 * 
 * Key benefits:
 * - Single source of truth for browser/page objects
 * - Proper resource management and cleanup
 * - Thread-safe object sharing across step definitions
 * - Centralized configuration management
 */
public class TestContext {
    
    // Logger for debugging and error tracking
    private static final Logger logger = LoggerFactory.getLogger(TestContext.class);
    
    private static TestContext instance;
    private Playwright playwright;
    private Browser browser;
    private Page page;
    private ConfigManager config;
    private boolean isInitialized = false;
    
    /**
     * Constructor for dependency injection
     * 
     * PicoContainer will create instances of this class and inject them
     * into step definitions. This allows proper resource sharing across
     * all step definition classes.
     */
    public TestContext() {
        this.config = ConfigManager.getInstance();
    }
    
    /**
     * Get singleton instance of TestContext
     * 
     * @return TestContext instance
     */
    public static TestContext getInstance() {
        if (instance == null) {
            instance = new TestContext();
        }
        return instance;
    }
    
    /**
     * Initialize Playwright objects
     * 
     * This method sets up the browser and page objects based on configuration.
     * It's called once per test execution and ensures proper initialization.
     * 
     * Similar to Python's @pytest.fixture(scope="session") in conftest.py
     */
    public void initializeBrowser() {
        if (isInitialized) {
            return; // Already initialized
        }
        
        try {
            // Initialize Playwright - it will automatically download browsers if needed
            playwright = Playwright.create();
            
            // Get browser type (default to chromium)
            BrowserType browserType = getBrowserType();
            
            // Launch browser with configuration
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                    .setHeadless(config.isHeadless());
            
            
            // Log the launch mode for debugging
            logger.info("Launching browser in {} mode", config.isHeadless() ? "headless" : "headed");
            
            browser = browserType.launch(launchOptions);
            page = browser.newPage();
            
            // Set default timeout
            page.setDefaultTimeout(config.getTimeout());
            
            isInitialized = true;
            
            logger.info("Browser initialized successfully");
            logger.info("Environment: {}", System.getProperty("env", "dev"));
            logger.info("Base URL: {}", config.getBaseUrl());
            logger.info("Headless mode: {}", config.isHeadless());
            
        } catch (Exception e) {
            logger.error("Failed to initialize browser: {}", e.getMessage());
            throw new RuntimeException("Browser initialization failed", e);
        }
    }
    
    /**
     * Get browser type based on configuration
     * 
     * @return BrowserType instance
     */
    private BrowserType getBrowserType() {
        String browserName = config.getBrowser().toLowerCase();
        
        switch (browserName) {
            case "firefox":
                return playwright.firefox();
            case "webkit":
                return playwright.webkit();
            case "chrome":
            case "chromium":
            default:
                return playwright.chromium();
        }
    }
    
    /**
     * Get the current page object
     * 
     * @return Page instance
     * @throws IllegalStateException if browser not initialized
     */
    public Page getPage() {
        if (!isInitialized || page == null) {
            throw new IllegalStateException("Browser not initialized. Call initializeBrowser() first.");
        }
        return page;
    }
    
    /**
     * Get the browser instance
     * 
     * @return Browser instance
     * @throws IllegalStateException if browser not initialized
     */
    public Browser getBrowser() {
        if (!isInitialized || browser == null) {
            throw new IllegalStateException("Browser not initialized. Call initializeBrowser() first.");
        }
        return browser;
    }
    
    /**
     * Get the Playwright instance
     * 
     * @return Playwright instance
     * @throws IllegalStateException if not initialized
     */
    public Playwright getPlaywright() {
        if (!isInitialized || playwright == null) {
            throw new IllegalStateException("Playwright not initialized. Call initializeBrowser() first.");
        }
        return playwright;
    }
    
    /**
     * Get the configuration manager
     * 
     * @return ConfigManager instance
     */
    public ConfigManager getConfig() {
        return config;
    }
    
    /**
     * Check if browser is initialized
     * 
     * @return true if initialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }
    
    /**
     * Clean up browser resources
     * 
     * This method properly closes all browser resources and resets the state.
     * It's called after each test scenario to ensure clean state.
     * 
     * Similar to Python's fixture teardown in conftest.py
     */
    public void cleanup() {
        try {
            if (page != null) {
                page.close();
                page = null;
            }
            
            if (browser != null) {
                browser.close();
                browser = null;
            }
            
            if (playwright != null) {
                playwright.close();
                playwright = null;
            }
            
            isInitialized = false;
            
            logger.info("Browser resources cleaned up successfully");
            
        } catch (Exception e) {
            logger.error("Error during cleanup: {}", e.getMessage());
        }
    }
    
    /**
     * Reset the singleton instance
     * 
     * This method is useful for testing or when you need to start fresh.
     * It's similar to Python's fixture scope management.
     */
    public static void reset() {
        if (instance != null) {
            instance.cleanup();
            instance = null;
        }
    }
}
