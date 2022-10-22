import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class FeesFunctionTest {

    private RequestSpecification reqSpec;

    private Cookies cookies;

    private HashMap<String, String> reqBody;

    private String fees_id;




    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);

        reqBody = new HashMap<>();
        reqBody.put("name", "Registration");
        reqBody.put("code", "RGT456789");
        reqBody.put("intCode", "8475390758");
        reqBody.put("priority", "22");
        reqBody.put("active", "true");

    }

    @Test
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

    @Test(dependsOnMethods = "loginTest")
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

    @Test(dependsOnMethods = "loginNegativeTest")
    public void createFeesTest() {

        fees_id=given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/fee-types")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(reqBody.get("name")))
                .extract().jsonPath().getString("id");

    }

    @Test(dependsOnMethods = "createFeesTest")
    public void getFeesTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/fee-types/" + fees_id)
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(reqBody.get("name")));

    }

    @Test(dependsOnMethods = "getFeesTest")
    public void createFeesNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .post("/school-service/api/fee-types")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(dependsOnMethods = "createFeesNegativeTest")
    public void editFeesTest() {

        HashMap<String, String> newReqBody = new HashMap<>();
        newReqBody.put("id", fees_id);
        newReqBody.put("name", "Course");
        newReqBody.put("code", "IT101");
        newReqBody.put("intCode", "53907");
        newReqBody.put("priority", "88");
        newReqBody.put("active", "true");


        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(newReqBody)
                .when()
                .put("/school-service/api/fee-types")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newReqBody.get("name")));

    }

    @Test(dependsOnMethods = "editFeesTest")
    public void deleteFeesTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/fee-types/" + fees_id)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(dependsOnMethods = "deleteFeesTest")
    public void getFeesNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/fee-types/" + fees_id)
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(dependsOnMethods = "getFeesNegativeTest")
    public void deleteFeesNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/fee-types/" + fees_id)
                .then()
                .log().body()
                .statusCode(400);

    }


}
