package io.avaje.modules.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.TypeElement;

import io.avaje.spi.ServiceProvider;

@ServiceProvider(Processor.class)
@SupportedAnnotationTypes("io.avaje.modules.GenerateProvider")
public final class Generator extends AbstractProcessor {

  private ModuleElement moduleElement;

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    super.init(env);
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  @Override
  public boolean process(Set<? extends TypeElement> tes, RoundEnvironment renv) {

    findModule(tes, renv);
    //  moduleElement.getDirectives(); this somehow fails everything????
    if (renv.processingOver()) {
      try (var writer =
          processingEnv
              .getFiler()
              .createSourceFile("io.avaje.modules.example.GeneratedProvider")
              .openWriter()) {
        writer.append(
            """
            package io.avaje.modules.example;

            public class GeneratedProvider implements io.avaje.modules.example.ServiceInterface {}
            """);
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  ModuleElement findModule(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (this.moduleElement == null) {
      moduleElement =
          annotations.stream()
              .map(roundEnv::getElementsAnnotatedWith)
              .flatMap(Collection::stream)
              .findAny()
              .map(this::getModuleElement)
              .orElseThrow();
    }
    return moduleElement;
  }

  ModuleElement getModuleElement(Element e) {
    if (e == null || e instanceof ModuleElement) {
      return (ModuleElement) e;
    }
    return getModuleElement(e.getEnclosingElement());
  }
}
