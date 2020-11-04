package de.mle.stackoverflow.jackson;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class JsonController {
    @GetMapping(path = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getJson() {
        return "{ \"singleItemList\" : 3 }";
    }

    @GetMapping(path = "/flux")
    public Flux<Integer> getFlux() {
        Flux
                .range(1, 100)
                .delayElements(Duration.ofSeconds(1))
                .subscribe(signal -> System.out.println("On the server in background: " + signal.intValue()));
        return Flux.range(1, 100);
    }
}
