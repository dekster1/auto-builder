package io.github.tosabi.autobuilder;

public enum Style {

  SETTER("set"),

  COLLECTION("addTo");

  private final String prefix;

  Style(String prefix) {
    this.prefix = prefix;
  }

  public String getPrefix() {
    return prefix;
  }
}