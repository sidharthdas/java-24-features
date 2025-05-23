
# Java 24 Features

# Stream Gatherer:

            A Gatherer is a component that:
                        1. Takes item from stream
                        2. Transform or filter
                        3. Pass the item to the next 
                        4. To develop your custom intermediate operation

### Gatherer is build upon 2 elements: ``` Gatherer interface``` & ``` Gatherers factory class ```

Gatherer Example:

Using Lambda Expresion:
```
Gatherer<Student, ?, Student> gatherer = () -> ((state, element, downstream) -> {
            if(element != null) {
                if(element.age() > 25) {
                    return downstream.push(element);
                }
            }

            return true;
        });
```
Using the Gatherer of method: (Gatherer.of() -> Parallel Execution & Gatherer.ofSequential() -> Sequential Execution)
```
Gatherer<Student, ?, Student> g = Gatherer.of((state, element, downstream) -> {
            if(element != null && element.age() > 25) {
                return downstream.push(element);
            }
            return true;
        });
```

4 functions define Gatherer:

1. Initializer (Optional): Create a state to track the information, for example, initialize a list to get the largest number so far
2. Integrator (Mandatory): To do all the filter & map operations
3. Combiner (Optional): Merge the states when processing in parallel (parallel stream), for example: Combining 2 lists of the largest integers from different threads
4. Finisher (Optional): Perform final action after all elements are processed, like sending the largest number after checking all the elements


## Integrator: 
<img width="1611" alt="image" src="https://github.com/user-attachments/assets/15c5f3f8-b120-4a83-a001-92a2e8385136" />

```
List<Student> students = List.of(
                new Student("sid", "a", "dav", 25),
                new Student("RAM", "a", "dav", 30),
                new Student("Sita", "b", "dav", 35)
        );

        Gatherer<Student, ?, Student> g = Gatherer.of((state, element, downstream) -> { // Integrator
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
```

## Finisher:
<img width="1123" alt="image" src="https://github.com/user-attachments/assets/0c7e2be3-c2c7-4017-bdb3-b8c03796669f" />

Example:

<img width="906" alt="image" src="https://github.com/user-attachments/assets/2724ffb7-6fd8-4898-b4dc-508f44795d59" />

```
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
                //If any leftover is present
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
```

## Initializer:

```
Gatherer<Integer, AtomicInteger, Integer> g = Gatherer.ofSequential(AtomicInteger::new,
                ((state, number, downstream) -> {
                        var val = state.addAndGet(number);
                        return downstream.push(val);
                }));

        var l = Stream.of(10,20,30,40)
                .gather(g)
                .toList();

        System.out.println(l);

```
## Finisher:

<img width="1114" alt="image" src="https://github.com/user-attachments/assets/f784823c-378f-4ed0-a5c9-2f959aafd071" />

<img width="867" alt="image" src="https://github.com/user-attachments/assets/cb1ff455-5003-4a4d-8d68-04e7999175bc" />


```
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
```

## Combiner:

### Parallel Gatherer:

<img width="1153" alt="image" src="https://github.com/user-attachments/assets/fb4d1d4c-801f-43e7-9325-d4e5c4a288f3" />

```
Gatherer<Integer, AtomicInteger, Integer> gatherer = Gatherer.of(
                AtomicInteger::new, // Initializer
                ((state, element, downstream) -> { //Integrator
                    return downstream.push(state.addAndGet(element));
                }),
                (s1, s2) -> { // Combiner
                    s1.addAndGet(s2.get());
                    return s1;
                },
                (broker, downstream) -> { //Finisher
                    downstream.push(broker.get());
                }
        );

        var v = List.of(10, 20, 30, 40, 50, 60)
                .stream()
                .gather(gatherer)
                .toList();

        System.out.println(v);
```

## Interrupt Gatherer
<img width="1126" alt="image" src="https://github.com/user-attachments/assets/11cbb360-b7fb-4ab0-89be-55534d80b77d" />

#### Important Point:

<img width="596" alt="image" src="https://github.com/user-attachments/assets/b03924f7-0b53-4b61-b5e7-eedead62e8a8" />


```
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
```

## Combining 2 or more Gatherer: (using andThen() method)

```
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

```







