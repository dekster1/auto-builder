package io.github.tosabi.autobuilder;

import io.github.tosabi.autobuilder.code.ClassWriter;
import io.github.tosabi.autobuilder.code.FieldParser;

import javax.lang.model.element.Element;

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
    FieldParser fieldParser = FieldParser.of(constructor.getConstructor());

    ClassWriter classWriter = new ClassWriter();
    classWriter.definePackage(packageName);
    classWriter.defineClass("public class", className);
    classWriter.addFields(fieldParser.getFields());

    classWriter.addPrivateConstructor();

    for (Parameter field : fieldParser.getFields()) {
      classWriter.createSetter(field, className);
    }

    return classWriter.write();
  }

  private String getPackageName(Element element) {
    Element enclosing = element.getEnclosingElement();
    String name = enclosing.getSimpleName().toString();

    return enclosing.asType().toString().replace("." + name, "");
  }
}