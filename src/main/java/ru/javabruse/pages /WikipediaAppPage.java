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

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration SHORT_DELAY = Duration.ofMillis(1000);
    private static final Duration MEDIUM_DELAY = Duration.ofMillis(2000);
    private static final Duration LONG_DELAY = Duration.ofMillis(3000);

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // Локаторы элементов
    private final By searchContainer = By.id("org.wikipedia.alpha:id/search_container");
    private final By searchInputField = By.id("org.wikipedia.alpha:id/search_src_text");
    private final By firstSearchResult = By.id("org.wikipedia.alpha:id/page_list_item_title");
    private final By navigationButton = AppiumBy.accessibilityId("Navigate up");
    private final By skipOnboardingButton = By.id("org.wikipedia.alpha:id/fragment_onboarding_skip_button");
    private final By closePopupButton = By.id("org.wikipedia.alpha:id/closeButton");
    private final By articleTitlePrimary = By.xpath("//android.widget.TextView[1]");

    public WikipediaAppPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    /**
     * Пропускает экран onboarding, если он отображается.
     */
    public void skipOnboarding() {
        clickIfPresent(skipOnboardingButton, MEDIUM_DELAY);
    }

    /**
     * Закрывает всплывающее окно, если оно отображается.
     */
    public void closePopupIfPresent() {
        clickIfPresent(closePopupButton, SHORT_DELAY);
    }

    private void clickIfPresent(By locator, Duration delayBeforeCheck) {
        try {
            Thread.sleep(delayBeforeCheck.toMillis());
            if (isElementPresent(locator)) {
                driver.findElement(locator).click();
                Thread.sleep(SHORT_DELAY.toMillis());
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
     * Проверяет, отображается ли контейнер поиска на главной странице.
     */
    public boolean isSearchContainerDisplayed() {
        try {
            skipOnboarding();
            Thread.sleep(MEDIUM_DELAY.toMillis());
            return driver.findElement(searchContainer).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Выполняет поиск статьи и открывает первый результат.
     */
    public void searchArticle(String searchQuery) {
        try {
            skipOnboarding();
            Thread.sleep(MEDIUM_DELAY.toMillis());

            driver.findElement(searchContainer).click();
            Thread.sleep(SHORT_DELAY.toMillis());

            WebElement searchInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(searchInputField));
            searchInput.sendKeys(searchQuery);

            Thread.sleep(LONG_DELAY.toMillis());
            driver.findElement(firstSearchResult).click();

            Thread.sleep(MEDIUM_DELAY.toMillis());
            closePopupIfPresent();

        } catch (Exception e) {
            System.err.println("Ошибка поиска: " + e.getMessage());
        }
    }

    /**
     * Получает заголовок текущей статьи.
     * 
     * @return заголовок статьи или пустую строку в случае ошибки
     */
    public String getArticleTitle() {
        try {
            Thread.sleep(LONG_DELAY.toMillis());
            closePopupIfPresent();

            WebElement titleElement = driver.findElement(articleTitlePrimary);
            return titleElement.getText();

        } catch (Exception primaryMethodException) {
            return tryAlternativeTitleLocator();
        }
    }

    private String tryAlternativeTitleLocator() {
        try {
            WebElement titleElement = driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiSelector().className(\"android.widget.TextView\").instance(0)"));
            return titleElement.getText();
        } catch (Exception alternativeMethodException) {
            return "";
        }
    }

    /**
     * Возвращается на предыдущий экран.
     */
    public void navigateBack() {
        try {
            Thread.sleep(SHORT_DELAY.toMillis());
            driver.findElement(navigationButton).click();
            Thread.sleep(MEDIUM_DELAY.toMillis());
            closePopupIfPresent();
        } catch (Exception e) {
            driver.navigate().back(); // Резервный способ навигации
        }
    }

    /**
     * Универсальный метод ожидания для использования в тестах.
     */
    public void waitFor(Duration duration) throws InterruptedException {
        Thread.sleep(duration.toMillis());
    }
}
