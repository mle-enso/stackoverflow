package de.mle.stackoverflow;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2LoginControllerIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Test
    public void documentRequestBodyArray() {
        ResponseEntity resp = webClient
                .get()
                .uri("/")
                .retrieve()
                .toBodilessEntity()
                .block();

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(resp.getHeaders().getLocation().getHost()).isEqualTo("github.com");
        assertThat(resp.getHeaders().getLocation().getPath()).isEqualTo("/login/oauth/authorize");
    }
}
