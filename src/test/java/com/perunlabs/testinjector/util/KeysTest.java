package com.perunlabs.testinjector.util;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;

import com.google.inject.BindingAnnotation;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class KeysTest {

  @Test
  public void key_of_string_field() throws Exception {
    Field field = field("stringField");
    assertThat(Keys.keyOf(field)).isEqualTo(Key.get(String.class));
  }

  String stringField;

  @Test
  public void key_of_list_of_strings_field() throws Exception {
    Field field = field("listOfStrings");
    assertThat(Keys.keyOf(field)).isEqualTo(Key.get(new TypeLiteral<List<String>>() {}));
  }

  List<String> listOfStrings;

  @Test
  public void key_of_annotated_string_field() throws Exception {
    Field field = field("annotatedStringField");
    assertThat(Keys.keyOf(field)).isEqualTo(Key.get(String.class, Names.named("abc")));
  }

  @Named("abc")
  String annotatedStringField;

  @Test(expected = MoreThanOneBindingAnnotationException.class)
  public void double_annotated_fields_are_not_allowed() throws Exception {
    Keys.keyOf(field("doubleAnnotatedStringField"));
  }

  @Named("abc")
  @MyBindingAnnotation
  String doubleAnnotatedStringField;

  // provider

  @Test
  public void string_key_provided_by_field() throws Exception {
    Field field = field("stringProviderField");
    assertThat(Keys.keyProvidedBy(field)).isEqualTo(Key.get(String.class));
  }

  Provider<String> stringProviderField;

  @Test
  public void list_of_strings_provided_by_field() throws Exception {
    Field field = field("listOfStringsProviderField");
    assertThat(Keys.keyProvidedBy(field)).isEqualTo(Key.get(new TypeLiteral<List<String>>() {}));
  }

  Provider<List<String>> listOfStringsProviderField;

  @Test
  public void annotated_string_provided_by_field() throws Exception {
    Field field = field("annotatedStringProviderField");
    assertThat(Keys.keyProvidedBy(field)).isEqualTo(Key.get(String.class, Names.named("abc")));
  }

  @Named("abc")
  Provider<String> annotatedStringProviderField;

  @Test(expected = MoreThanOneBindingAnnotationException.class)
  public void double_annotated_provider_fields_are_not_allowed() throws Exception {
    Keys.keyProvidedBy(field("doubleAnnotatedStringProviderField"));
  }

  @Named("abc")
  @MyBindingAnnotation
  Provider<String> doubleAnnotatedStringProviderField;

  @Retention(RUNTIME)
  @Target({ FIELD, METHOD, PARAMETER, TYPE })
  @BindingAnnotation
  @interface MyBindingAnnotation {}

  private static Field field(String name) throws NoSuchFieldException {
    return KeysTest.class.getDeclaredField(name);
  }
}
