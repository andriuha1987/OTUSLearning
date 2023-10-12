package ru.otus.qa.auto.steps;

import com.google.inject.Inject;
import io.cucumber.java.ru.И;
import ru.otus.qa.auto.components.SpecializationsComponent;
import ru.otus.qa.auto.pages.MainPage;

public class SpecializationsComponentSteps {
    @Inject
    private SpecializationsComponent specializationsComponent;

    @И("Выбирается самая ранняя по дате специализация")
    public void searchFirstCourse() {
        specializationsComponent.chooseFirstCourseByDate();
    }

    @И("Выбирается самая поздняя по дате специализация")
    public void searchLastCourse() {
        specializationsComponent.chooseLastCourseByDate();
    }

}
