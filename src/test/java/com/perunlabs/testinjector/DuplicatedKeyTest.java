/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector;

import static com.perunlabs.testinjector.inject.TestInjector.injectTest;
import static com.perunlabs.testinjector.util.Collections.newArrayList;

import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import com.google.inject.name.Named;
import com.perunlabs.testinjector.bind.DuplicateBindingException;

public class DuplicatedKeyTest {

  @Test(expected = DuplicateBindingException.class)
  public void binding_twice_same_key_fails() throws Exception {
    injectTest(new KeyBoundTwice());
  }

  private static class KeyBoundTwice {
    @Bind
    String field1 = "abc";
    @Bind
    String field2 = "def";
  }

  @Test(expected = DuplicateBindingException.class)
  public void binding_twice_same_annotated_key_fails() throws Exception {
    injectTest(new AnnotatedKeyBoundTwice());
  }

  private static class AnnotatedKeyBoundTwice {
    @Named("name")
    @Bind
    String field1 = "abc";
    @Named("name")
    @Bind
    String field2 = "def";
  }

  @Test(expected = DuplicateBindingException.class)
  public void bind_and_mock_with_the_same_key_fails() throws Exception {
    injectTest(new BindAndMockWithSameKey());
  }

  private static class BindAndMockWithSameKey {
    @Bind
    List<String> field = newArrayList();

    @Mock
    List<String> otherField;
  }

  @Test(expected = DuplicateBindingException.class)
  public void mocking_twice_the_same_key_fails() throws Exception {
    injectTest(new KeyMockedTwice());
  }

  private static class KeyMockedTwice {
    @Mock
    List<String> field;

    @Mock
    List<String> otherField;
  }

  @Test(expected = DuplicateBindingException.class)
  public void mocking_and_spying_the_same_key_fails() throws Exception {
    injectTest(new MockAndSpyWithTheSameKey());
  }

  private static class MockAndSpyWithTheSameKey {
    @Spy
    List<String> field = newArrayList();

    @Mock
    List<String> otherField;
  }

  @Test(expected = DuplicateBindingException.class)
  public void binding_and_spying_the_same_key_fails() throws Exception {
    injectTest(new BindAndSpyWithTheSameKey());
  }

  private static class BindAndSpyWithTheSameKey {
    @Bind
    List<String> field = newArrayList();

    @Spy
    List<String> otherField = newArrayList();
  }

  @Test(expected = DuplicateBindingException.class)
  public void spying_the_same_key_twice_fails() throws Exception {
    injectTest(new KeySpyTwice());
  }

  private static class KeySpyTwice {
    @Spy
    List<String> field = newArrayList();

    @Spy
    List<String> otherField = newArrayList();
  }
}
