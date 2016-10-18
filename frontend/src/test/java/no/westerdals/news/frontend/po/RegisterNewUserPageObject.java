package no.westerdals.news.frontend.po;

import no.westerdals.news.frontend.ExpectedResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RegisterNewUserPageObject extends BasePageObject {

    public RegisterNewUserPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getExpectedTitle() {
        return ExpectedResult.REGISTER_NEW_USER_TITLE;
    }

    public void enterUserDetails(String username, String password, String firstName, String middleName,
                                 String surname) {
        enterUserDetails(username, password, password, firstName, middleName, surname);
    }

    public void enterUserDetails(String username, String password, String confirmPassword, String firstName,
                                 String middleName, String surname) {
        WebElement usernameField = driver.findElement(By.id("register-user-form:username"));
        WebElement passwordField = driver.findElement(By.id("register-user-form:password"));
        WebElement confirmPasswordField = driver.findElement(By.id("register-user-form:confirm-password"));
        WebElement firstNameField = driver.findElement(By.id("register-user-form:first-name"));
        WebElement middleNameField = driver.findElement(By.id("register-user-form:middle-name"));
        WebElement surnameField = driver.findElement(By.id("register-user-form:surname"));

        // Make sure the inputs are clean
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        firstNameField.clear();
        middleNameField.clear();
        surnameField.clear();

        // Write input
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        confirmPasswordField.sendKeys(confirmPassword);
        firstNameField.sendKeys(firstName);
        middleNameField.sendKeys(middleName);
        surnameField.sendKeys(surname);
    }

    public void clickRegister() {
        WebElement registerButton = driver.findElement(By.id("register-user-form:register-button"));
        registerButton.click();
        waitForPageToLoad();
    }
}
