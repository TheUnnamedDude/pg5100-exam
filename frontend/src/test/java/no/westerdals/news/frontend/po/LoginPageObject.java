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
        WebElement loginButton = driver.findElement(By.id("register-new-user-button"));
        loginButton.click();
        waitForPageToLoad();
        return new RegisterNewUserPageObject(driver);
    }
}
