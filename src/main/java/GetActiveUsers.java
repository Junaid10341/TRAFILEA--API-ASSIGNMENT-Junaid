import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class GetActiveUsers {

    @Test
    public void getActiveUsers() {
        // Data: Set the base URI for RestAssured
        RestAssured.baseURI = "https://gorest.co.in";

        // Given: Set up the request specification
        // When: Send the request to get the list of users
        Response usersResponse = RestAssured
                .given()  // Given: Request setup
                .when()   // When: Request execution
                .get("/public/v1/users");  // Data: The endpoint to hit

        // Then: Validate the response status code
        assertEquals(200, usersResponse.statusCode());

        // Then: Extract the list of users from the response
        List<Map<String, Object>> users = usersResponse.jsonPath().getList("data");

        // Data: Find the first active user in the list
        Map<String, Object> activeUser = users.stream()
                .filter(user -> "active".equals(user.get("status")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No active user found"));

        // Data: Extract the user ID of the active user
        int userId = ((Number) activeUser.get("id")).intValue();

        // Given: Set up the request specification for getting user details
        // When: Send the request to get the details of the active user
        Response userDetailsResponse = RestAssured
                .given()  // Given: Request setup
                .when()   // When: Request execution
                .get("/public/v1/users/" + userId);  // Data: The endpoint with user ID

        // Then: Print the response for debugging
        userDetailsResponse.prettyPrint();

        // Then: Validate the response status code
        assertEquals(200, userDetailsResponse.statusCode());

        // Then: Verify that the user status is active
        String userStatus = userDetailsResponse.jsonPath().getString("data.status");
        assertEquals("active", userStatus);
    }
}
