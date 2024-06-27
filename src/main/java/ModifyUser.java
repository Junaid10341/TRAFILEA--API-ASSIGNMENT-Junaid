import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.List;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModifyUser {

    @Test
    public void modifyUser() {
        // Data: Set the base URI for RestAssured
        RestAssured.baseURI = "https://gorest.co.in";

        // Given: Set up the request specification to fetch all users
        // When: Send the request to get the list of users
        Response usersResponse = given()
                .basePath("/public/v1/users")
                .when()  // When: Execute the request
                .get();

        // Then: Extract the list of users from the response
        List<Map<String, Object>> users = usersResponse.jsonPath().getList("data");

        // Data: Fetch the first user from the list
        Map<String, Object> firstUser = users.get(0);
        int userId = ((Number) firstUser.get("id")).intValue();

        // Data: New name and email for the user
        String newName = "John Doe";
        String newEmail = "jana.waters11@hotmail.us"; // Make sure this email is unique

        // Given: Set up the request specification for updating the user
        // When: Send the PATCH request to update the user
        Response patchResponse = given()
                .basePath("/public/v1/users/" + userId)
                .header("Authorization", "Bearer 55d6636b25b84832f383665a17f72117ee2b5e655a78ba968912c9a37d1c050f")
                .contentType("application/json")
                .body("{ \"name\": \"" + newName + "\", \"email\": \"" + newEmail + "\", \"status\": \"active\" }")
                .when()  // When: Execute the request
                .patch();

        // Then: Print the response
        System.out.println(patchResponse.jsonPath().getString("data"));

        // Then: Assertions to verify the response
        if (patchResponse.getStatusCode() == 422) {
            // If the status code is 422, it means there is a validation error (likely due to duplicate email)
            String errorMessage = patchResponse.jsonPath().getString("data.message");
            System.out.println("Error: " + errorMessage);
            assertTrue(errorMessage.contains("has already been taken"), "Expected error message to indicate email already exists");
        } else {
            // Otherwise, check for a successful update
            assertEquals(200, patchResponse.getStatusCode(), "Expected status code 200");
            assertEquals(newName, patchResponse.jsonPath().getString("data.name"), "Expected user name to be " + newName);
        }
    }
}
