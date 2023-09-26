package ru.otus.qa.auto.listeners;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

@Slf4j
public class MouseListener implements WebDriverListener {

    protected WebDriver driver;
    private String originalStyle;

    public MouseListener(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void beforeClick(WebElement element) {
        saveElementBorderStyle(element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", element);
    }

    @Override
    public void afterClick(WebElement element) {
        try {
            originalStyle = (originalStyle == null) ? "" : originalStyle;
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='" + originalStyle + "'", element);
        } catch (Exception e) {
            log.debug("Something went wrong with element highlight", e);
        }
    }

    private void saveElementBorderStyle(WebElement webElement) {
        originalStyle = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].style.border", webElement);
    }
}
