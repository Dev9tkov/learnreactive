package com.learnreactivespring.fluxandmono;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoTimeTest {

    @Test
    public void infiniteSequence() {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100))
                .log();//stars from 0 -->...
        infiniteFlux
                .subscribe((element) -> System.out.println("Value is : " + element));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void infiniteSequenceTest() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(100))
                .take(3)
                .log();
        StepVerifier.create(infiniteFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceMap() throws InterruptedException {
        Flux<Integer> infiniteFlux = Flux.interval(Duration.ofMillis(100))
                .map(Long::intValue)
                .take(3)
                .log();
        StepVerifier.create(infiniteFlux)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }

    @Test
    public void infiniteSequenceMapWithDelay() throws InterruptedException {
        Flux<Integer> infiniteFlux = Flux.interval(Duration.ofMillis(100))
                .delayElements(Duration.ofSeconds(1))
                .map(Long::intValue)
                .take(3)
                .log();
        StepVerifier.create(infiniteFlux)
                .expectSubscription()
                .expectNext(0, 1, 2)
                .verifyComplete();
    }
}
