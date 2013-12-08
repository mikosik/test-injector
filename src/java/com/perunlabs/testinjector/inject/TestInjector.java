/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector.inject;

import static com.google.inject.Guice.createInjector;
import static com.perunlabs.testinjector.inject.Preconditions.checkPreconditions;
import static com.perunlabs.testinjector.util.Keys.keyOf;
import static com.perunlabs.testinjector.util.Reflections.annotatedFields;

import java.lang.reflect.Field;

import org.mockito.MockitoAnnotations;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.perunlabs.testinjector.bind.BindingModule;
import com.perunlabs.testinjector.bind.FieldBindingsCollector;

public class TestInjector {
  private final FieldBindingsCollector fieldBindingsCollector = new FieldBindingsCollector();
  private final Object test;

  public TestInjector(Object test) {
    this.test = test;
  }

  public void injectTest() {
    checkPreconditions(test);
    MockitoAnnotations.initMocks(test);

    fieldBindingsCollector.collectBindings(test);

    checkThatKeysOfInjectedFieldsAreNotUsedByOtherAnnotations();
    injectGuiceStuff();
  }

  private void checkThatKeysOfInjectedFieldsAreNotUsedByOtherAnnotations() {
    for (Field field : annotatedFields(test.getClass(), Inject.class)) {
      fieldBindingsCollector.assertNotBound(keyOf(field), field);
    }
  }

  private void injectGuiceStuff() {
    BindingModule bindingModule = new BindingModule(fieldBindingsCollector.bindings());
    Injector injector = createInjector(getTestModule(), bindingModule);
    injector.injectMembers(test);
  }

  private Module getTestModule() {
    if (test instanceof Module) {
      return (Module) test;
    } else {
      return new Module() {
        @Override
        public void configure(Binder binder) {}
      };
    }
  }
}
