package com.perunlabs.testinjector.bind;

import java.lang.reflect.Field;

@SuppressWarnings("serial")
public class DuplicateBindingException extends RuntimeException {

  public DuplicateBindingException(Binding<?> binding, Field field) {
    super(createMessage(binding, field));
  }

  private static String createMessage(Binding<?> binding, Field field) {
    StringBuilder builder = new StringBuilder();

    builder.append("Duplicate binding for key = " + binding.key() + ".\n");
    builder.append("It is bound by both: ");
    builder.append(binding.field());
    builder.append(" and ");
    builder.append(field);

    return builder.toString();
  }
}
