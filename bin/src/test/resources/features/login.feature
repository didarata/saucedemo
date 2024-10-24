Feature: login

  Scenario: Add products to cart
    Given I am on the login page
    When I login with standard_user
    Then I should see the products page
    When I add two products to the cart
    Then they should be successfully added to the cart