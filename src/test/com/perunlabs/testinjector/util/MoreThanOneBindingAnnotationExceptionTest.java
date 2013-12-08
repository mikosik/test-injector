package com.perunlabs.testinjector.util;

import static com.perunlabs.testinjector.util.Reflections.getElementAnnotations;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;

import com.google.inject.BindingAnnotation;
import com.google.inject.name.Named;

public class MoreThanOneBindingAnnotationExceptionTest {

  @Test
  public void test() throws Exception {
    Field field = this.getClass().getDeclaredField("field");
    List<Annotation> annotations = getElementAnnotations(field, BindingAnnotation.class);

    Exception exception = new MoreThanOneBindingAnnotationException(field, annotations);

    assertThat(exception.getMessage()).isEqualTo(
        "Field field has more than one binding annotation: @Named, @MyBindingAnnotation");
  }

  @Named("abc")
  @MyBindingAnnotation
  String field;

  @Retention(RUNTIME)
  @Target({ FIELD, METHOD, PARAMETER, TYPE })
  @BindingAnnotation
  @interface MyBindingAnnotation {}
}
