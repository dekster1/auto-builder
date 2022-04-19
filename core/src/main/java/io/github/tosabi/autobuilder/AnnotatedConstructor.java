package io.github.tosabi.autobuilder;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

final class AnnotatedConstructor {

  private final Element element;
  private final ExecutableElement constructor;

  private final AutoBuilder annotation;

  public AnnotatedConstructor(Element element) {
    this.element = element;
    this.constructor = (ExecutableElement) element;
    this.annotation = constructor.getAnnotation(AutoBuilder.class);

  }

  public Element getElement() {
    return element;
  }

  public ExecutableElement getConstructor() {
    return constructor;
  }

  public String getClassName() {
    return annotation.className();
  }

  public String getMethodName() {
    return annotation.methodName().isEmpty() ? "build" : annotation.methodName();
  }
}