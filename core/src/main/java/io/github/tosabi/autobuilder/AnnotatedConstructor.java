package io.github.tosabi.autobuilder;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.Set;

final class AnnotatedConstructor {

  private final Element element;
  private final Elements elementUtils;
  private final ExecutableElement constructor;

  private final AutoBuilder annotation;

  public AnnotatedConstructor(Element element, Elements elementUtils) {
    this.element = element;
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

  public TypeElement getTypeElement() {
    return (TypeElement) element.getEnclosingElement();
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