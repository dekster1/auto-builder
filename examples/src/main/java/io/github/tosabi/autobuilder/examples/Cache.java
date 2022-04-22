package io.github.tosabi.autobuilder.examples;

import io.github.tosabi.autobuilder.AutoBuilder;
import io.github.tosabi.autobuilder.BuilderParameter;
import io.github.tosabi.autobuilder.Style;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cache<K, V> {

  public Map<K, V> map;

  @AutoBuilder
  public Cache(Map<K, V> map, @BuilderParameter(style = Style.COLLECTION) Set<V> list) {}
}
