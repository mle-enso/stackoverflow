package de.mle.stackoverflow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

public class PrometheusEndpointIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void queryPrometheusEndpoint() {
        webTestClient
                .get()
                .uri("/actuator/prometheus")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody().blockFirst().contains("sample_count");
    }
}
