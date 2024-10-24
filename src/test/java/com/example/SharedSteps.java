package com.example;

import com.example.pages.LoginPage;
import com.example.pages.ProductsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SharedSteps {
    static WebDriver driver = CartOperationsStepDefinitions.driver;
    static Properties credentials = new Properties();
    static LoginPage loginPage = new LoginPage(driver);
    static ProductsPage productsPage = new ProductsPage(driver);

    public static void loadCredentials() {
        try (InputStream input = SharedSteps.class.getClassLoader().getResourceAsStream("credentials.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find credentials.properties");
                return;
            }
            credentials.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @When("I login with {string}")
    public static void loginWithUser(String username) {
        String password = credentials.getProperty(username);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
    }

    @Then("I should see the products page")
    public void i_should_see_the_products_page() {
        Assert.assertTrue(productsPage.getInventoryContainer().isDisplayed(), "Products page is not displayed");
        Assert.assertTrue(productsPage.getProductsTitle().isDisplayed(), "Products title is not displayed");
    }

    @When("I add two products to the cart")
    public void i_add_two_products_to_the_cart() {
        productsPage.getFirstProductAddButton().click();
        productsPage.getSecondProductAddButton().click();
    }

    @Then("they should be successfully added to the cart")
    public void they_should_be_successfully_added_to_the_cart() {
        String cartCount = productsPage.getCartBadge().getText();
        Assert.assertEquals(cartCount, "2", "Two products were not successfully added to the cart");
    }
}