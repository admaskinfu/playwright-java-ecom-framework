package com.ecom.automation.pages;

import com.ecom.automation.locators.HomePageLocators;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * HomePage - Page Object Model for the e-commerce homepage
 * 
 * This class encapsulates all interactions with the homepage elements.
 * It follows the Page Object Model pattern to keep test code clean and maintainable.
 * 
 * Similar to Python's page object pattern, but using Playwright's Java API.
 */
public class HomePage {
    
    private final Page page;
    
    // Constructor - requires a Playwright Page object
    public HomePage(Page page) {
        this.page = page;
    }
    
    /**
     * Navigate to the homepage
     * 
     * @param baseUrl the base URL of the application
     */
    public void navigateToHomepage(String baseUrl) {
        page.navigate(baseUrl);
        
        // Wait for page to be fully loaded
        page.waitForLoadState();
    }
    
    /**
     * Get all product elements from the homepage
     * 
     * @return List of product locators
     */
    public List<Locator> getProductElements() {
        // Using CSS selector to find all product items
        // This selector targets the product containers on the homepage
        return page.locator(HomePageLocators.PRODUCT_GRID).all();
    }
    
    /**
     * Get the count of products displayed on the homepage
     * 
     * @return number of products
     */
    public int getProductCount() {
        return getProductElements().size();
    }
    
    /**
     * Get product name by index
     * 
     * @param index product index (0-based)
     * @return product name
     */
    public String getProductName(int index) {
        List<Locator> products = getProductElements();
        if (index >= 0 && index < products.size()) {
            return products.get(index).textContent();
        }
        throw new IndexOutOfBoundsException("Product index out of range: " + index);
    }
    
    /**
     * Get product price by index
     * 
     * @param index product index (0-based)
     * @return product price
     */
    public String getProductPrice(int index) {
        // Get the price element for the product at the given index
        List<Locator> products = getProductElements();
        if (index >= 0 && index < products.size()) {
            // Find the price element that corresponds to this product
            Locator product = products.get(index);
            // The price is typically in a sibling element or parent container
            return product.locator("..").locator(HomePageLocators.PRODUCT_PRICE).textContent();
        }
        throw new IndexOutOfBoundsException("Product index out of range: " + index);
    }
    
    /**
     * Check if a product has both name and price
     * 
     * @param index product index (0-based)
     * @return true if product has both name and price
     */
    public boolean hasProductNameAndPrice(int index) {
        try {
            String name = getProductName(index);
            String price = getProductPrice(index);
            return name != null && !name.trim().isEmpty() && 
                   price != null && !price.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get page title for verification
     * 
     * @return page title
     */
    public String getPageTitle() {
        return page.title();
    }
    
    /**
     * Wait for products to load on the page
     * 
     * @param timeoutMs timeout in milliseconds
     */
    public void waitForProductsToLoad(int timeoutMs) {
        // Wait for at least one product to be visible
        page.waitForSelector(HomePageLocators.PRODUCT_GRID, 
                           new Page.WaitForSelectorOptions().setTimeout(timeoutMs));
    }
    
    /**
     * Get the number of product columns displayed
     * 
     * @return number of columns
     */
    public int getProductColumns() {
        try {
            // Get all product elements
            List<Locator> allProducts = getProductElements();
            if (allProducts.isEmpty()) {
                return 0;
            }
            
            // Get the first product's position
            Locator firstProduct = allProducts.get(0);
            double firstProductY = firstProduct.boundingBox().y;
            
            // Count products in the first row (same Y position)
            int columns = 0;
            for (Locator product : allProducts) {
                double productY = product.boundingBox().y;
                // Consider products in the same row if Y position is within 10 pixels
                if (Math.abs(productY - firstProductY) <= 10) {
                    columns++;
                } else {
                    break; // Stop when we hit the next row
                }
            }
            
            return columns;
        } catch (Exception e) {
            // If we can't determine columns, throw an exception
            // This will cause the test to fail with a clear error message
            throw new RuntimeException("Failed to determine number of product columns: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get the number of product rows displayed
     * 
     * @return number of rows
     */
    public int getProductRows() {
        try {
            // Get all product elements
            List<Locator> allProducts = getProductElements();
            if (allProducts.isEmpty()) {
                return 0;
            }
            
            // Count unique Y positions to determine number of rows
            Set<Double> uniqueYPositions = new HashSet<>();
            for (Locator product : allProducts) {
                double productY = product.boundingBox().y;
                // Round to nearest 50 pixels to account for minor variations
                // This should group products in the same row together
                double roundedY = Math.round(productY / 50.0) * 50.0;
                uniqueYPositions.add(roundedY);
            }
            
            return uniqueYPositions.size();
        } catch (Exception e) {
            // If we can't determine rows, throw an exception
            // This will cause the test to fail with a clear error message
            throw new RuntimeException("Failed to determine number of product rows: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if sorting dropdown is visible at the top of the page
     * 
     * @return true if sorting dropdown is at the top
     */
    public boolean isSortingDropdownAtTop() {
        try {
            // Try multiple selectors for sorting dropdown
            Locator sortingDropdown = page.locator(HomePageLocators.SORTING_DROPDOWN).first();
            if (!sortingDropdown.isVisible()) {
                return false;
            }
            
            // Check if it's in the top section of the page
            double dropdownY = sortingDropdown.boundingBox().y;
            int pageHeight = page.viewportSize().height;
            
            // Consider it "top" if it's in the first 60% of the viewport (more flexible)
            return dropdownY < (pageHeight * 0.6);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if sorting dropdown is visible at the bottom of the page
     * 
     * @return true if sorting dropdown is at the bottom
     */
    public boolean isSortingDropdownAtBottom() {
        try {
            // Try multiple selectors for sorting dropdown
            Locator sortingDropdown = page.locator(HomePageLocators.SORTING_DROPDOWN).first();
            if (!sortingDropdown.isVisible()) {
                return false;
            }
            
            // Check if it's in the bottom section of the page
            double dropdownY = sortingDropdown.boundingBox().y;
            int pageHeight = page.viewportSize().height;
            
            // Consider it "bottom" if it's in the last 40% of the viewport
            return dropdownY > (pageHeight * 0.6);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if the "shop" heading is displayed
     * 
     * @return true if shop heading is visible
     */
    public boolean isShopHeadingDisplayed() {
        try {
            // Look for various possible selectors for the shop heading
            Locator shopHeading = page.locator(HomePageLocators.SHOP_HEADING).first();
            return shopHeading.isVisible();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if the header menu is displayed
     * 
     * @return true if header menu is visible
     */
    public boolean isHeaderMenuDisplayed() {
        try {
            // Look for common header menu selectors
            Locator headerMenu = page.locator(HomePageLocators.HEADER_MENU).first();
            return headerMenu.isVisible();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if any sorting dropdown exists on the page
     * 
     * @return true if any sorting dropdown is visible
     */
    public boolean isSortingDropdownDisplayed() {
        try {
            // Try multiple selectors for sorting dropdown
            Locator sortingDropdown = page.locator(HomePageLocators.SORTING_DROPDOWN).first();
            return sortingDropdown.isVisible();
        } catch (Exception e) {
            return false;
        }
    }
}
