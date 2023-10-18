package ru.otus.qa.auto.api.services;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.otus.qa.auto.api.dto.Pet;

public class PetApi extends CommonApi {
    private final static String PET = "pet";

    public PetApi()  {
        requestSpecification = given()
                .baseUri(BASE_URI)
                .basePath(PET)
                .contentType(ContentType.JSON)
                .log().all();
    }

    public ValidatableResponse createPet(Pet pet) {
        return  given(requestSpecification)
                .body(pet)
                .when()
                .post()
                .then()
                .log().all();
    }

    public ValidatableResponse deletePet(Long id) {
        return  given(requestSpecification)
                .when()
                .delete("/" + id)
                .then()
                .log().all();
    }

    public ValidatableResponse createWrongPet() {
        return  given(requestSpecification)
                .body("{ WRONG }")
                .when()
                .post()
                .then()
                .log().all();
    }

    public Pet getPet(Long id) {
        return  given(requestSpecification)
                .when()
                .get("/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .body()
                .as(Pet.class);
    }
}
