package ru.otus.qa.auto.extensions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.*;
import ru.otus.qa.auto.annotations.Driver;
import ru.otus.qa.auto.factory.FactoryWebDriver;
import ru.otus.qa.auto.listeners.MouseListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UIExtension implements BeforeEachCallback, AfterEachCallback, AfterTestExecutionCallback {
    private WebDriver driver;

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        WebDriver initDriver = new FactoryWebDriver().getWebDriver();
        WebDriverListener listener = new MouseListener(initDriver);
        driver = new EventFiringDecorator<>(listener).decorate(initDriver);

        List<Field> fields = getAnnotatedFields(Driver.class, extensionContext);
        for (Field field : fields) {
            if (field.getType().getName().equals(WebDriver.class.getName())) {
                try {
                    field.setAccessible(true);
                    field.set(extensionContext.getTestInstance().get(), driver);
                } catch (IllegalAccessException e) {
                    throw new Error(String.format("Could not access or set webdriver in field: %s - is this field public?", field), e);
                }
            }
        }
    }

    private List<Field> getAnnotatedFields(Class<? extends Annotation> annotation, ExtensionContext extensionContext) {
        List<Field> list = new ArrayList<>();
        Class<?> testClass = extensionContext.getTestClass().get();
        while (testClass != null) {
            for (Field field : testClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotation)) {
                    list.add(field);
                }
            }
            testClass = testClass.getSuperclass();
        }
        return list;
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        if(driver != null) {
            driver.close();
            driver.quit();
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {

    }
}
