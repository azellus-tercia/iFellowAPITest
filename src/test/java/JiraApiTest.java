import hooks.ApiHooks;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static endpoints.EndPoints.*;
import static utils.Configuration.getConfigurationValue;

public final class JiraApiTest extends ApiHooks {
    @Test
    @DisplayName("Создание новой задачи и ее удаление через базовую авторизацию.")
    public void Test_CreateBugBasicAuth() {
        Response createTaskResponse = RestAssured.given()
                .spec(jiraRequest)
                .auth()
                .preemptive()
                .basic(getConfigurationValue("username"), getConfigurationValue("password"))
                .body(getJSONObject("createBug.json")
                        .put("description", "Here is some description.")
                        .toString())
                .post(JIRA_CREATE_ISSUE);

        String responseId = createTaskResponse
                .jsonPath()
                .get("id")
                .toString();

        RestAssured
                .given()
                .spec(jiraRequest)
                .auth()
                .preemptive()
                .basic(getConfigurationValue("username"), getConfigurationValue("password"))
                .delete(JIRA_CREATE_ISSUE + "/" + responseId);
    }

    @Test
    @DisplayName("Создание новой задачи и ее удаление через OAuth2.0 авторизацию.")
    public void Test_CreateBugOAuth2() {
        Response createTaskResponse = RestAssured.given()
                .spec(jiraRequest)
                .auth()
                .oauth2(getToken())
                .body(getJSONObject("createBug.json")
                        .put("description", "Here is some description.")
                        .toString())
                .post(JIRA_CREATE_ISSUE);

        String responseId = createTaskResponse
                .jsonPath()
                .get("id")
                .toString();

        RestAssured
                .given()
                .spec(jiraRequest)
                .auth()
                .oauth2(getToken())
                .delete(JIRA_CREATE_ISSUE + "/" + responseId);
    }

    @Test
    @DisplayName("Создание новой задачи и ее удаление через cookie авторизацию.")
    public void Test_CreateBugCookieAuth() {
        JSONObject requestCookies = new JSONObject();
        requestCookies.put("username", getConfigurationValue("username"));
        requestCookies.put("password", getConfigurationValue("password"));

        Response sessionCookies = RestAssured.given()
                .spec(jiraRequest)
                .body(requestCookies.toString())
                .post(JIRA_GET_SESSION_ID);

        String sessionId = sessionCookies.jsonPath().get("session.value");

        Response createTaskResponse = RestAssured.given()
                .spec(jiraRequest)
                .sessionId(sessionId)
                .body(getJSONObject("createBug.json")
                        .put("description", "Here is some description.")
                        .toString())
                .post(JIRA_CREATE_ISSUE);

        String responseId = createTaskResponse
                .jsonPath()
                .get("id")
                .toString();

        RestAssured
                .given()
                .spec(jiraRequest)
                .sessionId(sessionId)
                .delete(JIRA_CREATE_ISSUE + "/" + responseId);
    }

    @Test
    @DisplayName("Получить и вывести количество всех задач в Jira.")
    public void Test_GetAllTasks() {
        Response createTaskResponse = RestAssured.given()
                .spec(jiraRequest)
                .auth()
                .oauth2(getToken())
                .get(JIRA_GET_ALL_TASKS);

        System.out.println(createTaskResponse.jsonPath().get("total").toString());
    }
}
