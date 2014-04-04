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
import java.util.ArrayList;

import org.mockito.MockitoAnnotations;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.perunlabs.testinjector.bind.BindingModule;
import com.perunlabs.testinjector.bind.FieldBindingsCollector;

public class TestInjector {
  public static void injectTest(Object test) {
    checkPreconditions(test);
    MockitoAnnotations.initMocks(test);

    FieldBindingsCollector fieldBindingsCollector = new FieldBindingsCollector();
    fieldBindingsCollector.collectBindings(test);

    checkThatKeysOfInjectedFieldsAreNotUsedByOtherAnnotations(test, fieldBindingsCollector);
    injectGuiceStuff(test, fieldBindingsCollector);
  }

  private static void checkThatKeysOfInjectedFieldsAreNotUsedByOtherAnnotations(Object test,
      FieldBindingsCollector fieldBindingsCollector) {
    for (Field field : annotatedFields(test.getClass(), Inject.class)) {
      fieldBindingsCollector.assertNotBound(keyOf(field), field);
    }
  }

  private static void injectGuiceStuff(Object test, FieldBindingsCollector fieldBindingsCollector) {
    BindingModule bindingModule = new BindingModule(fieldBindingsCollector.bindings());
    ArrayList<Module> modules = new ArrayList<Module>();
    modules.add(bindingModule);
    if (test instanceof Module) {
      modules.add((Module) test);
    }
    createInjector(modules).injectMembers(test);
  }
}
