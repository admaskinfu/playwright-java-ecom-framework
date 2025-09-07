package com.ecom.automation.factory;

import com.ecom.automation.pages.HomePage;
import com.microsoft.playwright.Page;

/**
 * PageFactory - Centralized page object creation and management
 * 
 * This factory class provides a clean way to create page objects with
 * proper dependency injection. It follows the Factory pattern to ensure
 * consistent page object initialization across the framework.
 * 
 * Key benefits:
 * - Centralized page object creation
 * - Consistent initialization patterns
 * - Easy to extend for new pages
 * - Clean separation of concerns
 * 
 * Similar to Python's page object factories but implemented as a
 * dedicated factory class for better type safety and IDE support.
 */
public class PageFactory {
    
    /**
     * Create a HomePage instance with the given Page object
     * 
     * @param page Playwright Page object
     * @return HomePage instance
     */
    public static HomePage createHomePage(Page page) {
        return new HomePage(page);
    }
    
    
    // Future page objects can be added here:
    // public static ProductPage createProductPage(Page page) { ... }
    // public static CartPage createCartPage(Page page) { ... }
    // public static CheckoutPage createCheckoutPage(Page page) { ... }
}
