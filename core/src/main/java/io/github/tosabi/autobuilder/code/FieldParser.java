package io.github.tosabi.autobuilder.code;

import io.github.tosabi.autobuilder.BuilderParameter;
import io.github.tosabi.autobuilder.Parameter;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.LinkedHashSet;
import java.util.Set;

public class FieldParser {

  private final Set<Parameter> fields;

  private FieldParser(Set<Parameter> fields) {
    this.fields = fields;
  }

  public Set<Parameter> getFields() {
    return fields;
  }

  /**
   * Converts the type parameters of an {@link ExecutableElement} into fields
   * and saves them in a new {@code FieldParser} instance.
   *
   * @param element The executable element to parse
   * @return a new {@code FieldParser} instance with all the parsed fields
   */
  public static FieldParser of(ExecutableElement element) {
    Set<Parameter> fields = new LinkedHashSet<>();

    for (VariableElement variableElement : element.getParameters()) {
      String identifier = variableElement.getSimpleName().toString();
      StringBuilder builder = new StringBuilder(identifier.substring(1));
      char first = identifier.toLowerCase().charAt(0);

      Parameter parameter = new Parameter(
              variableElement.asType().toString(),
              builder.insert(0, first).toString(),
              variableElement.getAnnotation(BuilderParameter.class)
      );

      if (!parameter.isIgnored()) {
        fields.add(parameter);
      }
    }
    return new FieldParser(fields);
  }
}