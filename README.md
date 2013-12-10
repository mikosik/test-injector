test-injector
=============

Injects your tests using guice and mockito.

test-injector relieves you from specyfing most of set-up code in your junit tests.
Let's learn it using simple example.

Given the following class ...

```java
public class MyClass {
  @Inject
  public MyClass(ServiceA a, ServiceB b) {
    ...
  }
}
```

... we set-up test for it this way

```java
import com.google.inject.Inject;
import com.perunlabs.testinjector.Bind;
import com.perunlabs.testinjector.InjectedTestCase;
import org.mockito.Mock;

public class MyClassTest extends InjectedTestCase {
  @Mock
  ServiceA serviceA;
  @Bind
  ServiceB serviceB = new FakeServiceB();

  @Inject
  MyClass myClass;

  // tests go here
  ...
  
}
```

Magic happens inside @Before method inherited from InjectedTestCase class.
It will perform following tasks before each test run:
 * run MockitoAnnotations.initMocks(...) which assigns proper values to fields annotated with @Mock, @Spy, @Captor
 * detects all fields annotated with @Bind, @Mock, @Spy, @Captor. Treats the as guice bindings - type of field is bound to its instance. Binding annotations (for example @Named are respected as well).
 * injects all fields annotated with @Inject using guice configured with bindings described above and also specified inside configure method (explained below).

If you need some more fancy guice bindings then you can simply implement configure method from guice AbstractModule class (it is super class of InjectedTestCase).
Let's rewrite above example, and replace mocking serviceA with creating it via some guice module.

```java
import com.google.inject.Inject;
import com.perunlabs.testinjector.Bind;
import com.perunlabs.testinjector.InjectedTestCase;
import org.mockito.Mock;

public class MyClassTest extends InjectedTestCase {
  @Bind
  ServiceB serviceB = new FakeServiceB();

  @Inject
  MyClass myClass;

  @Override
  public void configure() {
    install(new FakeServiceAModule());
  }

  // tests go here
  ... 
}
```



