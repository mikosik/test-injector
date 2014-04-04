package com.perunlabs.testinjector;

import static com.perunlabs.testinjector.inject.TestInjector.injectTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

public class CaptorTest {
  private static final String STRING = "abc";

  @Test
  public void captor_field() throws Exception {
    CaptorField test = new CaptorField();

    injectTest(test);

    ArgumentCaptor<String> captor = test.captor;
    assertThat(captor).isNotNull();

    // checking that object can capture values which means it is a captor
    @SuppressWarnings("unchecked")
    List<String> mock = Mockito.mock(List.class);
    mock.add(STRING);
    Mockito.verify(mock).add(captor.capture());
    assertThat(captor.getValue()).isEqualTo(STRING);
  }

  private static class CaptorField {
    @Captor
    ArgumentCaptor<String> captor;
  }
}
