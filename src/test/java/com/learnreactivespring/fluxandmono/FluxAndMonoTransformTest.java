package com.learnreactivespring.fluxandmono;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

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

    @Test
    public void transformUsingFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> {
                    return Flux.fromIterable(converToList(s));//a-> list[a, newValue], b-> list[b, newValue]
                })
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> converToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "NewValue");
    }

    @Test
    public void transformUsingFlatMapUsingParallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2) //Flux<Flux>String>> -> (A,B) (C,D) (E,F)
                .flatMap((s) ->
                    s.map(this::converToList).subscribeOn(parallel()))//Flux<List<String>>
                    .flatMap(s -> Flux.fromIterable(s))//Flux<String>
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMapParallelMaintaainOrder() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2) //Flux<Flux>String>> -> (A,B) (C,D) (E,F)
                .concatMap((s) ->
                        s.map(this::converToList).subscribeOn(parallel()))//Flux<List<String>>
                .flatMap(s -> Flux.fromIterable(s))//Flux<String>
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }
}
