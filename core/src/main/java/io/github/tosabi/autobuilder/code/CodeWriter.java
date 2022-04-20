package io.github.tosabi.autobuilder.code;

import io.github.tosabi.autobuilder.MethodSpec;
import io.github.tosabi.autobuilder.Parameter;
import io.github.tosabi.autobuilder.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.tosabi.autobuilder.util.Collect.mapList;

public abstract class CodeWriter {

  public static final String NEW_LINE = System.getProperty("line.separator");

  protected final StringBuilder builder = new StringBuilder();

  /** Inherited only */
  protected CodeWriter() {}

  /** @return All the written builder class code */
  public abstract String write();

  protected void append(boolean newLine, Object... args) {
    builder.append(NEW_LINE);
    for (Object arg : args) {
      if (arg instanceof Indent) {
        Indent indent = (Indent) arg;
        builder.append(indent.emit());
      } else {
        builder.append(arg);
      }
    }
    if (newLine) builder.append(NEW_LINE);
  }

  /** Creates a new {code ClassWriter} instance with the given parameters */
  public static CodeWriter classWriter(String packageName, TypeSpec spec) {
    return new ClassWriter(packageName, spec);
  }

  static final class ClassWriter extends CodeWriter {
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

  static final class MethodWriter extends CodeWriter {
    private final MethodSpec spec;

    public MethodWriter(MethodSpec spec) {
      this.spec = spec;
    }

    @Override
    public String write() {
      List<String> modifiers = mapList(new ArrayList<>(spec.getModifiers()), Modifier::toString);
      List<String> parameters = mapList(new ArrayList<>(
              spec.getParameters()), parameter -> parameter.getType() + " " + parameter.getIdentifier()
      );

      builder.append("  ");
      builder.append(new Sequence(modifiers, " ").unify());
      appendIn(" ", spec.getReturnType(), " ");
      appendIn(spec.getMethodName(), "(", new Sequence(parameters, ", ").unify(), ") {");

      for (Map.Entry<String, Indent> entry : spec.getStatements().entrySet()) {
        if (entry.getValue() == Indent.BODY) {
          append(false, Indent.BODY, entry.getKey());
        } else {
          append(false, Indent.FLOW, entry.getKey());
        }
      }
      append(true, "  }");
      return builder.toString();
    }

    private void appendIn(Object... args) {
      for (Object arg : args) {
        builder.append(arg);
      }
    }
  }
}
