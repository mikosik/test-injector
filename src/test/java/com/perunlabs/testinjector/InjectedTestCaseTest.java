/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class InjectedTestCaseTest {
  private static final String STRING = "abc";

  @Test
  public void injected_test_case_run_test_injector() throws Exception {
    MyTestCase test = new MyTestCase();
    test.performTestInjection();
    assertThat(test.provider.get()).isEqualTo(STRING);
  }

  private static class MyTestCase extends InjectedTestCase {
    @Inject
    Provider<String> provider;
    @Bind
    String string = STRING;
  }
}
