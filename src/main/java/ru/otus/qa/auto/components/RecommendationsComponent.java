package ru.otus.qa.auto.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class RecommendationsComponent extends CoursesComponent<RecommendationsComponent> {
    @FindBy(xpath = "//div/*[text()='Рекомендации для вас']/following-sibling::div/div")
    private List<WebElement> recommendations;

    public RecommendationsComponent(WebDriver driver) {
        super(driver);

    }

    /**
     * Получение названий курсов по рекомендациям
     */
    public List<String> getTitleCoursesByRecommendations() {
        return getTitleCourses(recommendations);
    }
}
