package com.example.steps;

import com.example.support.ApiClient;
import com.example.support.TestContext;
import com.example.support.TestDataBuilder;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import org.junit.Assert;

/**
 * Cucumber step definitions specific to the Greeting Update feature.
 *
 * <p>Provides the setup step that creates a test greeting and stores its generated
 * {@code id} in the {@link TestContext} so subsequent steps can reference it via
 * the <code>{existingGreetingId}</code> placeholder.
 */
public class GreetingUpdateSteps {

    private final TestContext context;
    private final ApiClient apiClient;

    public GreetingUpdateSteps(TestContext context) {
        this.context = context;
        this.apiClient = new ApiClient();
    }

    /**
     * Creates a greeting for the given name and stores the returned {@code id}
     * under the supplied context key so that subsequent steps can resolve
     * path placeholders like <code>{existingGreetingId}</code>.
     *
     * @param name       the greeting name
     * @param contextKey the key under which the new greeting's id is stored
     */
    @Given("a greeting exists with name {string} and id stored as {string}")
    public void aGreetingExistsWithNameAndIdStoredAs(String name, String contextKey) {
        String body = TestDataBuilder.defaultGreetingBody(name);
        Response response = apiClient.post("/api/greet", body);

        Assert.assertTrue(
                "Failed to create greeting for setup, status: " + response.statusCode()
                        + ", body: " + response.asString(),
                response.statusCode() == 200 || response.statusCode() == 201
        );

        String id = response.jsonPath().getString("id");
        Assert.assertNotNull("Created greeting must have an 'id' field", id);
        Assert.assertFalse("Created greeting 'id' must not be empty", id.trim().isEmpty());

        context.store(contextKey, id);
    }
}
