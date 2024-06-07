package ch.twidev.invodb.mapper.annotations;

import ch.twidev.invodb.common.format.DataFormat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Primitive {

    Class<? extends DataFormat> formatter();

}
