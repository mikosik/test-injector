package com.perunlabs.testinjector.inject;

import static com.perunlabs.testinjector.util.Reflections.annotatedFields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.Mock;
import org.mockito.Spy;

import com.perunlabs.testinjector.Bind;
import com.perunlabs.testinjector.util.Reflections;

public class Preconditions {

  public static void checkPreconditions(Object test) {
    checkThatFieldsAnnotatedWithAreNotNull(test, Bind.class);
    checkThatFieldsAnnotatedWithAreNotNull(test, Spy.class);
    checkThatFieldsAnnotatedWithMockAreNull(test);
  }

  private static void checkThatFieldsAnnotatedWithAreNotNull(Object test,
      Class<? extends Annotation> annotation) {
    for (Field field : annotatedFields(test.getClass(), annotation)) {
      if (Reflections.getFieldValue(test, field) == null) {
        throw new RuntimeException("Field " + field + " is set to null and has @"
            + annotation.getSimpleName() + " annotation.");
      }
    }
  }

  private static void checkThatFieldsAnnotatedWithMockAreNull(Object test) {
    for (Field field : annotatedFields(test.getClass(), Mock.class)) {
      if (Reflections.getFieldValue(test, field) != null) {
        throw new RuntimeException("Field '" + field.getName()
            + "' annotated with @Mock should not be assigned to non-null value. ");
      }
    }
  }
}
