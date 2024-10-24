# This feature file is related to Jira ticket: ABC-123

@regression @smoketest @cart @cart_operations
Feature: Cart Operations

  @add_remove_item @smoke_prod
  Scenario Outline: Add products to cart and remove them
    Given I am logged in as "<user>"
    Then I should see the products page
    When I add "<product1>" and "<product2>" to the cart
    Then they should be successfully added to the cart
    When I remove "<product1>" and "<product2>" from the cart
    Then they should be successfully removed from the cart

    Examples:
      | user          | product1                | product2                          |
      | standard_user | Sauce Labs Backpack     | Sauce Labs Bike Light             |
      | standard_user | Sauce Labs Bolt T-Shirt | Sauce Labs Fleece Jacket          |
      | standard_user | Sauce Labs Onesie       | Test.allTheThings() T-Shirt (Red) |

@complete_purchase @e2e
Scenario Outline: Complete purchase and verify
  Given I am logged in as "<user>"
  Then I should see the products page
  When I add "<product1>" and "<product2>" to the cart
  Then they should be successfully added to the cart
  And I go to the cart and verify "<product1>" and "<product2>" are displayed
  When I proceed to checkout and try to submit the empty form
  Then I should see an error message for the empty form
  When I clear the form, fill it in and continue
  Then I should see the order summary with correct price, items, and buttons for "<product1>" and "<product2>"
  When I complete the purchase
  Then I should see a success message and the order confirmation page

  Examples:
    | user            | product1                | product2                          |
    | standard_user   | Sauce Labs Backpack     | Sauce Labs Bike Light             |
    | standard_user   | Sauce Labs Bolt T-Shirt | Sauce Labs Fleece Jacket          |
    | standard_user   | Sauce Labs Onesie       | Test.allTheThings() T-Shirt (Red) |