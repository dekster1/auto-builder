package io.github.tosabi.autobuilder;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.Set;

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
}