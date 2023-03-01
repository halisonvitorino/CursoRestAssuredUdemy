package classes;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTests {

    @Test
    public void validaPrimeiroNivel(){
        //Usando Hamcrest + RestAssured
        given()
        .when()
                .get("http://restapi.wcaquino.me/users/1")
        .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", containsString("Silva"))
                .body("age", greaterThan(18))
        ;
    }

    @Test
    public void validaPrimeiroNivelOutrasFormas(){
        //Usando Junit + RestAssured
        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");

        //Usando Path
        assertEquals(1, (Integer) response.path("id"));

        //Usando JsonPath
        JsonPath jsonpath = new JsonPath(response.asString());
        assertEquals(1, jsonpath.getInt("id"));

        //Usando From
        int id = JsonPath.from(response.asString()).getInt("id");
        assertEquals(1, id);
    }

    @Test
    public void validaSegundoNivel(){
        //Usando Hamcrest + RestAssured
        given()
                .when()
                .get("http://restapi.wcaquino.me/users/2")
                .then()
                .statusCode(200)
                .body("id", is(2))
                .body("name", containsString("Maria"))
                .body("age", greaterThan(18))
                .body("endereco.rua", is("Rua dos bobos"))
        ;
    }

    @Test
    public void validaLista(){
        //Usando Hamcrest + RestAssured
        given()
                .when()
                .get("http://restapi.wcaquino.me/users/3")
                .then()
                .statusCode(200)
                .body("id", is(3))
                .body("name", containsString("Ana"))
                .body("age", greaterThan(18))
                .body("filhos", hasSize(2))
                .body("filhos[0].name", is("Zezinho"))
                .body("filhos[1].name", is("Luizinho"))
                .body("filhos.name", contains("Zezinho","Luizinho"))
                .body("filhos.name", hasItems("Zezinho","Luizinho"))
        ;
    }

    @Test
    public void validaUsuarioInexistente(){
        //Usando Hamcrest + RestAssured
        given()
                .when()
                .get("http://restapi.wcaquino.me/users/4")
                .then()
                .statusCode(404)
                .log().all()
                .body("error", is("Usuário inexistente"))
        ;
    }

    @Test
    public void validaListaRaiz(){
        //Usando Hamcrest + RestAssured
        given()
                .when()
                .get("http://restapi.wcaquino.me/users")
                .then()
                .statusCode(200)
                .log().all()
                .body("name[1]", is("Maria Joaquina"))
                .body("age[0]", is(30))
                .body("age[2]", is(20))
                .body("salary[1]", is(2500))
                .body("", hasSize(3))
                .body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
                .body("filhos[2].name[1]", is("Luizinho"))
                .body("filhos.name", hasItems(Arrays.asList("Zezinho","Luizinho")))
        ;
    }

    @Test
    public void validacaoAvancada(){
        //Usando Hamcrest + RestAssured
        given()
        .when()
                .get("http://restapi.wcaquino.me/users")
        .then()
                .statusCode(200)
                .log().all()
                .body("", hasSize(3))
                .body("age.findAll{it <= 25}.size()", is(2))
                .body("age.findAll{it <= 25 && it > 20}.size()", is(1))
                .body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
                .body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
                .body("find{it.age <= 25}.name", is("Maria Joaquina"))
                .body("findAll{it.name.contains('q')}.name", hasItem("Maria Joaquina"))
                .body("id.max()", is(3))
                .body("salary.min()", is(1234.5678f))
        ;
    }

    @Test
    public void validandoJsonPathEJava(){
        //Usando Hamcrest + RestAssured + Java + JUnit + JsonPath
        ArrayList<String> names =
        given()
        .when()
                .get("http://restapi.wcaquino.me/users")
        .then()
                .statusCode(200)
                .log().all()
                .extract().path("name.findAll{it.startsWith('Maria')}");

        assertEquals(1, names.size());
        assertEquals(names.get(0).toUpperCase(), "Maria Joaquina".toUpperCase());

        System.out.println("Lista de nomes:  " + names)
        ;
    }
}
