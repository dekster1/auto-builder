package io.github.tosabi.autobuilder.code;

public enum MethodLine {
  BODY(';', 4), EXPRESSION('{', 4), STATEMENT(';', 6);

  final char end;
  final int indent;

  MethodLine(char end, int indent) {
    this.end = end;
    this.indent = indent;
  }

  public String indent() {
    StringBuilder builder = new StringBuilder(indent);
    for (int i = 0; i < indent; i++) {
      builder.append(" ");
    }
    return builder.toString();
  }
}