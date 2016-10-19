package no.westerdals.news.frontend.po;

import no.westerdals.news.frontend.ExpectedResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPageObject extends BasePageObject {

    public LoginPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getExpectedTitle() {
        return ExpectedResult.LOGIN_PAGE_TITLE;
    }

    public RegisterNewUserPageObject navigateToRegisterNewUser() {
        WebElement registerNewUserButton = driver.findElement(By.id("register-new-user-button"));
        registerNewUserButton.click();
        waitForPageToLoad();
        return new RegisterNewUserPageObject(driver);
    }

    public void enterCredentials(String username, String password) {
        WebElement usernameField = driver.findElement(By.id("login-form:username"));
        WebElement passwordField = driver.findElement(By.id("login-form:password"));

        usernameField.clear();
        passwordField.clear();

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(By.id("login-form:login-button")).click();
        waitForPageToLoad();
    }
}
