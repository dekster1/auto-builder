package io.github.tosabi.autobuilder;

import javax.lang.model.element.Element;

public class AutoBuilderException extends Exception {
  
  private final Element element;

  public AutoBuilderException(Element element, String message, Object... args) {
    super(String.format(message, args));
    this.element = element;
  }

  public Element getElement() {
    return element;
  }
}
