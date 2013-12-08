/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector;

import org.junit.Before;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.perunlabs.testinjector.inject.TestInjector;

public class MockitoTestCase implements Module {
  @Override
  public void configure(Binder binder) {}

  @Before
  public void performTestInjection() throws Exception {
    new TestInjector(this).injectTest();
  }
}
