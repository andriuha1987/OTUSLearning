package ru.otus.qa.auto.steps;

import com.google.inject.Inject;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Пусть;
import ru.otus.qa.auto.pages.MainPage;

public class MainPageSteps {
    @Inject
    private MainPage mainPage;

    @Пусть("Открыта главная страница")
    public void openPage() {
        mainPage.open();
    }

    @И("Ищется курс {string}")
    public void searchCourse(String title) {
        mainPage.searchCourseByTitle(title);
    }
}
