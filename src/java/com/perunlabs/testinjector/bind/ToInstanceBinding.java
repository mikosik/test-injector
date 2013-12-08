package com.perunlabs.testinjector.bind;

import java.lang.reflect.Field;

import com.google.inject.Binder;
import com.google.inject.Key;

public class ToInstanceBinding<T> extends Binding<T> {
  private final T instance;

  public ToInstanceBinding(Key<T> key, T instance, Field field) {
    super(key, field);
    this.instance = instance;
  }

  @Override
  public void install(Binder binder) {
    binder.bind(key()).toInstance(instance);
  }
}
