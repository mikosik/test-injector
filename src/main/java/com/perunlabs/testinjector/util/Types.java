package com.perunlabs.testinjector.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

public class Types {

  public static TypeLiteral<?> typeProvidedBy(Field field) {
    if (!isProvider(field)) {
      throw new IllegalArgumentException("Field " + field.toGenericString() + " is not a Provider");
    }

    TypeLiteral<?> typeLiteral = typeOf(field);
    if (!(typeLiteral.getType() instanceof ParameterizedType)) {
      throw new IllegalArgumentException("Type " + typeLiteral
          + " is a raw Provider. Use generic one instead.");
    }
    return TypeLiteral.get(((ParameterizedType) typeLiteral.getType()).getActualTypeArguments()[0]);
  }

  public static boolean isProvider(Field field) {
    TypeLiteral<?> typeLiteral = typeOf(field);
    return typeLiteral.getRawType().equals(Provider.class);
  }

  public static TypeLiteral<?> typeOf(Field field) {
    return TypeLiteral.get(field.getGenericType());
  }
}
