package io.github.tosabi.autobuilder;

import io.github.tosabi.autobuilder.code.BetaClassWriter;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

public class BuilderGenerator {

  static final String SUFFIX = "Builder";

  private final AnnotatedConstructor constructor;
  private final String className;

  public BuilderGenerator(AnnotatedConstructor constructor) {
    this.constructor = constructor;
    this.className = constructor.getElement().getEnclosingElement().getSimpleName() + SUFFIX;
  }

  public String getClassName() {
    return className;
  }

  public String generate() {
    Element element = constructor.getElement();
    String packageName = getPackageName(element);
    ElementParameters elementParameters = ElementParameters.of(constructor.getConstructor());
    Set<MethodSpec> methods = new LinkedHashSet<>();

    for (Parameter parameter : elementParameters.getParameters()) {
      methods.add(MethodSpec.methodBuilder()
              .addModifier(Modifier.PUBLIC)
              .name(parameter.getMethodName())
              .returns(className)
              .addParameter(parameter)
              .addStatement("this.%s = %s;",
                      parameter.getIdentifier(), parameter.getIdentifier()
              )
              .addStatement("return this;")
              .create()
      );
    }

    TypeSpec typeSpec = TypeSpec.newSpec()
            .packageName(packageName)
            .className(className)
            .parameters(elementParameters)
            .addMethods(methods)
            .create();

    return new BetaClassWriter(packageName, typeSpec).write();
  }

  private String getPackageName(Element element) {
    Element enclosing = element.getEnclosingElement();
    String name = enclosing.getSimpleName().toString();

    return enclosing.asType().toString().replace("." + name, "");
  }
}