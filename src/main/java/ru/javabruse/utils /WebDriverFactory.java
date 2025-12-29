package ru.javabruse.utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.time.Duration;

/**
 * Фабрика для создания и настройки драйверов WebDriver.
 * Поддерживает создание ChromeDriver для веб-тестов и AndroidDriver для мобильных тестов.
 */
public class WebDriverFactory {
    
    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(5);
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";
    private static final String WIKIPEDIA_PACKAGE = "org.wikipedia.alpha";
    private static final String WIKIPEDIA_ACTIVITY = "org.wikipedia.main.MainActivity";
    
    private static final String PLATFORM_ANDROID = "Android";
    private static final String AUTOMATION_UIAUTOMATOR2 = "UiAutomator2";
    private static final String EMULATOR_NAME = "Android Emulator";

    private WebDriverFactory() {
        // Приватный конструктор для утилитного класса
    }

    /**
     * Создает и настраивает ChromeDriver для веб-тестирования.
     */
    public static WebDriver createChromeDriver() {
        ChromeOptions options = createChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        configureTimeouts(driver);
        return driver;
    }

    /**
     * Создает и настраивает AndroidDriver для тестирования мобильного приложения.
     */
    public static AndroidDriver createAndroidDriver() throws Exception {
        DesiredCapabilities capabilities = createAndroidCapabilities();
        return new AndroidDriver(new URL(APPIUM_SERVER_URL), capabilities);
    }

    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        return options;
    }

    private static DesiredCapabilities createAndroidCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        capabilities.setCapability("platformName", PLATFORM_ANDROID);
        capabilities.setCapability("deviceName", EMULATOR_NAME);
        capabilities.setCapability("automationName", AUTOMATION_UIAUTOMATOR2);
        capabilities.setCapability("appPackage", WIKIPEDIA_PACKAGE);
        capabilities.setCapability("appActivity", WIKIPEDIA_ACTIVITY);
        capabilities.setCapability("noReset", false);
        
        return capabilities;
    }

    private static void configureTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
    }
}
