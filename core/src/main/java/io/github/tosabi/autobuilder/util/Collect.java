package io.github.tosabi.autobuilder.util;

import java.util.*;
import java.util.function.Function;

public final class Collect {
  private Collect() {}

  @SafeVarargs
  public static <T> Set<T> unmodifiableSet(T... elements) {
    Set<T> set = new HashSet<>();
    Collections.addAll(set, elements);

    return Collections.unmodifiableSet(set);
  }

  public static <T, V> List<V> mapList(List<T> list, Function<T, V> function) {
    List<V> result = new ArrayList<>();
    for (T value : list) {
      result.add(function.apply(value));
    }
    return result;
  }

  public static <T, V> Set<V> mapSet(Set<T> set, Function<T, V> function) {
    Set<V> result = new LinkedHashSet<>();
    for (T value : set) {
      result.add(function.apply(value));
    }
    return result;
  }

  public static <T, V> Collection<V> mapCollection(Collection<T> collection,
                                                   Collection<V> view,
                                                   Function<T, V> function) {
    for (T value : collection) {
      view.add(function.apply(value));
    }
    return view;
  }
}
