package com.perunlabs.testinjector;

import static com.perunlabs.testinjector.inject.TestInjector.injectTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import com.google.inject.BindingAnnotation;
import com.google.inject.name.Named;
import com.perunlabs.testinjector.util.MoreThanOneBindingAnnotationException;

public class AnnotationTest {

  @Test(expected = MoreThanOneBindingAnnotationException.class)
  public void mocking_twice_annotated_field_fails() throws Exception {
    injectTest(new TwiceAnnotatedMock());
  }

  private static class TwiceAnnotatedMock {
    @Named("x")
    @MyAnnotation
    @Mock
    Runnable mock;
  }

  @Test(expected = MoreThanOneBindingAnnotationException.class)
  public void spying_twice_annotated_field_fails() throws Exception {
    injectTest(new TwiceAnnotatedSpy());
  }

  private static class TwiceAnnotatedSpy {
    @Named("x")
    @MyAnnotation
    @Spy
    Iterable<String> spy = new ArrayList<String>();
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
  @BindingAnnotation
  @interface MyAnnotation {}
}
