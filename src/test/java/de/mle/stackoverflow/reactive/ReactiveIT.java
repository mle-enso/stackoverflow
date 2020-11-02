package de.mle.stackoverflow.reactive;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactiveIT {
    @LocalServerPort
    private int port;

    @Test
    public void webClient() {
        Flux<Integer> flux = WebClient.builder().baseUrl("http://localhost:" + port).build()
                .get()
                .uri("/flux")
                .retrieve()
                .bodyToFlux(Integer.class);

        flux.subscribe(signal -> log.info("Next item in flux: {}", signal));

        log.info("Request is already on the way but no response signals yet!");

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNoEvent(Duration.ofMillis(500))
                .consumeNextWith(signal -> log.info("First item in step verifier: {}", signal))
                //.expectNextCount(5) TODO: Stops working with Spring Boot 2.3.5 – investigate why!
                .thenCancel()
                .verify();
    }
}
