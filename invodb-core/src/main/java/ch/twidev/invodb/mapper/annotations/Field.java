package ch.twidev.invodb.mapper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {

    String name() default "";

    boolean caseSensitive() default false;

    // add column type

    boolean isNullable() default true;
}
