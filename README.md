# module-directive-bug
Showcase annotation processing bug/feature???.

When this line is uncommented: https://github.com/SentryMan/module-directive-bug/blob/5d4a51eb83ed4e4a58c0a08ddf0b0454d5d72a17/module-processor-core/src/main/java/io/avaje/modules/internal/Generator.java#L39 the code somehow fails to compile even though the code generates as normal.

```
Caused by: org.apache.maven.plugin.compiler.CompilationFailureException: Compilation failure
/M:/Dev/module-directive-bug/blackbox-test-module/src/main/java/module-info.java:[6,41] the service implementation does not have 
a default constructor: io.avaje.modules.example.GeneratedProvider
```

## Expected result:
I should be able to get the `requires` directives from the `ModuleElement` without failing compilation.

## Actual Result:
The code generates fine, and I can see the `requires` directives from the element, but `module-info.java` suddenly fails to compile. 


## How to reproduce:
1. Clone this repo
2. Uncomment the code linked above located in `/module-processor-core/src/main/java/io/avaje/modules/internal/Generator.java`
3. Run `mvn clean compile` to see the error
4. Comment the line to see that everything compiles

## Workaround
We can use `Filer` to retrieve the module-info file and read it line by line. Since we don't call `ModuleElement` methods compilation will work as expected. 
```java
      try (var inputStream =
              processingEnv
                  .getFiler
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
