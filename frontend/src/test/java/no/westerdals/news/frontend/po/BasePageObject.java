package no.westerdals.news.frontend.po;

import com.sun.org.apache.xpath.internal.operations.Bool;
import no.westerdals.news.frontend.ExpectedResult;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePageObject {
    WebDriver driver;
    private WebDriverWait wait;

    public BasePageObject(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void logout() {
        WebElement logoutButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logout-form:logout-button")));
        logoutButton.click();
        waitForPageToLoad();
    }

    public boolean isLoggedIn() {
        return !driver.findElements(By.id("logout-form:logout-button")).isEmpty();

    }

    public LoginPageObject navigateToLogin() {
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        waitForPageToLoad();
        return new LoginPageObject(driver);
    }

    public abstract String getExpectedTitle();

    public boolean isOnPage() {
        return waitForPageToLoad() && driver.getTitle().contains(getExpectedTitle());
    }

    public boolean waitForPageToLoad() {
        if (driver instanceof FirefoxDriver) {
            try { // For some reason firefox's click doesnt execute instantly so I added a small sleep before waiting for the docment to get ready.
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return wait.until((ExpectedCondition<Boolean>) input -> jsExecutor.executeScript("return document.readyState").equals("complete"));
    }

    public void waitForAjax() {
        // Ok I'm sorry. I really am, but atm its 03:46 and I didn't know ajax the ajax calls would fuck with running
        // a update so I added this workaround to wait for the ajax call to complete, this is far from a optimal
        // solution but I couldn't find a good way to wait for all ajax calls to be completed
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
