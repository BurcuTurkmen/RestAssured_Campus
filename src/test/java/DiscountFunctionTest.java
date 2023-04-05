
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DiscountFunctionTest {


    private RequestSpecification reqSpec;

    private Cookies cookies;

    private HashMap<String, String> reqBody;

    private String discount_id;




    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://demo.mersys.io";

        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);

        reqBody = new HashMap<>();
        reqBody.put("description", "BlackFriday");
        reqBody.put("code","84796457");
        reqBody.put("priority","93");
        reqBody.put("active","true");


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
    public void createDiscountTest() {


        discount_id=given()
                .spec(reqSpec)
                .body(reqBody)
                .cookies(cookies)
                .when()
                .post("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(201)
                .body("description", equalTo(reqBody.get("description")))
                .extract().jsonPath().getString("id");

    }

    @Test(dependsOnMethods = "createDiscountTest")
    public void getDiscountTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/discounts/" + discount_id)
                .then()
                .log().body()
                .statusCode(200)
                .body("description", equalTo(reqBody.get("description")));

    }

    @Test(dependsOnMethods = "getDiscountTest")
    public void createDiscountNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(dependsOnMethods = "createDiscountNegativeTest")
    public void editDiscountTest() {

        HashMap<String, String> newReqBody = new HashMap<>();
        newReqBody.put("id", discount_id);
        newReqBody.put("description", "BLKFRDAY");
        newReqBody.put("code", "blk35498");
        newReqBody.put("priority", "45");
        newReqBody.put("active", "true");


        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(newReqBody)
                .when()
                .put("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(200)
                .body("description", equalTo(newReqBody.get("description")));

    }

    @Test(dependsOnMethods = "editDiscountTest")
    public void deleteDiscountTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/discounts/" + discount_id)
                .then()
                .log().body()
                .statusCode(200);

    }

    @Test(dependsOnMethods = "deleteDiscountTest")


    public void getDiscountNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/discounts/" + discount_id)
                .then()
                .log().body()
                .statusCode(400);
                //.body("description", equalTo(reqBody.get("description")));

    }

    @Test(dependsOnMethods = "getDiscountNegativeTest")
    public void deleteUserNegativeTest() {

        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/discounts/" + discount_id)
                .then()
                .log().body()
                .statusCode(401);

    }



    }




