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

  /** @return The parameter full-qualified type. */
  public String getType() {
    return type;
  }

  /** The parameter name, this name will be used to name fields and methods. */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Gets the parameter method name. If the parameter is not annotated with
   * {@link BuilderParameter} a name will be auto generated. Same case if the
   * parameter is annotated but the specified name is empty.
   *
   * @return the parameter method name
   */
  public String getMethodName() {
    String name = annotation != null ? annotation.methodName() : "";

    // generate if empty
    return name.isEmpty() ? "set" +
            Character.toUpperCase(identifier.charAt(0)) +
            identifier.substring(1) : name;
  }

  /** @return {@code true} if the element can be nullable. */
  public boolean isNullable() {
    return isPrimitive() || annotation == null || annotation.nullable();
  }

  /** @return an {@link Optional} containing the annotation if present or either an empty one. */
  public Optional<BuilderParameter> getAnnotation() {
    return Optional.ofNullable(annotation);
  }

  private boolean isPrimitive() {
    return type.equals("double") || type.equals("float") ||
            type.equals("boolean") || type.equals("byte") ||
            type.equals("int") || type.equals("char") ||
            type.equals("long") || type.equals("short");
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
    return "Parameter{type=" + type +
            ", identifier=" + identifier +
            ", annotated=" + getAnnotation().isPresent() +
            "}";
  }
}