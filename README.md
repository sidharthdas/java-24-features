
# Java 24 Features

## Stream Gatherer:
```
A Gatherer is a component that:
Takes item from stream
Transform or filter
Pass the item to the next 
To develop your own custom intermediate operation
```
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
3. Combiner (Optional): Merge the states when processing in parallel (parallel stream), for example: Combining 2 list of the largest integers from different threads
4. Finisher (Optional)


## Integrator: 
<img width="1611" alt="image" src="https://github.com/user-attachments/assets/15c5f3f8-b120-4a83-a001-92a2e8385136" />



