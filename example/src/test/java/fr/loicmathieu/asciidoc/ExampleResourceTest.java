package fr.loicmathieu.asciidoc;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class ExampleResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
            .when().get("/")
            .then()
                .statusCode(200)
                .body(containsString("<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/reveal.js@3.9.2/css/theme/moon.css\" id=\"theme\">"))
                .body(containsString("<h1>Reveal.js Sample document</h1>"))
                .body(containsString("<h2>Slide 2 with some code</h2>"))
                .body(containsString("<script src=\"browserWatch.js\"></script>"));
    }

}