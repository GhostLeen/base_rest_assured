package com.denal;

import com.denal.models.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.denal.helpers.CustomApiListener.allureWithCustomTemplates;
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
                .filter(allureWithCustomTemplates())
                .spec(request)
                .body(user)
                .when()
                .post("/register")
                .then()
                .spec(response200)
                .body("token", is(notNullValue()));
    }

    @Test
    @DisplayName("Неуспешная регистрация неподдерживаемого пользователя")
    void unSuccessRegisterNonSupportedUserTest() {

        UserGenerator user = UserGenerator.builder()
                .email("diff.mail@reqres.in")
                .password("diff_pass")
                .build();

        given()
                .filter(allureWithCustomTemplates())
                .spec(request)
                .body(user)
                .when()
                .post("/register")
                .then()
                .log().body()
                .spec(response400)
                .body("error", is("Note: Only defined users succeed registration"));
    }

    @Test
    @DisplayName("Неуспешная регистрация. Не переданы значения")
    void unSuccessRegisterTest() {

        given()
                .filter(allureWithCustomTemplates())
                .spec(request)
                .when()
                .post("/register")
                .then()
                .spec(response400);
    }

    @Test
    @DisplayName("Получение пользователя по id")
    void getUserByIdTest() {

        UserData data = given()
                .filter(allureWithCustomTemplates())
                .spec(request)
                .when()
                .get("/users/9")
                .then()
                .log().status()
                .spec(response200)
                .extract().as(UserData.class);

        assertEquals(9, data.getData().getId());
        assertEquals("Tobias", data.getData().getFirstName());
        assertEquals("Funke", data.getData().getLastName());
    }

    @Test
    @DisplayName("Удаление пользователя по id")
    void deleteUserByIdTest() {

        given()
                .filter(allureWithCustomTemplates())
                .spec(request)
                .when()
                .delete("/users/4")
                .then()
                .spec(response204);
    }

    @Test
    @DisplayName("Успешное создание пользователя")
    void successCreateUserTest() {

        UserCreateModel userMade = new UserCreateModel();
                userMade.setName("Mark");
                userMade.setJob("developer");

        UserCreateModel user = given()
                .filter(allureWithCustomTemplates())
                .spec(request)
                .body(userMade)
                .when()
                .post("/users")
                .then()
                .log().status()
                .spec(response201)
                .extract().as(UserCreateModel.class);

        assertEquals("Mark", user.getName());
        assertEquals("developer", user.getJob());
    }

    @Test
    @DisplayName("Получение списка ресурсов и проверка с Groovy")
    void getResourseListWithGroovyCheckTest() {
        given()
                .filter(allureWithCustomTemplates())
                .spec(request)
                .when()
                .get("/unknown")
                .then()
                .spec(response200)
                .body("data.findAll{it.id == 2}.name", hasItem("fuchsia rose"))
                .body("data.findAll{it.id == 3}.year", hasItem(2002))
                .body("data.findAll{it.id == 5}.pantone_value", hasItem("17-1456"));
    }

    //Пример из урока с отключением форматирования кода при ctrl+alt+L
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