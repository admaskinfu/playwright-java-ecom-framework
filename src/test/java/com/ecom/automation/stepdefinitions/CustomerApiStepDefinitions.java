package com.ecom.automation.stepdefinitions;

import com.ecom.automation.api.SimpleOAuthClient;
import com.ecom.automation.config.ConfigManager;
import com.ecom.automation.utils.ApiCredentialValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * CustomerApiStepDefinitions - Step definitions for customer API tests
 * 
 * This class contains Cucumber step definitions for customer API testing.
 * It uses the SimpleOAuthClient directly for OAuth 1.0a authentication.
 * 
 * Similar to Python's step definitions but using OAuth 1.0a and TestNG
 */
public class CustomerApiStepDefinitions {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerApiStepDefinitions.class);
    private ConfigManager config;
    private SimpleOAuthClient oauthClient;
    private Map<String, Object> customerData;
    private SimpleOAuthClient.SimpleApiResponse apiResponse;
    private String createdCustomerId;
    private String existingCustomerEmail;
    private ObjectMapper objectMapper;
    
    public CustomerApiStepDefinitions() {
        this.config = ConfigManager.getInstance();
        this.objectMapper = new ObjectMapper();
        setupOAuthClient();
    }
    
    /**
     * Setup OAuth client for API authentication
     */
    private void setupOAuthClient() {
        try {
            // Validate API credentials before proceeding
            ApiCredentialValidator.validateApiCredentials();
            
            // Initialize OAuth 1.0a client for WooCommerce
            oauthClient = new SimpleOAuthClient(
                config.getApiConsumerKey(),
                config.getApiConsumerSecret(),
                config.getApiBaseUrl()
            );
            
            logger.info("OAuth client initialized successfully");
            
        } catch (Exception e) {
            logger.error("Failed to setup OAuth client: {}", e.getMessage());
            throw new RuntimeException("OAuth client setup failed", e);
        }
    }
    
    /**
     * Generate a unique email for testing to avoid conflicts in parallel execution
     * 
     * @param prefix the prefix for the email (e.g., "test.customer", "existing.customer")
     * @return a unique email address
     */
    private String generateUniqueEmail(String prefix) {
        long timestamp = System.currentTimeMillis();
        long threadId = Thread.currentThread().getId();
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return String.format("%s.%d.%d.%d@example.com", prefix, timestamp, threadId, random);
    }

    /**
     * Helper method to parse JSON from OAuth response
     *
     * @param jsonPath the JSON path to extract (e.g., "id", "email")
     * @return the value at the specified path, or null if not found
     */
    private String getJsonValue(String jsonPath) {
        try {
            JsonNode rootNode = objectMapper.readTree(apiResponse.getBody());
            JsonNode valueNode = rootNode.at("/" + jsonPath);
            return valueNode.isMissingNode() ? null : valueNode.asText();
        } catch (Exception e) {
            logger.error("Failed to parse JSON from response: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Helper method to parse JSON array from OAuth response
     * 
     * @return List of Maps representing the JSON array
     */
    private List<Map<String, Object>> getJsonArray() {
        try {
            JsonNode rootNode = objectMapper.readTree(apiResponse.getBody());
            if (rootNode.isArray()) {
                return objectMapper.convertValue(rootNode, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
            }
            return null;
        } catch (Exception e) {
            logger.error("Failed to parse JSON array from response: {}", e.getMessage());
            return null;
        }
    }
    
    @Given("I have valid customer data with email and password")
    public void i_have_valid_customer_data_with_email_and_password() {
        logger.info("Preparing valid customer data with email and password");
        
        customerData = new HashMap<>();
        String testEmail = generateUniqueEmail("test.customer");
        String testPassword = "TestPassword123!";
        
        customerData.put("email", testEmail);
        customerData.put("password", testPassword);
        
        logger.info("Customer data prepared - Email: {}", testEmail);
    }
    
    @When("I create a new customer via POST \\/customers")
    public void i_create_a_new_customer_via_post_customers() {
        logger.info("Creating new customer via POST /customers");
        
        apiResponse = oauthClient.post("/customers", customerData);
        
        logger.info("API call completed - Status: {}", apiResponse.getStatusCode());
    }
    
    @Then("the customer should be created successfully")
    public void the_customer_should_be_created_successfully() {
        logger.info("Verifying customer was created successfully");
        
        int statusCode = apiResponse.getStatusCode();
        logger.info("Response status code: {}", statusCode);
        
        // Assert response status (201 for created, or 200 for success)
        if (statusCode != 201 && statusCode != 200) {
            logger.error("❌ API call failed with status: {}", statusCode);
            logger.error("Response body: {}", apiResponse.getBody());
            logger.error("Request data: {}", customerData);
        }
        Assert.assertTrue(statusCode == 201 || statusCode == 200, 
            "Expected status code 200 or 201, but got: " + statusCode + 
            "\nResponse body: " + apiResponse.getBody());
        
        logger.info("✅ Customer created successfully");
    }
    
    @Then("the response should contain the customer email")
    public void the_response_should_contain_the_customer_email() {
        logger.info("Verifying response contains customer email");
        
        String responseBody = apiResponse.getBody();
        String expectedEmail = (String) customerData.get("email");
        
        Assert.assertTrue(responseBody.contains(expectedEmail), 
            "Response should contain the customer email: " + expectedEmail);
        
        logger.info("✅ Response contains customer email: {}", expectedEmail);
    }
    
    @Then("the response should contain a customer ID")
    public void the_response_should_contain_a_customer_id() {
        logger.info("Verifying response contains customer ID");
        
        String responseBody = apiResponse.getBody();
        
        Assert.assertTrue(responseBody.contains("\"id\""), 
            "Response should contain customer ID");
        
        // Extract and log the customer ID
        String customerId = getJsonValue("id");
        if (customerId != null) {
            logger.info("✅ Customer ID found: {}", customerId);
            createdCustomerId = customerId; // Store for later use
        }
    }
    
    // =================================================================
    // ECOM-166: GET /customers lists all users
    // =================================================================
    
    @Given("I have access to the customers API")
    public void i_have_access_to_the_customers_api() {
        logger.info("Verifying access to customers API");
        // API access is already validated in BaseApiTest constructor
        logger.info("✅ API access confirmed");
    }
    
    @When("I retrieve all customers via GET \\/customers")
    public void i_retrieve_all_customers_via_get_customers() {
        logger.info("Retrieving all customers via GET /customers");
        
        apiResponse = oauthClient.get("/customers");
        
        logger.info("API call completed - Status: {}", apiResponse.getStatusCode());
    }
    
    @Then("the response should be successful")
    public void the_response_should_be_successful() {
        logger.info("Verifying response is successful");
        
        int statusCode = apiResponse.getStatusCode();
        Assert.assertTrue(statusCode >= 200 && statusCode < 300, 
            "Expected successful status code (2xx), but got: " + statusCode);
        
        logger.info("✅ Response is successful");
    }
    
    @Then("the response should contain a list of customers")
    public void the_response_should_contain_a_list_of_customers() {
        logger.info("Verifying response contains list of customers");
        
        String responseBody = apiResponse.getBody();
        Assert.assertTrue(responseBody.contains("[") && responseBody.contains("]"), 
            "Response should contain a JSON array");
        
        // Try to parse as list to verify it's a proper array
        List<Map<String, Object>> customers = getJsonArray();
        Assert.assertNotNull(customers, "Response should be parseable as a list");
        
        logger.info("✅ Response contains list of customers ({} items)", customers.size());
    }
    
    // =================================================================
    // ECOM-167: POST /customers fails if password not provided
    // =================================================================
    
    @Given("I have customer data with email but no password")
    public void i_have_customer_data_with_email_but_no_password() {
        logger.info("Preparing customer data with email but no password");
        
        customerData = new HashMap<>();
        String testEmail = generateUniqueEmail("test.customer");
        
        customerData.put("email", testEmail);
        // Intentionally not adding password
        
        logger.info("Customer data prepared - Email: {}, Password: (none)", testEmail);
    }
    
    @When("I attempt to create a customer via POST \\/customers")
    public void i_attempt_to_create_a_customer_via_post_customers() {
        logger.info("Attempting to create customer via POST /customers");
        
        apiResponse = oauthClient.post("/customers", customerData);
        
        logger.info("API call completed - Status: {}", apiResponse.getStatusCode());
    }
    
    @Then("the request should fail with appropriate error")
    public void the_request_should_fail_with_appropriate_error() {
        logger.info("Verifying request failed with appropriate error");
        
        int statusCode = apiResponse.getStatusCode();
        Assert.assertTrue(statusCode >= 400, 
            "Expected error status code (4xx or 5xx), but got: " + statusCode);
        
        logger.info("✅ Request failed with appropriate error status: {}", statusCode);
    }
    
    @Then("the response should indicate password is required")
    public void the_response_should_indicate_password_is_required() {
        logger.info("Verifying response indicates password is required");
        
        String responseBody = apiResponse.getBody().toLowerCase();
        Assert.assertTrue(responseBody.contains("password") || responseBody.contains("required"), 
            "Response should indicate password is required");
        
        logger.info("✅ Response indicates password is required");
    }
    
    // =================================================================
    // ECOM-168: Create customer with only email and password has names as empty string
    // =================================================================
    
    @Given("I have valid customer data with email and password only")
    public void i_have_valid_customer_data_with_email_and_password_only() {
        logger.info("Preparing customer data with email and password only (no names)");
        
        customerData = new HashMap<>();
        String testEmail = generateUniqueEmail("test.customer");
        String testPassword = "TestPassword123!";
        
        customerData.put("email", testEmail);
        customerData.put("password", testPassword);
        // Intentionally not adding first_name or last_name
        
        logger.info("Customer data prepared - Email: {}, Password: (set), Names: (none)");
    }
    
    @Then("the response should contain empty string for first_name")
    public void the_response_should_contain_empty_string_for_first_name() {
        logger.info("Verifying response contains empty string for first_name");
        
        String firstName = getJsonValue("first_name");
        Assert.assertEquals(firstName, "", "first_name should be empty string");
        
        logger.info("✅ Response contains empty string for first_name");
    }
    
    @Then("the response should contain empty string for last_name")
    public void the_response_should_contain_empty_string_for_last_name() {
        logger.info("Verifying response contains empty string for last_name");
        
        String lastName = getJsonValue("last_name");
        Assert.assertEquals(lastName, "", "last_name should be empty string");
        
        logger.info("✅ Response contains empty string for last_name");
    }
    
    // =================================================================
    // ECOM-169: Username is autogenerated based on email
    // =================================================================
    
    @Then("the response should contain a username based on the email")
    public void the_response_should_contain_a_username_based_on_the_email() {
        logger.info("Verifying response contains username based on email");
        
        String username = getJsonValue("username");
        String email = (String) customerData.get("email");
        
        Assert.assertNotNull(username, "Username should not be null");
        Assert.assertFalse(username.isEmpty(), "Username should not be empty");
        
        // Username is typically derived from email (before @ symbol)
        String expectedUsername = email.split("@")[0];
        Assert.assertTrue(username.contains(expectedUsername) || username.equals(expectedUsername), 
            "Username should be based on email: " + email);
        
        logger.info("✅ Response contains username based on email: {}", username);
    }
    
    // =================================================================
    // ECOM-170: Create customer fails if email exists
    // =================================================================
    
    @Given("I have a customer with an existing email")
    public void i_have_a_customer_with_an_existing_email() {
        logger.info("Preparing customer with existing email");
        
        // First create a customer to get an existing email
        customerData = new HashMap<>();
        existingCustomerEmail = generateUniqueEmail("existing.customer");
        String testPassword = "TestPassword123!";
        
        customerData.put("email", existingCustomerEmail);
        customerData.put("password", testPassword);
        
        // Create the first customer
        SimpleOAuthClient.SimpleApiResponse firstResponse = oauthClient.post("/customers", customerData);
        Assert.assertTrue(firstResponse.getStatusCode() == 201 || firstResponse.getStatusCode() == 200, 
            "First customer should be created successfully");
        
        logger.info("Existing customer created with email: {}", existingCustomerEmail);
    }
    
    @When("I attempt to create another customer with the same email")
    public void i_attempt_to_create_another_customer_with_the_same_email() {
        logger.info("Attempting to create another customer with same email");
        
        // Prepare data with the same email
        Map<String, Object> duplicateData = new HashMap<>();
        duplicateData.put("email", existingCustomerEmail);
        duplicateData.put("password", "AnotherPassword123!");
        
        apiResponse = oauthClient.post("/customers", duplicateData);
        
        logger.info("API call completed - Status: {}", apiResponse.getStatusCode());
    }
    
    @Then("the response should indicate email already exists")
    public void the_response_should_indicate_email_already_exists() {
        logger.info("Verifying response indicates email already exists");
        
        String responseBody = apiResponse.getBody().toLowerCase();
        Assert.assertTrue(responseBody.contains("email") && 
                         (responseBody.contains("already") || responseBody.contains("exists") || 
                          responseBody.contains("duplicate") || responseBody.contains("taken")), 
            "Response should indicate email already exists");
        
        logger.info("✅ Response indicates email already exists");
    }
    
    // =================================================================
    // ECOM-171: Status code 404 for non-existing customer
    // =================================================================
    
    @When("^I attempt to retrieve a non-existing customer via GET /customers/\\{id\\}$")
    public void i_attempt_to_retrieve_a_non_existing_customer_via_get_customers_id() {
        logger.info("Attempting to retrieve non-existing customer");
        
        // Use a non-existing ID (very high number)
        String nonExistingId = "999999999";
        apiResponse = oauthClient.get("/customers/" + nonExistingId);
        
        logger.info("API call completed - Status: {}", apiResponse.getStatusCode());
    }
    
    @Then("the response should return status code 404")
    public void the_response_should_return_status_code_404() {
        logger.info("Verifying response returns status code 404");
        
        int statusCode = apiResponse.getStatusCode();
        Assert.assertEquals(statusCode, 404, 
            "Expected status code 404, but got: " + statusCode);
        
        logger.info("✅ Response returns status code 404");
    }
    
    @Then("the response should indicate customer not found")
    public void the_response_should_indicate_customer_not_found() {
        logger.info("Verifying response indicates customer not found");
        
        String responseBody = apiResponse.getBody().toLowerCase();
        Assert.assertTrue(responseBody.contains("not found") || responseBody.contains("not_found") || 
                         responseBody.contains("404") || responseBody.contains("error"), 
            "Response should indicate customer not found");
        
        logger.info("✅ Response indicates customer not found");
    }
    
    // =================================================================
    // ECOM-172: Get customer by id returns one customer
    // =================================================================
    
    @Given("I have a created customer with a known ID")
    public void i_have_a_created_customer_with_a_known_id() {
        logger.info("Creating a customer to get a known ID");
        
        // Create a customer first
        customerData = new HashMap<>();
        String testEmail = generateUniqueEmail("test.customer");
        String testPassword = "TestPassword123!";
        
        customerData.put("email", testEmail);
        customerData.put("password", testPassword);
        
        SimpleOAuthClient.SimpleApiResponse createResponse = oauthClient.post("/customers", customerData);
        Assert.assertTrue(createResponse.getStatusCode() == 201 || createResponse.getStatusCode() == 200, 
            "Customer should be created successfully");
        
        // Parse the response to get the customer ID
        try {
            JsonNode rootNode = objectMapper.readTree(createResponse.getBody());
            JsonNode idNode = rootNode.get("id");
            createdCustomerId = idNode != null ? idNode.asText() : null;
        } catch (Exception e) {
            logger.error("Failed to parse customer ID from response: {}", e.getMessage());
            createdCustomerId = null;
        }
        Assert.assertNotNull(createdCustomerId, "Customer ID should not be null");
        
        logger.info("Customer created with ID: {}", createdCustomerId);
    }
    
    @When("^I retrieve the customer via GET /customers/\\{id\\}$")
    public void i_retrieve_the_customer_via_get_customers_id() {
        logger.info("Retrieving customer by ID: {}", createdCustomerId);
        
        apiResponse = oauthClient.get("/customers/" + createdCustomerId);
        
        logger.info("API call completed - Status: {}", apiResponse.getStatusCode());
    }
    
    @Then("the response should contain exactly one customer")
    public void the_response_should_contain_exactly_one_customer() {
        logger.info("Verifying response contains exactly one customer");
        
        // For GET /customers/{id}, the response should be a single object, not an array
        String responseBody = apiResponse.getBody();
        Assert.assertFalse(responseBody.startsWith("["), 
            "Response should be a single customer object, not an array");
        
        // Verify it has customer properties
        Assert.assertTrue(responseBody.contains("\"id\""), 
            "Response should contain customer ID");
        Assert.assertTrue(responseBody.contains("\"email\""), 
            "Response should contain customer email");
        
        logger.info("✅ Response contains exactly one customer");
    }
    
    @Then("the customer ID should match the requested ID")
    public void the_customer_id_should_match_the_requested_id() {
        logger.info("Verifying customer ID matches requested ID");
        
        String responseId = getJsonValue("id");
        Assert.assertEquals(responseId, createdCustomerId, 
            "Customer ID should match the requested ID");
        
        logger.info("✅ Customer ID matches requested ID: {}", responseId);
    }
    
}
