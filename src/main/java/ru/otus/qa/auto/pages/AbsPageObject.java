package ru.otus.qa.auto.pages;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import ru.otus.qa.auto.di.GuiceScoped;
import ru.otus.qa.auto.factory.FactoryWebDriver;
import ru.otus.qa.auto.waiters.BaseWaiter;

import java.util.List;

public abstract class AbsPageObject {
    protected WebDriver driver;
    protected Actions actions;
    protected BaseWaiter baseWaiter;

    public AbsPageObject(WebDriver driver) {
        this.driver = (driver == null) ? new FactoryWebDriver().getWebDriver() : driver;
        this.actions = new Actions(this.driver);
        PageFactory.initElements(this.driver, this);
        baseWaiter = new BaseWaiter(this.driver);
    }

    @Inject
    public AbsPageObject(GuiceScoped guiceScoped) {
        this(guiceScoped.driver);
        guiceScoped.driver = this.driver;
    }

    public void okForAgreement() {
        List<WebElement> elements = driver.findElements(By.xpath("//span[text()='Посещая наш сайт, вы принимаете']/following-sibling::div/button"));
        if (!elements.isEmpty()) {
            this.baseWaiter.waitForElementClickable(elements.get(0));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", elements.get(0));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elements.get(0));
            PageFactory.initElements(driver, this);
        }
    }
}
