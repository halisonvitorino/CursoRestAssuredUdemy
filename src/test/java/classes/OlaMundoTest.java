package classes;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OlaMundoTest {

    @Test
    public void testOlaMundo(){
        Response resp = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
        assertEquals("Ola Mundo!", resp.getBody().asString());
        assertEquals(201, resp.statusCode());
        assertEquals("O statuscode deve ser = 200" , resp.statusCode());
        ValidatableResponse validaResp = resp.then();
        validaResp.statusCode(200);
    }

    @Test
    public void outrasFormasRestAssured(){
        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
        ValidatableResponse validaResp = response.then();
        validaResp.statusCode(200);
        get("http://restapi.wcaquino.me/ola").then().statusCode(200);
    }

    @Test
    public void modoFluente(){
        given() //Pre condições
        .when() //Ação de fato
                .get("http://restapi.wcaquino.me/ola")
        .then() //Validações
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void metodosHamcrest(){
        //numeros
        assertEquals(1, 1);
        assertNotEquals(2,3);
        assertThat(7, greaterThan(6));
        assertThat(7, equalTo(7));
        assertThat(7, greaterThanOrEqualTo(6));

        //Strings
        assertThat("arroz", startsWith("a"));
        assertEquals("Ola Mundo!", "Ola Mundo!", "ta certo");

        //Arrays
        List<Integer> numeros = Arrays.asList(1,2,3,4,5,6);
        assertThat(numeros, hasSize(6));
        assertThat(numeros, contains(1,2,3,4,5,6));
        assertThat(numeros, containsInAnyOrder(6,5,4,3,2,1));
        assertThat(numeros, hasItem(1));
        assertThat(numeros, hasItems(1,3));
    }

    @Test
    public void validaBody(){
        given()
        .when()
                .get("http://restapi.wcaquino.me/ola")
        .then()
                .assertThat()
                .statusCode(200)
                .body(is("Ola Mundo!"))
                .body(startsWith("O"))
                .body(containsString("Mundo"))
                .body(notNullValue())
        ;
    }
}
