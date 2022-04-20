package io.github.tosabi.autobuilder;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class ElementParameters {

  private final Set<Parameter> parameters;

  private ElementParameters(Set<Parameter> parameters) {
    this.parameters = parameters;
  }

  /** @return A {@link Set} with all the stored parameters */
  public Set<Parameter> getParameters() {
    return parameters;
  }

  /**
   * Converts all the parameters of an {@link ExecutableElement} into
   * {@link Parameter} instances and saves them in a new {@code ElementParameters}.
   *
   * @param element The executable element to parse
   * @return a new {@code ElementParameters} instance with all the parameters
   */
  public static ElementParameters of(ExecutableElement element) {
    Objects.requireNonNull(element, "element");
    Set<Parameter> parameters = new LinkedHashSet<>();

    for (VariableElement variableElement : element.getParameters()) {
      String identifier = variableElement.getSimpleName().toString();
      StringBuilder builder = new StringBuilder(identifier.substring(1));
      char first = identifier.toLowerCase().charAt(0);

      parameters.add(new Parameter(
              variableElement.asType().toString(),
              builder.insert(0, first).toString(),
              variableElement.getAnnotation(BuilderParameter.class)
      ));
    }
    return new ElementParameters(parameters);
  }

  @Override
  public String toString() {
    return parameters.toString();
  }
}