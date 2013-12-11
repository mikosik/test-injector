package com.perunlabs.testinjector.bind;

import java.lang.reflect.Field;

import com.google.inject.Binder;
import com.google.inject.Key;

public abstract class Binding<T> {
  private final Key<T> key;
  private final Field field;

  public Binding(Key<T> key, Field field) {
    this.key = key;
    this.field = field;
  }

  public Key<T> key() {
    return key;
  }

  public Field field() {
    return field;
  }

  public abstract void install(Binder binder);
}
