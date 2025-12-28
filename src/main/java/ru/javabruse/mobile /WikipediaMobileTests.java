package ru.javabruse.mobile;

import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.javabruse.pages.WikipediaAppPage;
import ru.javabruse.utils.WebDriverFactory;

/**
 * Тесты для мобильного приложения Wikipedia на Android.
 * Проверяют основные функции поиска и навигации.
 */
public class WikipediaMobileTests {

    private static final int INITIAL_WAIT_MS = 3000;
    private static final int SHORT_WAIT_MS = 2000;
    private static final int ARTICLE_LOAD_WAIT_MS = 3000;

    private AndroidDriver driver;
    private WikipediaAppPage wikipediaApp;

    @BeforeMethod
    public void setUp() throws Exception {
        driver = WebDriverFactory.createAndroidDriver();
        wikipediaApp = new WikipediaAppPage(driver);
        Thread.sleep(INITIAL_WAIT_MS);
    }

    @AfterMethod
    public void cleanUp() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void searchFieldShouldBeVisibleOnMainScreen() {
        boolean isSearchFieldDisplayed = wikipediaApp.isSearchContainerDisplayed();
        
        log("Search field visibility: " + isSearchFieldDisplayed);
        Assert.assertTrue(isSearchFieldDisplayed, 
                         "Search field should be visible on main screen");
    }

    @Test(priority = 2)
    public void shouldOpenArticleWhenSearchingForAppium() throws InterruptedException {
        wikipediaApp.searchArticle("Appium");
        Thread.sleep(ARTICLE_LOAD_WAIT_MS);

        String articleTitle = wikipediaApp.getArticleTitle();
        log("Article title: '" + articleTitle + "'");

        assertArticleTitleIsValid(articleTitle, "Appium");
    }

    @Test(priority = 3)
    public void shouldReturnToMainScreenAfterOpeningArticle() throws InterruptedException {
        wikipediaApp.searchArticle("Selenium");
        Thread.sleep(ARTICLE_LOAD_WAIT_MS);

        String articleTitle = wikipediaApp.getArticleTitle();
        log("Opened article: " + articleTitle);

        wikipediaApp.navigateBack();
        Thread.sleep(SHORT_WAIT_MS);

        boolean isMainScreenDisplayed = wikipediaApp.isSearchContainerDisplayed();
        Assert.assertTrue(isMainScreenDisplayed, 
                         "Main screen with search field should be visible after navigation back");
    }

    private void assertArticleTitleIsValid(String actualTitle, String expectedKeyword) {
        Assert.assertNotNull(actualTitle, "Article title should not be null");
        Assert.assertFalse(actualTitle.isEmpty(), "Article title should not be empty");
        
        String lowercaseTitle = actualTitle.toLowerCase();
        String lowercaseKeyword = expectedKeyword.toLowerCase();
        Assert.assertTrue(lowercaseTitle.contains(lowercaseKeyword),
                        String.format("Article title should contain '%s'. Actual: %s", 
                                     expectedKeyword, actualTitle));
    }

    private void log(String message) {
        System.out.println(message);
    }
}
