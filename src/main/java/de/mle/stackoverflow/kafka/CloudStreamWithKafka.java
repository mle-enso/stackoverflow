package de.mle.stackoverflow.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Configuration
public class CloudStreamWithKafka {
    @Bean
    public Function<Flux<Long>, Flux<SquareNumber>> processor() {
        return longFlux -> longFlux
                .map(i -> i * i)
                .map(SquareNumber::new);
    }

    @Bean
    public Consumer<SquareNumber> consumer() {
        return i -> log.info("Consumed {}", i);
    }

    @Bean
    public Function<String, String> concatHash() {
        return value -> value.concat("#");
    }

    @Bean
    public Function<String, String> concatStar() {
        return value -> value.concat("*");
    }
}
