package io.github.tosabi.autobuilder;

import io.github.tosabi.autobuilder.code.CodeWriter;
import io.github.tosabi.autobuilder.code.Sequence;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BuilderGenerator {

  private final AnnotatedConstructor constructor;

  private final Element element;
  private final ClassName className;
  private final ElementParameters elementParameters;

  public BuilderGenerator(AnnotatedConstructor constructor) {
    this.constructor = constructor;
    this.element = constructor.getElement();
    this.elementParameters = ElementParameters.of(constructor.getConstructor());
    this.className = ClassName.of(constructor);
  }

  /** @return The builder class simple name. */
  public String getClassName() {
    return className.getName();
  }

  /**
   * Generates all the builder class code.
   */
  public String generate() {
    Set<MethodSpec> methods = new LinkedHashSet<>();
    String packageName = getPackageName(element);

    for (Parameter parameter : elementParameters.getParameters()) {
      String identifier = parameter.getIdentifier();
      methods.add(MethodSpec.methodBuilder()
              .addModifier(Modifier.PUBLIC)
              .name(parameter.getMethodName())
              .returns(className.getFullName())
              .addParameter(parameter)
              .addStatement("this.%s = %s;", identifier, identifier)
              .addStatement("return this;")
              .create()
      );
    }

    methods.add(buildMethod());

    return CodeWriter.classWriter(packageName, className, elementParameters, methods).write();
  }

  /**
   * Generates the builder class "build()" method.
   */
  private MethodSpec buildMethod() {
    List<String> parameterList = new ArrayList<>();
    String methodName = constructor.getMethodName();

    String elementClass = element.getEnclosingElement().getSimpleName().toString();
    String returnType = className.isParameterized() ?
            elementClass + className.getTypeParameters() : elementClass;

    MethodSpec.Builder builder = MethodSpec.methodBuilder()
            .addModifier(Modifier.PUBLIC)
            .name(methodName.isEmpty() ? "build" : methodName)
            .returns(returnType);

    for (Parameter parameter : elementParameters.getParameters()) {
      if (!parameter.isNullable()) {
        builder.addFlowControl(
                "if (%s == null)",
                new String[]{"throw new NullPointerException(\"%s == null\");"},
                parameter.getIdentifier()
        );
      }
      parameterList.add(parameter.getIdentifier());
    }

    return builder.addStatement(
                    "return new %s(%s);",
                    className.isParameterized() ? elementClass + "<>" : elementClass,
                    new Sequence(parameterList, ", ").unify()
    ).create();
  }

  private String getPackageName(Element element) {
    Element enclosing = element.getEnclosingElement();
    TypeElement type = constructor.getElementUtils().getTypeElement(enclosing.toString());

    PackageElement packageElement = constructor.getElementUtils().getPackageOf(type);
    return packageElement.isUnnamed() ? null : packageElement.getQualifiedName().toString();
  }
}