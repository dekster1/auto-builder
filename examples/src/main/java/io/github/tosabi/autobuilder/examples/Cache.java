package io.github.tosabi.autobuilder.examples;

import io.github.tosabi.autobuilder.AutoBuilder;

import java.util.List;
import java.util.Map;

public class Cache<K, V> {

  public Map<K, V> map;

  @AutoBuilder
  public Cache(Map<K, V> map, List<V> list) {}
}
