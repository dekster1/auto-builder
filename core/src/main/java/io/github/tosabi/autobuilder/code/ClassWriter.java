package io.github.tosabi.autobuilder.code;

import io.github.tosabi.autobuilder.MethodSpec;
import io.github.tosabi.autobuilder.Parameter;
import io.github.tosabi.autobuilder.TypeSpec;

public final class ClassWriter extends CodeWriter {

  private final TypeSpec spec;
  private final String className, packageName;

  public ClassWriter(String packageName, TypeSpec spec) {
    this.packageName = packageName;
    this.spec = spec;
    this.className = spec.getClassName();
  }

  @Override
  public String write() {
    if (packageName != null) {
      builder.append("package ").append(packageName).append(";").append(NEW_LINE);
    }
    append(true, "public class ", className, " {");
    for (Parameter parameter : spec.getParameters().getParameters()) {
      append(false, "  private ", parameter.getType(), " ", parameter.getIdentifier(), ";");
    }
    builder.append(NEW_LINE);
    append(true, "  public ", className, "() {}");
    builder.append(NEW_LINE);

    for (MethodSpec method : spec.getMethods()) {
      CodeWriter methodWriter = new MethodWriter(method);
      builder.append(methodWriter.write());
    }

    return builder.append("}").toString();
  }
}
