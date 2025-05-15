package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class D_FinisherImplGatherer {

    public static void main(String[] args) {

        Gatherer<Integer, List<Integer>, List<Integer>> g = Gatherer.ofSequential(
                ArrayList::new,
                (buffer, element, downstream) -> {
                    buffer.add(element);
                    if (buffer.size() == 3) {
                        downstream.push(new ArrayList<>(buffer));
                        buffer.clear();
                    }

                    return true;
                },

                //If any left over is present
                (buffer, downstream) -> {
                    if (!buffer.isEmpty()) {
                        downstream.push(buffer);
                    }

                }
        );


        List<List<Integer>> l = Stream.of(1,2,3,4,5,6,7,8)
                .gather(g)
                .toList();

        System.out.println(l);
    }
}
