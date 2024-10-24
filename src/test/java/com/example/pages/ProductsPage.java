package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductsPage {
    WebDriver driver;
    WebDriverWait wait;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public WebElement getInventoryContainer() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inventory_container")));
    }

    public WebElement getProductsTitle() {
        return wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//span[@class='title' and text()='Products']")));
    }

    public WebElement getFirstProductAddButton() {
        return wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//button[contains(@id, 'add-to-cart-sauce-labs-backpack')]")));
    }

    public WebElement getSecondProductAddButton() {
        return wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//button[contains(@id, 'add-to-cart-sauce-labs-bike-light')]")));
    }

    public WebElement getFirstProductRemoveButton() {
        return wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//button[contains(@id, 'remove-sauce-labs-backpack')]")));
    }

    public WebElement getSecondProductRemoveButton() {
        return wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//button[contains(@id, 'remove-sauce-labs-bike-light')]")));
    }

    public WebElement getCartBadge() {
        return driver.findElement(By.className("shopping_cart_badge"));
    }

    public WebElement getCartButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link")));
    }

    public WebElement getFirstProductInCart() {
        return wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Sauce Labs Backpack']")));
    }

    public WebElement getSecondProductInCart() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Sauce Labs Bike Light']")));
    }

    public WebElement getProductInCart(String productName) {
        return wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='" + productName + "']")));
    }

    public WebElement getCheckoutButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout")));
    }

    public WebElement getContinueButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("continue")));
    }

    public WebElement getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@data-test='error']")));
    }

    public WebElement getFirstNameField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
    }

    public WebElement getLastNameField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name")));
    }

    public WebElement getPostalCodeField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code")));
    }

    public WebElement getSummaryInfo() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("summary_info")));
    }

    public WebElement getItemTotal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("summary_subtotal_label")));
    }

    public WebElement getTax() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("summary_tax_label")));
    }

    public WebElement getTotal() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("summary_total_label")));
    }

    public WebElement getFinishButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("finish")));
    }

    public WebElement getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")));
    }

    public WebElement getOrderConfirmationMessage() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Thank you for your order!']")));
    }

    public WebElement getOrderConfirmationImage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pony_express")));
    }

    public WebElement getMenuButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn")));
    }

    public WebElement getLogoutButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
    }

    public WebElement getProductAddButton(String productName) {
        String formattedProductName = formatProductName(productName);
        return wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//button[@data-test='add-to-cart-" + formattedProductName + "']")));
    }

    public WebElement getProductRemoveButton(String productName) {
        String formattedProductName = formatProductName(productName);
        return wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//button[@data-test='remove-" + formattedProductName + "']")));
    }

    private String formatProductName(String productName) {
        return productName.toLowerCase()
                .replaceAll("[^a-z0-9.()]+", "-")
                .replaceAll("-+", "-")
                .replaceAll("-$", "");
    }

    public WebElement getProductInSummary(String productName) {
        return wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='" + productName + "']")));
    }

    public double getProductPrice(String productName) {
        String priceText = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//div[contains(@class, 'inventory_item') and .//div[text()='"
                        + productName + "']]//div[@class='inventory_item_price']")))
                .getText();
        return Double.parseDouble(priceText.replace("$", ""));
    }

    public double getTotalPrice() {
        String totalText = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("summary_total_label"))).getText();
        return Double.parseDouble(totalText.replace("Total: $", ""));
    }
}