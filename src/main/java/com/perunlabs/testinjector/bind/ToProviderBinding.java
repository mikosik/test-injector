package com.perunlabs.testinjector.bind;

import java.lang.reflect.Field;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;

public class ToProviderBinding<T> extends Binding<T> {
  private final Provider<T> provider;

  public ToProviderBinding(Key<T> key, Provider<T> provider, Field field) {
    super(key, field);
    this.provider = provider;
  }

  @Override
  public void install(Binder binder) {
    binder.bind(key()).toProvider(provider);
  }
}
