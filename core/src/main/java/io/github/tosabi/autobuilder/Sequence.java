package io.github.tosabi.autobuilder;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public final class Sequence {

  private final List<String> arguments;
  private String separator;

  public Sequence(List<String> arguments, String separator) {
    this.arguments = requireNonNull(arguments, "arguments");
    this.separator = requireNonNull(separator, "separator");
  }

  /** Unify all the arguments with the separator */
  public String unify() {
    StringBuilder builder = new StringBuilder();
    int index = 0;
    for (String arg : arguments) {
      builder.append(arg);
      if (++index < arguments.size()) {
        builder.append(separator);
      }
    }
    return builder.toString();
  }

  /** @return The unified arguments with a start and end character */
  public String unify(char start, char end) {
    return start + unify() + end;
  }

  /** Unify all the arguments with other list of arguments */
  public String unifyWith(List<String> list) {
    requireNonNull(list, "list");
    List<String> combined = new ArrayList<>(arguments.size());
    for (int i = 0; i < arguments.size(); i++) {
      combined.add(list.get(i) + " " + arguments.get(i));
    }
    return new Sequence(combined, separator).unify();
  }

  /** Sets the sequence separator */
  public void setSeparator(String separator) {
    this.separator = requireNonNull(separator, "separator");
  }

  /** @return The current separator */
  public String getSeparator() {
    return separator;
  }

  /** @return The {@link List} containing all the arguments */
  public List<String> getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return "Sequence{" + unify() + "}";
  }
}