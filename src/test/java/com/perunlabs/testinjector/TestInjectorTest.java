/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector;

import static com.perunlabs.testinjector.inject.TestInjector.injectTest;
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

import com.google.inject.BindingAnnotation;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.perunlabs.testinjector.bind.DuplicateBindingException;
import com.perunlabs.testinjector.util.MoreThanOneBindingAnnotationException;

public class TestInjectorTest {
  private static final List<String> STRING_LIST = new ArrayList<String>();
  private static final String STRING = "some string";

  @Test
  public void fields_marked_with_spy_annotation_are_set_to_mockito_spy() throws Exception {
    TestWithSpyAnnotation test = new TestWithSpyAnnotation();

    injectTest(test);
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

    injectTest(test);
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
  public void null_field_with_spy_annotation_is_forbidden() throws Exception {
    try {
      injectTest(new TestWithNullFieldWithSpyAnnotation());
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

  @Test
  public void testFieldWithTwoBindingAnnotations() throws Exception {
    try {
      injectTest(new TestWithFieldWithTwoBindingAnnotations());
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
    injectTest(test);
    assertThat(test.field).isNotNull();
  }

  private static class TestWithFieldWithMockAnnotationAndGenericType {
    @Mock
    public Provider<List<String>> field;
  }
}
