package com.ecom.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

/**
 * TestUtils - Common utility methods for test automation
 * 
 * This class provides reusable utility methods that can be used across
 * different test classes and step definitions. It follows the utility
 * class pattern for common operations.
 * 
 * Key benefits:
 * - Reusable common operations
 * - Consistent error handling
 * - Performance measurement utilities
 * - String manipulation helpers
 * 
 * Similar to Python's utility modules but implemented as a static
 * utility class for better performance and memory efficiency.
 */
public class TestUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);
    
    /**
     * Wait for a specified duration with logging
     * 
     * @param milliseconds Duration to wait in milliseconds
     * @param reason Reason for waiting (for logging)
     */
    public static void waitFor(int milliseconds, String reason) {
        logger.debug("Waiting {}ms for: {}", milliseconds, reason);
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Wait interrupted: {}", e.getMessage());
        }
    }
    
    /**
     * Measure execution time of a runnable operation
     * 
     * @param operation The operation to measure
     * @param operationName Name of the operation (for logging)
     * @return Duration of the operation
     */
    public static Duration measureTime(Runnable operation, String operationName) {
        logger.debug("Starting operation: {}", operationName);
        Instant start = Instant.now();
        
        try {
            operation.run();
        } finally {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            logger.info("Operation '{}' completed in {}ms", operationName, duration.toMillis());
        }
        
        return Duration.between(start, Instant.now());
    }
    
    /**
     * Format a string with proper null handling
     * 
     * @param value The value to format
     * @return Formatted string or "null" if value is null
     */
    public static String safeString(Object value) {
        return value != null ? value.toString() : "null";
    }
    
    /**
     * Check if a string is null or empty
     * 
     * @param value The string to check
     * @return true if null or empty
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    /**
     * Get current timestamp as string
     * 
     * @return Current timestamp in ISO format
     */
    public static String getCurrentTimestamp() {
        return Instant.now().toString();
    }
    
    /**
     * Log test step with timestamp
     * 
     * @param stepName Name of the test step
     * @param details Additional details about the step
     */
    public static void logTestStep(String stepName, String details) {
        logger.info("TEST STEP: {} - {}", stepName, details);
    }
}
