package org.example;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class D_mutableStateGatherer {

    public static void main(String[] args) {

        Gatherer<Integer, AtomicInteger, Integer> g = Gatherer.ofSequential(AtomicInteger::new,
                ((state, number, downstream) -> {
                        var val = state.addAndGet(number);
                        return downstream.push(val);
                }));

        var l = Stream.of(10,20,30,40)
                .gather(g)
                .toList();

        System.out.println(l);
    }
}
