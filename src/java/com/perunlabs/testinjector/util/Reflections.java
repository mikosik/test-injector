/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector.util;

import static com.perunlabs.testinjector.util.Collections.newArrayList;
import static com.perunlabs.testinjector.util.Collections.newHashSet;
import static java.security.AccessController.doPrivileged;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Set;

public class Reflections {

  public static Object getFieldValue(Object instance, Field field) {
    boolean accessible = field.isAccessible();
    setAccessible(field, true);
    try {
      return field.get(instance);
    } catch (IllegalAccessException e) {
      throw new AssertionError(e);
    } finally {
      setAccessible(field, accessible);
    }
  }

  private static void setAccessible(final Field field, final boolean accessible) {
    doPrivileged(new PrivilegedAction<Void>() {
      @Override
      public Void run() {
        field.setAccessible(accessible);
        return null;
      }
    });
  }

  private static boolean isStatic(Field field) {
    return Modifier.isStatic(field.getModifiers());
  }

  public static Set<Field> annotatedFields(Class<?> klass, Class<? extends Annotation> annotation) {
    Set<Field> fields = newHashSet();
    for (Field field : nonStaticFields(klass)) {
      if (field.isAnnotationPresent(annotation)) {
        fields.add(field);
      }
    }
    return fields;
  }

  public static List<Annotation> getElementAnnotations(AnnotatedElement element,
      Class<? extends Annotation> klass) {
    return filterAnnotationsAnnotatedWith(element.getAnnotations(), klass);
  }

  private static List<Annotation> filterAnnotationsAnnotatedWith(Annotation[] annotations,
      Class<? extends Annotation> klass) {
    List<Annotation> result = newArrayList();
    for (Annotation annotation : annotations) {
      if (annotation.annotationType().isAnnotationPresent(klass)) {
        result.add(annotation);
      }
    }
    return result;
  }

  private static List<Field> nonStaticFields(Class<?> klass) {
    List<Field> result = newArrayList();
    for (Field field : klass.getDeclaredFields()) {
      if (!isStatic(field)) {
        result.add(field);
      }
    }
    return result;
  }
}
