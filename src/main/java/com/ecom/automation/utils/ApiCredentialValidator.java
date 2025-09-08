package com.ecom.automation.utils;

import com.ecom.automation.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ApiCredentialValidator - Utility class for validating API credentials
 * 
 * This class provides methods to validate API credentials and provide
 * clear error messages when credentials are missing or invalid.
 * 
 * Similar to Python's credential validation utilities but using Java
 */
public class ApiCredentialValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(ApiCredentialValidator.class);
    
    /**
     * Validate API credentials and provide clear error messages
     * 
     * @throws RuntimeException if credentials are missing or invalid
     */
    public static void validateApiCredentials() {
        ConfigManager config = ConfigManager.getInstance();
        String consumerKey = config.getApiConsumerKey();
        String consumerSecret = config.getApiConsumerSecret();
        
        logger.info("Validating API credentials...");
        
        // Check consumer key
        if (consumerKey == null || consumerKey.trim().isEmpty()) {
            String errorMsg = createMissingCredentialError("API_CONSUMER_KEY", "ck_your_actual_key_here");
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        if (consumerKey.equals("your_consumer_key_here")) {
            String errorMsg = createMissingCredentialError("API_CONSUMER_KEY", "ck_your_actual_key_here");
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        // Check consumer secret
        if (consumerSecret == null || consumerSecret.trim().isEmpty()) {
            String errorMsg = createMissingCredentialError("API_CONSUMER_SECRET", "cs_your_actual_secret_here");
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        if (consumerSecret.equals("your_consumer_secret_here")) {
            String errorMsg = createMissingCredentialError("API_CONSUMER_SECRET", "cs_your_actual_secret_here");
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        
        logger.info("✅ API credentials validation passed");
    }
    
    /**
     * Create a detailed error message for missing credentials
     * 
     * @param envVarName the environment variable name
     * @param exampleValue example value for the credential
     * @return formatted error message
     */
    private static String createMissingCredentialError(String envVarName, String exampleValue) {
        return String.format(
            "❌ %s environment variable is not set!\n\n" +
            "🔧 Setup Instructions:\n" +
            "1. Set the environment variable:\n" +
            "   export %s=\"%s\"\n\n" +
            "2. Or add it to your shell profile (~/.bashrc, ~/.zshrc, etc.):\n" +
            "   echo 'export %s=\"%s\"' >> ~/.bashrc\n" +
            "   source ~/.bashrc\n\n" +
            "3. Or set it temporarily for this session:\n" +
            "   %s=\"%s\" mvn test\n\n" +
            "4. Check your configuration files:\n" +
            "   - src/test/resources/config/dev.properties\n" +
            "   - src/test/resources/config/staging.properties\n" +
            "   - src/test/resources/config/prod.properties\n\n" +
            "📚 For more help, see: API_SETUP.md",
            envVarName, envVarName, exampleValue, envVarName, exampleValue, 
            envVarName, exampleValue
        );
    }
    
}
