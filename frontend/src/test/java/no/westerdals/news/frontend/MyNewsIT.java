package no.westerdals.news.frontend;

import no.westerdals.news.frontend.po.LoginPageObject;
import no.westerdals.news.frontend.po.NewsPageObject;
import no.westerdals.news.frontend.po.RegisterNewUserPageObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyNewsIT extends WebIntegrationTestBase {
    NewsPageObject newsPage;


    @Before
    public void setup() {
        assertTrue(isJBossRunning());
        this.newsPage = new NewsPageObject(driver);
        newsPage.navigateToNewspage();

        assertTrue(newsPage.isOnPage());

        if (newsPage.isLoggedIn()) {
            newsPage.logout();
        }
    }

    private void navigateAndCreateUser(String username) {
        LoginPageObject loginPage = newsPage.navigateToLogin();
        assertTrue(loginPage.isOnPage());
        RegisterNewUserPageObject registerNewUser = loginPage.navigateToRegisterNewUser();

        assertTrue(registerNewUser.isOnPage());

        registerNewUser.enterUserDetails(username, "password", "Firstname", null, "Surname");
        registerNewUser.clickRegister();

        assertTrue(newsPage.isOnPage());

        assertTrue(newsPage.canSeePostInput());
    }

    @Test
    public void testCreateNews() {
        navigateAndCreateUser("CreateNews");

        assertEquals(0, newsPage.getPostsByAuthor("CreateNews"));

        newsPage.writePostContent("Post1");
        newsPage.clickCreatePost();

        newsPage.writePostContent("Post2");
        newsPage.clickCreatePost();

        assertEquals(2, newsPage.getPostsByAuthor("CreateNews"));
    }

    @Test
    public void testNewsAfterLogout() {
        String username = "NewsAfterLogout";
        navigateAndCreateUser(username);

        String postContent1 = "Testing news after logout 1";
        String postContent2 = "Testing news after logout 2";

        newsPage.writePostContent(postContent1);
        newsPage.clickCreatePost();

        newsPage.writePostContent(postContent2);
        newsPage.clickCreatePost();

        newsPage.logout();

        assertFalse(newsPage.isLoggedIn());

        assertTrue(newsPage.getNumberOfArticleWithContent(postContent1) > 0);
        assertTrue(newsPage.getNumberOfArticleWithContent(postContent2) > 0);
    }
}
