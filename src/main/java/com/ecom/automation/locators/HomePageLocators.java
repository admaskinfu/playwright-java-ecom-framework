package com.ecom.automation.locators;

/**
 * HomePageLocators - Centralized locator management for HomePage
 * 
 * This class contains all CSS selectors, XPath expressions, and other locators
 * used in the HomePage class. This follows the best practice of separating
 * locators from page logic for better maintainability.
 * 
 * Benefits:
 * - Single source of truth for all selectors
 * - Easy to update when UI changes
 * - Better readability and organization
 * - Reduces duplication across page objects
 */
public class HomePageLocators {
    
    // Product-related locators
    public static final String PRODUCT_CONTAINER = ".product";
    public static final String PRODUCT_TITLE = ".woocommerce-loop-product__title";
    public static final String PRODUCT_PRICE = ".price";
    
    // Sorting and filtering locators
    public static final String SORTING_DROPDOWN = "select[name='orderby'], .woocommerce-ordering select, .orderby, select.orderby";
    
    // Navigation and header locators
    public static final String SHOP_HEADING = "h1:has-text('Shop'), h2:has-text('Shop'), .shop-title:has-text('Shop')";
    public static final String HEADER_MENU = "nav, .main-navigation, .header-menu, .site-navigation";
    
    // Page structure locators
    public static final String PRODUCT_GRID = ".woocommerce-loop-product__titlee";
    
    // Private constructor to prevent instantiation
    private HomePageLocators() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }
}
