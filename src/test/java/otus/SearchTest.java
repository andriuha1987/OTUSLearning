package otus;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import ru.otus.qa.auto.annotations.Driver;
import ru.otus.qa.auto.components.FavoriteCoursesComponent;
import ru.otus.qa.auto.components.SpecializationsComponent;
import ru.otus.qa.auto.extensions.UIExtension;
import ru.otus.qa.auto.pages.MainPage;

@ExtendWith(UIExtension.class)
// Напоминалка себе от преподавателя о том, как реализовывать в разных либах
// @ExtendWith - Junit5
// @RunWith - Junit4
// Наследование от базового класса и там вся логика JavaReflection - TestNG
public class SearchTest {

    @Driver
    private WebDriver driver;

    @Test
    //Д/З №1 поиск курса среди всех на странице
    public void searchOtusCourseByTitle() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.searchCourseByTitle("Специализация Android");
    }

    @Test
    //Д/З №1 поиск самого позднего курса среди специализаций
    public void searchLastOtusSpecializationByDate() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        new SpecializationsComponent(driver).chooseLastCourseByDate();
    }

    @Test
    //Д/З №1 поиск самого позднего курса среди специализаций
    public void searchFirstOtusSpecializationByDate() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        new SpecializationsComponent(driver).chooseFirstCourseByDate();
    }
}
