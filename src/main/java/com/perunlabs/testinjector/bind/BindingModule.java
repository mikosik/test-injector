package com.perunlabs.testinjector.bind;

import java.util.Set;

import com.google.inject.AbstractModule;

public class BindingModule extends AbstractModule {
  private final Set<Binding<?>> bindings;

  public BindingModule(Set<Binding<?>> bindings) {
    this.bindings = bindings;
  }

  @Override
  protected void configure() {
    for (Binding<?> binding : bindings) {
      binding.install(binder());
    }
  }
}
