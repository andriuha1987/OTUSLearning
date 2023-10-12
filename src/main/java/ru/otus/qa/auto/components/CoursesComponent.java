package ru.otus.qa.auto.components;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.otus.qa.auto.data.CourseEntry;
import ru.otus.qa.auto.data.CourseSourceData;
import ru.otus.qa.auto.di.GuiceScoped;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class CoursesComponent<T> extends BaseComponent<CoursesComponent> {
    public CoursesComponent(WebDriver driver) {
        super(driver);
    }

    @Inject
    public CoursesComponent(GuiceScoped guiceScoped) {
        super(guiceScoped);
    }

    /**
     * Получение названий курсов
     */
    public List<String> getTitleCourses(List<WebElement> coursesElements) {
        return coursesElements.stream()
                .map(webElement -> webElement.findElement(By.xpath(".//img")).getAttribute("alt"))
                .collect(Collectors.toList());
    }
}
