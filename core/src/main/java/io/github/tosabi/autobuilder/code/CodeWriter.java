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
  public abstract String encode();

  protected void write(boolean newLine, String line, Object... args) {
    builder.append(NEW_LINE);
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof MethodLine) {
        args[i] = ((MethodLine) args[i]).indent();
      }
    }
    builder.append(String.format(line, args));
    if (newLine) builder.append(NEW_LINE);
  }

  protected void writeIn(Object... args) {
    for (Object arg : args) {
      builder.append(arg);
    }
  }

  /** Creates a new {code ClassWriter} instance with the given parameters */
  public static CodeWriter classWriter(String packageName,
                                       TypeInfo typeInfo,
                                       ElementParameters parameters,
                                       Set<MethodSpec> methods) {
    return new ClassWriter(packageName, typeInfo, parameters, methods);
  }

  static final class ClassWriter extends CodeWriter {
    private final String packageName;
    private final TypeInfo typeInfo;
    private final ElementParameters parameters;
    private final Set<MethodSpec> methods;

    public ClassWriter(String packageName,
                       TypeInfo typeInfo,
                       ElementParameters parameters,
                       Set<MethodSpec> methods) {
      this.packageName = packageName;
      this.typeInfo = typeInfo;
      this.parameters = parameters;
      this.methods = methods;
    }

    @Override
    public String encode() {
      if (packageName != null) {
        writeIn("package ", packageName, ";");
        builder.append(NEW_LINE);
      }

      write(true, "public class %s {", typeInfo.getFullName());
      for (Parameter parameter : parameters.getParameters()) {
        write(false, "  private %s %s;", parameter.getType(), parameter.getIdentifier());
      }
      builder.append(NEW_LINE);
      write(true, "  public %s() {}", typeInfo.getName());
      builder.append(NEW_LINE);

      for (MethodSpec method : methods) {
        CodeWriter methodWriter = new MethodWriter(method);
        builder.append(methodWriter.encode());
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
    public String encode() {
      Sequence modifiers = new Sequence(getModifiers(), " ");
      Sequence parameters = new Sequence(getParameters(), ", ");

      builder.append("  ");
      builder.append(modifiers.unify());
      writeIn(" ", spec.getReturnType(), " ");
      writeIn(spec.getMethodName(), "(", parameters.unify(), ") {");

      for (Map.Entry<String, MethodLine> entry : spec.getStatements().entrySet()) {
        write(false, "%s%s", entry.getValue(), entry.getKey());
        char end = entry.getValue().end;
        writeIn(end == '{' ? " " + end : end);
        if (entry.getValue() == MethodLine.STATEMENT) {
          write(false, "%s}", MethodLine.EXPRESSION);
        }
      }
      write(true, "  }");
      return builder.toString();
    }

    private List<String> getModifiers() {
      return mapList(new ArrayList<>(spec.getModifiers()), Modifier::toString);
    }

    private List<String> getParameters() {
      List<String> parameters = new ArrayList<>();
      for (Map.Entry<String, String> entry : spec.getParameters().entrySet()) {
        parameters.add(entry.getKey() + " " + entry.getValue());
      }
      return parameters;
    }
  }
}
