package ru.otus.qa.auto.di;

import io.cucumber.guice.ScenarioScoped;
import org.openqa.selenium.WebDriver;
import ru.otus.qa.auto.factory.FactoryWebDriver;

@ScenarioScoped
public class GuiceScoped {
    public WebDriver driver = new FactoryWebDriver().getWebDriver();
}
