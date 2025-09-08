package com.ecom.automation.api;

import com.ecom.automation.config.ConfigManager;
import com.ecom.automation.utils.ApiCredentialValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * SimpleCustomerApiTest - Direct OAuth 1.0a test for WooCommerce API
 * 
 * This test bypasses REST Assured and uses our SimpleOAuthClient directly
 * to test the ECOM-165 scenario with proper OAuth 1.0a authentication.
 */
public class SimpleCustomerApiTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleCustomerApiTest.class);
    private ConfigManager config;
    private SimpleOAuthClient oauthClient;

    @BeforeClass
    public void setup() {
        logger.info("Setting up Simple Customer API test");
        config = ConfigManager.getInstance();
        
        // Validate API credentials
        ApiCredentialValidator.validateApiCredentials();
        
        // Initialize OAuth client
        oauthClient = new SimpleOAuthClient(
            config.getApiConsumerKey(),
            config.getApiConsumerSecret(),
            config.getApiBaseUrl()
        );
        
        logger.info("OAuth client initialized successfully");
    }

    @Test(description = "ECOM-165: Verify POST /customers creates user with email and password only")
    public void testCreateCustomerWithEmailAndPasswordOnly() {
        logger.info("=== Starting ECOM-165: Create customer with email and password only ===");
        
        String uniqueEmail = "test.customer." + System.currentTimeMillis() + "@example.com";
        String password = "Password123!";

        logger.info("Creating customer with email: {}", uniqueEmail);

        Map<String, String> customerData = new HashMap<>();
        customerData.put("email", uniqueEmail);
        customerData.put("password", password);

        // Make OAuth POST request
        SimpleOAuthClient.SimpleApiResponse response = oauthClient.post("/customers", customerData);

        logger.info("Response status code: {}", response.getStatusCode());
        logger.info("Response body: {}", response.getBody());

        // Assertions
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 201,
                "Expected status code 200 or 201, but got: " + response.getStatusCode());
        
        // Parse response body to verify email and ID
        if (response.getBody().contains(uniqueEmail)) {
            logger.info("✅ Customer email found in response");
        } else {
            Assert.fail("Expected customer email not found in response");
        }
        
        if (response.getBody().contains("\"id\"")) {
            logger.info("✅ Customer ID found in response");
        } else {
            Assert.fail("Expected customer ID not found in response");
        }
        
        logger.info("✅ Customer created successfully!");
    }
}
