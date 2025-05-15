
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
3. Combiner (Optional): Merge the states when processing in parallel (parallel stream), for example: Combining 2 lists of the largest integers from different threads
4. Finisher (Optional): Perform final action after all elements are processed like sending the largest number after checking all the elements


## Integrator: 
<img width="1611" alt="image" src="https://github.com/user-attachments/assets/15c5f3f8-b120-4a83-a001-92a2e8385136" />

## Finisher:
<img width="1123" alt="image" src="https://github.com/user-attachments/assets/0c7e2be3-c2c7-4017-bdb3-b8c03796669f" />

Example:
<img width="906" alt="image" src="https://github.com/user-attachments/assets/2724ffb7-6fd8-4898-b4dc-508f44795d59" />





