package org.jspec.core;

import java.lang.reflect.Constructor;
import java.util.Arrays;

@FunctionalInterface
public interface Block {
  void apply() throws Throwable;

  static Block failing(Throwable t) {
    return () -> { throw t; };
  }

  static Block collect(Iterable<? extends Block> blocks) {
    return () -> {
      for (Block b : blocks) {
        b.apply();
      }
    };
  }

  static Block collect(Block... blocks) {
    return collect(Arrays.asList(blocks));
  }

  static Block reflect(Class<?> c) {
    return () -> {
      Constructor<?> cons = c.getDeclaredConstructor();
      cons.setAccessible(true);
      cons.newInstance();
    };
  }
}
