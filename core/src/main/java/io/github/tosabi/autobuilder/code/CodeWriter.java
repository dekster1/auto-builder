package io.github.tosabi.autobuilder.code;

public abstract class CodeWriter {

  public static final String NEW_LINE = System.getProperty("line.separator");

  protected final StringBuilder builder = new StringBuilder();

  protected CodeWriter() {}

  public abstract String write();

  protected void append(boolean newLine, Object... args) {
    builder.append(NEW_LINE);
    for (Object arg : args) {
      builder.append(arg);
    }
    if (newLine) builder.append(NEW_LINE);
  }

  protected void appendln(Object... args) {
    for (Object arg : args) {
      builder.append(arg);
    }
  }

  protected void newLine() {
    builder.append(NEW_LINE);
  }
}
