package ru.otus.qa.auto.factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.otus.qa.auto.exception.BrowserNotSupportedException;
import ru.otus.qa.auto.factory.impl.ChromeSettings;
import ru.otus.qa.auto.factory.impl.IBrowserSettings;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FactoryWebDriver {
    public final long implicitlyWaitSecond = Integer.parseInt(System.getProperty("webdriver.timeouts.implicitlywait", "5"));
    public final long pageLoadTimeout = Integer.parseInt(System.getProperty("webdriver.timeouts.pageloadtimeout", "30"));

    public WebDriver getWebDriver() {
        return getWebDriver(System.getProperty("browser", "chrome").toLowerCase(Locale.ROOT));
    }

    public WebDriver getWebDriver(String browserName) {
        WebDriver driver = getWebDriverWithoutSettings(browserName);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitlyWaitSecond));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        return driver;
    }

    public WebDriver getWebDriverWithoutSettings(String browserName) {
        if (Boolean.parseBoolean(System.getProperty("selenoid.enabled", "false"))) {
            return getRemoteWebDriver(browserName);
        } else {
            switch (browserName) {
                case "chrome":
                    IBrowserSettings chromeSettings = new ChromeSettings();
                    WebDriverManager.chromedriver().setup();
                    // На рабочем месте сетевые политики блочат ресурсы откуда WebDriverManager выкачивает драйвер
                    // Поэтому приходится указывать драйвер напрямую
                    // System.setProperty("webdriver.chrome.driver", "C:\\\\Program Files\\\\chromedriver-win64\\\\chromedriver.exe");

                    return new ChromeDriver((ChromeOptions) chromeSettings.getCapabilities());
                case "firefox":
                    return new FirefoxDriver();
                default:
                    throw new BrowserNotSupportedException(browserName);
            }
        }
    }

    public WebDriver getRemoteWebDriver(String browserName) {
        MutableCapabilities selenoidCapabilities = new DesiredCapabilities();

        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("browserName", browserName);
        selenoidOptions.put("browserVersion", System.getProperty("browser.version"));
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("enableVideo", false);
        selenoidCapabilities.setCapability("selenoid:options", selenoidOptions);

        if (browserName.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-fullscreen");
            selenoidCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
        }

        try {
            return new RemoteWebDriver(
                    URI.create(System.getProperty("selenoid.url", "http://127.0.0.1/wd/hub/")).toURL(),
                    selenoidCapabilities
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException("Ошибка получения URI для selenoid");
        }
    }
}
