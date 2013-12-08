/**
 * Copyright 2011 Marcin Mikosik
 * All rights reserved.
 */

package com.perunlabs.testinjector.util;

import static com.perunlabs.testinjector.util.Reflections.annotatedFields;
import static com.perunlabs.testinjector.util.Reflections.getElementAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.inject.BindingAnnotation;
import com.google.inject.name.Named;
import com.perunlabs.testinjector.util.Reflections;

public class ReflectionsTest {

  @Test
  public void get_field_value_can_read_private_field() throws Exception {
    String string = "string";
    Field field = ClassWithPrivateField.class.getDeclaredField("field");
    ClassWithPrivateField instance = new ClassWithPrivateField();
    instance.setField(string);

    assertThat(Reflections.getFieldValue(instance, field)).isEqualTo(string);
    assertFieldIsNotAccessible(instance, field);
  }

  class ClassWithPrivateField {
    private String field;

    public void setField(String value) {
      field = value;
    }

    public String getField() {
      return field;
    }
  }

  @Test
  public void get_field_value_can_read_private_primitive_field() throws Exception {
    int value = 33;
    Field field = ClassWithPrivatePrimitiveField.class.getDeclaredField("field");
    ClassWithPrivatePrimitiveField instance = new ClassWithPrivatePrimitiveField();
    instance.setField(value);

    assertThat(Reflections.getFieldValue(instance, field)).isEqualTo(value);
    assertFieldIsNotAccessible(instance, field);
  }

  class ClassWithPrivatePrimitiveField {
    private int field;

    public void setField(int value) {
      field = value;
    }

    public int getField() {
      return field;
    }
  }

  @Test
  public void annotated_fields_returns_field_annotated_with_given_annotation() throws Exception {
    Set<Field> fields = annotatedFields(ClassWithAnnotatedField.class, Deprecated.class);
    assertThat(fields).containsOnly(ClassWithAnnotatedField.class.getField("field"));
  }

  class ClassWithAnnotatedField {
    @Deprecated
    public String field;

    public String fieldNotAnnotated;

    @Named("abc")
    public String fieldAnnotatedWithWrongAnnotation;
  }

  @Test
  public void annotated_fields_returns_empty_set_when_no_field_is_annotated() throws Exception {
    assertThat(annotatedFields(ClassWithoutAnnotatedFields.class, Deprecated.class)).isEmpty();
  }

  class ClassWithoutAnnotatedFields {
    public String field;
  }

  @Test
  public void get_element_annotations_returns_annotations_of_given_type() throws SecurityException,
      NoSuchFieldException {
    Class<ClassWithFieldWithManyAnnotations> klass = ClassWithFieldWithManyAnnotations.class;
    Field field = klass.getField("field");

    List<Annotation> annotations = getElementAnnotations(field, BindingAnnotation.class);

    assertThat(annotations).hasSize(2);
    assertThat(annotations.get(0).annotationType()).isSameAs(Named.class);
    assertThat(annotations.get(1).annotationType()).isSameAs(MyAnnotation.class);

  }

  class ClassWithFieldWithManyAnnotations {
    @Deprecated
    @Named("xxx")
    @MyAnnotation
    public String field;
  }

  @Test
  public void get_element_annotation_returns_empty_set_for_not_annotated_field()
      throws SecurityException, NoSuchFieldException {
    Class<ClassWithFieldWithoutAnnotations> klass = ClassWithFieldWithoutAnnotations.class;
    Field field = klass.getField("field");

    List<Annotation> annotations = getElementAnnotations(field, BindingAnnotation.class);

    assertThat(annotations).isEmpty();
  }

  class ClassWithFieldWithoutAnnotations {
    public String field;
  }

  private static void assertFieldIsNotAccessible(Object instance, Field field) {
    try {
      field.set(instance, "new value");
      fail("Field should not be accessible");
    } catch (IllegalArgumentException e) {
      fail();
    } catch (IllegalAccessException e) {
      // this is expected
    }
  }
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
@BindingAnnotation
@interface MyAnnotation {}
