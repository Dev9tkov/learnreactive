package com.learnreactivespring.repository;

import com.learnreactivespring.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@ActiveProfiles("nonprod")
public class ItemReactorRepositoryTest {
    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemsList = Arrays.asList(new Item(null, "Smsung TV", 400.0),
            new Item(null, "LG TV", 420.0),
            new Item(null, "Apple Watch", 299.8),
            new Item(null, "Beats Headphones", 149.9),
            new Item("ABC", "Bose Headphones", 149.9)
    );

    @Before
    public void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemsList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Insert Item is : " + item))
                .blockLast();//USING ONLY IN TEST
    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getItemById() {
        StepVerifier.create(itemReactiveRepository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Bose Headphones"))
                .verifyComplete();
    }

    @Test
    public void findItemByDescription() {
        StepVerifier.create(itemReactiveRepository.findByDescription("Bose Headphones"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem() {
        Item item = new Item(null, "Google Home Mini", 30.00);
        Mono<Item> saveItem = itemReactiveRepository.save(item);
        StepVerifier.create(saveItem)
                .expectSubscription()
                .expectNextMatches(i -> i.getId() != null && i.getDescription().equals("Google Home Mini"))
                .verifyComplete();

    }
}
