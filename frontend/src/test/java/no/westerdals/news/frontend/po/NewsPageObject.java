package no.westerdals.news.frontend.po;


import no.westerdals.news.frontend.ExpectedResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class NewsPageObject extends BasePageObject {

    public NewsPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public String getExpectedTitle() {
        return ExpectedResult.NEWS_PAGE_TITLE;
    }

    public NewsPageObject navigateToNewspage() {
        driver.get("http://localhost:8080/news-application/news/");
        return this;
    }

    public boolean canSeePostInput() {
        return !driver.findElements(By.id("new-post:content")).isEmpty();
    }

    public void writePostContent(String content) {
        WebElement webElement = driver.findElement(By.id("new-post:content"));

        webElement.clear();
        webElement.sendKeys(content);
    }

    public void clickCreatePost() {
        WebElement createButton = driver.findElement(By.id("new-post:create"));
        createButton.click();
        waitForPageToLoad();
    }

    public int getNumberOfPosts() {
        return driver.findElements(By.className("post")).size();
    }

    public long getPostsByAuthor(String author) {
        return driver.findElements(By.className("post"))
                .stream()
                .map(e -> e.findElement(By.className("author-value")))
                .filter(postAuthor -> postAuthor.getText().equals(author))
                .count();
    }

    public long getNumberOfArticleWithContent(String expectedContent) {
        return driver.findElements(By.className("post"))
                .stream()
                .map(e -> e.findElement(By.className("content-value")))
                .filter(postContent -> postContent.getText().equals(expectedContent))
                .count();
    }
}
