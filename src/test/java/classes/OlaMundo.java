package classes;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundo {
    public static void main(String[] args) {
        Response resp = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
        System.out.println(resp.getBody().asString().equals("Ola Mundo!"));
        System.out.println(resp.getBody().asString());
        System.out.println(resp.statusCode() == 200);

        ValidatableResponse validaResp = resp.then();
        validaResp.statusCode(300);
    }
}
