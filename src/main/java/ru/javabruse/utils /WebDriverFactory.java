package ru.javabruse.utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.time.Duration;

/**
 * Фабрика для создания экземпляров WebDriver с различными конфигурациями.
 * Предоставляет готовые настройки для Chrome и Android драйверов.
 */
public class WebDriverFactory {
    
    private static final Duration DEFAULT_IMPLICIT_WAIT = Duration.ofSeconds(5);
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";
    private static final String WIKIPEDIA_APP_PACKAGE = "org.wikipedia.alpha";
    private static final String WIKIPEDIA_MAIN_ACTIVITY = "org.wikipedia.main.MainActivity";

    /**
     * Создает и настраивает ChromeDriver для веб-тестирования.
     * 
     * @return настроенный экземпляр ChromeDriver
     */
    public static WebDriver createChromeDriver() {
        ChromeOptions options = configureChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        configureDriverTimeouts(driver);
        return driver;
    }

    private static ChromeOptions configureChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        return options;
    }

    private static void configureDriverTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_WAIT);
    }

    /**
     * Создает и настраивает AndroidDriver для мобильного тестирования Wikipedia приложения.
     * 
     * @return настроенный экземпляр AndroidDriver
     * @throws Exception если не удается подключиться к Appium серверу
     */
    public static AndroidDriver createAndroidDriver() throws Exception {
        DesiredCapabilities capabilities = configureAndroidCapabilities();
        return new AndroidDriver(new URL(APPIUM_SERVER_URL), capabilities);
    }

    private static DesiredCapabilities configureAndroidCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("appPackage", WIKIPEDIA_APP_PACKAGE);
        capabilities.setCapability("appActivity", WIKIPEDIA_MAIN_ACTIVITY);
        capabilities.setCapability("noReset", false);
        
        return capabilities;
    }
}
