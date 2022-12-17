package de.mle.stackoverflow;

import de.mle.stackoverflow.jackson.Project;
import de.mle.stackoverflow.jackson.WorkPackageEstimateType;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

public class StackOverflowControllerIT extends IntegrationTestConfigWithPortAndTestProfile {
    @LocalServerPort
    private int port;

    @Test
    public void activeProfiles() {
        webTestClient
                .get()
                .uri("/stack/activeProfiles")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(is("[\"test\"]"));
    }

    @Test
    public void documentRequestBodyArray() {
        webTestClient
                .post()
                .uri("/stack/contract")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(List.of(new MessageContract("one"), new MessageContract("two")))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("documentArray",
                        requestFields(
                                fieldWithPath("[]").description("a message array"),
                                fieldWithPath("[].message").description("a message"))));
    }

    @Test
    public void restAssured() {
        RestAssured.given()
                .baseUri("http://localhost:" + port)
                .accept(ContentType.JSON)
                .get("/stack/jsonFile")
                .then()
                .statusCode(200)
                .body("al.findIndexOf { it.aid == 1461 }", is(2))
                .body("al.find { it.aid == 1461 }._c", is("Gurgaon1"))
                .body("al.find { it.aid == 1461 }.pc", is("122003"))
                .body("al", hasSize(4))
                .body("al[0].aid", is(1464));
        // .body("al", is(Arrays.asList("…")));
    }

    @Test
    public void restAssuredDeserialize() {
        Project project = RestAssured.given()
                .baseUri("http://localhost:" + port)
                .accept(ContentType.JSON)
                .get("/stack/deserialize")
                .body().as(Project.class);

        assertThat(project.getEstimateType()).isEqualTo(WorkPackageEstimateType.WEEK);
        assertThat(project.getEstimateType().getDisplayName()).isEqualTo("Weeks");
    }
}
