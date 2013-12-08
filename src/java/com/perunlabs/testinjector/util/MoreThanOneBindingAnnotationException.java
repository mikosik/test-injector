package com.perunlabs.testinjector.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

@SuppressWarnings("serial")
public class MoreThanOneBindingAnnotationException extends RuntimeException {
  public MoreThanOneBindingAnnotationException(Field field, List<Annotation> annotations) {
    super("Field " + field.getName() + " has more than one binding annotation: "
        + annotationList(annotations));
  }

  private static String annotationList(List<Annotation> annotations) {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < annotations.size(); i++) {
      if (i != 0) {
        builder.append(", ");
      }
      builder.append("@" + annotations.get(i).annotationType().getSimpleName());
    }
    return builder.toString();
  }
}
