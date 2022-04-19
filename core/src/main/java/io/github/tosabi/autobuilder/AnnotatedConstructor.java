package io.github.tosabi.autobuilder;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeParameterElement;
import java.util.List;

public final class AnnotatedConstructor {

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

  public List<? extends TypeParameterElement> getTypeParameters() {
    return constructor.getTypeParameters();
  }

  public String getClassName() {
    return annotation.className();
  }

  public String getMethodName() {
    String methodName = annotation.methodName();
    if (methodName.isEmpty()) {
      throw new IllegalArgumentException("Invalid method name");
    }
    return methodName;
  }
}