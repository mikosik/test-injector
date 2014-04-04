package com.perunlabs.testinjector;

import static com.perunlabs.testinjector.inject.TestInjector.injectTest;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.util.Providers;

public class BindTest {
  private static final String STRING = "abc";

  @Test
  public void binds_field() throws Exception {
    BindField test = new BindField();
    injectTest(test);
    assertThat(test.inject).isSameAs(STRING);
  }

  private static class BindField {
    @Bind
    String bind = STRING;
    @Inject
    private String inject;
  }

  @Test
  public void binds_private_field() throws Exception {
    BindPrivateField test = new BindPrivateField();
    injectTest(test);
    assertThat(test.inject).isSameAs(STRING);
  }

  private static class BindPrivateField {
    @Bind
    private final String bind = STRING;
    @Inject
    private String inject;
  }

  @Test
  public void binds_annotated_field() throws Exception {
    BindAnnotatedField test = new BindAnnotatedField();
    injectTest(test);
    assertThat(test.inject).isSameAs(STRING);
  }

  private static class BindAnnotatedField {
    @Bind
    @Named("name")
    String bind = STRING;
    @Inject
    @Named("name")
    private String inject;
  }

  @Test(expected = RuntimeException.class)
  public void binding_null_field_fails() throws Exception {
    injectTest(new BindNullField());
  }

  private static class BindNullField {
    @Bind
    String bind = null;
  }

  @Test
  public void binds_provider() throws Exception {
    ProviderField test = new ProviderField();
    injectTest(test);
    assertThat(test.inject).isSameAs(STRING);
  }

  private static class ProviderField {
    @Bind
    Provider<String> bind = Providers.of(STRING);
    @Inject
    private String inject;
  }

  @Test(expected = RuntimeException.class)
  public void binding_raw_provider_fails() throws Exception {
    RawProviderField test = new RawProviderField();
    injectTest(test);
  }

  private static class RawProviderField {
    @SuppressWarnings("rawtypes")
    @Bind
    Provider bind = Providers.of(STRING);
  }

  @Test(expected = RuntimeException.class)
  public void binding_wildcard_provider_fails() throws Exception {
    WildcardProviderField test = new WildcardProviderField();
    injectTest(test);
  }

  private static class WildcardProviderField {
    @Bind
    Provider<?> bind = Providers.of(STRING);
  }
}
