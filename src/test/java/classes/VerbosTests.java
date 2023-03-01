package classes;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.registerParser;
import static org.hamcrest.Matchers.*;

public class VerbosTests {

    public static RequestSpecification requestSpecificationLog;
    public static ResponseSpecification responseSpecificationStatusCode, responseSpecificationLog ;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://restapi.wcaquino.me";
        //RestAssured.port = 8080;
        //RestAssured.basePath = "/users";
        RequestSpecBuilder recBuilder = new RequestSpecBuilder();
        recBuilder.log(LogDetail.ALL);
        requestSpecificationLog = recBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectStatusCode(201);
        resBuilder.log(LogDetail.ALL);
        responseSpecificationStatusCode = resBuilder.build();
        responseSpecificationLog        = resBuilder.build();

        RestAssured.requestSpecification  = requestSpecificationLog;
        RestAssured.responseSpecification = responseSpecificationStatusCode;
        RestAssured.responseSpecification = responseSpecificationLog;
    };

    @Test
    public void salvarUsuario() {
        given()
                .contentType("application/json")
                .body("{\"name\": \"Jose\", \"age\": 25}")
        .when()
                .post("/users")
        .then()
                .body("name", is("Jose"))
                .body("id", is(notNullValue()))
                .body("age", is(25))
        ;
    }

    @Test
    public void naoSalvarUsuarioSemNome() {
        given()
                .contentType("application/json")
                .body("{\"age\": 25}")
        .when()
                .post("/users")
        .then()
                .body("error", is("Name é um atributo obrigatório"))
                .body("id", is(nullValue()))
        ;
    }

    @Test
    public void salvarUsuarioXML() {
        given()
                .contentType(ContentType.XML)
        .body("<user><name>Joao da Silva</name><age>25</age></user>")
        .when()
                .post("/usersXML")
        .then()
                .body("user.name", is("Joao da Silva"))
                .body("user.id", is(notNullValue()))
                .body("user.age", is(25))
        ;
    }
}

