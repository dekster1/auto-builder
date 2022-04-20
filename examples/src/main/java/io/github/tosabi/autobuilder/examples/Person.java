package io.github.tosabi.autobuilder.examples;

import io.github.tosabi.autobuilder.AutoBuilder;
import io.github.tosabi.autobuilder.BuilderParameter;

import java.util.List;

public class Person {

  String firstName;
  String lastName;
  int age;
  float height;
  String address;

  @AutoBuilder
  Person(
          @BuilderParameter(nullable = false) String firstName,
          @BuilderParameter(nullable = false) String lastName,
          int age,
          float height,
          @BuilderParameter(nullable = false) String address,
          List<String> jobs
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.height = height;
    this.address = address;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getAge() {
    return age;
  }

  public float getHeight() {
    return height;
  }

  public String getAddress() {
    return address;
  }

  // Class content...

}