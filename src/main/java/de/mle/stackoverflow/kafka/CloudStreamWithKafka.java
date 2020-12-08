package de.mle.stackoverflow.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class CloudStreamWithKafka {
    @Bean
    public Function<Flux<Long>, Flux<String>> processor() {
        return longFlux -> longFlux
                .map(i -> i * i)
                .map(Object::toString);
    }

    @Bean
    public Consumer<String> consumer() {
        return i -> log.debug("Consumed {}", i);
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
