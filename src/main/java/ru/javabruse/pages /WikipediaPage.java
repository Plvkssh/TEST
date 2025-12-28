package ru.javabruse.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model для главной страницы Википедии.
 * Предоставляет методы для взаимодействия с основными элементами страницы.
 */
public class WikipediaPage {

    private static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(15);
    private static final String MAIN_PAGE_URL = "https://ru.wikipedia.org/wiki/Заглавная_страница";

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы элементов страницы
    private final By logo = By.id("p-logo");
    private final By searchInput = By.id("searchInput");
    private final By articleHeading = By.id("firstHeading");
    private final By randomPageLink = By.id("n-randompage");
    private final By bodyContent = By.id("bodyContent");

    public WikipediaPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT);
    }

    /**
     * Открывает главную страницу и проверяет отображение основного контента.
     *
     * @return true если логотип и основной контент отображаются корректно
     */
    public boolean isMainPageDisplayed() {
        driver.get(MAIN_PAGE_URL);
        
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(logo));
            WebElement content = wait.until(ExpectedConditions.visibilityOfElementLocated(bodyContent));
            return content.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Выполняет поиск по указанному запросу.
     *
     * @param searchQuery текст для поиска
     */
    public void search(String searchQuery) {
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        searchField.clear();
        searchField.sendKeys(searchQuery);
        searchField.submit();
    }

    /**
     * Получает заголовок текущей статьи.
     *
     * @return текст заголовка статьи
     */
    public String getArticleTitle() {
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(articleHeading));
        return heading.getText().trim();
    }

    /**
     * Открывает случайную страницу Википедии.
     */
    public void openRandomPage() {
        WebElement randomLink = wait.until(ExpectedConditions.elementToBeClickable(randomPageLink));
        randomLink.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(bodyContent));
    }

    /**
     * Проверяет доступность поля поиска.
     *
     * @return true если поле поиска отображается и активно для ввода
     */
    public boolean isSearchFieldAvailable() {
        WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        return searchField.isDisplayed() && searchField.isEnabled();
    }
}
