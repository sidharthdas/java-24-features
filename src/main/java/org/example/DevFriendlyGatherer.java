package org.example;

import java.util.List;
import java.util.stream.Gatherer;

public class DevFriendlyGatherer {

    public static void main(String[] args) {

        List<Student> students = List.of(
                new Student("sid", "a", "dav", 25),
                new Student("RAM", "a", "dav", 30),
                new Student("Sita", "b", "dav", 35)
        );

        Gatherer<Student, ?, Student> g = Gatherer.of((state, element, downstream) -> {
            if(element != null && element.age() > 25) {
                Student s = new Student(element.name().toUpperCase(), element.section(), element.section(), element.age());
                return downstream.push(s);
            }
            return true;
        });

        List<Student> aftergatherer = students
                .stream()
                .gather(g)
                .toList();

        System.out.println(aftergatherer);
    }
}
