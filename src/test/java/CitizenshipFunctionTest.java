import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CitizenshipFunctionTest {

    RequestSpecification reqSpec;

    Cookies cookies;

    HashMap<String, String> reqBody;

    private String citizenship_id;



    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);

        reqBody = new HashMap<>();
        reqBody.put("name", "Guatemalian");
        reqBody.put("shortName", "GTM");

    }

    @Test(priority = 1)
    public void loginTest() {

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", "richfield.edu");
        credentials.put("password", "Richfield2020!");
        credentials.put("rememberMe", "true");


        cookies=given()
                .spec(reqSpec)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().detailedCookies();

    }

    @Test(priority = 2)
    public void loginNegativeTest() {

        HashMap<String, String> credentials = new HashMap<>();
        credentials.put("username", "richfield.edu");
        credentials.put("password", "Richfield20");
        credentials.put("rememberMe", "true");

        given()
                .spec(reqSpec)
                .body(credentials)
                .cookies(cookies)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);

    }

    @Test(priority = 3)
    public void createCitizenshipTest() {

        citizenship_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(reqBody.get("name")))
                .extract().jsonPath().getString("id");

    }

    @Test(priority = 4)
    public void getCitizenshipTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/citizenships/" + citizenship_id)
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(reqBody.get("name")));

    }

    @Test(priority = 5)
    public void createCitizenshipNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .post("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(priority = 6)
    public void editCitizenshipTest() {

        HashMap<String, String> newReqBody =new HashMap<>();
        newReqBody.put("id", citizenship_id);
        newReqBody.put("name", "Columbian");
        newReqBody.put("shortName", "CLBM");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(newReqBody)
                .when()
                .put("/school-service/api/citizenships")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newReqBody.get("name")));

    }

    @Test(priority = 7)
    public void deleteCitizenshipTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/citizenships/" + citizenship_id)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(priority = 8)
    public void getCitizenshipNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/citizenships/" + citizenship_id)
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(priority = 9)
    public void deleteCitizenshipNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/citizenships/" + citizenship_id)
                .then()
                .log().body()
                .statusCode(400);

    }


}
