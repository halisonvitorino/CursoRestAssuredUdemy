package classes;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLTests {

    public static RequestSpecification requestSpecification;
    public static ResponseSpecification responseSpecification ;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://restapi.wcaquino.me";
        //RestAssured.port = 8080;
        //RestAssured.basePath = "http://localhost";
        RequestSpecBuilder recBuilder = new RequestSpecBuilder();
        recBuilder.log(LogDetail.ALL);
        requestSpecification = recBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectStatusCode(200);
        responseSpecification = resBuilder.build();

        RestAssured.requestSpecification  = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    };

    @Test
    public void validandoXML(){
        given()
        .when()
                .get("/usersXML/3")
        .then()
                .log().all()
                .rootPath("user")
                .body("name", is("Ana Julia"))
                .body("@id", is("3"))
                .body("filhos.name.size()", is(2))
                .body("filhos.name[1]", is("Luizinho"))
        ;
    }

    @Test
    public void validandoXMLEJava(){
        Object path =
        given()
        .when()
                .get("/usersXML")
        .then()
                .extract().path("users.user.name.findAll{it.toString().startsWith('Ana Julia')}");
        System.out.println("Path >> " + path);
        assertEquals("Ana Julia", path )
        ;
    }

    @Test
    public void validandoXPath(){
        given()
        .when()
                .get("/usersXML")
        .then()
                .body(hasXPath("count(/users/user)", is("3")))
                .body(hasXPath("/users/user[@id = 1]"))
                .body(hasXPath("//user[@id = 2]/name", is("Maria Joaquina")))
                .body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
                .body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", containsStringIgnoringCase("Zezinho")))
                .body(hasXPath("/users/user[@id = 1]/name", is("João da Silva")))
                .body(hasXPath("/users/user/name", is("João da Silva")))
                .body(hasXPath("/users/user[3]/name", is("Ana Julia")))
                .body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
                .body(hasXPath("/users/user[age < 24]/name", is("Ana Julia")))
        ;
    }
}
