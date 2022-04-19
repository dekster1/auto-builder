# auto-builder
A simple code generator to generate builder classes at compile time. Builder classes are very useful when you have constructors with too many 
parameters, as they make instantiating a lot easier, however, these classes tend to be laborious and sometimes it is annoying to write so much 
boilerplate code that they usually require. In order not to be forced to do boilerplate code for each builder class, auto-builder offers to generate 
such classes with just one annotation:
```java
public class Person {
  @AutoBuilder
  Person(
          String firstName,
          String lastName,
          int age,
          float height,
          String address
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.height = height;
    this.address = address;
  }
}
```
Now with the `@AutoBuilder` annotation in the constructor, you will be able to use a class named `PersonBuilder` with setter methods generated for each constructor
parameter:
```java
Person person = new PersonBuilder()
        .setFirstName("Daniel")
        .setLastName("Schopenhauer")
        .setAge(26)
        .setHeight(1.75)
        .setAddress("Some address")
        .build();
```
If you don't like the class name or the build method name, you can change them freely using the annotation options. Also, the annotation `@BuilderParameter` allows to change the name of the generated setter method for the parameter. Applying this, we would have:
```java
public class Person {
  @AutoBuilder(className = "PersonCreator", methodName = "create")
  Person(
          @BuilderParameter(methodName = "withFirstName") String firstName, // change the name of the setter method
          String lastName,
          int age,
          float height,
          String address
  ) {}
```

```java
Person person = new PersonCreator()
        .withFirstName("Daniel")
        .setLastName("Schopenhauer")
        .setAge(26)
        .setHeight(1.75)
        .setAddress("Some address")
        .create();
```
        
