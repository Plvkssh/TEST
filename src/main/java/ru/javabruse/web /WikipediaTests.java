package ru.javabruse.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.javabruse.pages.WikipediaPage;
import ru.javabruse.utils.WebDriverFactory;

/**
 * Тесты для веб-версии Википедии.
 * Проверяют основные функции главной страницы и поиска.
 */
public class WikipediaTests {

    private static final String WIKIPEDIA_BASE_URL = "https://ru.wikipedia.org/";
    private static final String RUSSIA_SEARCH_QUERY = "Россия";
    private static final String RUSSIA_ARTICLE_TITLE = "Россия";

    private WebDriver driver;
    private WikipediaPage wikipediaPage;

    @BeforeMethod
    public void setUp() {
        initializeWebDriver();
        openWikipediaHomePage();
        wikipediaPage = new WikipediaPage(driver);
    }

    private void initializeWebDriver() {
        WebDriverManager.chromedriver().setup();
        driver = WebDriverFactory.createChromeDriver();
        driver.manage().window().maximize();
    }

    private void openWikipediaHomePage() {
        driver.get(WIKIPEDIA_BASE_URL);
    }

    @Test
    public void mainPageShouldLoadSuccessfully() {
        boolean isMainPageLoaded = wikipediaPage.isMainPageDisplayed();
        
        Assert.assertTrue(isMainPageLoaded, 
                         "Главная страница Википедии должна загружаться корректно");
    }

    @Test
    public void shouldFindArticleWhenSearchingForRussia() {
        wikipediaPage.search(RUSSIA_SEARCH_QUERY);
        
        String actualTitle = wikipediaPage.getArticleTitle();
        
        Assert.assertEquals(actualTitle, RUSSIA_ARTICLE_TITLE,
                          String.format("Заголовок статьи должен быть '%s'. Фактический: %s", 
                                       RUSSIA_ARTICLE_TITLE, actualTitle));
    }

    @Test
    public void shouldNavigateToDifferentPageWhenClickingRandomLink() {
        wikipediaPage.isMainPageDisplayed();
        String initialUrl = driver.getCurrentUrl();

        wikipediaPage.openRandomPage();
        String newUrl = driver.getCurrentUrl();

        Assert.assertNotEquals(newUrl, initialUrl,
                             "URL должен измениться после открытия случайной статьи");
    }

    @Test
    public void searchFieldShouldBeAvailableOnMainPage() {
        wikipediaPage.isMainPageDisplayed();
        
        boolean isSearchFieldFunctional = wikipediaPage.isSearchFieldAvailable();
        
        Assert.assertTrue(isSearchFieldFunctional,
                         "Поле поиска должно быть доступно для использования");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
