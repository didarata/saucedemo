package com.example;

import com.example.pages.LoginPage;
import com.example.pages.ProductsPage;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.AfterAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class CartOperationsStepDefinitions {
    static WebDriver driver;
    static ProductsPage productsPage;
    static LoginPage loginPage;
    static Properties credentials = new Properties();
    static Properties userData = new Properties();

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        productsPage = new ProductsPage(driver);
        loginPage = new LoginPage(driver);
        loadCredentials();
        loadUserData();
    }

    @AfterAll
    public static void tearDown() {
        logout();
        verifyLogout();
        driver.quit();
    }

    @Given("I am logged in as {string}")
    public void i_am_logged_in_as(String user) {
        driver.get("https://www.saucedemo.com/");
        loginWithUser(user);
    }

    @When("I remove the products from the cart")
    public void i_remove_the_products_from_the_cart() {
        productsPage.getFirstProductRemoveButton().click();
        productsPage.getSecondProductRemoveButton().click();
    }

    @Then("they should be successfully removed from the cart")
    public void they_should_be_successfully_removed_from_the_cart() {
        try {
            productsPage.getCartBadge().getText();
            Assert.fail("Products were not successfully removed from the cart");
        } catch (NoSuchElementException e) {
            Assert.assertTrue(true, "Products were successfully removed from the cart");
        }
    }

    private double product1Price;
    private double product2Price;

    @When("I add {string} and {string} to the cart")
    public void i_add_products_to_the_cart(String product1, String product2) {
        product1Price = productsPage.getProductPrice(product1);
        product2Price = productsPage.getProductPrice(product2);
        productsPage.getProductAddButton(product1).click();
        productsPage.getProductAddButton(product2).click();
    }

    @When("I remove {string} and {string} from the cart")
    public void i_remove_products_from_the_cart(String product1, String product2) {
        productsPage.getProductRemoveButton(product1).click();
        productsPage.getProductRemoveButton(product2).click();
    }

    @Then("I go to the cart and verify {string} and {string} are displayed")
    public void i_go_to_the_cart_and_verify_products_are_displayed(String product1, String product2) {
        productsPage.getCartButton().click();
        Assert.assertTrue(productsPage.getProductInCart(product1).isDisplayed(),
                product1 + " is not displayed in the cart");
        Assert.assertTrue(productsPage.getProductInCart(product2).isDisplayed(),
                product2 + " is not displayed in the cart");
    }

    @When("I add two different products to the cart")
    public void i_add_two_different_products_to_the_cart() {
        productsPage.getFirstProductAddButton().click();
        productsPage.getSecondProductAddButton().click();
    }

    @Then("they should be successfully added again to the cart")
    public void they_should_be_successfully_added_again_to_the_cart() {
        String cartCount = productsPage.getCartBadge().getText();
        Assert.assertEquals(cartCount, "2", "Two products were not successfully added to the cart");
    }

    @Then("I should see the products in the cart")
    public void i_should_see_the_products_in_the_cart() {
        Assert.assertTrue(productsPage.getFirstProductInCart().isDisplayed(),
                "First product is not displayed in the cart");
        Assert.assertTrue(productsPage.getSecondProductInCart().isDisplayed(),
                "Second product is not displayed in the cart");
    }

    @When("I proceed to checkout and try to submit the empty form")
    public void i_continue_to_the_next_screen() {
        productsPage.getCheckoutButton().click();
        productsPage.getContinueButton().click();
    }

    @Then("I should see an error message for the empty form")
    public void i_should_see_an_error_message_for_the_empty_form() {

        Assert.assertTrue(productsPage.getErrorMessage().isDisplayed(), "Error message is not displayed");
    }

    @When("I clear the form, fill it in and continue")
    public void i_clear_the_form_fill_it_in_and_continue() {
        productsPage.getFirstNameField().clear();
        productsPage.getLastNameField().clear();
        productsPage.getPostalCodeField().clear();

        productsPage.getFirstNameField().sendKeys(userData.getProperty("firstName"));
        productsPage.getLastNameField().sendKeys(userData.getProperty("lastName"));
        productsPage.getPostalCodeField().sendKeys(userData.getProperty("postalCode"));

        productsPage.getContinueButton().click();
    }

    @Then("I should see the order summary with correct price, items, and buttons for {string} and {string}")
    public void the_price_items_and_buttons_should_be_displayed_correctly(String product1, String product2) {
        Assert.assertTrue(productsPage.getItemTotal().isDisplayed(), "Item total is not displayed");
        Assert.assertTrue(productsPage.getTax().isDisplayed(), "Tax is not displayed");
        Assert.assertTrue(productsPage.getTotal().isDisplayed(), "Total is not displayed");
        Assert.assertTrue(productsPage.getFinishButton().isDisplayed(), "Finish button is not displayed");

        Assert.assertTrue(productsPage.getProductInSummary(product1).isDisplayed(),
                product1 + " is not displayed in the summary");
        Assert.assertTrue(productsPage.getProductInSummary(product2).isDisplayed(),
                product2 + " is not displayed in the summary");

        double expectedTotalPrice = (product1Price + product2Price) * 1.08;
        expectedTotalPrice = Math.round(expectedTotalPrice * 100.0) / 100.0;
        String expectedTotalPriceStr = String.format("%.2f", expectedTotalPrice);
        String actualTotalPriceStr = String.format("%.2f", productsPage.getTotalPrice());
        Assert.assertEquals(actualTotalPriceStr, expectedTotalPriceStr, "Total price is incorrect");
    }

    @When("I complete the purchase")
    public void i_complete_the_purchase() {
        productsPage.getFinishButton().click();
    }

    @Then("I should see a success message and the order confirmation page")
    public void i_should_see_a_success_message_and_the_order_confirmation_page() {
        Assert.assertTrue(productsPage.getSuccessMessage().isDisplayed(), "Success message is not displayed");
        Assert.assertTrue(productsPage.getOrderConfirmationMessage().isDisplayed(),
                "Order confirmation message is not displayed");
        Assert.assertTrue(productsPage.getOrderConfirmationImage().isDisplayed(),
                "Order confirmation image is not displayed");
    }

    @When("I logout")
    public static void logout() {
        productsPage.getMenuButton().click();
        productsPage.getLogoutButton().click();
    }

    public static void verifyLogout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Assert.assertTrue(new LoginPage(driver).getLoginButtonWithWait().isDisplayed(),
                "Login button is not displayed, logout failed");
    }

    private static void loadCredentials() {
        try (FileInputStream input = new FileInputStream("src/test/resources/credentials.properties")) {
            credentials.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadUserData() {
        try (FileInputStream input = new FileInputStream("src/test/resources/user_data.properties")) {
            userData.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loginWithUser(String user) {
        String password = credentials.getProperty(user);
        loginPage.enterUsername(user);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
    }

    @Then("I should see the order confirmation page")
    public void i_should_see_the_order_confirmation_page() {
        Assert.assertTrue(productsPage.getOrderConfirmationMessage().isDisplayed(),
                "Order confirmation message is not displayed");
        Assert.assertTrue(productsPage.getOrderConfirmationImage().isDisplayed(),
                "Order confirmation image is not displayed");
    }
}