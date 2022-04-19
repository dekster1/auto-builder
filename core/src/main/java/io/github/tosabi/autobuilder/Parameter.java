package io.github.tosabi.autobuilder;

import java.util.Objects;
import java.util.Optional;

public class Parameter {

  private final String type, identifier;
  private final BuilderParameter annotation;

  public Parameter(String type, String identifier, BuilderParameter annotation) {
    this.type = type;
    this.identifier = identifier;
    this.annotation = annotation;
  }

  public String getType() {
    return type;
  }

  public String getIdentifier() {
    return identifier;
  }

  public boolean isIgnored() {
    return annotation != null && annotation.ignore();
  }

  public String getMethodName() {
    String name = annotation != null ? annotation.methodName() : "";

    return name.isEmpty() ? "set" +
            Character.toUpperCase(identifier.charAt(0)) +
            identifier.substring(1) : name;
  }

  public Optional<BuilderParameter> getAnnotation() {
    return Optional.ofNullable(annotation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, identifier);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null || o.getClass() != getClass()) {
      return false;
    }
    Parameter other = (Parameter) o;
    return Objects.equals(type, other.type)
            && Objects.equals(identifier, other.identifier);
  }

  @Override
  public String toString() {
    return "FieldInfo{type=" + type + ", identifier=" + identifier + "}";
  }
}
