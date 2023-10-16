package ru.otus.qa.auto.components;

import org.openqa.selenium.WebDriver;
import ru.otus.qa.auto.pages.AbsPageObject;

public abstract class BaseComponent<T> extends AbsPageObject {
    public BaseComponent(WebDriver driver) {
        super(driver);
    }
}
