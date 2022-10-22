import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class NationalitiesFunctionTests {

    private RequestSpecification reqSpec;

    private Cookies cookies;

    HashMap<String, String> reqBody;

    private String nationality_id;


    @BeforeClass
    public void setup () {

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);

    }

    @Test(priority = 1)
    public void loginTest () {

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", "richfield.edu");
        credentials.put("password", "Richfield2020!");
        credentials.put("rememberMe", "true");

        cookies = given()
                .spec(reqSpec)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("username", equalTo(credentials.get("username")))
                .extract().detailedCookies();

    }


    @Test(priority = 2)
    public void createNationalityTest() {

        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("name", "BCFNation");


        nationality_id = given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");

    }

    @Test(priority = 3)
    public void createNationalityNegativeTest() {

        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("name", "BCFNation");


        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(priority = 4)
    public void getNationalityTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/nationality/" + nationality_id)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 5)
    public void updateNationalityTest() {

        HashMap<String, String> newReqBody = new HashMap<>();
        newReqBody.put("id", nationality_id);
        newReqBody.put("name", "BCFNation22");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(newReqBody)
                .when()
                .put("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newReqBody.get("name")));

    }

    @Test(priority = 6)
    public void deleteNationalityTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/nationality/" + nationality_id)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 7)
    public void getNationalityNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/nationality/" + nationality_id)
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(priority = 8)
    public void deleteNationalityNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/nationality/" + nationality_id)
                .then()
                .log().body()
                .statusCode(400);

    }

}