# module-directive-bug
Showcase annotation processing bug/feature???.

When this line is uncommented: https://github.com/SentryMan/module-directive-bug/blob/5d4a51eb83ed4e4a58c0a08ddf0b0454d5d72a17/module-processor-core/src/main/java/io/avaje/modules/internal/Generator.java#L39 the code somehow fails to compile even though the code generates as normal.

## Expected result:
I should be able to get the `requires` directives from the `ModuleElement` without failing compilation Code should generated and compile without error when `ModuleElement#getDirectives` is called.

## Actual Result:
The code generates fine, and I can see the `requires` directives from the element, but `module-info.java` suddenly fails to compile. 


## How to reproduce:
1. Clone this repo
2. Uncomment the code linked above located in `/module-processor-core/src/main/java/io/avaje/modules/internal/Generator.java`
3. Run `mvn clean compile` to see the error
4. Comment the line to see that everything compiles
