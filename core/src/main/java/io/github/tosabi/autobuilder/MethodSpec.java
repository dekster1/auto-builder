package io.github.tosabi.autobuilder;

import io.github.tosabi.autobuilder.code.Indent;

import javax.lang.model.element.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class MethodSpec {

  private final String methodName;
  private final String returnType;

  private final Set<Modifier> modifiers;
  private final Set<Parameter> parameters;
  private final Map<String, Indent> statements;

  private MethodSpec(Specification specification) {
    this.methodName = specification.methodName;
    this.returnType = specification.returnType;
    this.modifiers = specification.modifiers;
    this.parameters = specification.parameters;
    this.statements = specification.statements;
  }

  public String getMethodName() {
    return methodName;
  }

  public String getReturnType() {
    return returnType;
  }

  public Set<Modifier> getModifiers() {
    return modifiers;
  }

  public Set<Parameter> getParameters() {
    return parameters;
  }

  public Map<String, Indent> getStatements() {
    return statements;
  }

  public static Specification methodBuilder() {
    return new Specification();
  }

  public static class Specification {
    private String methodName;
    private String returnType;

    private final Set<Modifier> modifiers = new LinkedHashSet<>();
    private Set<Parameter> parameters = new LinkedHashSet<>();
    private final Map<String, Indent> statements = new LinkedHashMap<>();

    private Specification() {}

    public Specification name(String methodName) {
      this.methodName = methodName;
      return this;
    }

    public Specification addModifier(Modifier modifier) {
      modifiers.add(modifier);
      return this;
    }

    public Specification addParameter(Parameter parameter) {
      parameters.add(parameter);
      return this;
    }

    public Specification setParameters(Set<Parameter> parameters) {
      this.parameters = parameters;
      return this;
    }

    public Specification addStatement(String statement, Object... args) {
      statements.put(String.format(statement, args), Indent.BODY);
      return this;
    }

    public Specification addFlowStatement(String control, String statement, Object... args) {
      statements.put(String.format(control, args), Indent.BODY);
      statements.put(statement, Indent.FLOW);
      return this;
    }

    public Specification returns(String returnType) {
      this.returnType = returnType;
      return this;
    }

    public MethodSpec create() {
      return new MethodSpec(this);
    }
  }
}
