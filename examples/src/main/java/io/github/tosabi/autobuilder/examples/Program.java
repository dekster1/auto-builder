package io.github.tosabi.autobuilder.examples;

public class Program {

  public static void main(String[] args) {
    Person person = new PersonBuilder()
            .setFirstName("John")
            .setLastName("Schopenhauer")
            .setAge(30)
            .setAddress("Address #XXXX 551")
            .build();
    System.out.println("Hi my name is " + person.getFirstName() + " " + person.getLastName());
  }
}
