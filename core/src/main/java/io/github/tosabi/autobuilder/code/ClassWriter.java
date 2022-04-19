package io.github.tosabi.autobuilder.code;

import io.github.tosabi.autobuilder.Parameter;

import java.util.*;

public final class ClassWriter {

  public static final String LINE_BREAK = System.getProperty("line.separator");

  private final StringBuilder builder = new StringBuilder();

  private String className;
  private final Map<String, String> fields = new LinkedHashMap<>();


  public void definePackage(String packageName) {
    if (packageName != null) {
      builder.append("package ")
              .append(packageName)
              .append(";")
              .append(LINE_BREAK);
    }
  }

  public void addImport(String importPackage) {
    builder.append("import ")
            .append(importPackage)
            .append(";");
  }

  public void defineClass(String startPart, String name) {
    className = name;
    builder.append(LINE_BREAK).append(LINE_BREAK)
            .append(startPart)
            .append(" ")
            .append(name)
            .append(" {")
            .append(LINE_BREAK);
  }

  public void addFields(Set<Parameter> set) {
    for (Parameter fieldInfo : set) {
      addField(fieldInfo.getType(), fieldInfo.getIdentifier());
    }
  }

  public void addField(String type, String identifier) {
    fields.put(identifier, type);
    builder.append("  private ")
            .append(type)
            .append(" ")
            .append(identifier)
            .append(";")
            .append(LINE_BREAK);
  }


  public void addPublicConstructor() {
    builder.append(LINE_BREAK)
            .append("  public ")
            .append(className)
            .append("() {}")
            .append(LINE_BREAK);
  }

  public void addMethod(MethodWriter method) {
    builder.append(LINE_BREAK)
            .append(method.end())
            .append(LINE_BREAK);
  }

  public void addNestedClass(ClassWriter jClass) {
    builder.append(LINE_BREAK);
    builder.append(jClass.write());
    builder.append(LINE_BREAK);
  }

  public void createSetter(Parameter parameter, String className) {
    String name = parameter.getIdentifier();

    addMethod(new MethodWriter()
            .defineSignature("  public", false, className)
            .name(parameter.getMethodName(), parameter.getType(), parameter.getIdentifier())
            .defineBody(" this." + name + " = " + name + ";", true));
  }

  public void createBuildMethod(String type, String methodName, List<Parameter> parameters) {
    StringBuilder params = new StringBuilder();
    int index = 0;
    for (Parameter parameter : parameters) {
      params.append(parameter.getIdentifier());
      if (++index < parameters.size()) {
        params.append(", ");
      }
    }

    addMethod(new MethodWriter()
            .defineSignature("  public", false, type)
            .name(methodName)
            .defineBody(" return new " + type + "(" + params + ");", false));
  }

  public String write() {
    builder.append(LINE_BREAK).append("}");
    return builder.toString();
  }
}
