package io.github.tosabi.autobuilder.util;

import java.util.*;
import java.util.function.Function;

public final class Collect {
  private Collect() {}

  public static <T, V> List<V> mapList(List<T> list, Function<T, V> function) {
    List<V> result = new ArrayList<>();
    for (T value : list) {
      result.add(function.apply(value));
    }
    return result;
  }
}