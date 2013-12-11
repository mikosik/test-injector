# build file for BUCK build system.
# buck can be downloaded from https://github.com/facebook/buck
#
# to build test-injector.jar type:
# buck build //:test-injector.jar
#
# to run tests type:
# buck build //:tests


prebuilt_jar(
  name = 'assertj-core',
  binary_jar = 'lib/assertj-core-1.5.0.jar', 
)

prebuilt_jar(
  name = 'guice',
  binary_jar = 'lib/guice-3.0-no_aop.jar',
)

prebuilt_jar(
  name = 'hamcrest',
  binary_jar = 'lib/hamcrest-core-1.3.jar',
)

prebuilt_jar(
  name = 'javax.inject',
  binary_jar = 'lib/javax.inject.jar',
)

prebuilt_jar(
  name = 'junit',
  binary_jar = 'lib/junit-4.11.jar',
)

prebuilt_jar(
  name = 'mockito',
  binary_jar = 'lib/mockito-core-1.9.5.jar',
)

prebuilt_jar(
  name = 'objenesis',
  binary_jar = 'lib/com.springsource.org.objenesis-1.0.0.jar',
)

DEPS = [
  ':assertj-core',
  ':guice',
  ':hamcrest',
  ':javax.inject',
  ':junit',
  ':mockito',
  ':objenesis',
]

java_library(
  name = 'test-injector.jar',
  srcs = glob(['src/main/java/**/*.java']),
  deps = DEPS,
)

java_test(
  name = 'tests',
  srcs = glob(['src/test/java/**/*.java']),
  deps = [':test-injector.jar'] + DEPS,
)

