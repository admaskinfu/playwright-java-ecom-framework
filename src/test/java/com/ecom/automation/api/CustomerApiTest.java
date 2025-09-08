package com.ecom.automation.api;

import com.ecom.automation.config.ConfigManager;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * CustomerApiTest - API tests for customer management
 * 
 * This class contains API tests for customer-related endpoints.
 * Tests the WooCommerce REST API for customer operations.
 * 
 * Similar to Python's API test classes but using REST Assured and TestNG
 */
public class CustomerApiTest extends BaseApiTest {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerApiTest.class);
    private ConfigManager config;
    
    @BeforeClass
    public void setUp() {
        this.config = ConfigManager.getInstance();
        logger.info("Setting up Customer API tests");
        logger.info("Environment: {}", System.getProperty("env", "dev"));
        logger.info("API Base URL: {}", config.getApiBaseUrl());
    }
    
    /**
     * Test: Verify 'POST /customers' creates user with email and password only
     * Test ID: ECOM-165
     * 
     * This test verifies that the WooCommerce API can create a new customer
     * with only email and password fields, which are the minimum required fields.
     */
    @Test(description = "ECOM-165: Verify POST /customers creates user with email and password only")
    public void testCreateCustomerWithEmailAndPasswordOnly() {
        logger.info("=== Starting ECOM-165: Create customer with email and password only ===");
        
        try {
            // Prepare test data
            String testEmail = "test.customer." + System.currentTimeMillis() + "@example.com";
            String testPassword = "TestPassword123!";
            
            // Create request body with minimal required fields
            Map<String, Object> customerData = new HashMap<>();
            customerData.put("email", testEmail);
            customerData.put("password", testPassword);
            
            logger.info("Creating customer with email: {}", testEmail);
            
            // Make API call
            Response response = post("/customers", customerData);
            
            // Verify response status
            int statusCode = response.getStatusCode();
            logger.info("Response status code: {}", statusCode);
            
            // Assert response status (201 for created, or 200 for success)
            Assert.assertTrue(statusCode == 201 || statusCode == 200, 
                "Expected status code 200 or 201, but got: " + statusCode);
            
            // Verify response body contains customer data
            String responseBody = response.getBody().asString();
            logger.info("Response body: {}", responseBody);
            
            // Assert response contains email
            Assert.assertTrue(responseBody.contains(testEmail), 
                "Response should contain the created customer's email");
            
            // Assert response contains customer ID
            Assert.assertTrue(responseBody.contains("\"id\""), 
                "Response should contain customer ID");
            
            // Verify customer was created successfully
            if (statusCode == 201 || statusCode == 200) {
                logger.info("✅ Customer created successfully with email and password only");
                
                // Extract customer ID for potential cleanup
                String customerId = response.jsonPath().getString("id");
                if (customerId != null) {
                    logger.info("Created customer ID: {}", customerId);
                    // In a real scenario, you might want to clean up this test data
                    // cleanupCustomer(customerId);
                }
            }
            
        } catch (Exception e) {
            logger.error("Test failed with exception: {}", e.getMessage(), e);
            Assert.fail("Test failed: " + e.getMessage());
        }
        
        logger.info("=== Completed ECOM-165: Create customer with email and password only ===");
    }
    
    /**
     * Test: Verify customer creation fails without required email field
     * 
     * This is a negative test to ensure the API properly validates required fields.
     */
    @Test(description = "Verify customer creation fails without required email field")
    public void testCreateCustomerFailsWithoutEmail() {
        logger.info("=== Starting negative test: Create customer without email ===");
        
        try {
            // Create request body without email (should fail)
            Map<String, Object> customerData = new HashMap<>();
            customerData.put("password", "TestPassword123!");
            
            logger.info("Attempting to create customer without email");
            
            // Make API call
            Response response = post("/customers", customerData);
            
            // Verify response status indicates error
            int statusCode = response.getStatusCode();
            logger.info("Response status code: {}", statusCode);
            
            // Assert response indicates error (400 for bad request)
            Assert.assertTrue(statusCode >= 400, 
                "Expected error status code (400+), but got: " + statusCode);
            
            logger.info("✅ API correctly rejected customer creation without email");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: {}", e.getMessage(), e);
            Assert.fail("Test failed: " + e.getMessage());
        }
        
        logger.info("=== Completed negative test: Create customer without email ===");
    }
    
    /**
     * Test: Verify customer creation fails without required password field
     * 
     * This is a negative test to ensure the API properly validates required fields.
     */
    @Test(description = "Verify customer creation fails without required password field")
    public void testCreateCustomerFailsWithoutPassword() {
        logger.info("=== Starting negative test: Create customer without password ===");
        
        try {
            // Create request body without password (should fail)
            String testEmail = "test.customer." + System.currentTimeMillis() + "@example.com";
            Map<String, Object> customerData = new HashMap<>();
            customerData.put("email", testEmail);
            
            logger.info("Attempting to create customer without password");
            
            // Make API call
            Response response = post("/customers", customerData);
            
            // Verify response status indicates error
            int statusCode = response.getStatusCode();
            logger.info("Response status code: {}", statusCode);
            
            // Assert response indicates error (400 for bad request)
            Assert.assertTrue(statusCode >= 400, 
                "Expected error status code (400+), but got: " + statusCode);
            
            logger.info("✅ API correctly rejected customer creation without password");
            
        } catch (Exception e) {
            logger.error("Test failed with exception: {}", e.getMessage(), e);
            Assert.fail("Test failed: " + e.getMessage());
        }
        
        logger.info("=== Completed negative test: Create customer without password ===");
    }
    
    /**
     * Cleanup method to delete test customer (if needed)
     * 
     * @param customerId the customer ID to delete
     */
    private void cleanupCustomer(String customerId) {
        try {
            logger.info("Cleaning up test customer: {}", customerId);
            Response response = delete("/customers/" + customerId);
            if (response.getStatusCode() == 200 || response.getStatusCode() == 204) {
                logger.info("Successfully cleaned up customer: {}", customerId);
            } else {
                logger.warn("Failed to cleanup customer: {} - Status: {}", customerId, response.getStatusCode());
            }
        } catch (Exception e) {
            logger.warn("Error during customer cleanup: {}", e.getMessage());
        }
    }
}
