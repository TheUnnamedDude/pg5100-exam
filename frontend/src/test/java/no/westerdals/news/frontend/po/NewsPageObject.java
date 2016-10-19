package no.westerdals.news.frontend.po;


import no.westerdals.news.frontend.ExpectedResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

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
                .findElements(By.tagName("input"))
                .stream()
                .filter(element -> element.getAttribute("value").equals("1"))
                .findAny()
                .orElse(null)
                .click();
        waitForAjax();
    }

    public void clickDownvoteForPostWithContent(String expectedContent) {
        getArticlesWithContent(expectedContent)
                .findAny()
                .orElse(null)
                .findElement(By.className("vote-selector"))
                .findElements(By.tagName("input"))
                .stream()
                .filter(element -> element.getAttribute("value").equals("-1"))
                .findAny()
                .orElse(null)
                .click();
        waitForAjax();
    }

    public void clickResetVoteForPostWithContent(String expectedContent) {
        getArticlesWithContent(expectedContent)
                .findAny()
                .orElse(null)
                .findElement(By.className("vote-selector"))
                .findElements(By.tagName("input"))
                .stream()
                .filter(element -> element.getAttribute("value").equals("0"))
                .findAny()
                .orElse(null)
                .click();
        waitForAjax();
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

    public void setOrdering(boolean byDate) {
        Select select = new Select(driver.findElement(By.id("sorting-form:sorting")));
        select.selectByValue(Boolean.toString(byDate));
    }

    public void clickUpdateOrdering() {
        driver.findElement(By.id("sorting-form:update-sorting")).click();
        waitForPageToLoad();
    }

    public int getUpvotesForFirstPostEntry() {
        return Integer.parseInt(driver.findElements(By.className("post"))
                .get(0)
                .findElement(By.className("score"))
                .getText());
    }
}
