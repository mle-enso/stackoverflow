package de.mle.stackoverflow.reactive;

import de.mle.stackoverflow.IntegrationTestConfigWithPortAndTestProfile;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
public class ReactiveIT extends IntegrationTestConfigWithPortAndTestProfile {
    @Autowired
    private WebTestClient client;

    @Test
    public void webClient() throws InterruptedException {
        Flux<Integer> flux= client
                .get()
                .uri("/flux")
                .exchange()
                .returnResult(Integer.class)
                .getResponseBody();

        log.info("Request is already on the way but no response signals yet!");

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNextMatches(i -> i == 1)
                .expectNextMatches(i -> i == 2)
                .expectNextCount(5)
                .thenCancel()
                .verify();
    }
}
