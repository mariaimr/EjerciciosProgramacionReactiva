package academy.devdojo.reactive.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
/**
 * Reactive Streams
 * 1. Asynchronous
 * 2. Non-blocking
 * 3. Backpressure
 */
public class MonoTest {

    @Test
    public void monoSubscriber(){
        String name = "maria isabel";
        Mono<String> mono = Mono.just(name)
                .log();

        mono.subscribe();
        log.info("---------------------------------");
        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumer(){
        String name = "maria isabel";
        Mono<String> mono = Mono.just(name)
                .log();

        mono.subscribe(s -> log.info("Value {} ", s));
        log.info("---------------------------------");
        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumerError(){
        String name = "maria isabel";
        Mono<String> mono = Mono.just(name)
                .map(s -> {throw new RuntimeException("Testing mono with Error"); });

        mono.subscribe(s -> log.info("Name {} ", s), s -> log.error("Something bad happened"));
        mono.subscribe(s -> log.info("Name {} ", s), Throwable::printStackTrace);

        log.info("---------------------------------");
        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void monoSubscriberConsumerComplete(){
        String name = "maria isabel";
        Mono<String> mono = Mono.just(name)
                .log()
                .map(String::toUpperCase);

        mono.subscribe(s -> log.info("Value {} ", s),
                Throwable::printStackTrace,
                () -> log.info("Finished"));
        log.info("---------------------------------");
        StepVerifier.create(mono)
                .expectNext(name.toUpperCase())
                .verifyComplete();
    }

    @Test
    public void monoSubscriberConsumerSubscription(){
        String name = "maria isabel";
        Mono<String> mono = Mono.just(name)
                .log()
                .map(String::toUpperCase);

        mono.subscribe(s -> log.info("Value {} ", s),
                Throwable::printStackTrace,
                () -> log.info("Finished"),
                subscription -> subscription.request(5));

        log.info("---------------------------------");

       StepVerifier.create(mono)
                .expectNext(name.toUpperCase())
                .verifyComplete();
    }

    @Test
    public void monoDoOnMethods(){
        String name = "maria isabel";
        Mono<Object> mono = Mono.just(name)
                .log()
                .map(String::toUpperCase)
                .doOnSubscribe(subscription -> log.info("Subscribed"))
                .doOnRequest(longNumber -> log.info("Request Received, starting doing somethind..."))
                .doOnNext(s -> log.info("Value is here. Executing doOnNext {}",s))
                .flatMap(s -> Mono.empty())
                .doOnNext(s -> log.info("Value is here. Executing doOnNext {}",s))
                .doOnSuccess(s -> log.info("doOnSuccess executed"));

        mono.subscribe(s -> log.info("Value {} ", s),
                Throwable::printStackTrace,
                () -> log.info("Finished"));
        log.info("---------------------------------");
       /* StepVerifier.create(mono)
                .expectNext(name.toUpperCase())
                .verifyComplete();*/
    }
}
