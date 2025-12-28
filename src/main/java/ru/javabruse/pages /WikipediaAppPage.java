package ru.javabruse.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object для главной страницы Wikipedia Android приложения.
 * Инкапсулирует взаимодействие с элементами интерфейса.
 */
public class WikipediaAppPage {

    private static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration SHORT_DELAY_MS = Duration.ofMillis(1000);
    private static final Duration MEDIUM_DELAY_MS = Duration.ofMillis(2000);
    private static final Duration LONG_DELAY_MS = Duration.ofMillis(3000);

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // Локаторы элементов
    private final By searchContainer = By.id("org.wikipedia.alpha:id/search_container");
    private final By searchInputField = By.id("org.wikipedia.alpha:id/search_src_text");
    private final By firstSearchResult = By.id("org.wikipedia.alpha:id/page_list_item_title");
    private final By navigationButton = AppiumBy.accessibilityId("Navigate up");
    private final By skipButton = By.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button");
    private final By closePopupButton = By.id("org.wikipedia.alpha:id/closeButton");

    public WikipediaAppPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT);
    }

    /**
     * Пропускает onboarding экран если он отображается.
     */
    public void skipOnboarding() {
        waitAndClickIfPresent(skipButton, MEDIUM_DELAY_MS);
    }

    /**
     * Закрывает всплывающее окно если оно отображается.
     */
    public void closePopupIfPresent() {
        waitAndClickIfPresent(closePopupButton, SHORT_DELAY_MS);
    }

    private void waitAndClickIfPresent(By locator, Duration waitBeforeCheck) {
        try {
            Thread.sleep(waitBeforeCheck.toMillis());
            if (isElementPresent(locator)) {
                driver.findElement(locator).click();
                Thread.sleep(SHORT_DELAY_MS.toMillis());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверяет отображение контейнера поиска на главной странице.
     *
     * @return true если элемент отображается
     */
    public boolean isSearchContainerDisplayed() {
        try {
            skipOnboarding();
            Thread.sleep(MEDIUM_DELAY_MS.toMillis());
            return driver.findElement(searchContainer).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Выполняет поиск статьи и открывает первый результат.
     *
     * @param searchQuery текст для поиска
     */
    public void searchArticle(String searchQuery) {
        try {
            skipOnboarding();
            Thread.sleep(MEDIUM_DELAY_MS.toMillis());

            driver.findElement(searchContainer).click();
            Thread.sleep(SHORT_DELAY_MS.toMillis());

            WebElement searchInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(searchInputField));
            searchInput.sendKeys(searchQuery);

            Thread.sleep(LONG_DELAY_MS.toMillis());
            driver.findElement(firstSearchResult).click();

            Thread.sleep(MEDIUM_DELAY_MS.toMillis());
            closePopupIfPresent();

        } catch (Exception e) {
            System.err.println("Search error: " + e.getMessage());
        }
    }

    /**
     * Получает заголовок текущей статьи.
     *
     * @return заголовок статьи или пустую строку если не удалось получить
     */
    public String getArticleTitle() {
        try {
            Thread.sleep(LONG_DELAY_MS.toMillis());
            closePopupIfPresent();

            // Первый способ получения заголовка
            WebElement titleElement = driver.findElement(
                    By.xpath("//android.widget.TextView[1]"));
            return titleElement.getText();

        } catch (Exception e) {
            System.err.println("Failed to get article title, trying alternative locator...");

            // Альтернативный способ через UiSelector
            try {
                WebElement titleElement = driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiSelector().className(\"android.widget.TextView\").instance(0)"));
                return titleElement.getText();
            } catch (Exception ex) {
                return "";
            }
        }
    }

    /**
     * Возвращается на предыдущий экран.
     */
    public void navigateBack() {
        try {
            Thread.sleep(SHORT_DELAY_MS.toMillis());
            driver.findElement(navigationButton).click();
            Thread.sleep(MEDIUM_DELAY_MS.toMillis());
            closePopupIfPresent();
        } catch (Exception e) {
            // Fallback на стандартную навигацию
            driver.navigate().back();
        }
    }
}
