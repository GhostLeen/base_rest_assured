package com.denal;

import com.denal.models.UserData;
import com.denal.models.UserGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.denal.spec.Specs.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresWithSpecAndModelTest {

    @Test
    @DisplayName("Успешная регистрация дефолтного пользователя")
    void successRegisterTest() {

        UserGenerator user = UserGenerator.builder()
                .email("eve.holt@reqres.in")
                .password("pistol")
                .build();

        given()
                .spec(request)
                .body(user)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .spec(response)
                .body("id", is(4));
    }

    @Test
    @DisplayName("Неуспешная регистрация неподдерживаемого пользователя")
    void unSuccessRegisterNonSupportedUserTest() {

        UserGenerator user = UserGenerator.builder()
                .email("diff.mail@reqres.in")
                .password("diff_pass")
                .build();

        given()
                .spec(request)
                .body(user)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .spec(response400)
                .body("error", is("Note: Only defined users succeed registration"));
    }

    @Test
    @DisplayName("Неуспешная регистрация. Не переданы значения")
    void unSuccessRegisterTest() {

        given()
                .spec(request)
                .when()
                .post("/register")
                .then()
                .log().status()
                .spec(response400);
    }

    @Test
    @DisplayName("Получение пользователя по id")
    void getUserByIdTest() {

        UserData data = given()
                .spec(request)
                .when()
                .get("/users/9")
                .then()
                .spec(response)
                .log().body()
                .extract().as(UserData.class);

        assertEquals(9, data.getData().getId());
        assertEquals("Tobias", data.getData().getFirstName());
        assertEquals("Funke", data.getData().getLastName());
    }

    @Test
    @DisplayName("Получение списка ресурсов и проверка с Groovy")
    void getResourseListWithGroovyCheckTest() {
        given()
                .spec(request)
                .when()
                .get("/unknown")
                .then()
                .spec(response)
                .log().body()
                .body("data.findAll{it.id == 2}.name", hasItem("fuchsia rose"))
                .body("data.findAll{it.id == 3}.year", hasItem(2002))
                .body("data.findAll{it.id == 5}.pantone_value", hasItem("17-1456"));
    }

    //Пример из урока с отключением форматирования кода при ctrl+alt+L
    @Test
    public void checkEmailUsingGroovy() {
        // @formatter:off
        given()
                .spec(request)
                .when()
                .get("/users")
                .then()
                .log().body()
                .body("data.findAll{it.email =~/.*?@reqres.in/}.email.flatten()",
                        hasItem("eve.holt@reqres.in"));
        // @formatter:on
    }

}