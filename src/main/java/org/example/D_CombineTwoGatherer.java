package org.example;

import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class D_CombineTwoGatherer {

    public static void main(String[] args) {

        Gatherer<String, ?, String> gatherer1 = Gatherer.ofSequential(
                ((state, element, downstream) ->
                        downstream.push(element.toUpperCase()))
        );

        Gatherer<String, ?, String> gatherer2 = Gatherer.ofSequential(
                ((state, element, downstream) -> {
                    if (element.length() >= 3) {
                        downstream.push(element);
                    }
                    return true;
                }
                )
        );

        var v = Stream.of("java", "python", "c++", "c")
                .gather(gatherer1.andThen(gatherer2))
                .toList();

        System.out.println(v);


    }
}
