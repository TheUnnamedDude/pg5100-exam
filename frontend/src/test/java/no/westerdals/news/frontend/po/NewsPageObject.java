package no.westerdals.news.frontend.po;


import no.westerdals.news.frontend.ExpectedResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Stream;

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

    private Stream<WebElement> getArticlesWithContent(String expectedContent) {
        return driver.findElements(By.className("post"))
                .stream()
                .filter(post -> post
                        .findElement(By.className("content-value"))
                        .getText().equals(expectedContent));
    }

    public long getNumberOfArticleWithContent(String expectedContent) {
        return getArticlesWithContent(expectedContent)
                .count();
    }

    public UserDetailsPageObject clickAuthorForPostWithContent(String expectedContent, String expectedUsername) {
        getArticlesWithContent(expectedContent)
                .findAny()
                .orElse(null) // We'll get a NPE and a failed test if it cant find the element, which is fine
                .findElement(By.className("author"))
                .click();
        waitForPageToLoad();
        return new UserDetailsPageObject(driver, expectedUsername);
    }

    public void clickUpvoteForPostWithContent(String expectedContent) {
        getArticlesWithContent(expectedContent)
                .findAny()
                .orElse(null)
                .findElement(By.className("vote-selector"))
                .findElements(By.tagName("label"))
                .stream()
                .filter(element -> element.getText().equals("Upvote"))
                .findAny()
                .orElse(null)
                .click();
        waitForPageToLoad();
    }

    public int getUpvotesForPostWithContent(String expectedContent) {
        return Integer.parseInt(getArticlesWithContent(expectedContent)
                .findAny()
                .orElse(null)
                .findElement(By.className("score"))
                .getText());
    }

    public boolean canUpvotePosts() {
        return !driver.findElements(By.className("vote-selector")).isEmpty();
    }
}
