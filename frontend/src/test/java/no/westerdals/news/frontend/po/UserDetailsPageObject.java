package no.westerdals.news.frontend.po;

import org.openqa.selenium.WebDriver;

public class UserDetailsPageObject extends BasePageObject {
    private String expectedUsername;

    public UserDetailsPageObject(WebDriver driver, String expectedUsername) {
        super(driver);
        this.expectedUsername = expectedUsername;
    }

    @Override
    public String getExpectedTitle() {
        return expectedUsername;
    }
}
