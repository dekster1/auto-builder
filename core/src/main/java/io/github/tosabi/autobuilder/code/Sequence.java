package io.github.tosabi.autobuilder.code;

import java.util.ArrayList;
import java.util.List;

public final class Sequence {

  private final List<String> arguments;
  private String separator;

  public Sequence(List<String> arguments, String separator) {
    this.arguments = arguments;
    this.separator = separator;
  }

  /** Unify all the arguments with other list of arguments */
  public String unifyWith(List<String> list) {
    List<String> combined = new ArrayList<>(arguments.size());
    for (int i = 0; i < arguments.size(); i++) {
      combined.add(list.get(i) + " " + arguments.get(i));
    }
    return new Sequence(combined, separator).unify();
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

  public void setSeparator(String separator) {
    this.separator = separator;
  }

  public String getSeparator() {
    return separator;
  }

  public List<String> getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return arguments.toString();
  }
}