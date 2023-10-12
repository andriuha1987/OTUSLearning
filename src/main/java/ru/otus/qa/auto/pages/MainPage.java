package ru.otus.qa.auto.pages;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import ru.otus.qa.auto.components.FavoriteCoursesComponent;
import ru.otus.qa.auto.components.RecommendationsComponent;
import ru.otus.qa.auto.components.SpecializationsComponent;
import ru.otus.qa.auto.di.GuiceScoped;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MainPage extends AbsBasePage<MainPage> {

    @Inject
    public MainPage(GuiceScoped guiceScoped) {
        super(guiceScoped);
    }

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void searchCourseByTitle(String expectedCourseTitle) {
        List<String> allCourses = getAllCourses();
        Assertions.assertFalse(allCourses.isEmpty(), "Не найдена ни один курс на странице");
        log.info("Список всех курсов - {}", allCourses);
        Assertions.assertTrue(allCourses.contains(expectedCourseTitle), String.format("На странице не найден курс %s", expectedCourseTitle));
    }

    public List<String> getAllCourses() {
        List<String> allCourses = new ArrayList<>(new FavoriteCoursesComponent(driver).getTitleCourses());
        allCourses.addAll(new RecommendationsComponent(driver).getTitleCoursesByRecommendations());
        allCourses.addAll(new SpecializationsComponent(driver).getSpecializationCourses());
        return allCourses;
    }
}
