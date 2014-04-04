package com.perunlabs.testinjector;

import static com.perunlabs.testinjector.inject.TestInjector.injectTest;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.google.inject.Binder;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;

public class InjectTest {
  @Test
  public void injects_field() throws Exception {
    InjectField test = new InjectField();
    injectTest(test);
    assertThat(test.field).isInstanceOf(String.class);
  }

  private static class InjectField {
    @Inject
    public String field;
  }

  @Test
  public void injects_private_field() throws Exception {
    InjectPrivateField test = new InjectPrivateField();
    injectTest(test);
    assertThat(test.field).isInstanceOf(String.class);
  }

  private static class InjectPrivateField {
    @Inject
    private String field;
  }

  @Test
  public void injects_provider() throws Exception {
    InjectProviderField test = new InjectProviderField();
    injectTest(test);
    assertThat(test.field.get()).isInstanceOf(String.class);
  }

  private static class InjectProviderField {
    @Inject
    Provider<String> field;
  }

  @Test(expected = ConfigurationException.class)
  public void does_not_inject_raw_provider() throws Exception {
    InjectRawProviderField test = new InjectRawProviderField();
    injectTest(test);
  }

  @SuppressWarnings("rawtypes")
  private static class InjectRawProviderField {
    @SuppressWarnings("unused")
    @Inject
    Provider field;
  }

  @Test(expected = RuntimeException.class)
  public void does_not_inject_wildcard_provider() throws Exception {
    InjectWildcardProviderField test = new InjectWildcardProviderField();
    injectTest(test);
  }

  private static class InjectWildcardProviderField {
    @SuppressWarnings("unused")
    @Inject
    Provider<?> field;
  }

  @Test
  public void injects_bindings_from_configure_method() throws Exception {
    InjectModuleBinding test = new InjectModuleBinding();
    injectTest(test);
    assertThat(test.integer).isEqualTo(Integer.valueOf(77));
  }

  @SuppressWarnings("boxing")
  private static class InjectModuleBinding implements Module {
    @Inject
    public Integer integer;

    @Override
    public void configure(Binder binder) {
      binder.bind(Integer.class).toInstance(77);
    }
  }
}
