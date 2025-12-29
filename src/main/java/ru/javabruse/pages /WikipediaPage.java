package ru.javabruse.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object для главной страницы Википедии.
 * Инкапсулирует взаимодействие с основными элементами веб-интерфейса.
 */
public class WikipediaPage {

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);
    private static final String MAIN_PAGE_URL = "https://ru.wikipedia.org/wiki/Заглавная_страница";

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы элементов страницы
    private final By logo = By.id("p-logo");
    private final By searchInput = By.id("searchInput");
    private final By articleHeading = By.id("firstHeading");
    private final By randomPageLink = By.id("n-randompage");
    private final By bodyContent = By.id("bodyContent");
    private final By searchButton = By.id("searchButton");

    public WikipediaPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    /**
     * Открывает главную страницу Википедии.
     */
    public void open() {
        driver.get(MAIN_PAGE_URL);
    }

    /**
     * Проверяет, что главная страница загружена корректно.
     */
    public boolean isLoaded() {
        try {
            waitForLogo();
            WebElement content = waitForBodyContent();
            return content.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Выполняет поиск статьи по указанному запросу.
     */
    public void search(String searchQuery) {
        WebElement searchField = waitForSearchInput();
        searchField.clear();
        searchField.sendKeys(searchQuery);
        searchField.submit();
    }

    /**
     * Получает заголовок текущей отображаемой статьи.
     */
    public String getArticleTitle() {
        WebElement heading = waitForArticleHeading();
        return heading.getText().trim();
    }

    /**
     * Открывает случайную статью Википедии.
     */
    public void openRandomArticle() {
        WebElement randomLink = waitForClickableRandomLink();
        randomLink.click();
        waitForBodyContent();
    }

    /**
     * Проверяет доступность поиска на странице.
     */
    public boolean isSearchAvailable() {
        WebElement searchField = waitForSearchInput();
        return searchField.isDisplayed() && searchField.isEnabled();
    }

    // Приватные вспомогательные методы для работы с элементами

    private void waitForLogo() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(logo));
    }

    private WebElement waitForBodyContent() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(bodyContent));
    }

    private WebElement waitForSearchInput() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
    }

    private WebElement waitForArticleHeading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(articleHeading));
    }

    private WebElement waitForClickableRandomLink() {
        return wait.until(ExpectedConditions.elementToBeClickable(randomPageLink));
    }
}
