Feature: Homepage Product Display
  As a customer visiting the e-commerce store
  I want to see available products on the homepage
  So that I can browse and select items to purchase

  @smoke @homepage @ECOM-1
  Scenario: Verify 16 products are displayed on homepage
    Given I am on the homepage
    When I view the product listing
    Then I should see exactly 16 products displayed

  @smoke @homepage @ECOM-2
  Scenario: Verify 4 columns of products are displayed
    Given I am on the homepage
    When I view the product listing
    Then I should see exactly 4 columns of products

  @smoke @homepage @ECOM-3
  Scenario: Verify 4 rows of products are displayed
    Given I am on the homepage
    When I view the product listing
    Then I should see exactly 4 rows of products

  @smoke @homepage @ECOM-5
  Scenario: Verify sorting dropdown is displayed on top of page
    Given I am on the homepage
    When I look for sorting controls
    Then I should see sorting dropdown at the top of the page

  @smoke @homepage @ECOM-6
  Scenario: Verify sorting dropdown is displayed on bottom of page
    Given I am on the homepage
    When I look for sorting controls
    Then I should see sorting dropdown at the bottom of the page

  @smoke @homepage @ECOM-7
  Scenario: Verify the heading "shop" is displayed on the home page
    Given I am on the homepage
    When I look for page headings
    Then I should see the heading "shop" displayed

  @smoke @homepage @ECOM-8
  Scenario: Verify header menu is displayed
    Given I am on the homepage
    When I look for the header menu
    Then I should see the header menu displayed
