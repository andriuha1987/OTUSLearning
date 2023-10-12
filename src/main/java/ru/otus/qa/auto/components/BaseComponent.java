package ru.otus.qa.auto.components;

import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import ru.otus.qa.auto.di.GuiceScoped;
import ru.otus.qa.auto.pages.AbsPageObject;

public abstract class BaseComponent<T> extends AbsPageObject {
    public BaseComponent(WebDriver driver) {
        super(driver);
    }

    @Inject
    public BaseComponent(GuiceScoped guiceScoped) {
        super(guiceScoped);
    }
}
