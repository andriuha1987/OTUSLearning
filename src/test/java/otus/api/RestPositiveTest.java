package otus.api;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.qa.auto.api.dto.Category;
import ru.otus.qa.auto.api.dto.Pet;
import ru.otus.qa.auto.api.dto.Status;
import ru.otus.qa.auto.api.dto.Tag;
import ru.otus.qa.auto.api.services.PetApi;

import java.util.List;

public class RestPositiveTest {
    protected PetApi petApi;
    protected Long createdId;

    @BeforeEach
    public void init() {
        petApi = new PetApi();
    }

    @AfterEach
    public void clear() {
        if (createdId != null) {
            petApi.deletePet(createdId);
        }
    }

    @Test
    // Проверка корректного создания Pet. Проверяем статус ответа.
    // метод POST /pet
    public void addPet() {
        Pet pet = Pet.builder()
                .id(0L)
                .category(new Category(12L, "cat"))
                .name("Sima")
                .status(Status.SOLD)
                .photoUrls(List.of())
                .tags(List.of(new Tag(25L, "perfect")))
                .build();

        ValidatableResponse validatableResponse = petApi.createPet(pet)
                .statusCode(200);

        Pet responsePet = validatableResponse.extract().body().as(Pet.class);
        createdId = responsePet.getId();
        Assertions.assertNotEquals(0L, createdId, "В ответе id равен 0");
        Assertions.assertEquals(pet, responsePet, "Созданный экземпляр Pet не совпал с созданным на сервере");
    }

    @Test
    // Проверка создания Pet без указания каких-либо полей. Проверяем статус ответа и наличие сгенерированного ID в системе
    // метод POST /pet
    public void addEmptyPet() {
        ValidatableResponse validatableResponse = petApi
                .createPet(new Pet())
                .statusCode(200);

        Pet responsePet = validatableResponse.extract().body().as(Pet.class);
        createdId = responsePet.getId();
        Assertions.assertNotNull(createdId, "В ответе не был найден id");
    }

    @Test
    // Проверка поиска созданного Pet. Проверяем все поля с теми, что были указаны при создании
    // метод POST /pet + GET /pet/{petId}
    public void addAndCheckPet() {
        Pet pet = Pet.builder()
                .id(1234L)
                .category(new Category(12L, "dog"))
                .name("Muhtar")
                .status(Status.SOLD)
                .photoUrls(List.of())
                .tags(List.of(new Tag(25L, "good")))
                .build();

        createdId = petApi
                .createPet(pet)
                .statusCode(200)
                .extract()
                .body()
                .as(Pet.class)
                .getId();

        Pet createdPet = petApi.getPet(createdId);
        Assertions.assertEquals(pet, createdPet, "Созданный экземпляр Pet не совпал с созданным на сервере");
    }
}
