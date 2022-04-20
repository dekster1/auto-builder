package io.github.tosabi.autobuilder;

import java.util.LinkedHashSet;
import java.util.Set;

public class TypeSpec {

  private final String name;
  private final String packageName;
  private final ElementParameters parameters;
  private final Set<MethodSpec> methods;

  private TypeSpec(Specification specification) {
    this.name = specification.name;
    this.packageName = specification.packageName;
    this.parameters = specification.parameters;
    this.methods = specification.methods;
  }

  public String getClassName() {
    return name;
  }

  public String getPackageName() {
    return packageName;
  }

  public ElementParameters getParameters() {
    return parameters;
  }

  public Set<MethodSpec> getMethods() {
    return methods;
  }

  public static Specification newSpec() {
    return new Specification();
  }

  public static class Specification {
    String name;
    String packageName;
    ElementParameters parameters;
    Set<MethodSpec> methods = new LinkedHashSet<>();

    public Specification className(String name) {
      this.name = name;
      return this;
    }

    public Specification packageName(String packageName) {
      this.packageName = packageName;
      return this;
    }

    public Specification parameters(ElementParameters parameters) {
      this.parameters = parameters;
      return this;
    }

    public Specification addMethod(MethodSpec method) {
      methods.add(method);
      return this;
    }

    public Specification addMethods(Set<MethodSpec> methods) {
      this.methods.addAll(methods);
      return this;
    }

    public TypeSpec create() {
      return new TypeSpec(this);
    }
  }
}