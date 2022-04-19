# auto-builder
A simple code generator to generate builder classes at compile time. Builder classes are very useful when you have constructors with too many 
parameters, as they make instantiating a lot easier, however, these classes tend to be laborious and sometimes it is annoying to write so much 
boilerplate code that they usually require. In order not to be forced to do boilerplate code for each builder class, auto-builder offers to generate 
such classes with just one annotation:
```java
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
```
