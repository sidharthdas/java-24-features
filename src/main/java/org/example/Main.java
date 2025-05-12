package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Gatherer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        List<Student> students = List.of(
                new Student("sid", "a", "dav", 25),
                new Student("RAM", "a", "dav", 30),
                new Student("Sita", "b", "dav", 35)
        );

        Set<Student> collect = new HashSet<>(students);

        Gatherer<Student, ?, Student> gatherer = () -> ((state, element, downstream) -> {
            if(element != null) {
                if(element.age() > 25) {
                    return downstream.push(element);
                }
            }

            return true;
        });

        var v = students.stream()
                .gather(gatherer)
                .toList();

        System.out.println(v);
    }

}

record Student(String name, String section, String school, int age) {

}
