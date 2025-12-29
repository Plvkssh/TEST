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
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void searchFieldShouldBeVisibleOnMainScreen() {
        boolean isSearchFieldDisplayed = wikipediaApp.isSearchContainerDisplayed();
        
        logTestStep("Поисковое поле отображается: " + isSearchFieldDisplayed);
        Assert.assertTrue(isSearchFieldDisplayed, 
                         "Поисковое поле должно быть видно на главном экране");
    }

    @Test(priority = 2)
    public void shouldOpenArticleWhenSearchingForAppium() throws InterruptedException {
        wikipediaApp.searchArticle("Appium");
        waitForArticleToLoad();

        String articleTitle = wikipediaApp.getArticleTitle();
        logTestStep("Заголовок статьи: '" + articleTitle + "'");

        validateArticleTitle(articleTitle, "Appium");
    }

    @Test(priority = 3)
    public void shouldReturnToMainScreenAfterOpeningArticle() throws InterruptedException {
        wikipediaApp.searchArticle("Selenium");
        waitForArticleToLoad();

        String articleTitle = wikipediaApp.getArticleTitle();
        logTestStep("Открыта статья: " + articleTitle);

        wikipediaApp.navigateBack();
        Thread.sleep(SHORT_WAIT_MS);

        boolean isMainScreenDisplayed = wikipediaApp.isSearchContainerDisplayed();
        Assert.assertTrue(isMainScreenDisplayed, 
                         "После возврата должен отображаться главный экран с поисковым полем");
    }

    /**
     * Проверяет, что заголовок статьи соответствует ожидаемому ключевому слову.
     * 
     * @param actualTitle фактический заголовок статьи
     * @param expectedKeyword ожидаемое ключевое слово в заголовке
     */
    private void validateArticleTitle(String actualTitle, String expectedKeyword) {
        Assert.assertNotNull(actualTitle, "Заголовок статьи не должен быть null");
        Assert.assertFalse(actualTitle.isEmpty(), "Заголовок статьи не должен быть пустым");
        
        String lowercaseTitle = actualTitle.toLowerCase();
        String lowercaseKeyword = expectedKeyword.toLowerCase();
        Assert.assertTrue(lowercaseTitle.contains(lowercaseKeyword),
                        String.format("Заголовок статьи должен содержать '%s'. Фактический: %s", 
                                     expectedKeyword, actualTitle));
    }

    /**
     * Выводит информационное сообщение о ходе выполнения теста.
     * 
     * @param message сообщение для логирования
     */
    private void logTestStep(String message) {
        System.out.println("[TEST LOG] " + message);
    }

    /**
     * Ожидает загрузки статьи.
     * Использует стандартное время ожидания загрузки статьи.
     */
    private void waitForArticleToLoad() throws InterruptedException {
        Thread.sleep(ARTICLE_LOAD_WAIT_MS);
    }
}
