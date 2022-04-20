package io.github.tosabi.autobuilder;

import io.github.tosabi.autobuilder.code.CodeWriter;
import io.github.tosabi.autobuilder.code.Sequence;
import io.github.tosabi.autobuilder.util.Collect;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BuilderGenerator {

  static final String SUFFIX = "Builder";

  private final AnnotatedConstructor constructor;

  private final Element element;
  private final String className;
  private final ElementParameters elementParameters;

  public BuilderGenerator(AnnotatedConstructor constructor) {
    this.constructor = constructor;
    this.element = constructor.getElement();
    this.elementParameters = ElementParameters.of(constructor.getConstructor());

    String name = constructor.getClassName();
    this.className = name.isEmpty() ? element.getEnclosingElement().getSimpleName() + SUFFIX : name;
  }

  public String getClassName() {
    return className;
  }

  /** Generates all the builder class code */
  public String generate() {
    Set<MethodSpec> methods = new LinkedHashSet<>();
    String packageName = getPackageName(element);

    for (Parameter parameter : elementParameters.getParameters()) {
      String identifier = parameter.getIdentifier();
      methods.add(MethodSpec.methodBuilder()
              .addModifier(Modifier.PUBLIC)
              .name(parameter.getMethodName())
              .returns(className)
              .addParameter(parameter)
              .addStatement("this.%s = %s;", identifier, identifier)
              .addStatement("return this;")
              .create()
      );
    }

    TypeSpec typeSpec = TypeSpec.newSpec()
            .packageName(packageName)
            .className(className)
            .parameters(elementParameters)
            .addMethods(methods)
            .addMethod(buildMethod())
            .create();

    return CodeWriter.classWriter(packageName, typeSpec).write();
  }


  /** Generates the builder class "build()" method */
  private MethodSpec buildMethod() {
    String elementClass = element.getEnclosingElement().getSimpleName().toString();
    String methodName = constructor.getMethodName();

    List<String> parameterList = Collect.mapList(
            new ArrayList<>(elementParameters.getParameters()),
            Parameter::getIdentifier
    );

    return MethodSpec.methodBuilder()
            .addModifier(Modifier.PUBLIC)
            .name(methodName.isEmpty() ? "build" : methodName)
            .returns(elementClass)
            .addStatement("return new %s(%s);",
                    elementClass,
                    new Sequence(parameterList, ", ").unify()
            ).create();
  }

  private String getPackageName(Element element) {
    Element enclosing = element.getEnclosingElement();
    String name = enclosing.getSimpleName().toString();

    return enclosing.asType().toString().replace("." + name, "");
  }
}