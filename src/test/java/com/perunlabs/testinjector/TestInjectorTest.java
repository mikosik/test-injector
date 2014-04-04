/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector;

import static com.perunlabs.testinjector.inject.TestInjector.injectTest;
import static com.perunlabs.testinjector.util.Collections.newArrayList;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import com.google.inject.name.Named;
import com.perunlabs.testinjector.bind.DuplicateBindingException;

public class TestInjectorTest {

  @Test
  public void annotation_on_different_fields_with_the_same_type_is_forbidden() throws Exception {
    assertDuplicateBindingExceptionIsThrown(new DoubleBinding0());
    assertDuplicateBindingExceptionIsThrown(new DoubleBinding1());
    assertDuplicateBindingExceptionIsThrown(new DoubleBinding2());
    assertDuplicateBindingExceptionIsThrown(new DoubleBinding3());
    assertDuplicateBindingExceptionIsThrown(new DoubleBinding4());
    assertDuplicateBindingExceptionIsThrown(new DoubleBinding5());
    assertDuplicateBindingExceptionIsThrown(new DoubleBinding6());
  }

  private void assertDuplicateBindingExceptionIsThrown(Object test) throws Exception {
    try {
      injectTest(test);
      fail("exception should be thrown");
    } catch (DuplicateBindingException e) {
      // expected
    }
  }

  private static class DoubleBinding0 {
    @Bind
    @Named("abc")
    List<String> field = newArrayList();

    @Bind
    @Named("abc")
    List<String> otherField = newArrayList();
  }

  private static class DoubleBinding1 {
    @Bind
    List<String> field = newArrayList();

    @Mock
    List<String> otherField;
  }

  private static class DoubleBinding2 {
    @Bind
    List<String> field = newArrayList();

    @Bind
    List<String> otherField = newArrayList();
  }

  private static class DoubleBinding3 {
    @Mock
    List<String> field;

    @Mock
    List<String> otherField;
  }

  private static class DoubleBinding4 {
    @Spy
    List<String> field = newArrayList();

    @Mock
    List<String> otherField;
  }

  private static class DoubleBinding5 {
    @Bind
    List<String> field = newArrayList();

    @Spy
    List<String> otherField = newArrayList();
  }

  private static class DoubleBinding6 {
    @Spy
    List<String> field = newArrayList();

    @Spy
    List<String> otherField = newArrayList();
  }
}
