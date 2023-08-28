# module-directive-bug
Showcase annotation processing bug/feature???.

When `ModuleElement#getDirectives` is called: https://github.com/SentryMan/module-directive-bug/blob/9a08bffe97abd9d80218ee9df29fdf7c50b8e688/module-processor-core/src/main/java/io/avaje/modules/internal/Generator.java#L40 the code somehow fails to compile even though the code generates as normal.

```
Caused by: org.apache.maven.plugin.compiler.CompilationFailureException: Compilation failure
/M:/Dev/module-directive-bug/blackbox-test-module/src/main/java/module-info.java:[6,41] the service implementation does not have 
a default constructor: io.avaje.modules.example.GeneratedProvider
```

When this line is commented, it compiles as normal. (But then we can't use Module element for anything except error location hints)

## Expected result:
I should be able to get the `requires` directives from the `ModuleElement` without failing compilation.

## Actual Result:
The code generates fine into `/blackbox-test-module/target/generated-sources/annotations/io/avaje/modules/example/GeneratedProvider.java`, and I can see the `requires` directives logs from the element, but `module-info.java` suddenly fails to compile. 

## How to reproduce:
1. Clone this repo
2. Run `mvn clean compile` to see the error

## Workaround
###  Manually Parse `module-info.java`

We can use `Filer` to retrieve the module-info file and read it line by line. Since we don't call `ModuleElement` methods, compilation will work as expected. 
```java
      try (var inputStream =
              processingEnv
                  .getFiler()
                  .getResource(StandardLocation.SOURCE_PATH, "", "module-info.java")
                  .toUri()
                  .toURL()
                  .openStream();
          var reader = new BufferedReader(new InputStreamReader(inputStream))) {

        reader.lines(); // parse the strings and do something

      } catch (Exception e) {
        // can't read module
      }
```
