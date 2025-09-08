package com.ecom.automation.api;

import com.ecom.automation.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseApiTest - Base class for all API tests
 * 
 * This class provides common functionality for API testing including:
 * - Secure authentication setup
 * - Base URL configuration
 * - Common request specifications
 * - Response validation utilities
 * 
 * Follows the same pattern as Python's base test classes but using REST Assured
 */
public class BaseApiTest {
    
    private static final Logger logger = LoggerFactory.getLogger(BaseApiTest.class);
    protected ConfigManager config;
    protected RequestSpecification requestSpec;
    
    /**
     * Constructor - Initialize API test base
     */
    public BaseApiTest() {
        this.config = ConfigManager.getInstance();
        setupApiConfiguration();
    }
    
    /**
     * Setup API configuration and authentication
     * This method configures REST Assured with base URL and authentication
     */
    protected void setupApiConfiguration() {
        try {
            // Set base URI
            RestAssured.baseURI = config.getApiBaseUrl();
            
            // Create request specification with common settings
            requestSpec = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam("consumer_key", config.getApiConsumerKey())
                .queryParam("consumer_secret", config.getApiConsumerSecret());
            
            logger.info("API configuration initialized:");
            logger.info("- Base URL: {}", config.getApiBaseUrl());
            logger.info("- Consumer Key: {}...", maskApiKey(config.getApiConsumerKey()));
            logger.info("- Consumer Secret: {}...", maskApiSecret(config.getApiConsumerSecret()));
            
        } catch (Exception e) {
            logger.error("Failed to setup API configuration: {}", e.getMessage());
            throw new RuntimeException("API configuration setup failed", e);
        }
    }
    
    /**
     * Create a new request specification for API calls
     * 
     * @return RequestSpecification with authentication
     */
    protected RequestSpecification getRequestSpec() {
        return requestSpec.given();
    }
    
    /**
     * Perform a POST request
     * 
     * @param endpoint the API endpoint
     * @param requestBody the request body
     * @return Response object
     */
    protected Response post(String endpoint, Object requestBody) {
        logger.info("Making POST request to: {}", endpoint);
        Response response = getRequestSpec()
            .body(requestBody)
            .post(endpoint);
        
        logResponse(response);
        return response;
    }
    
    /**
     * Perform a GET request
     * 
     * @param endpoint the API endpoint
     * @return Response object
     */
    protected Response get(String endpoint) {
        logger.info("Making GET request to: {}", endpoint);
        Response response = getRequestSpec()
            .get(endpoint);
        
        logResponse(response);
        return response;
    }
    
    /**
     * Perform a PUT request
     * 
     * @param endpoint the API endpoint
     * @param requestBody the request body
     * @return Response object
     */
    protected Response put(String endpoint, Object requestBody) {
        logger.info("Making PUT request to: {}", endpoint);
        Response response = getRequestSpec()
            .body(requestBody)
            .put(endpoint);
        
        logResponse(response);
        return response;
    }
    
    /**
     * Perform a DELETE request
     * 
     * @param endpoint the API endpoint
     * @return Response object
     */
    protected Response delete(String endpoint) {
        logger.info("Making DELETE request to: {}", endpoint);
        Response response = getRequestSpec()
            .delete(endpoint);
        
        logResponse(response);
        return response;
    }
    
    /**
     * Log response details for debugging
     * 
     * @param response the response to log
     */
    protected void logResponse(Response response) {
        logger.info("Response Status: {}", response.getStatusCode());
        logger.info("Response Headers: {}", response.getHeaders().asList());
        if (response.getBody() != null) {
            logger.debug("Response Body: {}", response.getBody().asString());
        }
    }
    
    /**
     * Mask API key for secure logging
     * 
     * @param apiKey the API key to mask
     * @return masked API key
     */
    private String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.length() <= 8) {
            return "****";
        }
        return apiKey.substring(0, 4) + "****" + apiKey.substring(apiKey.length() - 4);
    }
    
    /**
     * Mask API secret for secure logging
     * 
     * @param apiSecret the API secret to mask
     * @return masked API secret
     */
    private String maskApiSecret(String apiSecret) {
        if (apiSecret == null || apiSecret.length() <= 8) {
            return "****";
        }
        return apiSecret.substring(0, 4) + "****" + apiSecret.substring(apiSecret.length() - 4);
    }
}
