package io.github.tosabi.autobuilder;

import io.github.tosabi.autobuilder.code.MethodIndent;

import javax.lang.model.element.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class MethodSpec {

  private final String methodName;
  private final String returnType;

  private final Set<Modifier> modifiers;
  private final Set<Parameter> parameters;
  private final Map<String, MethodIndent> statements;

  private MethodSpec(Builder builder) {
    this.methodName = builder.methodName;
    this.returnType = builder.returnType;
    this.modifiers = builder.modifiers;
    this.parameters = builder.parameters;
    this.statements = builder.statements;
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

  public Map<String, MethodIndent> getStatements() {
    return statements;
  }

  public static Builder methodBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String methodName;
    private String returnType;

    private final Set<Modifier> modifiers = new LinkedHashSet<>();
    private final Set<Parameter> parameters = new LinkedHashSet<>();
    private final Map<String, MethodIndent> statements = new LinkedHashMap<>();

    private Builder() {}

    public Builder name(String methodName) {
      requireNonNull(methodName, "methodName");
      this.methodName = methodName;
      return this;
    }

    public Builder addModifier(Modifier modifier) {
      requireNonNull(modifier, "modifier");
      modifiers.add(modifier);
      return this;
    }

    public Builder addParameter(Parameter parameter) {
      requireNonNull(parameter, "parameter");
      parameters.add(parameter);
      return this;
    }

    public Builder addStatement(String statement, Object... args) {
      statements.put(String.format(statement, args), MethodIndent.BODY);
      return this;
    }

    public Builder addFlowControl(String condition, String[] statements, Object... args) {
      this.statements.put(String.format(condition, args), MethodIndent.EXPRESSION);
      for (String statement : statements) {
        this.statements.put(String.format(statement, args), MethodIndent.STATEMENT);
      }
      return this;
    }

    public Builder returns(String returnType) {
      requireNonNull(returnType, "returnType");
      this.returnType = returnType;
      return this;
    }

    public MethodSpec create() {
      return new MethodSpec(this);
    }
  }
}
