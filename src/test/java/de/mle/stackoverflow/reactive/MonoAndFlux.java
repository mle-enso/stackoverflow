package de.mle.stackoverflow.reactive;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple3;

import java.time.Duration;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class MonoAndFlux {
    private Service service = new Service();

    @Test
    void flatMap() {
        Flux<Integer> numbers = Flux.just(1, 2, 3);
        Flux<String> xs = Flux.just("x");

        numbers.flatMap(i ->
                xs.map(x -> "Mapped to " + i + x)
        )
                .subscribe(System.out::println);
    }

    @Test
    public void delayMono() {
        // when
        Supplier<Mono<Integer>> runnable = () -> service.getDelayedInteger();

        // then
        StepVerifier.withVirtualTime(runnable)// From here we talk about virtual time.
                .expectSubscription() // No time yet elapsed
                .expectNoEvent(Duration.ofSeconds(1)) // No event (item) for 1 second
                .expectNext(1) // first event (item) arrives
                .expectComplete() // Assert the completion signal.
                .verify(Duration.ofMillis(100)); // Everything takes max. 100 ms in real-time!
    }

    @Test
    public void delayFlux() {
        // when
        Supplier<Flux<Integer>> runnable = () -> service.getDelayedIntegers();

        // then
        StepVerifier.withVirtualTime(runnable)// From here we talk about virtual time.
                .expectSubscription() // No time yet elapsed
                .expectNoEvent(Duration.ofSeconds(1)) // No event (item) for 1 second
                .expectNext(1) // first event (item) arrives
                .expectNoEvent(Duration.ofSeconds(1)) // no further events for another second
                .expectNext(2) // second event (item) arrives
                .thenAwait(Duration.ofSeconds(2)) // let the time pass…
                .expectNext(3, 4) // …and expect the last both items
                .expectComplete() // Assert the completion signal.
                .verify(Duration.ofMillis(100)); // Everything takes max. 100 ms in real-time!
    }

    @Test
    public void doFinallyAndDoOnSuccess() {
        Mono.just(1)
                .doOnSuccess(it -> log.warn("onSuccess {}", it))
                .doFinally(it -> log.warn("finally {}", it))
                .subscribe();
    }

    @Test
    public void defaultIfEmpty() {
        assertThat(Mono.empty().defaultIfEmpty(3).block()).isEqualTo(3);
    }

    @Test
    public void switchIfEmpty() {
        assertThat(Mono.empty().switchIfEmpty(Mono.just(3)).block()).isEqualTo(3);
    }

    @Test
    public void fluxZip() {
        val flux = Flux.zip(Flux.just(1, 2), Flux.just(3, 4), Flux.just(5, 6, 7)).map(Tuple3::toString);
        StepVerifier.create(flux)
                .expectNext("[1,3,5]")
                .expectNext("[2,4,6]")
                .verifyComplete();
    }

    @Test
    public void fluxMerge() {
        val flux = Flux.merge(Flux.just(1, 2), Flux.just(3, 4), Flux.just(5, 6, 7));
        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext(1, 2, 3, 4, 5, 6, 7)
                .verifyComplete();
    }

    @Test
    public void showRetryOnError() throws InterruptedException {
        Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3) {
                        return "tick " + input;
                    }
                    throw new RuntimeException("boom");
                })
                .retry(1)
                .elapsed()
                .subscribe(System.out::println, System.err::println);

        Thread.sleep(2100);
    }

    class Service {
        Mono<Integer> getDelayedInteger() {
            return Mono.just(1).delayElement(Duration.ofSeconds(1));
        }

        Flux<Integer> getDelayedIntegers() {
            return Flux.just(1, 2, 3, 4).delayElements(Duration.ofSeconds(1));
        }
    }
}
