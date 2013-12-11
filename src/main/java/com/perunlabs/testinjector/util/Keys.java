package com.perunlabs.testinjector.util;

import static com.perunlabs.testinjector.util.Reflections.getElementAnnotations;
import static com.perunlabs.testinjector.util.Types.typeOf;
import static com.perunlabs.testinjector.util.Types.typeProvidedBy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import com.google.inject.BindingAnnotation;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class Keys {
  public static Key<?> keyOf(Field field) {
    return getFieldKey(field, typeOf(field));
  }

  public static Key<?> keyProvidedBy(Field field) {
    return getFieldKey(field, typeProvidedBy(field));
  }

  private static Key<?> getFieldKey(Field field, TypeLiteral<?> typeLiteral) {
    List<Annotation> annotations = getElementAnnotations(field, BindingAnnotation.class);

    if (annotations.size() == 0) {
      return Key.get(typeLiteral);
    }
    if (annotations.size() == 1) {
      return Key.get(typeLiteral, annotations.get(0));
    }

    throw new MoreThanOneBindingAnnotationException(field, annotations);
  }
}
