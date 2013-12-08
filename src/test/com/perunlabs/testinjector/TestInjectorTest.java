/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector;

import static com.perunlabs.testinjector.util.Collections.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.google.inject.Binder;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.perunlabs.testinjector.bind.DuplicateBindingException;
import com.perunlabs.testinjector.inject.TestInjector;
import com.perunlabs.testinjector.util.MoreThanOneBindingAnnotationException;

public class TestInjectorTest {
  private static final List<String> STRING_LIST = new ArrayList<String>();
  private static final String STRING = "some string";

  @Test
  public void field_with_guice_inject_annotation_is_injected() throws Exception {
    TestWithGuiceInjection test = new TestWithGuiceInjection();

    new TestInjector(test).injectTest();

    assertThat(test.getField()).isNotNull();
    assertThat(test.getField()).isInstanceOf(ClassWithInjectableConstructor.class);
  }

  private static class TestWithGuiceInjection {
    @Inject
    private ClassWithInjectableConstructor field;

    public ClassWithInjectableConstructor getField() {
      return field;
    }
  }

  public static class ClassWithInjectableConstructor {
    @Inject
    public ClassWithInjectableConstructor() {}
  }

  @Test
  public void fields_marked_with_bind_annotation_can_be_injected_by_guice() throws Exception {
    TestWithBindAnnotation test = new TestWithBindAnnotation();

    new TestInjector(test).injectTest();

    assertThat(test.getStringProvider().get()).isSameAs(STRING);
  }

  private static class TestWithBindAnnotation {
    @Bind
    String string = STRING;
    @Inject
    private Provider<String> stringProvider;

    public Provider<String> getStringProvider() {
      return stringProvider;
    }
  }

  @Test
  public void fields_marked_with_spy_annotation_are_set_to_mockito_spy() throws Exception {
    TestWithSpyAnnotation test = new TestWithSpyAnnotation();

    new TestInjector(test).injectTest();
    List<String> list = test.getList();

    assertThat(list).isNotSameAs(STRING_LIST);

    // test that object can be spied which means it is a spy
    assertThat(list.size()).isEqualTo(0);
    Mockito.when(list.size()).thenReturn(100);
    assertThat(list.size()).isEqualTo(100);
  }

  private static class TestWithSpyAnnotation {
    @Spy
    List<String> list = STRING_LIST;

    public List<String> getList() {
      return list;
    }
  }

  @Test
  public void fields_marked_with_captor_annotation_are_set_to_mockito_captor() throws Exception {
    TestWithCaptorAnnotation test = new TestWithCaptorAnnotation();

    new TestInjector(test).injectTest();
    ArgumentCaptor<String> captor = test.getStringCaptor();
    assertThat(captor).isNotNull();

    // checking that object can capture values which means it is a captor
    @SuppressWarnings("unchecked")
    List<String> mock = Mockito.mock(List.class);
    mock.add(STRING);
    Mockito.verify(mock).add(captor.capture());
    assertThat(captor.getValue()).isEqualTo(STRING);
  }

  private static class TestWithCaptorAnnotation {
    @Captor
    ArgumentCaptor<String> stringCaptor;

    public ArgumentCaptor<String> getStringCaptor() {
      return stringCaptor;
    }
  }

  @Test
  public void guice_raw_provider_are_forbidden() throws Exception {
    TestInjector testInjector = new TestInjector(new TestWithRawProviderField());
    try {
      testInjector.injectTest();
      fail("exception should be thrown");
    } catch (RuntimeException e) {
      // expected
      assertThat(e.getMessage()).isEqualTo(
          "Type com.google.inject.Provider is a raw Provider. Use generic one instead.");
    }
  }

  private static class TestWithRawProviderField {
    @Mock
    @SuppressWarnings("rawtypes")
    Provider rawProvider;
  }

  @Test
  public void null_field_with_bind_annotation_is_forbidden() throws Exception {
    TestInjector testInjector = new TestInjector(new TestWithNullFieldWithBindAnnotation());

    try {
      testInjector.injectTest();
      fail("exception should be thrown");
    } catch (RuntimeException e) {
      // expected
      assertThat(e.getMessage())
          .isEqualTo(
              "Field java.lang.String "
                  + "com.perunlabs.testinjector.TestInjectorTest$TestWithNullFieldWithBindAnnotation.nullValue is set to null"
                  + " and has @Bind annotation.");
    }
  }

  private static class TestWithNullFieldWithBindAnnotation {
    @Bind
    String nullValue = null;
  }

  @Test
  public void null_field_with_spy_annotation_is_forbidden() throws Exception {
    TestInjector testInjector = new TestInjector(new TestWithNullFieldWithSpyAnnotation());

    try {
      testInjector.injectTest();
      fail("exception should be thrown");
    } catch (RuntimeException e) {
      // expected
      assertThat(e.getMessage())
          .isEqualTo(
              "Field java.lang.String "
                  + "com.perunlabs.testinjector.TestInjectorTest$TestWithNullFieldWithSpyAnnotation.nullValue is set to null"
                  + " and has @Spy annotation.");
    }
  }

  private static class TestWithNullFieldWithSpyAnnotation {
    @Spy
    String nullValue = null;
  }

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
    TestInjector testInjector = new TestInjector(test);

    try {
      testInjector.injectTest();
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

  @Test
  public void mock_annotation_on_field_is_forbidden_when_other_field_with_the_same_type_has_inject()
      throws Exception {
    TestWithFieldsOfTheSameTypeAndDifferentAnnotations test =
        new TestWithFieldsOfTheSameTypeAndDifferentAnnotations();

    try {
      test.performTestInjection();
      fail("exception should be thrown");
    } catch (DuplicateBindingException e) {
      // expected
    }
  }

  @SuppressWarnings("unused")
  private static class TestWithFieldsOfTheSameTypeAndDifferentAnnotations extends MockitoTestCase {
    @Mock
    List<String> mock;
    @Inject
    List<String> inject;
  }

  @Test
  public void testFieldWithTwoBindingAnnotations() throws Exception {
    TestInjector testInjector = new TestInjector(new TestWithFieldWithTwoBindingAnnotations());

    try {
      testInjector.injectTest();
      fail("exception should be thrown");
    } catch (MoreThanOneBindingAnnotationException e) {
      // expected
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
  @BindingAnnotation
  @interface MyAnnotation {}

  private static class TestWithFieldWithTwoBindingAnnotations {
    @Named("x")
    @MyAnnotation
    @Mock
    List<String> field;
  }

  @Test
  public void generice_types_can_be_mocked() throws Exception {
    TestWithFieldWithMockAnnotationAndGenericType test =
        new TestWithFieldWithMockAnnotationAndGenericType();
    new TestInjector(test).injectTest();
    assertThat(test.field).isNotNull();
  }

  private static class TestWithFieldWithMockAnnotationAndGenericType {
    @Mock
    public Provider<List<String>> field;
  }

  @Test
  public void mocked_field_with_bind_annotation_can_be_injected() throws Exception {
    TestWithMockedAndNamedField test = new TestWithMockedAndNamedField();
    new TestInjector(test).injectTest();
    assertThat(test.field).isSameAs(test.injectedField.get());
  }

  private static class TestWithMockedAndNamedField {
    @Mock
    @Named("x")
    public List<String> field;

    @Inject
    @Named("x")
    public Provider<List<String>> injectedField;
  }

  @Test
  public void binding_from_configure_methods_are_used() throws Exception {
    TestWithConfigureMethod test = new TestWithConfigureMethod();
    new TestInjector(test).injectTest();
    assertThat(test.integer).isEqualTo(Integer.valueOf(77));
  }

  @SuppressWarnings("boxing")
  private static class TestWithConfigureMethod implements Module {
    @Inject
    public Integer integer;

    @Override
    public void configure(Binder binder) {
      binder.bind(Integer.class).toInstance(77);
    }
  }

  @Test
  public void non_null_field_cannot_be_mocked() throws Exception {
    WithNonNullMock test = new WithNonNullMock();
    TestInjector testInjector = new TestInjector(test);
    try {
      testInjector.injectTest();
      fail("exception should be thrown");
    } catch (RuntimeException e) {
      // expected
    }
  }

  private static class WithNonNullMock {
    @Mock
    Runnable runnable = new Runnable() {
      @Override
      public void run() {}
    };
  }
}
