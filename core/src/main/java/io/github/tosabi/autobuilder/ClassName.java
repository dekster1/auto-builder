package io.github.tosabi.autobuilder;

import io.github.tosabi.autobuilder.util.Collect;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import java.util.List;
import java.util.Objects;

public final class ClassName {

  /** Default suffix for builder classes */
  static final String SUFFIX = "Builder";

  private final AnnotatedConstructor constructor;

  private ClassName(AnnotatedConstructor constructor) {
    this.constructor = Objects.requireNonNull(constructor, "constructor");
  }

  /** @return A new {@code ClassName} instance. */
  public static ClassName of(AnnotatedConstructor constructor) {
    return new ClassName(constructor);
  }

  /**
   * @return The constructor simple class name or the name specified in {@link AutoBuilder}
   * annotation. In case that a name was not specified, or it was specified and is empty, then
   * a name with the constructor class name and the suffix "Builder" will be generated.
   */
  public String getName() {
    Element element = constructor.getElement();
    String name = constructor.getClassName();

    return name.isEmpty() ? element.getEnclosingElement().getSimpleName() + SUFFIX : name;
  }

  /**
   * @return The full parameterized name of the builder class. If the class is not parameterized
   * then {@link #getName()} will be returned.
   */
  public String getFullName() {
    return isParameterized() ? getName() + getTypeParameters() : getName();
  }

  /** @return A string containing all the class type parameters or either an empty string. */
  public String getTypeParameters() {
    if (!isParameterized()) {
      return "";
    }
    TypeElement element = getTypeElement();

    List<String> parameters = Collect.mapList(element.getTypeParameters(), TypeParameterElement::toString);
    Sequence sequence = new Sequence(parameters, ", ");

    return sequence.unify('<', '>');
  }

  /** @return {@code true} if the annotated constructor class is parameterized. */
  public boolean isParameterized() {
    return (!getTypeElement().getTypeParameters().isEmpty());
  }

  private TypeElement getTypeElement() {
    return (TypeElement) constructor.getElement().getEnclosingElement();
  }

  @Override
  public String toString() {
    return getName();
  }
}