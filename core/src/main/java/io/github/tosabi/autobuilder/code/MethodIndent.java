package io.github.tosabi.autobuilder.code;

public enum MethodIndent {
  BODY(4), EXPRESSION(4), STATEMENT(6);

  final int sp;

  MethodIndent(int sp) {
    this.sp = sp;
  }

  public String emit() {
    StringBuilder builder = new StringBuilder(sp);
    for (int i = 0; i < sp; i++) {
      builder.append(" ");
    }
    return builder.toString();
  }
}