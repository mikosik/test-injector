package com.perunlabs.testinjector.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Test;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

public class TypesTest {

  /*
   * typeOf()
   */

  @Test
  public void type_of_string_field() throws Exception {
    Field field = field("stringField");
    assertThat(Types.typeOf(field)).isEqualTo(TypeLiteral.get(String.class));
  }

  String stringField;

  @Test
  public void type_of_list_of_strings_field() throws Exception {
    Field field = field("listOfStringsField");
    assertThat(Types.typeOf(field)).isEqualTo(new TypeLiteral<List<String>>() {});
  }

  List<String> listOfStringsField;

  /*
   * isProvider()
   */

  @Test
  public void string_field_is_not_provider() throws Exception {
    Field field = field("stringField");
    assertThat(Types.isProvider(field)).isFalse();
  }

  @Test
  public void list_of_strings_field_is_not_provider() throws Exception {
    Field field = field("listOfStringsField");
    assertThat(Types.isProvider(field)).isFalse();
  }

  @Test
  public void string_provider_is_provider() throws Exception {
    Field field = field("stringProviderField");
    assertThat(Types.isProvider(field)).isTrue();
  }

  /*
   * typeProvidedBy()
   */

  @Test
  public void type_provided_by_string_provider() throws Exception {
    Field field = field("stringProviderField");
    assertThat(Types.typeProvidedBy(field)).isEqualTo(TypeLiteral.get(String.class));
  }

  Provider<String> stringProviderField;

  @Test
  public void type_provided_by_list_of_strings_provider() throws Exception {
    Field field = field("listOfStringsProviderField");
    assertThat(Types.typeProvidedBy(field)).isEqualTo(new TypeLiteral<List<String>>() {});
  }

  Provider<List<String>> listOfStringsProviderField;

  private static Field field(String name) throws Exception {
    return TypesTest.class.getDeclaredField(name);
  }
}
