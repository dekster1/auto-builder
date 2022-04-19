package io.github.tosabi.autobuilder.code;

final class MethodWriter {

  private final StringBuilder builder = new StringBuilder();
  private boolean firstParam = true;
  private boolean forInterface;

  public MethodWriter forInterface() {
    forInterface = true;
    return this;
  }

  public MethodWriter defineSignature(String accessModifier, boolean asStatic, String returnType) {
    builder.append(forInterface ? "" : accessModifier)
            .append(asStatic? " static ": " ")
            .append(returnType)
            .append(" ");
    return this;
  }

  public MethodWriter name(String name) {
    builder.append(name)
            .append("(");
    return this;
  }

  public MethodWriter addParam(String type, String identifier) {
    if (!firstParam) {
      builder.append(", ");
    } else {
      firstParam = false;
    }
    builder.append(type)
            .append(" ")
            .append(identifier);

    return this;
  }

  public MethodWriter defineBody(String body) {
    if (forInterface) {
      throw new IllegalArgumentException("Interface cannot define a body");
    }
    builder.append(") {")
            .append(ClassWriter.LINE_BREAK)
            .append("   ")
            .append(body)
            .append(ClassWriter.LINE_BREAK)
            .append("    return this;")
            .append(ClassWriter.LINE_BREAK)
            .append("  }")
            .append(ClassWriter.LINE_BREAK);
    return this;
  }

  public String end() {
    return forInterface ? ");" : builder.toString();
  }
}
