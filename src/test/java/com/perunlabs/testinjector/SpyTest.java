package com.perunlabs.testinjector;

import static com.perunlabs.testinjector.inject.TestInjector.injectTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Spy;
import org.mockito.internal.util.MockUtil;

public class SpyTest {
  private static final List<String> STRING_LIST = new ArrayList<String>();

  @Test
  public void spy_field() throws Exception {
    SpyField test = new SpyField();
    injectTest(test);
    assertThat(new MockUtil().isSpy(test.spy)).isTrue();
  }

  private static class SpyField {
    @Spy
    List<String> spy = STRING_LIST;
  }

  @Test(expected = RuntimeException.class)
  public void spying_null_field_fails() throws Exception {
    injectTest(new NullSpyField());
  }

  private static class NullSpyField {
    @Spy
    String nullValue = null;
  }
}
