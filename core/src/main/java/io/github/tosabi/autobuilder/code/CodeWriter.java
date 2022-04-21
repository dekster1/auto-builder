package io.github.tosabi.autobuilder.code;

import io.github.tosabi.autobuilder.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.github.tosabi.autobuilder.util.Collect.mapList;

public abstract class CodeWriter {

  public static final String NEW_LINE = System.getProperty("line.separator");

  protected final StringBuilder builder = new StringBuilder();

  /** Inherited only */
  protected CodeWriter() {}

  /** @return All the written builder class code */
  public abstract String write();

  protected void append(boolean newLine, String line, Object... args) {
    builder.append(NEW_LINE);
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof Indent) {
        args[i] = ((Indent) args[i]).emit();
      }
    }
    builder.append(String.format(line, args));
    if (newLine) builder.append(NEW_LINE);
  }

  /** Creates a new {code ClassWriter} instance with the given parameters */
  public static CodeWriter classWriter(String packageName,
                                       ClassName className,
                                       ElementParameters parameters,
                                       Set<MethodSpec> methods) {
    return new ClassWriter(packageName, className, parameters, methods);
  }

  static final class ClassWriter extends CodeWriter {
    private final String packageName;
    private final ClassName className;
    private final ElementParameters parameters;
    private final Set<MethodSpec> methods;

    public ClassWriter(String packageName,
                       ClassName className,
                       ElementParameters parameters,
                       Set<MethodSpec> methods) {
      this.packageName = packageName;
      this.className = className;
      this.parameters = parameters;
      this.methods = methods;
    }

    @Override
    public String write() {
      if (packageName != null) {
        builder.append("package ").append(packageName).append(";").append(NEW_LINE);
      }

      append(true, "public class %s {", className.getFullName());
      for (Parameter parameter : parameters.getParameters()) {
        append(false, "  private %s %s;", parameter.getType(), parameter.getIdentifier());
      }
      builder.append(NEW_LINE);
      append(true, "  public %s() {}", className.getName());
      builder.append(NEW_LINE);

      for (MethodSpec method : methods) {
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
        append(false, "%s%s", entry.getValue(), entry.getKey());
        switch (entry.getValue()) {
          case EXPRESSION:
            appendIn(" {");
            break;
          case STATEMENT:
            append(false, "%s}", Indent.EXPRESSION);
            break;
          default: break;
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
