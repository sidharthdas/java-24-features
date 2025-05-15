
# Java 24 Features

## Stream Gatherer:
```
A Gatherer is a component that:
Takes item from stream
Transform or filter
Pass item to next 
To develop own custom intermidiate operation
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
Using Gatherer of method:
```
Gatherer<Student, ?, Student> g = Gatherer.of((state, element, downstream) -> {
            if(element != null && element.age() > 25) {
                return downstream.push(element);
            }
            return true;
        });
```


