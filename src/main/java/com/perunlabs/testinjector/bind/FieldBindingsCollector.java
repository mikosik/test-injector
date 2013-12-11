/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector.bind;

import static com.perunlabs.testinjector.util.Keys.keyOf;
import static com.perunlabs.testinjector.util.Keys.keyProvidedBy;
import static com.perunlabs.testinjector.util.Reflections.annotatedFields;
import static com.perunlabs.testinjector.util.Reflections.getFieldValue;
import static com.perunlabs.testinjector.util.Types.isProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mockito.Mock;
import org.mockito.Spy;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.perunlabs.testinjector.Bind;
import com.perunlabs.testinjector.util.Collections;

public class FieldBindingsCollector {
  private final Map<Key<?>, Binding<?>> bindings = Collections.newHashMap();

  public Set<Binding<?>> bindings() {
    return new HashSet<Binding<?>>(bindings.values());
  }

  public void assertNotBound(Key<?> key, Field field) {
    Binding<?> existingBinding = bindings.get(key);
    if (existingBinding != null) {
      throw new DuplicateBindingException(existingBinding, field);
    }
  }

  public void collectBindings(Object test) {
    collectBindings(test, Mock.class);
    collectBindings(test, Spy.class);
    collectBindings(test, Bind.class);
  }

  private void collectBindings(Object test, Class<? extends Annotation> annotation) {
    for (Field field : annotatedFields(test.getClass(), annotation)) {
      if (isProvider(field)) {
        createProviderBinding(test, field);
      } else {
        createInstanceBinding(test, field);
      }
    }
  }

  /*
   * instance binding
   */

  private void createInstanceBinding(Object test, Field field) {
    Key<?> key = keyOf(field);
    Object instance = getFieldValue(test, field);

    /**
     * It is safe to cast instance to type denoted by key as instance object has
     * been taken from field of that type.
     */
    castAndAddInstanceBinding(key, instance, field);
  }

  private <T> void castAndAddInstanceBinding(Key<T> key, Object value, Field field) {
    @SuppressWarnings("unchecked")
    T castValue = (T) value;
    addInstanceBinding(key, castValue, field);
  }

  private <T> void addInstanceBinding(Key<T> key, T value, Field field) {
    assertNotBound(key, field);
    bindings.put(key, new ToInstanceBinding<T>(key, value, field));
  }

  /*
   * provider binding
   */

  private void createProviderBinding(Object test, Field field) {
    Key<?> key = keyProvidedBy(field);
    Object instance = getFieldValue(test, field);

    /**
     * It is safe to cast instance to type denoted by key as instance object has
     * been taken from field of that type.
     */
    castAndAddProviderBinding(key, instance, field);
  }

  private <T> void castAndAddProviderBinding(Key<T> key, Object value, Field field) {
    @SuppressWarnings("unchecked")
    Provider<T> castValue = (Provider<T>) value;
    addProviderInstanceBinding(key, castValue, field);
  }

  private <T> void addProviderInstanceBinding(Key<T> key, Provider<T> value, Field field) {
    assertNotBound(key, field);
    bindings.put(key, new ToProviderBinding<T>(key, value, field));
  }
}
