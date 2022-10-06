import hooks.ApiHooks;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static endpoints.EndPoints.*;

public final class WebApiTest extends ApiHooks {
    @Test
    @DisplayName("Задание 1. Работа с API по Rick and Morty.")
    public void Test_RickAndMorty() {
        Response getMortySmith = RestAssured.given()
                .spec(rickAndMorty)
                .queryParam("name", "morty smith")
                .get(RICK_AND_MORTY_CHARACTERS);

        String mortySpecies = getMortySmith.jsonPath().get("results[0].species");
        String mortyLocation = getMortySmith.jsonPath().get("results[0].location.name");
        String lastEpisode = getMortySmith.jsonPath().get("results[0].episode[-1]");

        String lastCharacterUrl = RestAssured.given()
                .spec(rickAndMorty)
                .get(lastEpisode)
                .jsonPath()
                .get("characters[-1]");

        Response lastCharacter = RestAssured.given()
                .spec(rickAndMorty)
                .get(lastCharacterUrl);

        String characterSpecies = lastCharacter.jsonPath().get("species");
        String characterLocation = lastCharacter.jsonPath().get("location.name");

        Assertions.assertEquals(mortySpecies, characterSpecies, "Эти персонажи имеют разную расу.");
        Assertions.assertNotEquals(mortyLocation, characterLocation, "Эти персонажи находятся в одном месте.");
    }

    @Test
    @DisplayName("Задание 2. Работа с API по reqres.in.")
    public void Test_ReqRes() {
        Response createUser = RestAssured.given()
                .spec(reqRes)
                .body(getJSONObject("createUser.json").put("name", "Tomato").put("job", "Eat market").toString())
                .post(REQRES_CREATE_USER);

        Assertions.assertEquals(201, createUser.getStatusCode());
        Assertions.assertTrue(createUser.asString().contains("name"), "Отсутствует поле 'name'.");
        Assertions.assertTrue(createUser.asString().contains("job"), "Отсутствует поле 'job'.");
        Assertions.assertTrue(createUser.asString().contains("id"), "Отсутствует поле 'id'.");
        Assertions.assertTrue(createUser.asString().contains("createdAt"), "Отсутствует поле 'createdAt'.");
        Assertions.assertEquals("Tomato", createUser.jsonPath().get("name"), "Поле не равно 'Tomato'.");
        Assertions.assertEquals("Eat market", createUser.jsonPath().get("job"), "Поле не равно 'Eat market'.");
        Assertions.assertNotEquals("", createUser.jsonPath().get("id"), "Поле не заполнено.");
        Assertions.assertNotEquals("", createUser.jsonPath().get("createdAt"), "Поле не заполнено.");
    }
}
