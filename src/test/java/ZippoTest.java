
import static io.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ZippoTest {

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://api.zippopotam.us";

    }


    @Test
    public void test() {

        given()
                .when()
                .then();

    }


    @Test
    public void checkingStatusCode() {

        given()
                .when()
                .get("/us/08536")
                .then()
                .statusCode(200);

    }


    @Test
    public void loggingRequestDetails() {

        given()
                .log().all() // if we put this after given(), this is give us all the details about our request
                .when()
                .get("/us/08536")
                .then()
                .statusCode(200);

    }


    @Test
    public void loggingResponseDetails() {

        given()
                .when()
                .get("/us/08536")
                .then()
                .log().all()
                .statusCode(200);

    }


    @Test
    public void checkContentType() {

        given()
                .when()
                .get("/us/08536")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200);

    }


    @Test
    public void validateCountryTest() {

        given()
                .when()
                .get("/us/08536")
                .then()
                .body("country", equalTo("United States"))
                .statusCode(200);

    }


    @Test
    public void validateCountryAbv() {

        given()
                .when()
                .get("/us/08536")
                .then()
                .body("'country abbreviation'", equalTo("US"))
                .statusCode(200);

    }


    @Test
    public void validateStateTest() {

        given()
                .when()
                .get("/us/08536")
                .then()
                .body("places[0].state", equalTo("New Jersey"))
                .statusCode(200);

    }


    @Test
    public void pathParametersTest() {

        String country = "us";
        String zipcode = "08536";

        given()
                .pathParam("country", country)
                .pathParam("zipcode", zipcode)
                .when()
                .get("/{country}/{zipcode}")
                .then()
                .statusCode(200);

    }


    @Test
    public void queryParameters() {

        String gender = "female";
        String status = "inactive";

        given()
                .param("gender", gender)
                .param("status", status)
                .when()
                .get("https://gorest.co.in/public/v2/users")
                .then()
                .statusCode(200)
                .log().body();

    }


    @Test
    public void extractValueTest() { // we use this function, when we create a new user and store the info in a variable. In this kind of situation.

        // variable | Object class is the parent of all classes so we use here object class | we use extract method and store extracted value inside a variable
        Object countryInfo = given()
                .when()
                .get("/us/08536")
                .then()
                .extract().path("country"); // with this extracting the info from response inside a variable, we need to write which info we want

        System.out.println(countryInfo);

    }


}