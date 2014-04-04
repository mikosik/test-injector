package com.perunlabs.testinjector;

import static com.perunlabs.testinjector.inject.TestInjector.injectTest;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.MockUtil;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class MockTest {
  private static final String STRING = "abc";

  @Test
  public void mocks_field_with_mockito_mock() throws Exception {
    MockField test = new MockField();
    injectTest(test);
    assertThat(new MockUtil().isMock(test.mock)).isTrue();
  }

  private static class MockField {
    @Mock
    Runnable mock;
  }

  @Test
  public void mocks_field() throws Exception {
    InjectMockField test = new InjectMockField();
    injectTest(test);
    assertThat(test.mock).isSameAs(test.inject);
  }

  private static class InjectMockField {
    @Mock
    Runnable mock;
    @Inject
    Runnable inject;
  }

  @Test
  public void mocks_generic_field() throws Exception {
    GenericMockField test = new GenericMockField();
    injectTest(test);
    assertThat(test.mock).isSameAs(test.inject);
  }

  private static class GenericMockField {
    @Mock
    Iterable<String> mock;
    @Inject
    Iterable<String> inject;
  }

  @Test
  public void mocks_annotated_field() throws Exception {
    AnnotatedMock test = new AnnotatedMock();
    injectTest(test);
    assertThat(test.mock).isSameAs(test.inject);
  }

  private static class AnnotatedMock {
    @Mock
    @Named("x")
    public Runnable mock;

    @Inject
    @Named("x")
    public Runnable inject;
  }

  public void mocks_provider() throws Exception {
    ProviderField test = new ProviderField();
    injectTest(test);
    assertThat(test.mock).isSameAs(test.inject);
  }

  private static class ProviderField {
    @Mock
    Provider<String> mock;
    @Inject
    Provider<String> inject;
  }

  @Test(expected = RuntimeException.class)
  public void mocking_raw_provider_fails() throws Exception {
    injectTest(new RawProvider());
  }

  private static class RawProvider {
    @Mock
    @SuppressWarnings("rawtypes")
    Provider rawProvider;
  }

  @Test(expected = RuntimeException.class)
  public void mocking_wildcard_provider_fails() throws Exception {
    injectTest(new WildcardProvider());
  }

  private static class WildcardProvider {
    @Mock
    Provider<?> rawProvider;
  }

  @Test(expected = RuntimeException.class)
  public void mocking_non_null_field_fails() throws Exception {
    NonNullField test = new NonNullField();
    injectTest(test);
  }

  private static class NonNullField {
    @Mock
    String mock = STRING;
  }
}
