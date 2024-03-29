package io.github.tosabi.autobuilder;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@AutoService(Processor.class)
public class AutoBuilderProcessor extends AbstractProcessor {

  /** Shared element utils */
  static Elements ELEMENTS;

  private Messager messager;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    if (ELEMENTS == null) {
      ELEMENTS = processingEnv.getElementUtils();
    }
    messager = processingEnv.getMessager();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new LinkedHashSet<>();
    annotations.add(AutoBuilder.class.getCanonicalName());
    return annotations;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    try {
      for (Element element : roundEnv.getElementsAnnotatedWith(AutoBuilder.class)) {
        if (element.getKind() != ElementKind.CONSTRUCTOR) {
          error(element, "Only constructors can be annotated with %s", AutoBuilder.class.getSimpleName());
          return true;
        }

        AnnotatedConstructor constructor = new AnnotatedConstructor(element, processingEnv.getElementUtils());
        if (constructor.getConstructor().getParameters().size() < 1) {
          // Can't create builder classes for empty constructors
          throw new AutoBuilderException(element, "Couldn't create a builder for empty constructor %s", element);
        } else if (constructor.hasPrivateAccess()) {
          // If the constructor has private access, it cannot be instantiated through the builder class
          throw new AutoBuilderException(element, "Constructor %s has private access in %s",
                  element,
                  element.getEnclosingElement().asType().toString());
        }

        BuilderGenerator generator = new BuilderGenerator(constructor);
        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(generator.getClassName());
        BufferedWriter bufferedWriter = new BufferedWriter(sourceFile.openWriter());
        bufferedWriter.write(generator.generate());
        bufferedWriter.close();
      }
    } catch (IOException e) {
      error(null, e.toString());
    } catch (AutoBuilderException e) {
      error(e.getElement(), e.toString());
    }
    return false;
  }

  private void error(Element element, String message, Object... args) {
    messager.printMessage(
            Diagnostic.Kind.ERROR,
            String.format(message, args),
            element
    );
  }
}
