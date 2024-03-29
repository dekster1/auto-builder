package io.github.tosabi.autobuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.CLASS)
public @interface BuilderParameter {

  /**
   * Sets the method name that will allow to set the annotated parameter in the
   * builder class.
   * <p>If this is not specified, a default method name with the parameter name will be generated.
   * For example, if the parameter name is "firstName", the method will be called "setFirstName".</p>
   *
   * @return the method name
   */
  String methodName() default "";

  Style style() default Style.SETTER;

  /**
   * Specifies whether the parameter can be null. If this option is set to false, a null check for
   * this parameter be auto generated in the build method.
   *
   * @return {@code true} if nullable
   */
  boolean nullable() default true;
}
