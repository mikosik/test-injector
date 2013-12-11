/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector;

import org.junit.Before;

import com.google.inject.AbstractModule;
import com.perunlabs.testinjector.inject.TestInjector;

public class InjectedTestCase extends AbstractModule {
  @Override
  protected void configure() {}

  @Before
  public void performTestInjection() throws Exception {
    new TestInjector(this).injectTest();
  }
}
