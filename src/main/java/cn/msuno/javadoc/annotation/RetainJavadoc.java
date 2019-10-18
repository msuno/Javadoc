package cn.msuno.javadoc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.msuno.javadoc.build.JsonJavadocBuilder;

/**
 * Indicates the annotated element should have its Javadoc made available at runtime,
 * even if it's not in one of the packages specified by the {@code javadoc.packages}
 * annotation processor option.
 * <p>
 * Note that if a {@code javadoc.packages} is not specified, this annotation is
 * redundant since the default behavior is to retain Javadoc for all classes.
 *
 * @see JsonJavadocBuilder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface RetainJavadoc {
}
