package ch.twidev.invodb.mapper.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Get {

    String field() default "";

    String by() default "";

}
