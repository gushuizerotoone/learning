package com.gushui.learning.reactor.core;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Thomas on 2018/2/28.
 */
public class ReactorIntegrationTest {

  @Test
  public void givenFlux_whenSubscribing_thenStream() throws InterruptedException {
    List<Integer> elements = new ArrayList<>();
    Flux.just(1,2,3,4)
            .log()
            .map(i -> {
              System.out.println(i + ": " + Thread.currentThread());
              return i * 2;
            })
            .subscribe(elements::add);
    assertThat(elements).containsExactly(2, 4, 6, 8);
  }

  @Test
  public void givenFlux_whenZipping_thenCombine() {
    List<String> elements = new ArrayList<>();

    Flux.just(1,2,3,4)
            .log()
            .map(i -> i * 2)
            .zipWith(
                    Flux.range(0, Integer.MAX_VALUE)
                    .log(),
                    (two, one) -> String.format("First Flux: %s, Second Flux: %s", one, two)
            )
            .subscribe(elements::add);

    assertThat(elements).containsExactly(
            "First Flux: 0, Second Flux: 2",
            "First Flux: 1, Second Flux: 4",
            "First Flux: 2, Second Flux: 6",
            "First Flux: 3, Second Flux: 8");
  }

  @Test
  public void givenFlux_whenApplyingBackPressure_thenPushElementsInBatches() throws InterruptedException {
    List<Integer> elements = new ArrayList<>();

    Flux.just(1,2,3,4)
            .log()
            .map(i -> i * 2)
            .onBackpressureBuffer()
            .subscribe(new Subscriber<Integer>() {
              private Subscription s;
              private int onNextAmount;

              @Override
              public void onSubscribe(Subscription s) {
                this.s = s;
                s.request(2);
              }

              @Override
              public void onNext(Integer i) {
                elements.add(i);
                onNextAmount++;
                if (onNextAmount % 2 == 0) {
                  s.request(2);
                }
              }

              @Override
              public void onError(Throwable t) {

              }

              @Override
              public void onComplete() {

              }
            });

    assertThat(elements).containsExactly(2, 4, 6, 8);
  }

  @Test
  public void givenFlux_whenInParallel_thenSubscribeInDifferentThreads() throws InterruptedException {
    List<String> threadNames = new ArrayList<>();
    Flux.just(1, 2, 3, 4)
            .map(i -> Thread.currentThread().getName())
            .subscribeOn(Schedulers.parallel())
            .subscribe(threadNames::add);

    Thread.sleep(1000);
    System.out.println("main thread: " + Thread.currentThread());

    assertThat(threadNames).containsExactly("parallel-1", "parallel-1", "parallel-1", "parallel-1");
  }

  @Test
  public void givenConnectableFlux_whenConnected_thenShouldStream() {
    List<Integer> elements = new ArrayList<>();
    ConnectableFlux<Integer> publisher = Flux.just(1, 2, 3, 4).publish();
    publisher.map(i -> i * 2).subscribe(elements::add);
    assertThat(elements).isEmpty();

    publisher.connect();
    assertThat(elements).containsExactly(2, 4, 6, 8);
  }

}
