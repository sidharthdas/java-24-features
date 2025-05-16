package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Gatherer;

public class D_InterruptGatherer {

    public static void main(String[] args) {

        Gatherer<Integer, ?, Integer> gatherer = Gatherer.ofSequential(
                ((_, element, downstream) -> {
                    System.out.println(downstream.isRejecting());
                    return downstream.push(element);
                })
        );

        List<Integer> l = new ArrayList<>();
        int i = 0;
        while (i < 1000) {
            l.add(i);
            i++;
        }

        var v = l.stream()
                .gather(gatherer)
                .limit(10)
                .toList();
    }
}
