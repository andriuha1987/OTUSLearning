package ru.otus.qa.auto.factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.otus.qa.auto.exception.BrowserNotSupportedException;
import ru.otus.qa.auto.factory.impl.ChromeSettings;
import ru.otus.qa.auto.factory.impl.IBrowserSettings;

import java.time.Duration;
import java.util.Locale;

public class FactoryWebDriver {
    public final long implicitlyWaitSecond = Integer.parseInt(System.getProperty("webdriver.timeouts.implicitlywait", "5"));
    public final long pageLoadTimeout = Integer.parseInt(System.getProperty("webdriver.timeouts.pageloadtimeout", "30"));;

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
