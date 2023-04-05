import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class GoRestUserTest {

    private RequestSpecification reqSpec; // when we create this reqSpec in setup method, it`s local variable inside {} Thats why we need to set up here
    // and do it private so, it`s not accessible outside of this class. Declare here and initialize it under setup method

    private HashMap<String, String> requestBody; // declare here to use it and initialize it in requestbody = new HashMap
    private Object userId; // for using in other tests, we declare it here, so we can use in other tests too

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://gorest.co.in"; // this is our server url

        // reqSpec in RestAssured, with this we give below log-header-contenttype fields in this and no need to give these again again in each test
        // so, inside setup, we have req specification
        reqSpec = given() // includes test data, setup not a request body
                .log().uri() // going to log uri details
                .header("Authorization", "Bearer a970f0a198c804bfd481ad5fd1420649fdc33f0a25dfaa96fae1cde5f0a94637") // actual auth. under header in Postman
                .contentType(ContentType.JSON); // setting up content details


        // Inside setup method, I setup my Hashmap, so my test will be much simpler
        // for creating request body we need HashMap to make it similar to JSON
        // HashMap is data we send
        requestBody = new HashMap<>(); // use Hashmap for req body same as JSON in Postman (key, value pairs), and below infos` from Postman
        requestBody.put("name", "CookieLatte");
        requestBody.put("email", "cookielatte22@techblah.com");
        requestBody.put("gender", "male");
        requestBody.put("status", "active");
        // these are exactly same thing what we did in Postman. This data type(HashMap) created for this use case because we need key-value pairs structure

    }

    @Test
    public void createUserTest() { // this is just for test, no HashMap setup

        userId = given()
                .spec(reqSpec) // call those 3 reqSpec s here... instead of 3 more lines | this is given part
                .body(requestBody) // | this is given part too
                .when() // after when we have post method
                .post("/public/v2/users")// going to use endpoint from postman.
                .then() // ad
                .log().body() // log response body
                .body("name", equalTo(requestBody.get("name"))) // check if the user we create that same that we have here..(hamcrest)
                .statusCode(201) // check status code if it is 201
                .extract().path("id"); // going to extract id of user we create and store inside userId variable. I declare userId on the very top and
                                        // initialize it here, in the createUserTest.

    }

    @Test(dependsOnMethods = "createUserTest") // cunku createUserTest calismadan edit calismiyor, depends on method o yuzden ekledik
    public void editUserTest() {

        HashMap<String, String> updateRequestBody = new HashMap<>();
        updateRequestBody.put("name", "Updated Blah Blah name");

        given()
                .spec(reqSpec)
                .body(updateRequestBody)
                .when()
                .put("/public/v2/users/" + userId)
                .then()
                .statusCode(200);

    }

    @Test(dependsOnMethods = "editUserTest")
    public void deleteUserTest() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + userId)// extracting userId from Create user test and extract from there and use as a variable otherwise it`ll be a string
                .then()
                .log().body() // log the response body to see if we get any error, just in case to see any possible errors.. normally we don`t need that.
                .statusCode(204);

    }





}
