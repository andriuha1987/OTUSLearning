package otus.api;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.qa.auto.api.services.PetApi;

public class RestNegativeTest {
    protected PetApi petApi;

    @BeforeEach
    public void init() {
        petApi = new PetApi();
    }

    @Test
    // Проверка ошибки при некорректной отправке JSON. проверяем код статуса 400 и содержимое ошибки
    // метод POST /pet
    public void addWrongPet() {
        ValidatableResponse validatableResponse = petApi.createWrongPet();

        validatableResponse
                .statusCode(400)
                .body("code", equalTo(400))
                .body("type", equalTo("unknown"))
                .body("message", equalTo("bad input"));
    }
}
