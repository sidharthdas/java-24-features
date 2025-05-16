package org.example;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Gatherer;

public class D_CombinerImplGatherer {

    public static void main(String[] args) {

        Gatherer<Integer, AtomicInteger, Integer> gatherer = Gatherer.of(
                AtomicInteger::new,
                ((state, element, downstream) -> {
                    return downstream.push(state.addAndGet(element));
                }),
                (s1, s2) -> {
                    s1.addAndGet(s2.get());
                    return s1;
                },
                (broker, downstream) -> {
                    downstream.push(broker.get());
                }
        );

        var v = List.of(10, 20, 30, 40, 50, 60)
                .stream()
                .gather(gatherer)
                .toList();

        System.out.println(v);

    }
}
