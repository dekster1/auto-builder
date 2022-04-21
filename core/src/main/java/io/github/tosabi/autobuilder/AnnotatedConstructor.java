package io.github.tosabi.autobuilder;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import java.util.Objects;
import java.util.Set;

/**
 * Provides information of an annotated constructor.
 */
final class AnnotatedConstructor {

  private final Element element;
  private final Elements elementUtils;
  private final ExecutableElement constructor;

  private final AutoBuilder annotation;

  public AnnotatedConstructor(Element element, Elements elementUtils) {
    this.element = checkElement(element);
    this.elementUtils = elementUtils;
    this.constructor = (ExecutableElement) element;
    this.annotation = constructor.getAnnotation(AutoBuilder.class);
  }

  public Element getElement() {
    return element;
  }

  public Elements getElementUtils() {
    return elementUtils;
  }

  public ExecutableElement getConstructor() {
    return constructor;
  }

  public Set<Modifier> getModifiers() {
    return element.getModifiers();
  }

  public boolean hasPrivateAccess() {
    return getModifiers().contains(Modifier.PRIVATE);
  }

  public String getClassName() {
    return annotation.className();
  }

  public String getMethodName() {
    return annotation.methodName();
  }

  private Element checkElement(Element element) {
    Objects.requireNonNull(element, "element");
    if (element.getKind() != ElementKind.CONSTRUCTOR) {
      throw new IllegalArgumentException();
    }
    ExecutableElement executable = (ExecutableElement) element;
    if (executable.getAnnotation(AutoBuilder.class) == null) {
      throw new IllegalStateException("Constructor is not annotated with @AutoBuilder");
    }
    return element;
  }
}