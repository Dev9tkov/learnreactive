package com.learnreactivespring.fluxandmono;

import org.junit.Test;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransformTest {
    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void transformUsingMap() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .map(s -> s.toUpperCase())
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("ADAM", "ANNA", "JACK", "JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingMapLength() {
        Flux<Integer> stringFlux = Flux.fromIterable(names)
                .map(s -> s.length())
                .log();
        StepVerifier.create(stringFlux)
                .expectNext(4, 4, 4, 5)
                .verifyComplete();
    }

    @Test
    public void transformUsingMapLengthRepeat() {
        Flux<Integer> stringFlux = Flux.fromIterable(names)
                .map(s -> s.length())
                .repeat(1)
                .log();
        StepVerifier.create(stringFlux)
                .expectNext(4, 4, 4, 5)
                .expectNext(4, 4, 4, 5)
                .verifyComplete();
    }

    @Test
    public void transformUsingMapFilter() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                .filter(s -> s.length() > 4)
                .map(s -> s.toUpperCase())
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("JENNY")
                .verifyComplete();
    }
}
