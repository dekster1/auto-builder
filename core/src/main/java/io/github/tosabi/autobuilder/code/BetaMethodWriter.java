package io.github.tosabi.autobuilder.code;

import io.github.tosabi.autobuilder.MethodSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static io.github.tosabi.autobuilder.util.Collect.mapList;

final class BetaMethodWriter extends CodeWriter {

  private final MethodSpec spec;

  public BetaMethodWriter(MethodSpec spec) {
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
    appendln(" ", spec.getReturnType(), " ");
    // method parameters
    appendln(spec.getMethodName(), "(", new Sequence(parameters, ", ").unify(), ") {");

    for (String statement : spec.getStatements()) {
      append(false, "    ", statement);
    }
    append(true, "  }");
    return builder.toString();
  }
}
