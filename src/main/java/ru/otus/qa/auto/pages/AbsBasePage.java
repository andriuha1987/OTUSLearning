package ru.otus.qa.auto.pages;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import ru.otus.qa.auto.annotations.UrlTemplate;
import ru.otus.qa.auto.di.GuiceScoped;

import java.time.Duration;
import java.util.List;

public abstract class AbsBasePage<T> extends AbsPageObject {
    public static final String BASE_URL = System.getProperty("webdriver.base.url");
    public final int documentReadyStateTimeout = Integer.parseInt(System.getProperty("webdriver.timeouts.documentReadyState", "30"));

    @Inject
    public AbsBasePage(GuiceScoped guiceScoped) {
        super(guiceScoped);
    }

    public AbsBasePage(WebDriver driver) {
        super(driver);
    }

    private String getPath() {
        Class clazz = getClass();
        if (clazz.isAnnotationPresent(UrlTemplate.class)) {
            return ((UrlTemplate) clazz.getAnnotation(UrlTemplate.class)).value();
        }
        return "";
    }

    public T open() {
        String url = BASE_URL;
        driver.get(url + getPath());
        closeBanners();
        this.baseWaiter.waitForDocumentReadyState(Duration.ofSeconds(documentReadyStateTimeout));

        return (T)this;
    }
}
