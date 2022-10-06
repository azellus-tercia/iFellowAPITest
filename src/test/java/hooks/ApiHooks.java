package hooks;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static utils.Configuration.getConfigurationValue;

public abstract class ApiHooks {
    public static RequestSpecification jiraRequest;
    public static RequestSpecification rickAndMorty;
    public static RequestSpecification reqRes;

    @BeforeAll
    static void setUp() {
         jiraRequest = new RequestSpecBuilder()
                .setBaseUri(getConfigurationValue("jiraUrl"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

         rickAndMorty = new RequestSpecBuilder()
                 .setBaseUri(getConfigurationValue("rickAndMortyUrl"))
                 .setContentType(ContentType.JSON)
                 .setAccept(ContentType.JSON)
                 .build();

         reqRes = new RequestSpecBuilder()
                 .setBaseUri(getConfigurationValue("reqResUrl"))
                 .setContentType(ContentType.JSON)
                 .setAccept(ContentType.JSON)
                 .build();
    }

    public JSONObject getJSONObject(String fileName) {
        try {
            return new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/requests/" + fileName))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getToken() {
        return getConfigurationValue("token");
    }
}
