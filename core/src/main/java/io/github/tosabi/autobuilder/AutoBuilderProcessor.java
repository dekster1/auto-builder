package io.github.tosabi.autobuilder;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@AutoService(Processor.class)
public class AutoBuilderProcessor extends AbstractProcessor {

  private Messager messager;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

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
    for (Element element : roundEnv.getElementsAnnotatedWith(AutoBuilder.class)) {
      if (element.getKind() != ElementKind.CONSTRUCTOR) {
        error(element, "Only constructors can be annotated with %s", AutoBuilder.class.getSimpleName());
        return true;
      }

      AnnotatedConstructor constructor = new AnnotatedConstructor(element);
      BuilderGenerator generator = new BuilderGenerator(constructor);

      try {
        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(generator.getClassName());
        BufferedWriter bufferedWriter = new BufferedWriter(sourceFile.openWriter());
        bufferedWriter.write(generator.generate());
        bufferedWriter.close();
      } catch (IOException e) {
        error(element, e.toString());
      }
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
