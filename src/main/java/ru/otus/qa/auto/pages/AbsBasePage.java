package ru.otus.qa.auto.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.otus.qa.auto.annotations.UrlTemplate;

import java.util.List;

public abstract class AbsBasePage<T> extends AbsPageObject<T> {
    public static final String BASE_URL = System.getProperty("webdriver.base.url");

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

        return (T)this;
    }

    public void okForAgreement() {
        List<WebElement> elements = driver.findElements(By.xpath("//span[text()='Посещая наш сайт, вы принимаете']/following-sibling::div/button"));
        if (!elements.isEmpty()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", elements.get(0));
            actions.moveToElement(elements.get(0)).build().perform();
            elements.get(0).click();
        }
    }

}
