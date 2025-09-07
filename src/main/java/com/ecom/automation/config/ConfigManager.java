package com.ecom.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for handling environment-specific settings
 * 
 * This class manages different environment configurations (dev, staging, prod)
 * and provides a centralized way to access configuration values.
 * 
 * Similar to Python's configparser or environment variables, but more structured.
 */
public class ConfigManager {
    
    private static ConfigManager instance;
    private Properties properties;
    
    // Private constructor for Singleton pattern
    private ConfigManager() {
        loadProperties();
    }
    
    /**
     * Get singleton instance of ConfigManager
     * 
     * @return ConfigManager instance
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Load properties based on environment
     * 
     * Environment is determined by system property 'env' or defaults to 'dev'
     * This allows us to run tests against different environments:
     * - mvn test -Denv=dev
     * - mvn test -Denv=staging  
     * - mvn test -Denv=prod
     */
    private void loadProperties() {
        properties = new Properties();
        
        // Get environment from system property, default to 'dev'
        String environment = System.getProperty("env", "dev");
        
        // Load environment-specific properties file
        String propertiesFile = String.format("config/%s.properties", environment);
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
            if (input != null) {
                properties.load(input);
                System.out.println("Loaded configuration for environment: " + environment);
            } else {
                throw new RuntimeException("Properties file not found: " + propertiesFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file: " + propertiesFile, e);
        }
    }
    
    /**
     * Get property value by key
     * 
     * @param key property key
     * @return property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get property value with default
     * 
     * @param key property key
     * @param defaultValue default value if key not found
     * @return property value or default
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get base URL for current environment
     * 
     * @return base URL
     */
    public String getBaseUrl() {
        return getProperty("base.url");
    }
    
    /**
     * Get browser name for test execution
     * 
     * @return browser name
     */
    public String getBrowser() {
        return getProperty("browser", "chrome");
    }
    
    /**
     * Get headless mode setting
     * 
     * @return true if headless mode enabled
     */
    public boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }
    
    /**
     * Get timeout value in milliseconds
     * 
     * @return timeout value
     */
    public int getTimeout() {
        return Integer.parseInt(getProperty("timeout", "30000"));
    }
}
