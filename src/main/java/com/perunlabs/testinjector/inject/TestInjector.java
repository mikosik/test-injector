/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector.inject;

import static com.google.inject.Guice.createInjector;
import static com.perunlabs.testinjector.inject.Preconditions.checkPreconditions;

import java.util.ArrayList;

import org.mockito.MockitoAnnotations;

import com.google.inject.Module;
import com.perunlabs.testinjector.bind.BindingModule;
import com.perunlabs.testinjector.bind.FieldBindingsCollector;

public class TestInjector {
  public static void injectTest(Object test) {
    checkPreconditions(test);
    MockitoAnnotations.initMocks(test);

    FieldBindingsCollector fieldBindingsCollector = new FieldBindingsCollector();
    fieldBindingsCollector.collectBindings(test);

    injectGuiceStuff(test, fieldBindingsCollector);
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
