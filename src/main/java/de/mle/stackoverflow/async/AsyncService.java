package de.mle.stackoverflow.async;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.List;

public class AsyncService {
    public List<String> toCall() throws InterruptedException {
        Mono<List<String>> zip = Mono.zip(callServiceA(1), callServiceB(2)).map(this::toResultModel).share();
        zip.subscribe();

        System.out.println("=====");
        Thread.sleep(2_000);
        System.out.println("#####");

        return zip.block();
    }

    private List<String> toResultModel(Tuple2<String, String> objects) {
        return List.of(objects.getT1(), objects.getT2());
    }

    private Mono<String> callServiceA(int a) {
        System.out.println("call service a");
        return Mono.just("a").delayElement(Duration.ofSeconds(3)).doOnNext(System.out::println);
    }

    private Mono<String> callServiceB(int b) {
        System.out.println("call service b");
        return Mono.just("b").doOnNext(System.out::println);
    }
}
