import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresTests {

    //Домашнее задание. Разработка автотестов на запросы из https://reqres.in/

    @Test
    @Disabled
    @DisplayName("Успешная регистрация дефолтного пользователя")
    void successRegisterTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @Disabled
    @DisplayName("Неуспешная регистрация неподдерживаемого пользователя")
    void unSuccessRegisterNonSupportedUserTest() {
        String data = "{ \"email\": \"diff.mail@reqres.in\", \"password\": \"diff_pass\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }

    @Test
    @Disabled
    @DisplayName("Неуспешная регистрация. Неподдерживаемый формат сообщения без логина и пароля")
    void unSuccessRegisterWithoutDataTest() {

        given()
                .log().uri()
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .statusCode(415);
    }

    @Test
    @Disabled
    @DisplayName("Успешное создание пользователя")
    void successCreateUserTest() {
        String data = "{ \"name\": \"Mark\", \"job\": \"developer\"}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Mark"))
                .body("job", is("developer"));
    }

    @Test
    @Disabled
    @DisplayName("Получение пользователя по id")
    void getUserByIdTest() {

        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users/9")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.first_name", is("Tobias"))
                .body("data.last_name", is("Funke"));
    }

    @Test
    @Disabled
    @DisplayName("Обновление существующего пользователя")
    void updateUserByIdTest() {
        String data = "{ \"name\": \"Tony\"}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .patch("https://reqres.in/api/users/6")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("Tony"));
    }

    @Test
    @Disabled
    @DisplayName("Удаление пользователя по id")
    void deleteUserByIdTest() {
        Integer expectedStatusCode = 204;

        Integer actualStatusCode = given()
                .log().uri()
                .when()
                .delete("https://reqres.in/api/users/4")
                .then()
                .log().status()
                .extract().statusCode();

        assertEquals(expectedStatusCode, actualStatusCode);
    }
}


